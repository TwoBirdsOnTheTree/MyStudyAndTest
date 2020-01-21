package com.test.dubbo.service;

import com.test.dubbo.util.CallbackListener;

public interface CallbackService {
    void addListener(String key, CallbackListener listener);
}
