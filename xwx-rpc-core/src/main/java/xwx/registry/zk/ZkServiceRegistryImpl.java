package xwx.registry.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import xwx.registry.ServiceRegistry;
import xwx.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author : xwx
 * @date : 2022/8/19 上午10:30
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        String servicePath=CuratorUtils.ZK_ROOT+"/"+serviceName+inetSocketAddress;
        CuratorUtils.createPersistentNode(zkClient,servicePath);
        log.info("register a service : {},the service address is {}",serviceName,inetSocketAddress);
    }
}
