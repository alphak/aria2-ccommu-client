package com.ddqiang.hkmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HkManagerApp {
    private final static Logger logger = LoggerFactory.getLogger(HkManagerApp.class);

    public static void main(String[] args) throws Exception {
        logger.debug("Commu Client Application starting......");
        ConfigurableApplicationContext context = SpringApplication.run(HkManagerApp.class, args);
        logger.debug("ConsumeBlockingQueueMsg task starting.....");

        logger.debug("DirectoryWatcher task starting.....");
    }
}
