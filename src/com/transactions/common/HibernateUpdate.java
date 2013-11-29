package com.transactions.common;

import com.sigma.utils.Properties;
import com.sigma.utils.Utils;
import com.sigma.utils.jpa.Index;
import com.sigma.utils.jpa.Indexes;
import com.sigma.utils.jpa.JPAScanner;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: Bolgar E. <br>
 * Date: 21.04.2011 11:54:17 <br><br>
 * Изменение базы данных по JPA модели
 */
public class HibernateUpdate {
    /**
     * @param args: <br>
     *              -url url подключения к БД<br>
     *              -user пользователь БД<br>
     *              -password пароль БД<br>
     *              -driver драйвер БД<br>
     *              -dialect диалект БД<br>
     *              -jar путь к файлу модели
     *              -index маска для имени индекса, по умолчанию ind_{0}_{1}, где {0} имя таблицы {1} имя колонки
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /*
        args = new String[]{
                "-url", "jdbc:postgresql://localhost/office",
                "-user", "postgres",
                "-password", "root",
                "-driver", "org.postgresql.Driver",
                "-dialect", "org.hibernate.dialect.PostgreSQLDialect",
                "-jar", "D:\\Projects\\Sigma\\ASCIP3\\BackOffice\\.dist\\dist\\ascip-office-server.jar"
        };
        */
        Properties props = Utils.getRuntimeProperties(args);

        String locale = props.getProperty("locale");
        if (locale != null) {
            if (locale.toUpperCase().equals("ENGLISH") || locale.toUpperCase().equals("EN")) {
                System.out.println("Set english locale");
                Locale.setDefault(Locale.ENGLISH);
            }
        }

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", props.getProperty("url"));
        properties.setProperty("hibernate.connection.username", props.getProperty("user"));
        properties.setProperty("hibernate.connection.password", props.getProperty("password"));
        properties.setProperty("hibernate.connection.driver_class", props.getProperty("driver"));
        properties.setProperty("hibernate.dialect", props.getProperty("dialect"));
        File jar = new File(props.getProperty("jar"));
//        Utils.addclasspath(jar);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ant", properties);
        Ejb3Configuration cfg = new Ejb3Configuration();
        Set<String> entities = JPAScanner.scan(jar);

