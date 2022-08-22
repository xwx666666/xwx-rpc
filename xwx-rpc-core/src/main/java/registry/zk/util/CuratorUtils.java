package registry.zk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : xwx
 * @date : 2022/8/22 下午3:09
 */
@Slf4j
public class CuratorUtils {

    private static String ZK_ROOT="/xwx-rpc/";
    private static CuratorFramework zkClient;
    private static Set<String> REGISTERED_PATH_SET= ConcurrentHashMap.newKeySet();
    private static Map<String,String> SERVICE_ADDRESS_MAP=new ConcurrentHashMap<>();
    /**
     * get connection to zookeeper
     * @return
     */
     static CuratorFramework getZkClient(){
         if(zkClient!=null && zkClient.getState()== CuratorFrameworkState.STARTED){
             return zkClient;
         }
         RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
         CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                 .retryPolicy(retryPolicy)
                 .build();
         //start to connect
         zkClient.start();
         try {
             if(!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)){
                 throw new RuntimeException("get zookeeper connection failed with 30s");
             }
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return zkClient;
     }

    /**
     *
     * @param zkClient
     * @param path /xwx-rpc/HelloService/127.0.0.1:2181
     */
     public static void createPersistentNode(CuratorFramework zkClient,String path){
         try {
             if(REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path)!=null){
                 log.info("the node already exists");
             }else{
                 zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     public static List<String> getChildNodes(CuratorFramework zkClient,String serviceName){
            if(SERVICE_ADDRESS_MAP.containsKey(serviceName)){

            }
            return null;
     }




}
