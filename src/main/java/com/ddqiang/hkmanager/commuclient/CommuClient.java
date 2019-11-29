package com.ddqiang.hkmanager.commuclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;

@Component
public class CommuClient {
    public final static Logger logger = LoggerFactory.getLogger(CommuClient.class);

    @Autowired
    private CommuClientWebsocketInitializer commuClientWebsocketInitializer;

    @Autowired
    private CommuClientWebsocketHandler handler;

    @Autowired
    private EventLoopGroup group;

    @Autowired
    private URI websocketURI;

    private Bootstrap bootstrap;

    private Channel ch;

    @PostConstruct
    public void init() throws InterruptedException {
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .option(ChannelOption.SO_BACKLOG, 1024 * 1024 * 10)
                 .group(group)
                 .channel(NioSocketChannel.class)
                 .handler(commuClientWebsocketInitializer);
        connectServerAndHandshake();
    }

    @PreDestroy
    public void destroy(){
        group.shutdownGracefully();
    }

    private void connectServerAndHandshake() throws InterruptedException {
        ch = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
        handler.handshakeFuture().sync();
    }

    public ChannelFuture sendRpcRequest(String msg) throws InterruptedException {
        WebSocketFrame frame = new TextWebSocketFrame(msg);
        return ch.writeAndFlush(frame);
    }
}
