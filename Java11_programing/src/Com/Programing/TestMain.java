package Com.Programing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TestMain {
	public static void main(String[] args) throws IOException {

		/** �������� ���� */
		ServerSocket server = new ServerSocket(80);
		System.out.println("Http Server ����");

		try {
			while (true) {
				/** Ŭ���̾�Ʈ ��û�� �� ������ ��� */
				Socket socket = server.accept();

				try {
					/** Ŭ���̾�Ʈ ��û ������ �б� ���� ���� �۾� */
					BufferedReader readRequest
						= new BufferedReader(new InputStreamReader(socket.getInputStream()));

					String request = "";
					while (true) {
						String readLine = readRequest.readLine();
						if (readLine == null || readLine.equals("")){ break; }
						/** Ŭ���̾�Ʈ ��û���� �ܼ�ȭ�鿡 ��Ÿ���� */
						System.out.println(readLine);
						/** GET���� ������ �� request�� ��� - Ŭ���̾�Ʈ ��û URL */
						if(readLine.startsWith("GET")){  request = readLine; }
					}

					/** �����ڷ� �����ϱ� ���� �۽� �۾� */
					DataOutputStream dos
						= new DataOutputStream(socket.getOutputStream());

					/** ��������
              - ������ ����Ʈ���̸� ����� ������ ���� �켱 �����Ѵ�.
					*/
					String msg = "<html><body>";
					msg += "<span style='font-size:30pt; color:red; font-weight:bold;'>";
					msg += "HTTP ����";
					msg += "</span>";
					msg += "<br><br><span style='font-weight:bold; font-size:15pt'>";
					Date date = new Date(System.currentTimeMillis());
					msg += date.toString();
					msg += "<br><br>��û���� = " + request;
					msg += "</span>";
					msg += "</body></html>";

					/** ������ ����Ʈ �迭�� ���� */
					byte[] body = msg.getBytes("UTF-8");

					/** ������� ���� - ���������� �ݵ�� \r\n\r\n ���� �Է�*/
					dos.writeBytes("HTTP/1.1 200 OK \r\n");
					dos.writeBytes("Server:MyServerName\r\n");
					dos.writeBytes("Cache-Control:private\r\n");
					dos.writeBytes("Content-Length: " + body.length + "\r\n");
					dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
					dos.writeBytes("\r\n"); /** \r\n\r\n�� ������� ���Ḧ ��Ÿ����. */

					/** ���� ���� */
					dos.write(body, 0, body.length);

					/** flush()�� �̿��Ͽ� �ڷ� �����۽� */
					dos.writeBytes("\r\n");
					dos.flush();

				} finally { socket.close(); }  /** Http�� ��û ���� �� �����Ѵ�. */
			}

		} finally {
			server.close();
		}
	
}


}
