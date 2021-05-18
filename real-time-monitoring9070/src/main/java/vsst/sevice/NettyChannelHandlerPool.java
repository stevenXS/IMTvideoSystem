package vsst.sevice;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
/**
* @description: 通道组池，管理所有websocket连接
* @author: Ziqiang Lee
* @date: 2021/3/20
*/
public class NettyChannelHandlerPool {
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
