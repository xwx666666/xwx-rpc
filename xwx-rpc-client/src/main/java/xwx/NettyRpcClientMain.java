package xwx;

import api.HelloService;
import dto.RpcServiceConfig;
import lombok.extern.slf4j.Slf4j;
import proxy.RpcClientProxy;
import remoting.transport.RpcRequestTransport;
import remoting.transport.netty.client.NettyRpcClient;

import java.util.UUID;

/**
 * @author : xwx
 * @date : 2022/8/18 下午3:27
 */
@Slf4j
public class NettyRpcClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, new RpcServiceConfig());
        HelloService helloService=rpcClientProxy.getProxy(HelloService.class);
        String ret1 = helloService.hello("1");
        String ret2 = helloService.hello("i am xwx");
        log.info("helloService.hello(1) --result : {}",ret1);
        log.info("helloService.hello(i am xwx) --result : {}",ret2);
    }
}
