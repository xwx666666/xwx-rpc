package xwx.proxy;
import xwx.dto.RpcRequest;
import xwx.dto.RpcResponse;
import xwx.dto.RpcServiceConfig;
import lombok.extern.slf4j.Slf4j;
import xwx.remoting.transport.RpcRequestTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
                .requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .group("")
                .version("")
                .build();
        CompletableFuture<RpcResponse> completableFuture = (CompletableFuture)rpcRequestTransport.sendRpcRequest(request);
        RpcResponse rpcResponse = completableFuture.get();
        log.info("receive message from the server : {}",rpcResponse.getData());
        //TODO Check which type of the response it is
        return rpcResponse.getData();
    }
}
