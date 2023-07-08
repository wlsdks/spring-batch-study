package com.study.pass.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakaotalk") // properties에서 token정보를 불러올수 있도록 한다.
public class KakaoTalkMessageConfig {
    private String host;
    private String token;
}
