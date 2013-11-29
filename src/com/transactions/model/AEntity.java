package com.transactions.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Любая идентифицируемая сущность базы данных
 */
@MappedSuperclass
public abstract class AEntity implements Serializable {
    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + id + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        AEntity rec = (AEntity) obj;
        if (id == null && rec.getId() == null) {
            return super.equals(obj);
        } else
            return id == null ? rec.id == null : id.equals(rec.id);
    }

    @Override

    public int hashCode() {
        return hash(id);
    }

    public static int hash(Object v) {
        return v == null ? 0 : v.hashCode();
    }
}
