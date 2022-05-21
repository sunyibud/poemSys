package com.poemSys.common.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

import java.util.Properties;

/**
 * 配置验证码格式
 */
@Configuration
public class CaptchaConfig
{
    @Bean
    DefaultKaptcha producer() {
        Properties properties = new Properties();
        //是否有边框
        properties.put("captcha.border", "no");
        //文本颜色
        properties.put("captcha.textproducer.font.color", "black");
        //每个字符间的间隔
        properties.put("captcha.textproducer.char.space", "5");
        //高、宽、文字大小
        properties.put("captcha.image.height", "40");
        properties.put("captcha.image.width", "120");
        properties.put("captcha.textproducer.font.size", "30");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
