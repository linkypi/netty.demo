package org.lynch.thrift;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class ThriftServer {
    public static void main(String[] args)throws Exception {

        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg  = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);

        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
        arg.protocolFactory(new TCompactProtocol.Factory());
        arg.transportFactory(new TFastFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        THsHaServer server = new THsHaServer(arg);
        System.out.println("thrift 服务已启动.");
        server.serve();


    }
}
