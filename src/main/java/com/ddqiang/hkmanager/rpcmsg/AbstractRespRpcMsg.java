package com.ddqiang.hkmanager.rpcmsg;

public abstract class AbstractRespRpcMsg {
    private String jsonrpc;
//    private MsgType respType;

//    public AbstractRespRpcMsg(String jsonrpc, MsgType respType) {
//        this.jsonrpc = jsonrpc;
//        this.respType = respType;
//    }

    public AbstractRespRpcMsg(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

//    public MsgType getRespType() {
//        return respType;
//    }
//
//    public void setRespType(MsgType respType) {
//        this.respType = respType;
//    }




}
