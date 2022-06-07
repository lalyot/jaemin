package Com.Programing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpChatServer {
    public static void main(String[] args) {

        // ��������

        TcpChatServerManager tcsm = new TcpChatServerManager();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket socket = serverSocket.accept(); // Ŭ���̾�Ʈ �����û ���(�����û �������� ����)
                // -> �����û����? ������ ��ȯ
                // ��Ƽ Ŭ���̾�Ʈ -> ������ ������ -> ����Ʈ�� ����
                // -> ���� �Ŵ��� Ŭ������ ����
                
                System.out.println(socket.getRemoteSocketAddress() + " : ����");
                tcsm.addSocket(socket); // ���� ���� �����Ŵ����� ���ϸ�Ͽ� �߰�
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}


