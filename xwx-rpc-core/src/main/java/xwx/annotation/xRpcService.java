package xwx.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author : xwx
 * @date : 2022/8/23 下午2:25
 */
@Component
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface xRpcService {

    String group() default "";

    String version() default "";
}
