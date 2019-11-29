import com.ddqiang.hkmanager.tasks.Base64TorrentFileTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class NoneBeanTest {
    public final static Logger logger = LoggerFactory.getLogger(NoneBeanTest.class);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        File file = new File("F:/test/The.Vengeance.Trilogy.720p.BluRay.x264.DTS-WiKi.torrent");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = new Base64TorrentFileTask(file);
        FutureTask<String> futureTask = new FutureTask<String>(task);
        executor.submit(futureTask);
        String base64ed = futureTask.get();
        logger.debug("base64ed torrent file=[{}]", base64ed);
    }
}
