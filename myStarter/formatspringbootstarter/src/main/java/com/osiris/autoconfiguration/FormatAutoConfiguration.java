package com.osiris.autoconfiguration;

import com.osiris.format.JsonFormatProcessor;
import com.osiris.format.StringFormatProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author lijia at 2020-07-14
 */
@Configuration
public class FormatAutoConfiguration {

    @Primary
    @ConditionalOnMissingClass(value = "com.alibaba.fastjson.JSON")
    @Bean
    public StringFormatProcessor stringFormatProcessor(){
        return new StringFormatProcessor();
    }

    @ConditionalOnClass(name = "com.alibaba.fastjson.JSON")
    @Bean
    public JsonFormatProcessor jsonFormatProcessor(){
        return new JsonFormatProcessor();
    }

}
