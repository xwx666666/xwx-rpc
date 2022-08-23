package xwx.remoting.transport.netty.client;

import xwx.dto.RpcMessage;
import xwx.dto.RpcRequest;
import xwx.dto.RpcResponse;
import enums.SerializationTypeEnum;
import factory.SingletonFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import xwx.registry.ServiceDiscovery;
import xwx.registry.zk.ZkServiceDiscoveryImpl;
import xwx.remoting.codec.RpcMessageDecoder;
import xwx.remoting.codec.RpcMessageEncoder;
import xwx.remoting.constants.RpcConstants;
import xwx.remoting.transport.RpcRequestTransport;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author : xwx
 * @date : 2022/8/18 下午4:02
 */
public class NettyRpcClient implements RpcRequestTransport {
    private final Bootstrap bootstrap;
    private final ServiceDiscovery serviceDiscovery; //to get the ip address of the service
    private final EventLoopGroup eventLoopGroup;
    private final ChannelProvider channelProvider;
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClient(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap=new Bootstrap()
                  .group(eventLoopGroup)
                  .channel(NioSocketChannel.class)
                  .handler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      protected void initChannel(SocketChannel ch) throws Exception {
                          ChannelPipeline pipeline = ch.pipeline();
                          pipeline.addLast(new RpcMessageEncoder());
                          pipeline.addLast(new RpcMessageDecoder());
                          pipeline.addLast(new NettyRpcClientHandler());
                      }
                  });
        serviceDiscovery= SingletonFactory.getSingletonInstance(ZkServiceDiscoveryImpl.class);
        channelProvider=SingletonFactory.getSingletonInstance(ChannelProvider.class);
        unprocessedRequests=SingletonFactory.getSingletonInstance(UnprocessedRequests.class);
    }

    /**
     * send rpc request to the server
     * 1.getServiceAddress
     * 2.build rpcMessage
     * 3.send rpcMessage(it will be processed by the encoder)
     * 4.get response from server(the response is obtain through the decoder rpcMessage)
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        //look up the server address
        InetSocketAddress inetSocketAddress = serviceDiscovery.looupService(rpcRequest);
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        unprocessedRequests.add(rpcRequest.getRequestId(),resultFuture);
        //build a rpcMessage
        RpcMessage rpcMessage=RpcMessage.builder().messageType(RpcConstants.REQUEST_TYPE)
                                                  .codec(SerializationTypeEnum.KYRO.getCode())
                                                  .compress((byte)0)
                                                  .data(rpcRequest).build();
        try {
            Channel channel = getChannel(inetSocketAddress);
            if(channel.isActive()){
                channel.writeAndFlush(rpcMessage);
            }else{
               throw new RuntimeException("channel is unActive!");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultFuture;
    }


    /**
     * get channel to send request to the server
     * @param inetSocketAddress
     * @return
     * @throws InterruptedException
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) throws InterruptedException {
        Channel channel = channelProvider.get(inetSocketAddress);
        if(channel != null){
            return channel;
        }
        Channel newChannel = doConnect(inetSocketAddress);
        channelProvider.set(inetSocketAddress,newChannel);
        return newChannel;
    }

    // it is a synchronous method,i think it may be optimized by thread pool later
    public Channel doConnect(InetSocketAddress inetSocketAddress) throws InterruptedException {
            //synchronous wait
            ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress).sync();
            return channelFuture.channel();
    }

}
