package com.ddqiang.hkmanager.tasks;

import com.ddqiang.hkmanager.commuclient.CommuClient;
import com.ddqiang.hkmanager.elem.AbstractElem;
import com.ddqiang.hkmanager.rpcmsg.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class ConsumeBlockingQueueMsgTask implements Runnable {
    public final static Logger logger = LoggerFactory.getLogger(ConsumeBlockingQueueMsgTask.class);

    @Autowired
    private BlockingQueue<AbstractElem> blockingQueue;

    @Autowired
    private CommuClient commuClient;

    @Override
    public void run() {
        logger.debug("BlockingQueueMsg Consumer started.....");
        while(true){
            try {
                Object obj = blockingQueue.take();
                if(obj != null && obj instanceof AbstractElem){
                    AbstractElem elem = (AbstractElem) obj;
                    switch (elem.getMsgType()){
                    case REQMSG:
                        commuClient.sendRpcRequest((String) elem.getMsgContent());
                        break;
                    case RESPMSG:
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
