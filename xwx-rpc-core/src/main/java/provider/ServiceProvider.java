package provider;

import dto.RpcServiceConfig;

/**
 *
 * @author : xwx
 * @date : 2022/8/18 下午5:31
 */
public interface ServiceProvider {

    /**
     * @param rpcServiceConfig the target object which call the target method
     * @return
     */
    Object getService(RpcServiceConfig rpcServiceConfig);

    /**
     * bind the service and the object
     * @param rpcServiceConfig
     * @param object
     */
    void bindServiceInstance(RpcServiceConfig rpcServiceConfig, Object object);

    /**
     * publish the service to zookeeper
     * @param rpcServiceConfig
     */
    void publishService(RpcServiceConfig rpcServiceConfig);
}
