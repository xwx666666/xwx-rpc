package xwx.remoting.transport.netty.server;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import xwx.dto.RpcMessage;
import xwx.dto.RpcRequest;
import xwx.dto.RpcResponse;
import enums.SerializationTypeEnum;
import factory.SingletonFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import xwx.provider.ServiceProvider;
import xwx.provider.impl.ZKServiceProvider;
import xwx.remoting.constants.RpcConstants;

import java.lang.reflect.Method;

/**
 * handle remote call requests from clients
 * @author : xwx
 * @date : 2022/8/19 下午5:21
 */
@Slf4j
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private ServiceProvider serviceProvider;

    public NettyRpcServerHandler(){
        serviceProvider= SingletonFactory.getSingletonInstance(ZKServiceProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcMessage rpcMessage = (RpcMessage) msg;
        byte messageType = rpcMessage.getMessageType();
        RpcMessage reRpcMessage=null;
        if(messageType == RpcConstants.REQUEST_TYPE){
            RpcRequest rpcRequest = (RpcRequest) (((RpcMessage) msg).getData());
            Object service = serviceProvider.getService(rpcRequest.getServiceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(service, rpcRequest.getParameters());
            //build response
            RpcResponse rpcResponse=RpcResponse.success(result,rpcRequest.getRequestId());
             reRpcMessage=RpcMessage.builder().messageType(RpcConstants.RESPONSE_TYPE)
                    .codec(SerializationTypeEnum.KYRO.getCode())
                    .compress((byte)0)
                    .data(rpcResponse).build();
        }else if(messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
            log.info("get a heartbeat from client: [{}]",rpcMessage);
             reRpcMessage=RpcMessage.builder().messageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE)
                    .codec(SerializationTypeEnum.KYRO.getCode())
                    .compress((byte)0)
                    .data(RpcConstants.PONG).build();
        }
        ctx.writeAndFlush(reRpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()== IdleState.READER_IDLE){
                log.info("there is a read idle event,the channel will be closed");
                ctx.close();
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }
}
