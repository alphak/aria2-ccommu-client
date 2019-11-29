package com.ddqiang.hkmanager.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


    @PostConstruct
    public void init() {
        List<String> excludeExtsTmp = initList(exclExtsPath);
        List<String> excludeNamesTmp = initList(exclNamesPath);
        List<String> existsFileNamesTmp = initList(filenamesdbPath);
        setExcludeExts(excludeExtsTmp);
        setExcludeNames(excludeNamesTmp);
        setExistsFileNames(existsFileNamesTmp);
    }

    private List<String> initList(String filepath) {
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String data = null;
            while ((data = br.readLine()) != null) {
                list.add(data);
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
}
