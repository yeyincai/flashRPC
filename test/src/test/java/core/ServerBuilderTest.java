package core;

import com.flashrpc.core.Server;
import com.flashrpc.core.ServerBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by yeyc on 2016/12/28.
 */
public class ServerBuilderTest {

    @Test
    public void test() throws InterruptedException {
        Server server = ServerBuilder.forPort(8888).addService(String.class).build();
        server.start();

        TimeUnit.SECONDS.sleep(10);

        server.shutdown();
    }
}
