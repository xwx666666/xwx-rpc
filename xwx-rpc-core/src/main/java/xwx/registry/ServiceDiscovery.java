package xwx.registry;

import xwx.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author : xwx
 * @date : 2022/8/19 上午11:05
 */
public interface ServiceDiscovery {

    InetSocketAddress looupService(RpcRequest rpcRequest);

}
