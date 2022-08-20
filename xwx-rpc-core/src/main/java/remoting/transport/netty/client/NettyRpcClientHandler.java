package remoting.transport.netty.client;

import dto.RpcMessage;
import dto.RpcResponse;
import factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : xwx
 * @date : 2022/8/19 下午3:23
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private final NettyRpcClient nettyRpcClient;
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClientHandler(){
        nettyRpcClient= SingletonFactory.getSingletonInstance(NettyRpcClient.class);
        unprocessedRequests=SingletonFactory.getSingletonInstance(UnprocessedRequests.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage){
            RpcResponse rpcResponse = (RpcResponse) ((RpcMessage)msg).getData();
            log.info("receive message that contains the results of the call from client : [{}]",rpcResponse.getData());
            unprocessedRequests.complete(rpcResponse.getRequestId(),rpcResponse);
        }
        super.channelRead(ctx, msg);
    }
}
