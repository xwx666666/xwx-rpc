package dto;

import lombok.*;

import java.io.Serializable;

/**
 * the message which the client send to the server
 * @auther 薛文轩
 * @data 2022/8/18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1111111141950251207L;
    /**
     * facilitate the processing of the server
     */
    private int requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    /**
     * it is also the data of a service node in zookeeper
     * @return
     */
    public String getServiceName(){
        return interfaceName+group+version;
    }

}
