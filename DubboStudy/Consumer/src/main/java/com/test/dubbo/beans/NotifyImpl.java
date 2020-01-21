package com.test.dubbo.beans;

import com.test.dubbo.util.Notify;

public class NotifyImpl implements Notify {
    @Override
    public void onreturn(String msg) {
        System.out.println("notify: " + msg);
    }
}
