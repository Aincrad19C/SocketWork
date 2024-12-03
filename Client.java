import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
   public static void main(String[] args) {
      try {
         Socket s = new Socket("127.0.0.1", 8080);

         // 构建IO
         InputStream is = s.getInputStream();
         OutputStream os = s.getOutputStream();

         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
         BufferedReader br = new BufferedReader(new InputStreamReader(is));

         Scanner scanner = new Scanner(System.in);
         System.out.println("Enter 'q' to terminate the connection.");

         // 使用循环来持续读取用户输入并发送到服务器
         while (true) {
            System.out.println("Message to send:");
            String message = scanner.nextLine();

            // 检查是否输入了终止指令
            if ("q".equalsIgnoreCase(message)) {
               System.out.println("Terminating connection...");
               break;
            }

            // 向服务器端发送消息
            bw.write(message + "\n");
            bw.flush();

            // 读取服务器返回的消息
            String response = br.readLine();
            System.out.println("Server: " + response);
         }

         // 关闭资源
         scanner.close();
         bw.close();
         br.close();
         s.close();
      } catch (UnknownHostException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
