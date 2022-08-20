package compress;


/**
 * @author wangtao .
 * @createTime on 2020/10/3
 */


public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
