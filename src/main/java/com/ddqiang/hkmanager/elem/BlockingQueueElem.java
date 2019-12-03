package com.ddqiang.hkmanager.elem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddqiang.hkmanager.rpcmsg.ARIA2C_CONSTANT;
import com.ddqiang.hkmanager.rpcmsg.AbstractRespRpcMsg;
import com.ddqiang.hkmanager.rpcmsg.MsgType;
import com.ddqiang.hkmanager.rpcmsg.NormalRespRpcMsg;
import com.ddqiang.hkmanager.rpcmsg.NotificationRpcMsg;
import com.ddqiang.hkmanager.rpcmsg.ParseRespRpcMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingQueueElem extends AbstractElem implements ParseRespRpcMsg {
    public final static Logger logger = LoggerFactory.getLogger(BlockingQueueElem.class);

    public BlockingQueueElem(MsgType msgType, Object msgContent) {
        super(msgType, msgContent);
        //notification or normal response
        if(msgType == MsgType.RAWRESPMSG){
            if(msgContent instanceof String){
                String msgStr = (String)msgContent;
                JSONObject obj = JSON.parseObject(msgStr);
//                JSONObject subObj = obj.getJSONObject(ARIA2C_CONSTANT.KEY_RESP_RESULT);
                String subObj = obj.getString(ARIA2C_CONSTANT.KEY_RESP_RESULT);
                if(subObj != null){
                    setMsgType(MsgType.NORMMSG);
                    AbstractRespRpcMsg norm =  parseToRespObj(MsgType.NORMMSG, msgStr);
                    setMsgContent(norm);
                    return;
                }

                subObj = obj.getString(ARIA2C_CONSTANT.KEY_RESP_PARAMS);
                if (subObj != null) {
                    setMsgType(MsgType.NOTIMSG);
                    AbstractRespRpcMsg noti = parseToRespObj(MsgType.NOTIMSG, msgStr);
                    setMsgContent(noti);
                    return;
                }
            }
        }
    }

    @Override
    public AbstractRespRpcMsg parseToRespObj(MsgType type, Object msg) {
        if(type == null || !(msg instanceof String)){
            return null;
        }
        String msgStr = (String)msg;
        JSONObject obj = JSON.parseObject(msgStr);
        String jsonrpc = obj.getString(ARIA2C_CONSTANT.KEY_JSONRPC);
        switch (type){
        case NORMMSG:
            String clientId = obj.getString(ARIA2C_CONSTANT.KEY_CLIENTID);
            String result = obj.getString(ARIA2C_CONSTANT.KEY_RESP_RESULT);
            NormalRespRpcMsg norm = new NormalRespRpcMsg(jsonrpc, clientId, result);
            return norm;
        case NOTIMSG:
            String method = obj.getString(ARIA2C_CONSTANT.KEY_METHOD);
            String params = obj.getString(ARIA2C_CONSTANT.KEY_RESP_PARAMS);
            NotificationRpcMsg noti = new NotificationRpcMsg(jsonrpc, method, params);
            return noti;
        default:
            return null;
        }
    }
}
