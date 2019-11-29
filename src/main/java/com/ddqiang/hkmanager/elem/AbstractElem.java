package com.ddqiang.hkmanager.elem;

import com.ddqiang.hkmanager.rpcmsg.MsgType;

public abstract class AbstractElem {
    private MsgType msgType;
    private Object msgContent;

    public AbstractElem(MsgType msgType, Object msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public void setMsgContent(Object msgContent) {
        this.msgContent = msgContent;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public Object getMsgContent() {
        return msgContent;
    }
}
