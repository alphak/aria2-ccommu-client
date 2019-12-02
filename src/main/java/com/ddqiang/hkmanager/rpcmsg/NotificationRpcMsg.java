package com.ddqiang.hkmanager.rpcmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationRpcMsg extends AbstractRespRpcMsg {
    public final static Logger logger = LoggerFactory.getLogger(NotificationRpcMsg.class);

    private String rpcMethod;
    private Object params;

    public NotificationRpcMsg(String jsonrpc, String rpcMethod, Object params) {
        super(jsonrpc);
        this.rpcMethod = rpcMethod;
        this.params = params;
    }

    public String getRpcMethod() {
        return rpcMethod;
    }

    public void setRpcMethod(String rpcMethod) {
        this.rpcMethod = rpcMethod;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }


}
