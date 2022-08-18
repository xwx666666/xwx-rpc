package remoting.transport.netty;

import dto.RpcRequest;

/**
 * semd rpcRequest
 * @author : xwx
 * @date : 2022/8/18 下午4:43
 */
public interface RpcRequestTransport {

    /**
     * send rpcRequest and get results
     * @param rpcRequest
     * @return
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

}
