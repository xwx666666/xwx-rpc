package registry.zk;

import dto.RpcRequest;
import registry.ServiceDiscovery;
import remoting.transport.netty.server.NettyRpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author : xwx
 * @date : 2022/8/19 上午11:07
 */
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    @Override
    public InetSocketAddress looupService(RpcRequest rpcRequest) {
        try {
            return new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcServer.PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
