package com.ddqiang.hkmanager.rpcmsg;

import java.util.List;
import java.util.Map;

public abstract class AbstractRpcMsg implements RpcMsg {

    private String rpcToken;
    private String appId;
//    private String ariaRpcMethod;
    private List<Object> params;
    private String jsonrpcVer;

    public AbstractRpcMsg(String rpcToken, String appId, String jsonrpcVer) {
        this.rpcToken = "token:".concat(rpcToken);
        this.appId = appId;
//        this.ariaRpcMethod = ariaRpcMethod;
//        this.params = params;
        this.jsonrpcVer = jsonrpcVer;
    }

//    @Override
//    public Object constructRpcMsg(Map<String, Object> src) {
//        return null;
//    }

    public String getRpcToken() {
        return rpcToken;
    }

    public String getAppId() {
        return appId;
    }

//    public String getAriaRpcMethod() {
//        return ariaRpcMethod;
//    }

    public List<Object> getParams() {
        return params;
    }

    public String getJsonrpcVer() {
        return jsonrpcVer;
    }
}
