package com.gbw.scanner.server.scan;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.sink.SinkQueue;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class GBWScanResultServerHandler extends SimpleChannelInboundHandler<Object> {

    private final SinkQueue sinkQueue;

    public GBWScanResultServerHandler(SinkQueue sinkQueue) {

        this.sinkQueue = sinkQueue;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest) {

            HttpRequest request = (HttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            GBWScanResultServerData serverData = new GBWScanResultServerData(request);

            GBWScannerResult result = GBWScanResultFactory.make(serverData);

            if(result!=null)
                sinkQueue.put(result);

            sendResponse(ctx,request.uri());
        }


    }

    private static void sendResponse(ChannelHandlerContext ctx,String uri){

        String msg = "<html><head><title>test</title></head><body>你请求uri为：" + uri+"</body></html>";
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,OK, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE, Unpooled.EMPTY_BUFFER);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();
        ctx.close();
    }

}