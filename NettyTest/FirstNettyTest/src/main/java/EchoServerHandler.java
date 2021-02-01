import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            System.out.println("EchoServerHandler.channelRead调用");
            ByteBuf in = (ByteBuf) msg;
            String msgString = in.toString(CharsetUtil.UTF_8);
            System.out.println("Server received: " + msgString);

            ctx.write(Unpooled.copiedBuffer(("HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html;charset=UTF-8\n" +
                    "Content-Length: " + msgString.getBytes().length + "\n" +
                    "\n" +
                    msgString).getBytes(StandardCharsets.UTF_8)));
            ctx.fireChannelRead(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("EchoServerHandler.channelReadComplete调用");
        // ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        // ctx.pipeline().fireChannelReadComplete();
        ctx.fireChannelReadComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
