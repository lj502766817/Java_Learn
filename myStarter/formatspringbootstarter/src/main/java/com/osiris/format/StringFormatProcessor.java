package com.osiris.format;

/**
 * @author lijia at 2020-07-14
 */
public class StringFormatProcessor implements FormatProcessor {
    @Override
    public <T> String format(T obj) {
        return "StringFormatProcessor: "+obj.toString();
    }
}
