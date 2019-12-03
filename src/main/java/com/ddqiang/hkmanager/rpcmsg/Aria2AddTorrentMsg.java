package com.ddqiang.hkmanager.rpcmsg;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aria2AddTorrentMsg extends AbstractRpcMsg{
    public final static Logger logger = LoggerFactory.getLogger(Aria2AddTorrentMsg.class);

    private String base64TorrentFile;

    private final static String ARIA2RPCMETHOD = ARIA2C_CONSTANT.RPCMETHOD_ADDTORRENT;

    public Aria2AddTorrentMsg(String rpcToken, String appId, String jsonrpcVer, String base64TF) {
        super(rpcToken, appId, jsonrpcVer);
        this.base64TorrentFile = base64TF;
    }

    public String getBase64TorrentFile() {
        return base64TorrentFile;
    }

//    @Override
//    public Object constructRpcMsg(Map<String, Object> src) {
//        String jsonString = JSON.toJSONString(src);
//        logger.debug(jsonString);
//        return jsonString;
//    }
//
//    @Override
//    public Map<String, Object> constructMap() {
//        Map<String, Object> map = new HashMap<>();
//        map.put(ARIA2C_CONSTANT.KEY_JSONRPC, getJsonrpcVer());
//        map.put(ARIA2C_CONSTANT.KEY_APPID, getAppId());
//        map.put(ARIA2C_CONSTANT.KEY_METHOD, ARIA2RPCMETHOD);
//        List<Object> params = new ArrayList<>();
//        params.add(getRpcToken());
//        params.add(getBase64TorrentFile());
//        List<Object> uriList = new ArrayList<>();
//        Map<String, Object> options = new HashMap<>();
//        options.put(ARIA2C_CONSTANT.KEY_OPT_PAUSE, "true");
//        uriList.add(options);
//        params.add(uriList);
//        map.put(ARIA2C_CONSTANT.KEY_PARAMS, params);
//        return map;
//    }

    private void fillRequestHeader(Map<String, Object> map){
        map.put(ARIA2C_CONSTANT.KEY_JSONRPC, getJsonrpcVer());
        map.put(ARIA2C_CONSTANT.KEY_CLIENTID, getAppId());
        map.put(ARIA2C_CONSTANT.KEY_METHOD, ARIA2RPCMETHOD);
    }

    private List<Object> getRequestParams(){
        List<Object> params = new ArrayList<>();

        params.add(getRpcToken());
        params.add(getBase64TorrentFile());

        List<Object> urisList = new ArrayList<>();
        params.add(urisList);

        Map<String, Object> options = new HashMap<>();
        options.put(ARIA2C_CONSTANT.KEY_OPT_PAUSE, "true");
        params.add(options);

        return params;
    }

    @Override
    public Object constructSendingMsg() {
        Map<String, Object> map = new HashMap<>();
        fillRequestHeader(map);

        map.put(ARIA2C_CONSTANT.KEY_PARAMS, getRequestParams());

        String retStr = JSON.toJSONString(map);
        logger.debug("Aria2AddTorrentMsg request json string---{}", retStr);
        return retStr;
    }
}
