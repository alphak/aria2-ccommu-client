package com.ddqiang.hkmanager.elem;

import com.ddqiang.hkmanager.rpcmsg.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class BlockingQueueElem extends AbstractElem {
    public final static Logger logger = LoggerFactory.getLogger(BlockingQueueElem.class);

    public BlockingQueueElem(MsgType msgType, Object msgContent) {
        super(msgType, msgContent);
    }
}
