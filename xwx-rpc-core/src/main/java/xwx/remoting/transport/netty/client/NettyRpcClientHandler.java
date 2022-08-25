package xwx.remoting.transport.netty.client;

import enums.SerializationTypeEnum;
import factory.SingletonFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import xwx.dto.RpcMessage;
import xwx.dto.RpcResponse;
import xwx.remoting.constants.RpcConstants;

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
            RpcMessage rpcMessage = (RpcMessage) msg;
            byte messageType = rpcMessage.getMessageType();
            if(messageType==RpcConstants.HEARTBEAT_RESPONSE_TYPE){
                log.info("receive a response to read idle message : [{}]",rpcMessage.getData());
            }else if(messageType==RpcConstants.RESPONSE_TYPE){
                RpcResponse rpcResponse = (RpcResponse) rpcMessage.getData();
                log.info("receive message that contains the results of the call from client : [{}]",rpcResponse.getData());
                unprocessedRequests.complete(rpcResponse.getRequestId(),rpcResponse);
            }
        }
        super.channelRead(ctx, msg);
    }

    /**
     * if there is a write idle event,the method will be called
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()== IdleState.WRITER_IDLE){
//                Channel channel =  nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                log.info("send a WRITER_IDLE to the server: [{}] to keep the channel alive",ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder().codec(SerializationTypeEnum.KYRO.getCode())
                        .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                        .data(RpcConstants.PING).build();
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }

    }
}
