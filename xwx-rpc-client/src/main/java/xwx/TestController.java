package xwx;

import org.springframework.stereotype.Component;
import xwx.annotation.xRpcReference;
import xwx.api.HelloService;

/**
 * @author : xwx
 * @date : 2022/8/23 下午4:08
 */
@Component
public class TestController {

    @xRpcReference
    HelloService helloService;
}
