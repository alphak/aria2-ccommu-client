package com.ddqiang.hkmanager.commuclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("commuClientWebsocketInitializer")
public class CommuClientWebsocketInitializer extends ChannelInitializer<SocketChannel> {

    private final static Logger logger = LoggerFactory.getLogger(CommuClientWebsocketInitializer.class);

    @Autowired
    private CommuClientWebsocketHandler commuClientWebsocketHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline chp = socketChannel.pipeline();
        chp.addLast(new HttpClientCodec(),
                    new HttpObjectAggregator(1024 * 1024 * 10),
                    WebSocketClientCompressionHandler.INSTANCE);
        chp.addLast("commuClientWebsocketHandler", commuClientWebsocketHandler);
    }
}
