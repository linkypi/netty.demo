package org.lynch.nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 传统JAVA IO 服务器
public class OldIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(7000);

        while (true){
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            try{
                byte[] arr = new byte[4096];
                while (true){
                    int readCount = dataInputStream.read(arr, 0, arr.length);
                    if(-1==readCount){
                        break;
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
