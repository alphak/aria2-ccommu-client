package com.ddqiang.hkmanager.commuclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddqiang.hkmanager.elem.AbstractElem;
import com.ddqiang.hkmanager.elem.BlockingQueueElem;
import com.ddqiang.hkmanager.rpcmsg.ARIA2C_CONSTANT;
import com.ddqiang.hkmanager.rpcmsg.MsgType;
import com.ddqiang.hkmanager.rpcmsg.ParseRespRpcMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

@Component
public class CommuClientWebsocketHandler extends SimpleChannelInboundHandler<Object> {

    private final static Logger logger = LoggerFactory.getLogger(CommuClientWebsocketHandler.class);

    @Autowired
    private URI websocketURI;

    @Autowired
    private DefaultHttpHeaders defaultHttpHeaders;

    @Autowired
    private BlockingQueue<AbstractElem> blockingQueue;

    @Autowired
    private ExecutorService executor;

    private WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    @PostConstruct
    public void init() {
        this.handshaker = WebSocketClientHandshakerFactory
                .newHandshaker(websocketURI,
                               WebSocketVersion.V13,
                               null, true,
                               defaultHttpHeaders);;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
        logger.debug("WebSocket Client actived!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("WebSocket Client disconnected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Channel ch = channelHandlerContext.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                logger.debug("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                logger.debug("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                    ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            logger.debug("WebSocket Client received message: " + textFrame.text());
            blockingQueue.put(new BlockingQueueElem(MsgType.RAWRESPMSG, textFrame.text()));
        } else if (frame instanceof PongWebSocketFrame) {
            logger.debug("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            logger.debug("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

}
