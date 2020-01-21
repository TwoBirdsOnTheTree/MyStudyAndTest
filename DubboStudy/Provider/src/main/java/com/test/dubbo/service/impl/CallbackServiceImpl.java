package com.test.dubbo.service.impl;

import com.test.dubbo.service.CallbackService;
import com.test.dubbo.util.CallbackListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackServiceImpl implements CallbackService {
    private final Map<String, CallbackListener> listeners =
            new ConcurrentHashMap<>();

    public CallbackServiceImpl() {
        Thread t = new Thread(() -> {
            while (true) {
                listeners.entrySet().forEach(entry -> {
                    try {
                        // 定时循环触发回调 (真正回调执行的代码在consumer端)
                        // 因为entry.getValue()实例在consumer端
                        String msg = getChanged(entry.getKey());
                        entry.getValue().changed(msg);
                    } catch (Throwable tt) {
                        tt.printStackTrace();
                        // 这里remove没啥意义, 一般不会发生异常
                        // listeners.remove(entry.getKey());
                    }
                });
                SLEEP(2000);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.changed(getChanged(key));
    }

    private String getChanged(String key) {
        return "Changed: " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    }

    private static void SLEEP(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
