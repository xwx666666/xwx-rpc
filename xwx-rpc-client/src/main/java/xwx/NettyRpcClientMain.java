package xwx;

import xwx.api.HelloService;
import xwx.dto.RpcServiceConfig;
import lombok.extern.slf4j.Slf4j;
import xwx.proxy.RpcClientProxy;
import xwx.remoting.transport.RpcRequestTransport;
import xwx.remoting.transport.netty.client.NettyRpcClient;

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
