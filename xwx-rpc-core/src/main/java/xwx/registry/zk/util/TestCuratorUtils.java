package xwx.registry.zk.util;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;

/**
 * @author : xwx
 * @date : 2022/8/23 上午11:15
 */
public class TestCuratorUtils {
    public static void main(String[] args) throws Exception {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
//        List<String> childNodes = CuratorUtils.getChildNodes(zkClient, "api.HelloService");
        List<String> strings = zkClient.getChildren().forPath("/xwx-rpc/xwx.api.HelloService");
        System.out.println(strings);
        System.out.println(zkClient.delete().deletingChildrenIfNeeded().forPath("/xwx-rpc/xwx.api.HelloService"));
    }
}
