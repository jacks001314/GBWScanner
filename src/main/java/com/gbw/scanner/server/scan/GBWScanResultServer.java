package com.gbw.scanner.server.scan;

import com.gbw.scanner.sink.Sink;
import com.gbw.scanner.sink.SinkFactory;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.sink.es.ESSinkQueue;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.KeepAliveThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class GBWScanResultServer {

    private static void init(GBWScanResultServerConfig config, SinkQueue sinkQueue) throws Exception {

        // Configure SSL.
        SslContext sslCtx = null;
        if (config.isSSL()) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new GBWScanResultServerInitializer(sslCtx,sinkQueue));

            Channel ch = b.bind(config.getIp(),config.getPort()).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    (config.isSSL()? "https" : "http") + "://127.0.0.1:" + config.getPort() + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();

            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {

        if(args.length <1){

            System.err.println("Usage: GBWScanResultServer <configName>");
            System.exit(-1);
        }

        GBWScanResultServerConfig config = GsonUtils.loadConfigFromJsonFile(args[1],GBWScanResultServerConfig.class);

        /*Create sinkqueue */
        SinkQueue sinkQueue = new ESSinkQueue(config.getEsSinkConfig());

        /*create sink */
        Sink sink = SinkFactory.create(config,sinkQueue);

        sink.start();

        init(config,sinkQueue);

        /*keepalive main thread*/
        Runtime.getRuntime().addShutdownHook(new Thread("GBWScannerMain-shutdown-hook") {
            @Override
            public void run() {
                System.err.println("GBWScannerMain exit -------------------------------------------------------------------------------kao");;
            }
        });

        KeepAliveThread kpt = new KeepAliveThread("GBWScannerMain");

        kpt.start();
    }
}
