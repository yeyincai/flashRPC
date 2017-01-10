package core;

import com.flashrpc.core.client.ClientBuilder;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.net.InetSocketAddress;

/**
 * Created by yeyc on 2017/1/10.
 */
@State(value = Scope.Benchmark)
public class ClientHello {
    static  final Hello hello = ClientBuilder.builderClass(Hello.class).forAddress(InetSocketAddress.createUnresolved("127.0.0.1", 8888)).build();
}
