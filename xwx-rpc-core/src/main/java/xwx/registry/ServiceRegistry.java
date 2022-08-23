package xwx.registry;

import java.net.InetSocketAddress;

/**
 * register services to registration Center,such as zookeeper
 * @author : xwx
 * @date : 2022/8/19 上午10:22
 */
public interface ServiceRegistry {
    /**
     * @param serviceName interfaceName+version+group
     * @param inetSocketAddress
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
