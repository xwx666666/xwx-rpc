package serviceImpl;

import api.HelloService;

/**
 * @author : xwx
 * @date : 2022/8/18 下午4:30
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String option) {
        if("1".equals(option)){
            return "1+1=2";
        }else{
            return "this is a test";
        }
    }
}
