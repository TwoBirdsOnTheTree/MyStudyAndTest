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
            // 读取
            {
                System.out.println("================channelRead================");
                System.out.println("----------------request----------------");
                System.out.print("  -> msg: " + msg + ", msg class: " + msg.getClass().getName());
                System.out.print("\n");
                System.out.print("  ->");
                if (msg instanceof ByteBuf) {
                    ByteBuf byteBufMsg = (ByteBuf) msg;
                    byte[] bytes = new byte[128];
                    while (byteBufMsg.readableBytes() > 0) {
                        int canReadLength = Math.min(byteBufMsg.readableBytes(), bytes.length);
                        byteBufMsg.readBytes(bytes, 0, canReadLength);
                        System.out.print(new String(bytes, 0, canReadLength).replace("\n", "\n  ->"));
                    }
                }
                System.out.println("\n----------------request----------------");
            }
            // 先读取
            // 然后写入响应
            {
                String responseString = "From Netty Server";
                ctx.writeAndFlush(Unpooled.copiedBuffer("HTTP/1.1 200 OK\n" +
                        "Content-Length:" + responseString.getBytes(StandardCharsets.UTF_8).length + "\n" +
                        "Content-Type: application/json;charset=UTF-8\n" +
                        "\n" +
                        "From Netty Server", StandardCharsets.UTF_8));
            }

            // super.channelRead(ctx, msg);
        } finally {
            // 写入会自动释放 // ChannelInboundHandlerAdapter不会自动释放
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
