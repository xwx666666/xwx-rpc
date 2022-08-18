package proxy;
import dto.RpcRequest;
import dto.RpcResponse;
import dto.RpcServiceConfig;
import lombok.extern.slf4j.Slf4j;
import remoting.transport.netty.NettyRpcClient;
import remoting.transport.netty.RpcRequestTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 *  through the proxy,the user call the remote method as if calling the local method
 * @author : xwx
 * @date : 2022/8/18 下午3:44
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private RpcRequestTransport rpcRequestTransport;
    private RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport,RpcServiceConfig rpcServiceConfig){
        this.rpcRequestTransport=rpcRequestTransport;
        this.rpcServiceConfig=rpcServiceConfig;
    }

    public <T> T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
    }

    /**
     * when the user call one local method,the user call this method actually
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request=RpcRequest.builder()
                .requestId(UUID.randomUUID().clockSequence())
                .interfaceName(rpcServiceConfig.getServiceName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcResponse response = (RpcResponse)rpcRequestTransport.sendRpcRequest(request);
        log.info("receive message from the server : {}",response.getData());
        //TODO Check which type of the response it is
        return response.getData();
    }
}
