package com.ddqiang.hkmanager.cache;

import com.ddqiang.hkmanager.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CachingDataFromFile extends AbstractCacheData{
    private final static Logger logger = LoggerFactory.getLogger(CachingDataFromFile.class);

    @Value("${filtercache.exclude.extsfilepath}")
    private String exclExtsPath;

    @Value("${filtercache.exclude.namesfilepath}")
    private String exclNamesPath;

    @Value("${filtercache.exists.filenamedb}")
    private String filenamesdbPath;

    @Value("${filedatabase.path.dbfilepath}")
    private String dbFilePath;

    private List<FileItem> file_item_database;


    @PostConstruct
    public void init() {
        List<String> excludeExtsTmp = initList(exclExtsPath);
        List<String> excludeNamesTmp = initList(exclNamesPath);
        List<String> existsFileNamesTmp = initList(filenamesdbPath);
        setExcludeExts(excludeExtsTmp);
        setExcludeNames(excludeNamesTmp);
        setExistsFileNames(existsFileNamesTmp);

        file_item_database = initFileDatabase(dbFilePath);
    }

    private List<FileItem> initFileDatabase(String path){
        List<FileItem> db = new ArrayList<>();
        List<String> list = initList(path);
        for (String line:
             list) {
            String [] name_size = line.split("#");
            if(name_size.length == 2){
                String file_name = Utils.normalize(name_size[0]);
                long file_size = Long.parseLong(name_size[1]);
                if(file_size > 20) {
                    int index = file_name.lastIndexOf(".");
                    FileItem it = null;
                    if(index > 0) {
                        it = new FileItem(true, file_name, file_name.substring(0, file_name.lastIndexOf(".")),
                                          file_size, null, Utils.getFileExtention(file_name));
                    }else {
                        it = new FileItem(true, file_name, file_name, file_size, null, "");
                    }
                    db.add(it);
                }
            }
        }
        return db;
    }

    public boolean searchDb(FileItem item){
        if(item == null){
            return false;
        }
        String itemname = item.getFileNameNoExt();
        for (FileItem it:
                file_item_database) {
            if(itemname.contains(it.getFileNameNoExt())){
                return true;
            }

            if(it.getFileNameNoExt().contains(itemname)){
                return true;
            }
        }
        file_item_database.add(item);
        return false;
    }

    public boolean writeToFile(String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        for (FileItem it:
                file_item_database) {
            bw.write(it.getFileName()+"#"+it.getFileSize());
            bw.newLine();
            bw.flush();
        }

        bw.close();
        return true;
    }

    private List<String> initList(String filepath) {
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String data = null;
            while ((data = br.readLine()) != null) {
                list.add(data.toLowerCase());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            list = null;

        } catch (IOException e) {
            e.printStackTrace();
            list = null;
        } finally {
            return list;
        }
    }

    public void printLists() {
        logger.debug("print exlucdeExts as follow:");
        printList(getExcludeExts());
        logger.debug("print excludeNames as follow:");
        printList((getExcludeNames()));
        logger.debug("print existsFileNames as follow:");
        printList(getExistsFileNames());
    }

    private void printList(List<String> list) {
        if (list != null) {
            for (String str : list) {
                logger.debug(str);
            }
        }
    }

    public void printDb(){
        for (FileItem it:
                file_item_database) {
            logger.debug("filename with ext===={}", it.getFileName());
            logger.debug("filename without ext===={}", it.getFileNameNoExt());
            logger.debug("filename extention===={}", it.getExtention());
            logger.debug("file size===={}", it.getFileSize());
        }
    }
}
