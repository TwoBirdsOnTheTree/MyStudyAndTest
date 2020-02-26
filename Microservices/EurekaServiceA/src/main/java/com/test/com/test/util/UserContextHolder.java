package com.test.com.test.util;

public class UserContextHolder {
    public final static ThreadLocal<ContextObject> context = new ThreadLocal<ContextObject>() {
        @Override
        protected ContextObject initialValue() {
            return new ContextObject();
        }
    };
}
