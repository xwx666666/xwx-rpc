package provider.impl;

import dto.RpcServiceConfig;
import factory.SingletonFactory;
import provider.ServiceProvider;
import registry.ServiceRegistry;
import registry.zk.ZkServiceRegistryImpl;
import remoting.transport.netty.server.NettyRpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
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
    private Set<String> serviceNameSet;
    private ServiceRegistry serviceRegistry;

    public ZKServiceProvider(){
        serviceMap=new ConcurrentHashMap<>();
        serviceNameSet=ConcurrentHashMap.newKeySet();
        serviceRegistry= SingletonFactory.getSingletonInstance(ZkServiceRegistryImpl.class);
    }

    @Override
    public Object getService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        Object object = serviceMap.get(rpcServiceName);
        if(object==null){
            throw new RuntimeException("can not find service '"+rpcServiceConfig.getRpcServiceName()+"'");
        }
        return object;
    }


    private void saveServiceInLocal(RpcServiceConfig rpcServiceConfig) {
        if(rpcServiceConfig==null){
            throw new RuntimeException("rpcServiceConfig is null");
        }
        serviceMap.put(rpcServiceConfig.getRpcServiceName(),rpcServiceConfig.getService());
        serviceNameSet.add(rpcServiceConfig.getRpcServiceName());
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),new InetSocketAddress(host, NettyRpcServer.PORT));
            saveServiceInLocal(rpcServiceConfig);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
