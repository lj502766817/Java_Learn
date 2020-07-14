package com.osiris.format;

import com.alibaba.fastjson.JSON;

/**
 * @author lijia at 2020-07-14
 */
public class JsonFormatProcessor implements FormatProcessor {
    @Override
    public <T> String format(T obj) {
        return "JsonFormatProcessor: "+JSON.toJSONString(obj);
    }
}
