##flash RPC
####所用框架
衡量RPC性能三个重要因素:IO模型、线程模型、数据协议

![](http://img.blog.csdn.net/20160726110707643)

我们可以采用以下技术实现高性能rpc
- netty,默认实现异步非阻塞I/O（NIO），提高并发吞吐量
- http2,其多路复用特性可以使得客户端并发调用不需要创建连接池，性能优越
- protostuff,Protostuff基于Google protobuf，提供了更多的功能和更简易的用法
- CachedThreadPool,服务端业务处理线程组采用的是缓存线程组，避免阻塞业务处理


####模块设计
- core核心代码（定义服务端启动，客户端启动，元数据，整合个模块）
- transport-netty传输层封装netty
- codec-http2协议http2
- protocol-protostuff序列化采用的protostuff
- test测试
设计的意图提现模块解耦、高扩展性
