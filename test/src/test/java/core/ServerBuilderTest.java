package core;

import com.flashrpc.core.server.Server;
import com.flashrpc.core.server.ServerBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ServerBuilderTest {

    @Test
    public void test() throws InterruptedException {

        Server server = ServerBuilder.forPort(8888).addService(HelloImpl.class).build();
        server.start();
        TimeUnit.SECONDS.sleep(100);
        server.shutdown();
    }
}
