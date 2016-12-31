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
        assertThat(hello.sayHello("yyc")).isEqualTo("yyc-hello");
    }
}
