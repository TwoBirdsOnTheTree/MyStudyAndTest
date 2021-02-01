import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettySecondChannelInboundHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettySecondInboundHandler.channelRead0调用");
        //
        throw new RuntimeException("NettySecondChannelInboundHandler.channelRead0抛出异常");
    }
}
