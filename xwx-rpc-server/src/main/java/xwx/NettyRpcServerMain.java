package xwx;

import dto.RpcServiceConfig;
import factory.SingletonFactory;
import provider.impl.ZKServiceProvider;
import remoting.transport.netty.server.NettyRpcServer;
import serviceImpl.HelloServiceImpl;

/**
 * @auther 薛文轩
 * @data 2022/8/20 21:57
 */
public class NettyRpcServerMain {
    public static void main(String[] args) {
        HelloServiceImpl helloService = SingletonFactory.getSingletonInstance(HelloServiceImpl.class);
        ZKServiceProvider ZKServiceProvider = SingletonFactory.getSingletonInstance(provider.impl.ZKServiceProvider.class);
        RpcServiceConfig rpcServiceConfig=RpcServiceConfig.builder().service(helloService).group("").version("").build();
        ZKServiceProvider.publishService(rpcServiceConfig);
        new NettyRpcServer().start();
    }
}
