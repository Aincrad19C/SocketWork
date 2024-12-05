import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
   public static void main(String[] args) {
      try {
         ServerSocket ss = new ServerSocket(8080);
         System.out.println("HTTP Server is listening on port 8080...");

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

         // 读取HTTP请求的第一行
         String requestLine = br.readLine();
         if (requestLine == null) {
            return;
         }

         // 解析请求方法和路径
         String[] requestParts = requestLine.split(" ");
         String method = requestParts[0];
         String path = requestParts[1];

         // 根据请求方法处理请求
         if ("GET".equalsIgnoreCase(method)) {
            handleGet(path, bw);
         } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(br, bw);
         }

         // 客户端断开连接时，关闭资源
         System.out.println("Client " + socket.getInetAddress().getHostAddress() + " disconnected.");
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void handleGet(String path, BufferedWriter bw) throws IOException {
      // 这里只是一个示例，实际中你可能需要根据路径返回不同的响应
      bw.write("HTTP/1.1 200 OK\r\n");
      bw.write("Content-Type: text/plain\r\n");
      bw.write("\r\n");
      bw.write("GET request for " + path);
      bw.flush();
   }

   private void handlePost(BufferedReader br, BufferedWriter bw) throws IOException {
      // 读取POST请求的正文
      StringBuilder body = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null && !line.isEmpty()) {
         body.append(line).append("\n");
      }

      // 发送响应
      bw.write("HTTP/1.1 200 OK\r\n");
      bw.write("Content-Type: text/plain\r\n");
      bw.write("\r\n");
      bw.write("POST request with body: " + body.toString());
      bw.flush();
   }
}