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
     * publish the service to zookeeper
     * @param rpcServiceConfig
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
