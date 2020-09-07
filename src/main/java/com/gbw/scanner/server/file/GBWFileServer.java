package com.gbw.scanner.server.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class GBWFileServer {

    private static void run(String downloadDir,String ip,int port, String url) throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            // Added http decoder
                            .addLast("http-decoder", new HttpRequestDecoder())
                            // If the ObjectAggregator decoder is used to convert multiple messages into a single FullHttpRequest or FullHttpResponse
                            .addLast("http-aggregator", new HttpObjectAggregator(65536))
                            // Add http decoder
                            .addLast("http-encoder", new HttpResponseEncoder())
                            // Added chunked to support asynchronously sent streams (large file transfers), but not take up too much memory and prevent jdk memory overflow
                            .addLast("http-chunked", new ChunkedWriteHandler())
                            // Add custom business server handlers
                            .addLast("fileServerHandler", new GBWFileServerHandler(url,downloadDir));
                }
            });
            ChannelFuture future = b.bind(ip, port).sync();
            System.out.printf("HTTP file directory server starts： http://%s:%d%s\n", ip,port , url);
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        String ip = "127.0.0.1";
        int port = 9090;
        String uri = "/";
        String downloadDir = "/opt/data/server/files/";

        Options opts = new Options();
        opts.addOption("ip",true,"bind address");
        opts.addOption("port",true,"bind port");
        opts.addOption("uri",true,"default uri");
        opts.addOption("downloadDir",true,"download file Dir");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWFileServer", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("ip"))
            ip = cliParser.getOptionValue("ip");

        if(cliParser.hasOption("port"))
            port = Integer.parseInt(cliParser.getOptionValue("port"));

        if(cliParser.hasOption("uri"))
            uri = cliParser.getOptionValue("uri");

        if(cliParser.hasOption("downloadDir"))
            downloadDir = cliParser.getOptionValue("downloadDir");

        run(downloadDir,ip,port,uri);

    }

}
