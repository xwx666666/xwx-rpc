package serialize;

/**
 * @author : xwx
 * @date : 2022/8/19 下午2:24
 */
public interface Serializer {

    /**
     * convert object to binary data
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * convert binary data to object
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
