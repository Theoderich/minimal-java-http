package de.qaware.theo.http.java.minimal.netty;

import com.google.gson.Gson;
import de.qaware.theo.http.java.minimal.DbConnector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;

import java.sql.SQLException;

class NettyServerApplication {
    public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        NioEventLoopGroup blockingGroup = new NioEventLoopGroup(10);

        try {
            DbConnector dbConnector = new DbConnector();
            Gson gson = new Gson();

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpServerKeepAliveHandler())
                                    .addLast(new HttpObjectAggregator(4 * 1024))
                                    .addLast(blockingGroup, new NettyRequestHandler(dbConnector, gson));
                        }
                    });
            b.bind("localhost", 8080).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
