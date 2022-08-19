package factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * use the factory to get singleton object
 * @author : xwx
 * @date : 2022/8/19 上午9:47
 */
public class SingletonFactory {
    private static final Map<String,Object> SINGLETON_OBJECT_MAP=new ConcurrentHashMap<>();

    public static <T> T getSingletonInstance(Class<T> clazz){
        if(clazz==null){
            throw new IllegalArgumentException();
        }
        String clazzStr=clazz.toString();
        if(SINGLETON_OBJECT_MAP.containsKey(clazzStr)){
            return clazz.cast(SINGLETON_OBJECT_MAP.get(clazzStr));
        }else{
            //thread safe
            return clazz.cast(SINGLETON_OBJECT_MAP.computeIfAbsent(clazzStr,k->{
                try {
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }));
        }
    }
}
