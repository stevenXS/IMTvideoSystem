package vsst.sevice;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class NettyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static ChannelGroup channelGroup;
    //    private static AtomicInteger count = new AtomicInteger(0);
    public static final Logger logger= LoggerFactory.getLogger(NettyWebSocketHandler.class);
    public static int count=0;
    Date date = new Date();
    // 指定格式输出时间
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    static {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    // 客户端与服务器建立连接的时候触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 设置消息发送的高低水位，针对消息的平均大小、客户端并发接入数、JVM 内存大小进行计算，得出一个合理的高水位取值。
         * 服务端在推送消息时，对 Channel 的状态进行判断，如果达到高水位之后，Channel 的状态会被 Netty 置为不可写，此时服务端不要继续发送消息，防止发送队列积压。
         */
        ctx.channel().config().setWriteBufferHighWaterMark(1024*1024*40);
        logger.info(ctx.channel().remoteAddress()+"Unwritable="+ctx.channel().bytesBeforeUnwritable()+"::::"+"writable="+ctx.channel().bytesBeforeWritable());
        logger.info("与客户端:"+ctx.channel().remoteAddress()+"建立连接!");
//        if (ctx.channel().remoteAddress().toString().contains("202.115.52.99")){
//            return;
//        }
        // 添加到channelGroup通道组
        channelGroup.add(ctx.channel());

    }

    // 客户端与服务器关闭连接的时候触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与客户端"+ctx.channel().remoteAddress()+"断开连接，通道关闭！");
        channelGroup.remove(ctx.channel());
    }

    //继承SimpleChannelInboundHandler<TextWebSocketFrame>类重写的方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg.text());
        channelGroup.writeAndFlush(msg);





        count= ReferenceCountUtil.refCnt(textWebSocketFrame);
        if (count>0){
            ReferenceCountUtil.release(textWebSocketFrame);
        }

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        if (ctx.channel().isActive()){
        System.out.println(cause.getMessage()+"::::::"+cause.getLocalizedMessage()+cause.getCause());
        System.out.println(Thread.currentThread().getName());
        ctx.channel().close();
//        }
    }

    // 给固定的人发消息
    private void sendMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String message = msg.text() + " --- 你好，" + ctx.channel().remoteAddress() + " 给固定的人发消息";
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    // 发送群消息，此时其他客户端也能收到群消息
    private void sendAllMessage(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws InterruptedException {

        System.out.println("接受数据："+msg.text().length());
//        TextWebSocketFrame in = new TextWebSocketFrame(msg.text());
        channelGroup.writeAndFlush(msg.text());
    }

    /**
     * ByteBuf转化为String
     * @param buf
     * @return
     */
    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if(buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }

}
