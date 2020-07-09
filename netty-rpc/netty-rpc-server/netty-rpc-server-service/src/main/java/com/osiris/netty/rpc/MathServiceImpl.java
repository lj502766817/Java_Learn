package com.osiris.netty.rpc;

import com.osiris.netty.rpc.service.MathService;

/**
 * @author lijia
 */
@RpcRegister
public class MathServiceImpl implements MathService {
    @Override
    public Integer add(Integer a, Integer b) {
        return a+b;
    }

    @Override
    public Double mult(Double a, Double b) {
        return a*b;
    }
}
