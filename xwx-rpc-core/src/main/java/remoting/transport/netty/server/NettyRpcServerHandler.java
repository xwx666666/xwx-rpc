package remoting.transport.netty.server;

import dto.RpcMessage;
import dto.RpcRequest;
import dto.RpcServiceConfig;
import factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import provider.ServiceProvider;
import provider.impl.ZKServiceProvider;
import remoting.codec.RpcMessageDecoder;

/**
 * handle remote call requests from clients
 * @author : xwx
 * @date : 2022/8/19 下午5:21
 */
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private ServiceProvider serviceProvider;

    public NettyRpcServerHandler(){
        serviceProvider= SingletonFactory.getSingletonInstance(ZKServiceProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        RpcRequest rpcRequest = (RpcRequest) (((RpcMessage) sg).getData());
//        RpcServiceConfig.builder().group(rpcRequest.getGroup())
//                                  .version(rpcRequest.getVersion())
//                                  .service().build();
//        serviceProvider.getService(rpcRequest)
        super.channelRead(ctx, msg);
    }
}
