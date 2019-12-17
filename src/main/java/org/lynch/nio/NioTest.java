package org.lynch.nio;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
import sun.awt.geom.AreaOp;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest {
    public static void main(String[] args) {

       int count  = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

       System.out.println("processores : "+ count);

        IntBuffer intBuffer = IntBuffer.allocate(10);

        for (int i=0;i<5;i++){
            int random = new SecureRandom().nextInt(20);
            intBuffer.put(random);
        }
        System.out.println("before flip limit: "+ intBuffer.limit());
        intBuffer.flip();
        System.out.println("after flip limit: "+ intBuffer.limit());
        System.out.println("enter the loop");
        while (intBuffer.hasRemaining()){
            System.out.println("position: "+ intBuffer.position());
            System.out.println("limit: "+ intBuffer.limit());
            System.out.println("capacity: "+ intBuffer.capacity());
            System.out.println(intBuffer.get());
        }
    }


}
