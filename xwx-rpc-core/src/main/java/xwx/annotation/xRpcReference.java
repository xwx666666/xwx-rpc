package xwx.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author : xwx
 * @date : 2022/8/23 下午3:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Component
@Inherited
public @interface xRpcReference {

    String group() default "";

    String version() default "";

}
