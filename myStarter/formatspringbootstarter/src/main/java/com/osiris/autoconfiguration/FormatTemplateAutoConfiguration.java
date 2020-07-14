package com.osiris.autoconfiguration;

import com.osiris.MyFormatTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lijia at 2020-07-14
 * @Description
 * @Email lijia@ule.com
 */
@Import(FormatAutoConfiguration.class)
@EnableConfigurationProperties(FormatProperties.class)
@Configuration
public class FormatTemplateAutoConfiguration {

    @Bean
    public MyFormatTemplate myFormatTemplate(FormatProperties formatProperties){
        return new MyFormatTemplate(formatProperties);
    }

}
