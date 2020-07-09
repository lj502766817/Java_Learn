package com.osiris.netty.rpc.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author lijia
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 8759131730407446754L;
    /**
     * 调用的类
     */
    private String className;
    /**
     * 调用的方法名
     */
    private String methodName;
    /**
     * 形参类型
     */
    private Class<?>[] params;
    /**
     * 参数值
     */
    private Object[] value;

}
