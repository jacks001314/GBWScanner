package com.gbw.scanner.server.scan;

import com.gbw.scanner.sink.SinkQueue;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

public class GBWScanResultServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SinkQueue sinkQueue;
    private final SslContext sslCtx;

    public GBWScanResultServerInitializer(SslContext sslCtx, SinkQueue sinkQueue)
    {
        this.sslCtx = sslCtx;
        this.sinkQueue = sinkQueue;

    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        // p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        // p.addLast(new HttpContentCompressor());
        p.addLast(new GBWScanResultServerHandler(sinkQueue));
    }
}