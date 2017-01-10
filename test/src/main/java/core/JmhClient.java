package core;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by yeyc on 2017/1/10.
 */
public class JmhClient {
    @Benchmark
    @Group("JmhClient")
    @GroupThreads(50)
    public void getKey(){
        String s = ClientHello.hello.sayHello("yyc");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .forks(1)
                .include(JmhClient.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
