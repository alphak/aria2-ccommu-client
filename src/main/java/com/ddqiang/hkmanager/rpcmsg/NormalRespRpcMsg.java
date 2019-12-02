package com.ddqiang.hkmanager.rpcmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalRespRpcMsg extends AbstractRespRpcMsg {
    public final static Logger logger = LoggerFactory.getLogger(NormalRespRpcMsg.class);

    private String clientId;
    private Object result;

    public NormalRespRpcMsg(String jsonrpc, String clientId, Object result) {
        super(jsonrpc);
        this.clientId = clientId;
        this.result = result;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


}
