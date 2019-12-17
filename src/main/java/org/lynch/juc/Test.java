package org.lynch.juc;

import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Test {
    public static void main(String[] args) {

        Person person = new Person();

        AtomicIntegerFieldUpdater<Person> atomicIntegerFieldUpdater =
                AtomicIntegerFieldUpdater.newUpdater(Person.class,"age");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println(person.age++);
                System.out.println(atomicIntegerFieldUpdater.incrementAndGet(person));
            }).start();
        }
    }

    public static class Person{
        volatile int age;
    }
}
