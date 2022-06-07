package Com.Programing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpChatServer {
    public static void main(String[] args) {

        // 서버소켓

        TcpChatServerManager tcsm = new TcpChatServerManager();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket socket = serverSocket.accept(); // 클라이언트 연결요청 대기(연결요청 오기전엔 멈춤)
                // -> 연결요청오면? 소켓을 반환
                // 멀티 클라이언트 -> 소켓이 여러개 -> 리스트로 관리
                // -> 서버 매니져 클래스로 관리
                
                System.out.println(socket.getRemoteSocketAddress() + " : 연결");
                tcsm.addSocket(socket); // 얻은 소켓 서버매니져의 소켓목록에 추가
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}


