package xwx.springIOC;

import factory.SingletonFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import xwx.annotation.xRpcReference;
import xwx.annotation.xRpcService;
import xwx.dto.RpcServiceConfig;
import xwx.provider.ServiceProvider;
import xwx.provider.impl.ZKServiceProvider;
import xwx.proxy.RpcClientProxy;
import xwx.remoting.transport.RpcRequestTransport;
import xwx.remoting.transport.netty.client.NettyRpcClient;

import java.lang.reflect.Field;

/**
 * @author : xwx
 * @date : 2022/8/23 下午2:47
 */
@Slf4j
@Component
public class MyBeanAfterProcessor implements BeanPostProcessor {

    //save target object and publish services to zookeeper
    private ServiceProvider serviceProvider;

    private RpcRequestTransport rpcRequestTransport;

    public MyBeanAfterProcessor(){
        serviceProvider= SingletonFactory.getSingletonInstance(ZKServiceProvider.class);
        rpcRequestTransport=SingletonFactory.getSingletonInstance(NettyRpcClient.class);
    }

    /**
     * register service to zookeeper and save the target object locally
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if(clazz.isAnnotationPresent(xRpcService.class)){
            xRpcService xRpcService = clazz.getAnnotation(xRpcService.class);
            RpcServiceConfig rpcServiceConfig=RpcServiceConfig.builder()
                    .service(bean)
                    .group(xRpcService.group())
                    .version(xRpcService.version()).build();
            log.info("publish service :{}",bean.getClass());
            //register service to zookeeper and save the target object locally
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    /**
     * bind a proxy to the object reference
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        //get all the fields of the bean
        Field[] fields = clazz.getDeclaredFields();
        int len=fields.length;
        for(int i = 0 ; i < len ; i++){
            Field curField=fields[i];
            xRpcReference xRpcReference = curField.getAnnotation(xRpcReference.class);
            if(xRpcReference != null){
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                        .group(xRpcReference.group())
                        .version(xRpcReference.version())
                        .build();
                //make som configuration
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
                //create a proxy
                Object proxy = rpcClientProxy.getProxy(curField.getType());
                curField.setAccessible(true);
                try {
                    //set a new value (a proxy object)
                    curField.set(bean,proxy);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return bean;
    }
}
