package com.ne.jp.shumipro.component;


import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class SessionData implements Serializable {

    private static final long serialVersionUID = 7663295261238766538L;

    private String username;
    private String adminflg;
}
