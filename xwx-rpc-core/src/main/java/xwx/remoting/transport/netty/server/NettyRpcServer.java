package xwx.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import xwx.registry.zk.util.CuratorUtils;
import xwx.remoting.codec.RpcMessageDecoder;
import xwx.remoting.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;

/**
 * @author : xwx
 * @date : 2022/8/19 上午10:46
 */
@Slf4j
public class NettyRpcServer {
    public static final int PORT = 9998;

    public void start(){
        CuratorUtils.registerShutdownHook(CuratorUtils.getZkClient(),new InetSocketAddress("127.0.0.1",PORT));
        new NioEventLoopGroup();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new NettyRpcServerHandler());
                        }
                    }).bind(PORT)
                    .sync();
            //wait for closing
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            log.error("shutdown bossGroup and workerGroup");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

}
