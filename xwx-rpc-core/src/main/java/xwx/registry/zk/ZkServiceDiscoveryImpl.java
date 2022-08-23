package xwx.registry.zk;

import xwx.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import xwx.registry.ServiceDiscovery;
import xwx.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author : xwx
 * @date : 2022/8/19 上午11:07
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    //loadBalance

    /**
     * get address of a service
     * @param rpcRequest
     * @return
     */
    @Override
    public InetSocketAddress looupService(RpcRequest rpcRequest) {
        String serviceName=rpcRequest.getServiceName();
        log.info("look up serviceName :{}",serviceName);
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> childNodes = CuratorUtils.getChildNodes(zkClient, serviceName);
        if(childNodes==null || childNodes.size()==0){
            throw new RuntimeException("no such service");
        }
        //need a loadBalance
        String[] inetStr=childNodes.get(0).split(":");
        String ip=inetStr[0];
        int port=Integer.parseInt(inetStr[1]);
        return new InetSocketAddress(ip, port);
    }
}
