package com.osiris.format;

/**
 * @author lijia at 2020-07-14
 * @Description
 * @Email lijia@ule.com
 */
public interface FormatProcessor {

    <T> String format(T obj);

}
