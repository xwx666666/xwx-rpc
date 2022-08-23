package xwx.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : xwx
 * @date : 2022/8/19 上午11:12
 */
@Slf4j
public class ChannelProvider {

    private final Map<InetSocketAddress, Channel> CHANNEL_MAP=new ConcurrentHashMap<>();

    /**
     * get channel from map if the channel was cached
     * @param inetSocketAddress
     * @return
     */
    public Channel get(InetSocketAddress inetSocketAddress){
        Channel channel = CHANNEL_MAP.get(inetSocketAddress);
        if(channel!=null && channel.isActive()){
            log.info("get channel from cache-CHANNEL_MAP");
            return channel;
        }else{
            CHANNEL_MAP.remove(inetSocketAddress);
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress,Channel channel){
        CHANNEL_MAP.put(inetSocketAddress,channel);
    }
}
