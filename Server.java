import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
   public static void main(String[] args) {
      try {
         ServerSocket ss = new ServerSocket(8080);
         System.out.println("Server is listening on port 8080...");

         while (true) {
            Socket s = ss.accept();
            System.out.println("Client " + s.getInetAddress().getHostAddress() + " connected.");

            // 为每个客户端连接创建一个新的线程
            new Thread(new ClientHandler(s)).start();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}

class ClientHandler implements Runnable {
   private Socket socket;

   public ClientHandler(Socket socket) {
      this.socket = socket;
   }

   @Override
   public void run() {
      try {
         InputStream is = socket.getInputStream();
         OutputStream os = socket.getOutputStream();

         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

         // 使用循环来持续读取客户端发送的消息
         String message;
         while ((message = br.readLine()) != null) {
            System.out.println("Client: " + message);
            // process
            








            // 处理：将接收到的消息原样发送回客户端
            bw.write(message + " received\n");
            bw.flush();
         }

         // 客户端断开连接时，关闭资源
         System.out.println("Client " + socket.getInetAddress().getHostAddress() + " disconnected.");
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
