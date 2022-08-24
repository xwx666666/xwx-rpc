package xwx.provider.impl;

import factory.SingletonFactory;
import xwx.dto.RpcServiceConfig;
import xwx.provider.ServiceProvider;
import xwx.registry.ServiceRegistry;
import xwx.registry.zk.ZkServiceRegistryImpl;
import xwx.remoting.transport.netty.server.NettyRpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : xwx
 * @date : 2022/8/19 上午10:04
 */
public class ZKServiceProvider implements ServiceProvider {
    /**
     * key - serviceName
     * value - target object who will call the target method
     */
    private Map<String,Object> serviceMap;
    private ServiceRegistry serviceRegistry;

    public ZKServiceProvider(){
        serviceMap=new ConcurrentHashMap<>();
        serviceRegistry= SingletonFactory.getSingletonInstance(ZkServiceRegistryImpl.class);
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object object = serviceMap.get(rpcServiceName);
        if(object==null){
            throw new RuntimeException("can not find service '"+rpcServiceName+"'");
        }
        return object;
    }

    private void saveServiceInLocal(RpcServiceConfig rpcServiceConfig) {
        if(rpcServiceConfig==null){
            throw new RuntimeException("rpcServiceConfig is null");
        }
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if(serviceMap.containsKey(rpcServiceName)){
              return;
        }
        serviceMap.put(rpcServiceName,rpcServiceConfig.getService());
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            //put the address of current machine under the node of a specific service
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),new InetSocketAddress(host, NettyRpcServer.PORT));
            saveServiceInLocal(rpcServiceConfig);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
