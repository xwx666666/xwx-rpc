package xwx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : xwx
 * @date : 2022/8/18 下午3:27
 */
@Slf4j
@ComponentScan(value = "xwx")
public class NettyRpcClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext(NettyRpcClientMain.class);
        TestController bean = ctx.getBean(TestController.class);
        bean.useHello();
//        String ret1 = helloService.hello("1");
//        String ret2 = helloService.hello("i am xwx");
//        log.info("helloService.hello(1) --result : {}",ret1);
//        log.info("helloService.hello(i am xwx) --result : {}",ret2);
    }
}
