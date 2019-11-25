package org.lynch.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args)throws  Exception {
        Socket socket = new Socket("localhost", 7000);
        String filename = "G:\\java\\nettydemo\\src\\main\\22.zip";
        FileInputStream fileInputStream = new FileInputStream(filename);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount= fileInputStream.read(buffer))>=0){
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.printf("发送总字节数：%s, 耗时：%s",total,System.currentTimeMillis() - startTime);

        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
