package core;

/**
 * Created by yeyc on 2016/12/31.
 */
public class HelloImpl implements Hello {
    @Override
    public String sayHello(String name) {
        return name.concat("-hello!");
    }
}
