package core;

import com.flashrpc.core.client.ClientBuilder;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ClientBuilderTest {


    public static void main(String[] args) {

        InetSocketAddress serviceAddress = InetSocketAddress.createUnresolved("127.0.0.1", 8888);
        Hello hello = ClientBuilder.builderClass(Hello.class).forAddress(serviceAddress).build();

      /*  for (int i = 0; i < 100000; i++) {
            assert (hello.sayHello(i+"yyc")).equals(i+"yyc-hello!");
        }*/

        AtomicInteger ao = new AtomicInteger(0);
        SimpleExecutor sim = new SimpleExecutor(() -> {
            int i = ao.incrementAndGet();
            String s = hello.sayHello(i + "yyc");
            if (!s.equals(i + "yyc-hello!")){
                new RuntimeException();
            }
        });

        sim.execute(35, 60);
    }
}
