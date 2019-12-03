package com.ddqiang.hkmanager.rpcmsg;

public interface ParseRespRpcMsg {
    public AbstractRespRpcMsg parseToRespObj(MsgType type, Object msg);
}
