package dto;

import lombok.*;

/**
 * @author : xwx
 * @date : 2022/8/18 下午2:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RpcServiceConfig {
    /**
     * upgrade of the same implementation of the interface
     */
    private String version="";
    /**
     * different implementation of the interface
     */
    private String group="";

    /**
     * target object
     */
    private Object service;

    /**
     * @return the service name of the node in zookeeper
     */
    public String getRpcServiceName(){
        return getServiceName()+group+version;
    }

    /**
     *
     * @return the interface name which the service implements
     */
    public String getServiceName(){
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }

}
