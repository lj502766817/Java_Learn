package com.osiris.netty.rpc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijia
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3014388206355193352L;
    private String name;

    private Integer age;

}
