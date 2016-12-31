package core;

import com.flashrpc.core.client.ClientBuilder;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ClientBuilderTest {

    @Test
    public void test() throws InterruptedException {
        InetSocketAddress serviceAddress = InetSocketAddress.createUnresolved("127.0.0.1", 8888);
        Hello hello = ClientBuilder.builderClass(Hello.class).forAddress(serviceAddress).build();
        for (int i = 0; i < 100000; i++) {
            assertThat(hello.sayHello(i+"yyc")).isEqualTo(i+"yyc-hello!");
            System.out.println(i);
        }

        /*AtomicInteger ao = new AtomicInteger(0);
        SimpleExecutor sim = new SimpleExecutor(() -> {
            int i = ao.incrementAndGet();
            assertThat(hello.sayHello(i + "yyc")).isEqualTo(i + "yyc-hello!");
        });

        sim.execute(1, 30);*/
    }
}
