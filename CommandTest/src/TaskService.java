import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;


public class TaskService {

    Socket clientSocket;
    OutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    //与服务器端通信 change the information
    public String communicate(String info) {
        String m = "null";
        try {
            clientSocket = new Socket(InetAddress.getByName("localhost"),12345);
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            //写出信息

            objectOutputStream.write(info.getBytes());
            //清空缓冲区 实现输出流
            objectOutputStream.flush();
//            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//            //获取到信息
//            m = (String)objectInputStream.readObject();
            //关闭组件
            clientSocket.close();
//            objectInputStream.close();
            objectOutputStream.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return m;
    }
}