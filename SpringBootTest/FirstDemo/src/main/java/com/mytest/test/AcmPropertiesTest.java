package com.mytest.test;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("acm")
public class AcmPropertiesTest {
    private boolean enable;
    private String remoteAddress;
    private Security security = new Security();

    public Security getSecurity() {
        return this.security;
    }

    // 没有也可以的
    // 因为security已经初始化了
    /*public void setSecurity(Security security) {
        this.security = security;
    }*/

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public static class Security {
        private String usernameAaa_;
        private String password;

        public String getUsernameAaa_() {
            return usernameAaa_;
        }

        public void setUsernameAaa_(String usernameAaa_) {
            this.usernameAaa_ = usernameAaa_;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
