package xwx.remoting.transport.netty.client;

import xwx.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * requests which have not been processed by the server
 * @author : xwx
 * @date : 2022/8/19 下午1:19
 */
public class UnprocessedRequests {

    private final Map<String, CompletableFuture<RpcResponse>> UnprocessedRequests_MAP=new ConcurrentHashMap<>();

    public void add(String requestId,CompletableFuture<RpcResponse> completableFuture){
        UnprocessedRequests_MAP.put(requestId,completableFuture);
    }

    public void complete(String requestId,RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> completableFuture = UnprocessedRequests_MAP.remove(requestId);
        if(completableFuture!=null){
            completableFuture.complete(rpcResponse);
        }else{
            throw new IllegalStateException();
        }
    }

}