        for (String className : entities) cfg.addAnnotatedClass(Class.forName(className));
        cfg.configure("ant", (Map) factory.getClass().getMethod("getProperties").invoke(factory));
        Configuration configuration = cfg.getHibernateConfiguration();
        configuration.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
        SchemaUpdate export = new SchemaUpdate(configuration);
        export.setDelimiter("");
        File file = File.createTempFile("sql", ".tmp");
        export.setOutputFile(file.getAbsolutePath());
        export.execute(false, false);
        if ((export.getExceptions() != null) && (!export.getExceptions().isEmpty()))
            throw (Exception) export.getExceptions().get(0);
        Connection con = DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("password"));
        try {
            Statement st = con.createStatement();
            FileInputStream input = new FileInputStream(file);
            String string;
            while ((string = Utils.readLine(input)) != null) {
                try {
                    System.out.println(string);
                    st.execute(string);
                } catch (Exception e) {
                    System.err.println(string + "\n\tFaild:" + e);
                    throw e;
                }
            }
            st.close();
        } finally {
            con.close();
        }

        if ((export.getExceptions() != null) && (!export.getExceptions().isEmpty()))
            throw (Exception) export.getExceptions().get(0);

        indexes(entities, props);
    }

    public static void indexes(Set<String> entities, Properties properties) throws Exception {
        DriverManager.registerDriver((Driver) Class.forName(properties.getProperty("driver")).newInstance());
        Connection con = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        try {
            MessageFormat template = new MessageFormat(properties.getProperty("index", "ind_{0}_{1}"));
            for (String entityName : entities) {
                Class entity = Class.forName(entityName);
                if (entity.getAnnotation(Table.class) != null) {
                    Collection<IndexItem> list = getIndexItems(entity);
                    if (!list.isEmpty()) {
                        String table = ((Table) entity.getAnnotation(Table.class)).name();
                        for (IndexItem item : list) {
                            if (item.column == null) {
                                if (item.index.name().isEmpty())
                                    throw new Exception("Name not define for entity index [" + entityName + "]");
                                if (item.index.columns().length == 0)
                                    throw new Exception("Columns not define for entity index[" + entityName + "]");
                                index(table, item.index.name(), item.index.columns(), con);
                            } else {
                                String column;
                                if (item.column.getAnnotation(Column.class) != null) {
                                    column = item.column.getAnnotation(Column.class).name();
                                } else if (item.column.getAnnotation(JoinColumn.class) != null) {
                                    column = item.column.getAnnotation(JoinColumn.class).name();
                                } else if (item.column.getAnnotation(JoinTable.class) != null) {
                                    column = null;
                                } else column = item.column.getName();
                                if (column != null) {
                                    index(table, item.index.name().isEmpty() ? template.format(new Object[]{table, column}) : item.index.name(), new String[]{column}, con);
                                } else {
                                    JoinTable join = item.column.getAnnotation(JoinTable.class);
                                    index(join.name(), template.format(new Object[]{join.name(), join.joinColumns()[0].name()}), convert(join.joinColumns()), con);
                                    index(join.name(), template.format(new Object[]{join.name(), join.inverseJoinColumns()[0].name()}), convert(join.inverseJoinColumns()), con);
                                }
                            }

                        }
                    }
                }
            }
        } finally {
            con.close();
        }
    }

    private static String[] convert(JoinColumn[] columns) {
        String[] array = new String[columns.length];
        for (int i = 0; i < columns.length; i++) array[i] = columns[i].name();
        return array;
    }

    private static void index(String table, String name, String[] columns, Connection con) throws Exception {
        String[] current = getIndexColumns(table, name, con);
        if (!Arrays.equals(current, columns)) {
            Statement st = con.createStatement();
            try {
                if (current.length > 0) {
                    System.out.println("drop index " + name);
                    st.execute("drop index " + name);
                }
                String s = Arrays.toString(columns);
                s = s.substring(1, s.length() - 1);
                String sql = "create index " + name + " on " + table + " (" + s + ")";
                System.out.println(sql);
                st.execute(sql);
            } finally {
                st.close();
            }
        }
    }

    private static String[] getIndexColumns(String table, String name, Connection con) throws Exception {
        ResultSet rs = con.getMetaData().getIndexInfo(null, null, table, false, false);
        List<Item> list = new ArrayList<Item>();
        try {
            while (rs.next()) {
                if (name.equals(rs.getString("index_name"))) {
                    list.add(new Item(rs.getString("column_name"), rs.getInt("ordinal_position")));
                }
            }
        } finally {
            rs.close();
        }
        Collections.sort(list);
        String[] set = new String[list.size()];
        for (int i = 0; i < list.size(); i++) set[i] = list.get(i).column;
        return set;
    }

    private static Collection<IndexItem> getIndexItems(Class clazz) {
        List<IndexItem> list = new ArrayList<IndexItem>();
        Indexes indexes = (Indexes) clazz.getAnnotation(Indexes.class);
        if (indexes != null) for (Index index : indexes.value()) list.add(new IndexItem(index));
        Index index = (Index) clazz.getAnnotation(Index.class);
        if (index != null) list.add(new IndexItem(index));
        for (Field field : clazz.getDeclaredFields()) {
            index = field.getAnnotation(Index.class);
            if (index != null) list.add(new IndexItem(index, field));
        }
        clazz = clazz.getSuperclass();
        if (clazz != Object.class) list.addAll(getIndexItems(clazz));
        return list;
    }

    private static class Item implements Comparable<Item> {
        String column;
        int index;

        private Item(String column, int index) {
            this.column = column;
            this.index = index;
        }

        public int compareTo(Item o) {
            return ((Integer) index).compareTo(o.index);
        }
    }

    private static class IndexItem {
        private Index index;
        private Field column;

        private IndexItem(Index index) {
            this.index = index;
        }

        private IndexItem(Index index, Field column) {
            this.index = index;
            this.column = column;
        }

        @Override
        public String toString() {
            return column + "/" + index;
        }
    }
}
