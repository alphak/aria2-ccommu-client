import com.ddqiang.hkmanager.cache.CachingDataFromFile;
import com.ddqiang.hkmanager.cache.FileItem;
import com.ddqiang.hkmanager.commuclient.CommuClient;
import com.ddqiang.hkmanager.elem.AbstractElem;
import com.ddqiang.hkmanager.filterstrategy.DefaultFilterStrategy;
import com.ddqiang.hkmanager.rpcmsg.Aria2AddTorrentMsg;
import com.ddqiang.hkmanager.rpcmsg.RpcMsg;
import com.ddqiang.hkmanager.tasks.ConsumeBlockingQueueMsgTask;
import com.ddqiang.hkmanager.tasks.DirectoryWatcherTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HkAppBeansTest.class})
@ActiveProfiles("test")
@TestPropertySource("classpath:conf/application-test.yml")
@ComponentScan("com.ddqiang")
public class HkAppBeansTest {
    public final static Logger logger = LoggerFactory.getLogger(HkAppBeansTest.class);

    @Autowired
    private DefaultFilterStrategy defaultFilterStrategy;

    @Autowired
    private CachingDataFromFile cachingDataFromFile;

    @Autowired
    private DirectoryWatcherTask directoryWatcherTask;

    @Autowired
    private CommuClient commuClient;

    @Autowired
    private ConsumeBlockingQueueMsgTask consumeBlockingQueueMsgTask;

    @Autowired
    private BlockingQueue<AbstractElem> blockingQueue;

    @Autowired
    private ExecutorService executor;


    @Test
    public void defaultFilterStrategyTest0(){
        String ext = ".mht";
        cachingDataFromFile.printLists();
        if(defaultFilterStrategy.isInExcludeExts(ext, cachingDataFromFile.getExcludeExts())){
            logger.debug("------{} file extention is in exlucde exts{}--------", ext, cachingDataFromFile.getExcludeExts().toString());
        }else{
            logger.debug("------{} file extention is not in exlucde exts{}--------", ext, cachingDataFromFile.getExcludeExts().toString());
        }
    }

    @Test
    public void defaultFilterStrategyTest1(){
        String name = "Sympathy.for.Lady.Vengeance.2005.720p.Bluray.x264.DTS-WiKi Chs.srt ";
        cachingDataFromFile.printLists();
        if(defaultFilterStrategy.isInContained(name, cachingDataFromFile.getExcludeNames())){
            logger.debug("------{} file name has in exlucde key word{}--------", name, cachingDataFromFile.getExcludeNames().toString());
        }else{
            logger.debug("------{} file name is not in exlucde key word{}--------", name, cachingDataFromFile.getExcludeNames().toString());
        }
    }

    @Test
    public void defaultFilterStrategyTest2(){
        String name = "Fifa.19.part37.rar";
        cachingDataFromFile.printLists();
        if(defaultFilterStrategy.isInContained(name, cachingDataFromFile.getExistsFileNames())){
            logger.debug("------{} file name is in exists names{}--------", name, cachingDataFromFile.getExistsFileNames().toString());
        }else{
            logger.debug("------{} file name is not in exists names{}--------", name, cachingDataFromFile.getExistsFileNames().toString());
        }
    }

    @Test
    public  void watchTest() throws InterruptedException {
        Thread t = new Thread(directoryWatcherTask);
        t.start();

        while (true){
            Thread.sleep(120);
        }
    }

    @Test
    public void commuClientTest() throws IOException, InterruptedException {
        File torrentFile = new File("F:/test/The.Vengeance.Trilogy.720p.BluRay.x264.DTS-WiKi.torrent");
        byte [] data = Files.readAllBytes(Paths.get(torrentFile.getAbsolutePath()));

        Base64.Encoder encoder = Base64.getEncoder();

        String base64TF =  encoder.encodeToString(data);
        logger.debug("base64 torrent file=[{}]",base64TF);
        RpcMsg rpcMsg = new Aria2AddTorrentMsg("112358", "Test", "2.0", base64TF);
//        Map<String, Object> map = rpcMsg.constructMap();
//        logger.debug("serialized map =[{}]", map.toString());
        String jsonstr = (String) rpcMsg.constructSendingMsg();
        logger.debug("rpcMsg.constructRpcMsg() return String------{}", jsonstr);
        commuClient.sendRpcRequest(jsonstr);

        while (true){
            Thread.sleep(120);
        }
    }


    @Test
    public void AppTest() throws IOException, InterruptedException {
        logger.debug("ConsumeBlockingQueueMsg task starting.....");
        executor.submit(consumeBlockingQueueMsgTask);
        logger.debug("DirectoryWatcher task starting.....");
        executor.submit(directoryWatcherTask);
        while (true){
            Thread.sleep(120);
        }
    }

    @Test
    public void cachingDataFromFileDbInitTest(){
        cachingDataFromFile.printDb();
    }

    @Test
    public void cachingDataFromFileDbSearchTest(){
        FileItem it = new FileItem(false, "cawd031-5.mp4", "cawd031-5", 669, null, ".mp4");
        if(cachingDataFromFile.searchDb(it)){
            logger.debug("founded");
        }else{
            logger.debug("not exists");
        }
    }
}
