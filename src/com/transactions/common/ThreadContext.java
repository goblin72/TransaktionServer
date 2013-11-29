package com.transactions.common;

import com.sigma.utils.jpa.DataManager;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 30.04.13
 * Time: 16:21
 */
public class ThreadContext
{
    private static final ThreadLocal<ThreadContext> local = new ThreadLocal<ThreadContext>();

    public static ThreadContext getThreadContext() {
        ThreadContext context = local.get();
        if (context == null) {
            context = new ThreadContext();
            local.set(context);
        }
        return context;
    }

    private DataManager manager;

    public DataManager getManager() {
        return manager;
    }

    public void setManager(DataManager manager) {
        this.manager = manager;
    }
}
