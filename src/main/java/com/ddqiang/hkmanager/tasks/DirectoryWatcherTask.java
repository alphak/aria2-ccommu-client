package com.ddqiang.hkmanager.tasks;


import com.ddqiang.hkmanager.btparser.BTFileParser;
import com.ddqiang.hkmanager.cache.AbstractCacheData;
import com.ddqiang.hkmanager.elem.AbstractElem;
import com.ddqiang.hkmanager.elem.BlockingQueueElem;
import com.ddqiang.hkmanager.filterstrategy.FilterStrategy;
import com.ddqiang.hkmanager.rpcmsg.ARIA2C_CONSTANT;
import com.ddqiang.hkmanager.rpcmsg.Aria2AddTorrentMsg;
import com.ddqiang.hkmanager.rpcmsg.MsgType;
import com.ddqiang.hkmanager.rpcmsg.RpcMsg;
import com.turn.ttorrent.common.TorrentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

@Component
public class DirectoryWatcherTask implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(DirectoryWatcherTask.class);

    @Autowired
    private BTFileParser parser;

    @Autowired
    private AbstractCacheData cacheData;

    @Autowired
    private FilterStrategy filterStrategy;

    @Value("${sentinel.btfiles.watchpath}")
    private String path;

    @Value("${sentinel.btfiles.bakpath}")
    private String bakpath;

    @Autowired
    private ExecutorService executor;

    @Value("${aria2.rpc.token}")
    private String token;

    @Value("${aria2.app.id}")
    private String appId;

    @Value("${aria2.jsonrpc.version}")
    private String jsonRpcVer;

    @Autowired
    private BlockingQueue<AbstractElem> blockingQueue;

    private String getFileExtention(String filename){
        if(filename == null || "".equals(filename.trim()))
            return "";
        String [] tmp= filename.trim().split("\\\\");
        String name = tmp[tmp.length-1];
        return name.substring(name.lastIndexOf("."));
    }

    private String getFilenameWithOutExt(String filename){
        if(filename == null || "".equals(filename.trim()))
            return "";
        String [] tmp= filename.trim().split("\\\\");
        String name = tmp[tmp.length-1];
        return name.substring(0,name.lastIndexOf("."));
    }

    @Override
    public void run() {
        logger.debug("bt files watcher stated...");
        if(parser==null){
            logger.debug("bt parser not set properly...");
            return;
        }

        if(path == null || bakpath == null || "".equals(path) || "".equals(bakpath)){
            logger.debug("folder to be watch  or bt files' bak path is invalid...");
            return;
        }

        File bfpath;
        try{
            bfpath = new File(path);
            if(!bfpath.exists()){
                if(!bfpath.mkdirs()){
                    logger.debug("folder to be watch not exists and creation failed...");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(!bfpath.isDirectory()){
            logger.debug("folder to be watched is not a directory...");
            return;
        }

        File bfbakpath;
        try{
            bfbakpath = new File(bakpath);
            if(!bfbakpath.exists()){
                if(!bfbakpath.mkdirs()){
                    logger.debug("folder used to backup bt files not exists and creation failed...");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(!bfbakpath.isDirectory()){
            logger.debug("directory used to backup bt files is not a directory...");
            return;
        }

        //check bt files every 2 minites
        File[] fileLst = bfpath.listFiles();
        boolean shouldMove = true;
        boolean willBase64 = true;
        while (true){
            fileLst = bfpath.listFiles();
            if(fileLst != null && fileLst.length> 0){
                for (File f: fileLst) {
                    if(f.isFile() && f.getName().toLowerCase().endsWith(".torrent")){
                        shouldMove = true;
                        willBase64 = true;
                        logger.debug(f.getAbsolutePath());
                        try {
                            List<TorrentFile> torrentfiles = parser.parseTorrentFile(f);
                            for (TorrentFile tf: torrentfiles) {
                                logger.debug(tf.getRelativePathAsString());
                                String oriname = tf.getRelativePathAsString().trim();
                                String name = getFilenameWithOutExt(oriname);
                                logger.debug("name without extention=[{}]", name);
                                String ext = getFileExtention(oriname);
                                logger.debug("file extention=[{}]", ext);

                                if(filterStrategy.isInExcludeExts(ext, cacheData.getExcludeExts())){
                                    continue;
                                }

                                if(filterStrategy.isInContained(name, cacheData.getExcludeNames())){
                                    continue;
                                }

                                if(filterStrategy.isInContained(name, cacheData.getExistsFileNames())){
                                    willBase64 = false;
                                    break;
                                }
                            }
                            if(willBase64) {
                                Callable<String> task = new Base64TorrentFileTask(f);
                                FutureTask<String> future = new FutureTask<String>(task);
                                executor.submit(future);
                                String base64FileContent = future.get();
                                logger.debug("base64 encoded torrent file ==[{}]", base64FileContent);
                                RpcMsg rpcMsg = new Aria2AddTorrentMsg(token, appId, jsonRpcVer, base64FileContent);
//                                Map<String, Object> map = new HashMap<>();
//                                map.put(ARIA2C_CONSTANT.KEY_MSGTYPE, MsgType.REQMSG);
//                                map.put(ARIA2C_CONSTANT.KEY_MSGCONT, rpcMsg.constructSendingMsg());
                                blockingQueue.put(new BlockingQueueElem(MsgType.REQMSG, rpcMsg.constructSendingMsg()));
                            }
                        } catch (IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            logger.debug("[{}] parse failed ....", f.getName());
                        }finally {
                            if(willBase64){
                                //move base64ed .torrent file into backup dir
                                f.renameTo(new File(bakpath.concat(File.separator).concat(f.getName())));
                            }
                        }
                    }
                }
            }

            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                logger.debug("Runnable sleep failure....");
                e.printStackTrace();
                return;
            }
        }
    }
}
