import io.netty.channel.*;

public class NettyChannelOutboundHandlerAdapter extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("NettyChannelOutboundHandlerAdapter.write调用");
        Channel channel = ctx.channel();
        ChannelFuture channelFuture = ctx.write(msg, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyChannelOutboundHandlerAdapter.read调用");
        Channel channel = ctx.channel();
        //TODO 不能调用channel.read, why?
        // Channel channelRead = channel.read();
        // System.out.println("NettyChannelOutboundHandlerAdapter.read, channel == channelRead? " + (channel == channelRead));
        super.read(ctx);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("NettyChannelOutboundHandlerAdapter.close调用");
        super.close(ctx, promise);
    }
}
