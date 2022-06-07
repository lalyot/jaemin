package Com.Programing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class TcpChatServerManager {
    private List<Socket> socketList;

    // 생성자 : TCSM 객체를 생성하면 소켓리스트 하나 만듬
    public TcpChatServerManager() {
        socketList = new ArrayList<Socket>();
    }

    // 소켓 추가 메소드
    public void addSocket(Socket socket) {
        this.socketList.add(socket);
        new Thread(new ReceiverThread(socket)).start();
    }

    // 멀티클라이언트와 연결을 동시에 유지하기 위한 스레드 구성
    // 각각의 소켓정보를 가지고 있어야함.
    class ReceiverThread implements Runnable {
        private Socket socket; // 소켓

        public ReceiverThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                // 클라이언트가 보낸 메시지 읽기 위한 작업
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {

                    String msg = br.readLine(); // 클라이언트가 보낸 메시지 읽기
                    System.out.println(msg);

                    // 받은 메시지를 메시지 보낸 클라이언트 제외하고
                    // 모든 클라이언트한테 보내기

                    Socket tmpSocket = null;
                    try {                        
                        for (int i = 0; i < socketList.size(); i++) { // 소켓리스트를 순회하면서
                            tmpSocket = socketList.get(i);
                            // socketList.get(0) -> 소켓 객체
                            
                            if (socket.equals(tmpSocket)) continue;
                            // 메시지를 보낸 클라이언트라면 반복을 한번 건너뛰기
                            
                            // 서버가 받은 메시지를 클라이언트에 송신하기
                            BufferedWriter bw = new BufferedWriter(
                                    new OutputStreamWriter(tmpSocket.getOutputStream()));
                            
                            bw.write(msg + "\n");
                            bw.flush();
                        }
                    }catch(Exception e) {
                        System.out.println(tmpSocket.getRemoteSocketAddress() + "연결 종료");
                        socketList.remove(tmpSocket); 
                        //연결을 끊은 클라이언트를 위한 소켓 제거
                        
                        System.out.println("===============현재참여자=================");
                        for(Socket s : socketList) {
                            System.out.println(s.getRemoteSocketAddress());
                        }
                        System.out.println("==========================================");
                        
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
            finally {
                if(socket != null) {
                    System.out.println(socket.getRemoteSocketAddress() + "연결 종료");
                    socketList.remove(socket); 
                    //연결을 끊은 클라이언트를 위한 소켓 제거
                    
                    System.out.println("===============현재참여자=================");
                    for(Socket s : socketList) {
                        System.out.println(s.getRemoteSocketAddress());
                    }
                    System.out.println("==========================================");
                }
            }

        }

    }
}

