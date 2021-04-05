package Questao1_UDP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerToken {
	static int cont=0;
	static LinkedList<Socket> nos = new LinkedList<Socket>();

	
	public ServerToken()  throws IOException {
		ServerSocket server;
		server = new ServerSocket(3000);

		while(cont<4) {
			
		    Socket no = server.accept();
		    System.out.println("Cliente conectado\n");
		    nos.add(no);	
		    cont++;
		    Servidor s= new Servidor(no, cont);
		    Thread t=new Thread(s);
		    t.start();
		}		
		
		iniciarTopologiaAnel();		
			
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t2.start();
	}
	
	public static void iniciarTopologiaAnel() throws IOException {
		for(int i=0;i<4;i++) {			
			//Para o ultimo nó, enviar o endereço do primeiro nó
			if(i == 3 ) {				
				InetAddress addr = nos.get(0).getInetAddress();				
				
				DataOutputStream outDS = new DataOutputStream(nos.get(i).getOutputStream());
				outDS.writeUTF("ö"+addr.getHostAddress() +","+ (3001));
			}else {				
				InetAddress addr = nos.get(i+1).getInetAddress();			
			
				DataOutputStream outDS = new DataOutputStream(nos.get(i).getOutputStream());
				outDS.writeUTF("ö"+addr.getHostAddress() +","+ (3000+i+2));
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new ServerToken();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
