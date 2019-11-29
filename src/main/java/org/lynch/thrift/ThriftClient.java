package org.lynch.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

public class ThriftClient {
    public static void main(String[] args)throws Exception {
        TFramedTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8899), 600);
        TCompactProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);
        try{
           transport.open();
            Person person = client.getByUserName("张三");

            System.out.println("getByUserName: "+ person.toString());
            Person person1 = new Person();
            person1.setMarried(false);
            person1.setUsername("哒哒");
            person1.setAge(30);
            client.savePerson(person1);
        }finally {
           transport.close();
        }
    }
}
