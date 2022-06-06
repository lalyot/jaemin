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
    //�����Ӱ� ����Ϸ��� ���⿡ �ʵ�� �����ؾ� �Ѵ�
    //ä��â �������� �����ϴ� ������Ʈ
    //textarea ���� �̻��� ���� �Է� �����ֱ�
    
    private JTextArea textarea;
    private JTextField sendMsgTf;
    private JScrollPane scrollPane;
    //�������� ����� ���� ����
    private Socket socket;
    private BufferedWriter bw;
    
    public ClientFrame() {
        textarea = new JTextArea();
        sendMsgTf = new JTextField();
        textarea.setEditable(false);//���⸦ ������ edit �� �� ���� ����
        
        scrollPane = new JScrollPane(textarea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //As needed �� �ʿ信���ؼ� ������ �������� ��ũ�� �ٰ� �����
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //���� ��ũ���� �ȸ����
        
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("chatting");
    
        sendMsgTf.addKeyListener(new MsgSendListener());
        //�ؽ�Ʈ �ʵ忡 Ű �����ʸ� ���
        //�ؽ�Ʈ �ʵ带 ���Ѻ��� �ִٰ� Ư�� ��Ȳ�� ����
        //�̺�Ʈ �����ʿ� ���ǵ� ���� ����
        add(scrollPane,BorderLayout.CENTER);//�����ӿ� ���̱�
        //add(textarea,BorderLayout.CENTER);//�����ӿ� ���̱�
        add(sendMsgTf,BorderLayout.SOUTH);//�����ӿ� ���̱�    
    }
    //���� ������ ���� ����
    //���� �����ӵ� ������ ������ ������ �Ǿ���
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            OutputStream out = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 


    //���� Ŭ������ �̺�Ʈ ������ �����
    
    class MsgSendListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            
        }
        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {//Ű�� ���ȴٰ� ����������
            //����Ű�� ���ȴٰ� �������� �ؽ�Ʈ �ʵ忡 �ִ� ������ �ؽ�Ʈ ���� ��Ÿ����
            if (e.getKeyCode()==KeyEvent.VK_ENTER) {//������ Ű���� ������ �ִ� �ڵ� ���� ��Ÿ����
                //VK_ENTER = ��� , ���� Ű�� ���� Ű���� �ǹ��Ѵ�
                String msg = sendMsgTf.getText();
                System.out.println(msg);
                textarea.append("[ �� ]: "+msg+"\n");
                sendMsgTf.setText("");
                try {
                    bw.write(msg+"\n");
                    bw.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }//�ѹ����� �����ٴ� ���� �˸��� ���ؼ� bufferedWriter�� "\n"�� ���δ�
                
            }
            
        }
    }
    //���� Ŭ������ ���� ������ �ۼ�
    class TcpClientReceiveThread implements Runnable {
        private Socket socket;
        public TcpClientReceiveThread(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            //�����κ��� ���� �޼����� �о
            //�ؽ�Ʈ ���� �߰��ϱ�
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String msg = br.readLine();//�޼��� ���� �о����
                    textarea.append("[����]" + msg + "\n");                    
                }
            } catch (Exception e) {
                textarea.append("������ ����Ǿ����ϴ�.");
                //System.out.println("������ ����Ǿ����ϴ�.");
            }
            finally {                
                try {
                    if (socket!=null&&!socket.isClosed()) {
                    socket.close();//�� �� ���� �ݱ�            
                    }
                } catch (Exception e2) {
                    
                }
            }
        }
    }

    public static void main(String[] args) {
        
        try {
            //���� ������ , ��Ʈ��ȣ -> ���� ���� -> ���� ��û
            Socket socket = new Socket("10.10.10.134", 5000);
            //���� ��ü ����
            ClientFrame cf = new ClientFrame();
            cf.setSocket(socket);//���ο��� ������ ����
            TcpClientReceiveThread th1 = cf.new TcpClientReceiveThread(socket);
            //TcpClientReceiveThread�� ���� Ŭ������ ����Ǿ� �ֱ� ������
            //cf�� �����ؼ� socket�� �����Ѵ�
            new Thread(th1).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}


