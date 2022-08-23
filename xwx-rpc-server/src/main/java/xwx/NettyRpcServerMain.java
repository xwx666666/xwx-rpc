package xwx;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import xwx.remoting.transport.netty.server.NettyRpcServer;

/**
 * @auther 薛文轩
 * @data 2022/8/20 21:57
 */
//the class is a configuration class, so @Configuration can be removed
@ComponentScan(value={"xwx"})
public class NettyRpcServerMain {


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext(NettyRpcServerMain.class);
//        HelloService bean = ctx.getBean(HelloService.class);
//        if(bean.getClass().isAnnotationPresent(xRpcService.class)){
//            System.out.println("here");
//        }
        new NettyRpcServer().start();
//        String hello = bean1.helloService.hello("1");
//        System.out.println(hello);

    }


//    public static void main(String[] args) {
//        HelloServiceImpl helloService = SingletonFactory.getSingletonInstance(HelloServiceImpl.class);
//        ZKServiceProvider ZKServiceProvider = SingletonFactory.getSingletonInstance(xwx.provider.impl.ZKServiceProvider.class);
//        RpcServiceConfig rpcServiceConfig=RpcServiceConfig.builder().service(helloService).group("").version("").build();
//        ZKServiceProvider.publishService(rpcServiceConfig);
//        new NettyRpcServer().start();
//    }
}
