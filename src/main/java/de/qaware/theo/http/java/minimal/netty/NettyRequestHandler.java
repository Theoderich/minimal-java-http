package de.qaware.theo.http.java.minimal.netty;

import com.google.gson.Gson;
import de.qaware.theo.http.java.minimal.Customer;
import de.qaware.theo.http.java.minimal.DbConnector;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;
import java.util.List;

class NettyRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final DbConnector dbConnector;
    private final Gson gson;

    public NettyRequestHandler(DbConnector dbConnector, Gson gson) {
        this.dbConnector = dbConnector;
        this.gson = gson;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        List<Customer> allCustomers = dbConnector.getAllCustomers();
        ByteBuf buffer = Unpooled.copiedBuffer(gson.toJson(allCustomers), StandardCharsets.UTF_8);

        ctx.writeAndFlush(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer
        ));
    }
}
