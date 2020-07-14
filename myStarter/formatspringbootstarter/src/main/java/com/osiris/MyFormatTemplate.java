package com.osiris;

import com.osiris.autoconfiguration.FormatProperties;
import com.osiris.format.FormatProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lijia at 2020-07-14
 */
public class MyFormatTemplate {

    @Autowired
    private FormatProcessor formatProcessor;

    private FormatProperties formatProperties;

    public MyFormatTemplate(FormatProperties formatProperties) {
        this.formatProperties = formatProperties;
    }

    public <T> String doFormat(T obj){
        return "my proerties: "+formatProperties.getProperties()+",format value: "+formatProcessor.format(obj);
    }
}
