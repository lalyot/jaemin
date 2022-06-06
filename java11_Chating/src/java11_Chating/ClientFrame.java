package java11_Chating;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientFrame extends JFrame{
    //자유롭게 사용하려면 여기에 필드로 선언해야 한다
    //채팅창 프레임을 구성하는 컴포넌트
    //textarea 한줄 이상의 문자 입력 보여주기
    
    private JTextArea textarea;
    private JTextField sendMsgTf;
    private JScrollPane scrollPane;
    //서버와의 통신을 위한 소켓
    private Socket socket;
    private BufferedWriter bw;
    
    public ClientFrame() {
        textarea = new JTextArea();
        sendMsgTf = new JTextField();
        textarea.setEditable(false);//쓰기를 금지함 edit 할 수 없는 상태
        
        scrollPane = new JScrollPane(textarea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //As needed 즉 필요에의해서 내용이 많아지면 스크롤 바가 생긴다
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //가로 스크롤은 안만든다
        
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("chatting");
    
        sendMsgTf.addKeyListener(new MsgSendListener());
        //텍스트 필드에 키 리스너를 등록
        //텍스트 필드를 지켜보고 있다가 특정 상황이 오면
        //이벤트 리스너에 정의된 내용 실행
        add(scrollPane,BorderLayout.CENTER);//프레임에 붙이기
        //add(textarea,BorderLayout.CENTER);//프레임에 붙이기
        add(sendMsgTf,BorderLayout.SOUTH);//프레임에 붙이기    
    }
    //소켓 설정을 위한 세터
    //이제 프레임도 소켓의 정보를 가지게 되었다
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            OutputStream out = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 


    //내부 클래스로 이벤트 리스너 만들기
    
    class MsgSendListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            
        }
        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {//키가 눌렸다가 떼어졌을때
            //엔터키가 눌렸다가 떼어지면 텍스트 필드에 있는 내용이 텍스트 에어리어에 나타나게
            if (e.getKeyCode()==KeyEvent.VK_ENTER) {//각각의 키들이 가지고 있는 코드 값이 나타난다
                //VK_ENTER = 상수 , 엔터 키에 대한 키값을 의미한다
                String msg = sendMsgTf.getText();
                System.out.println(msg);
                textarea.append("[ 나 ]: "+msg+"\n");
                sendMsgTf.setText("");
                try {
                    bw.write(msg+"\n");
                    bw.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }//한문장이 끝났다는 것을 알리기 위해서 bufferedWriter에 "\n"을 붙인다
                
            }
            
        }
    }
    //내부 클래스로 수신 스레드 작성
    class TcpClientReceiveThread implements Runnable {
        private Socket socket;
        public TcpClientReceiveThread(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            //서버로부터 오는 메세지를 읽어서
            //텍스트 에어리어에 추가하기
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String msg = br.readLine();//메세지 한줄 읽어오기
                    textarea.append("[상대방]" + msg + "\n");                    
                }
            } catch (Exception e) {
                textarea.append("연결이 종료되었습니다.");
                //System.out.println("연결이 종료되었습니다.");
            }
            finally {                
                try {
                    if (socket!=null&&!socket.isClosed()) {
                    socket.close();//다 쓴 소켓 닫기            
                    }
                } catch (Exception e2) {
                    
                }
            }
        }
    }

    public static void main(String[] args) {
        
        try {
            //서버 아이피 , 포트번호 -> 소켓 생성 -> 연결 요청
            Socket socket = new Socket("10.10.10.134", 5000);
            //소켓 객체 생성
            ClientFrame cf = new ClientFrame();
            cf.setSocket(socket);//메인에서 프레임 생성
            TcpClientReceiveThread th1 = cf.new TcpClientReceiveThread(socket);
            //TcpClientReceiveThread가 내부 클래스로 선언되어 있기 때문에
            //cf로 접근해서 socket을 전달한다
            new Thread(th1).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}


