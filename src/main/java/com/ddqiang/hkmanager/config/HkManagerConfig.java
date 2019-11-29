package com.ddqiang.hkmanager.config;

import com.ddqiang.hkmanager.elem.AbstractElem;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@ComponentScan("com.ddqiang")
@PropertySource("classpath:conf/application.properties")
public class HkManagerConfig {

    private final static Logger logger = LoggerFactory.getLogger(HkManagerConfig.class);

    @Value("${commuclient.websocket.url}")
    private String websocketURI;

    @Bean
    public URI websocketURI() throws URISyntaxException {
        return new URI(websocketURI);
    }

    @Bean
    public EventLoopGroup group(){
        return new NioEventLoopGroup();
    }

    @Bean
    public DefaultHttpHeaders defaultHttpHeaders(){
        return new DefaultHttpHeaders();
    }

    @Bean
    public BlockingQueue<AbstractElem> blockingQueue(){
        return new LinkedBlockingQueue<AbstractElem>(20);
    }

    @Bean
    public ExecutorService executor(){
        return Executors.newCachedThreadPool();
    }

}
