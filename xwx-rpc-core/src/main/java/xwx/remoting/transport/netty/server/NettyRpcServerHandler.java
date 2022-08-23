package xwx.remoting.transport.netty.server;

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
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private ServiceProvider serviceProvider;

    public NettyRpcServerHandler(){
        serviceProvider= SingletonFactory.getSingletonInstance(ZKServiceProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcMessage rpcMessage = (RpcMessage) msg;
        if(rpcMessage.getMessageType()== RpcConstants.REQUEST_TYPE){
            RpcRequest rpcRequest = (RpcRequest) (((RpcMessage) msg).getData());
            Object service = serviceProvider.getService(rpcRequest.getServiceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(service, rpcRequest.getParameters());
            //build response
            RpcResponse rpcResponse=RpcResponse.success(result,rpcRequest.getRequestId());
            RpcMessage reRpcMessage=RpcMessage.builder().messageType(RpcConstants.RESPONSE_TYPE)
                    .codec(SerializationTypeEnum.KYRO.getCode())
                    .compress((byte)0)
                    .data(rpcResponse).build();
            ctx.writeAndFlush(reRpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
        super.channelRead(ctx, msg);
    }
}
