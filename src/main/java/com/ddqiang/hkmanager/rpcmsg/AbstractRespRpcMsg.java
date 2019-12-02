package com.ddqiang.hkmanager.rpcmsg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public abstract class AbstractRespRpcMsg {
    private String jsonrpc;
    private MsgType respType;

    public AbstractRespRpcMsg(String jsonrpc, MsgType respType) {
        this.jsonrpc = jsonrpc;
        this.respType = respType;
    }

    public AbstractRespRpcMsg(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public MsgType getRespType() {
        return respType;
    }

    public void setRespType(MsgType respType) {
        this.respType = respType;
    }


    public MsgType getRespRpcMsgType(Object msg) {
        if(msg == null){
            return MsgType.UNKOWNMSG;
        }
        if(msg instanceof String){
            String msgStr = (String)msg;
            JSONObject obj = JSON.parseObject(msgStr);
            JSONObject resultObj = obj.getJSONObject(ARIA2C_CONSTANT.KEY_RESP_RESULT);
            if(resultObj != null){
                return MsgType.NORMMSG;
            }
            JSONObject paramsObj = obj.getJSONObject(ARIA2C_CONSTANT.KEY_RESP_PARAMS);
            if(paramsObj != null){
                return MsgType.NOTIMSG;
            }
        }
        return MsgType.UNKOWNMSG;
    }

}
