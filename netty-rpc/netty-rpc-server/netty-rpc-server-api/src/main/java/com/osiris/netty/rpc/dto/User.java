package com.osiris.netty.rpc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijia
 */
@Data
public class User implements Serializable {

    private String name;

    private Integer age;

}
