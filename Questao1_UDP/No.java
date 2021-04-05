package Questao1_UDP;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class No {
	
	Thread escutaServidor;
	Thread aguardarConexao;
	Thread escutaVizinho;
	Socket clienteReceber, clienteEnviar,servidor;
	ServerSocket socketServer=null;	
	int index = -1;
	public No () throws IOException, InterruptedException{
		
		 try {
			 servidor = new Socket("localhost", 3000);
			  //clienteEnviar = new Socket();
		 }catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Erro: " + e.getMessage());
		 }
		 
		 
		escutaServidor = new Thread(new Runnable() {			
			Scanner s = null;
			@Override
				public void run() {				
				try {
								
					while(true){																			
						DataInputStream inD = new DataInputStream(servidor.getInputStream());
						String mensagemRecebida;
						//Exibe mensagem no console
							mensagemRecebida = inD.readUTF();
							if (mensagemRecebida.equalsIgnoreCase("fim")) {
								break;
							}else if(mensagemRecebida.substring(0,1).equals("—")) {
								index = Integer.parseInt(mensagemRecebida.substring(1,mensagemRecebida.length()));
								
								socketServer = null;
								socketServer = new ServerSocket(3000+index);
								aguardarConexao.start();
							}else if(mensagemRecebida.substring(0,1).equals("ö")) {
								//Quebrar os valores para pegar endereço e porta do vizinho
								String enderecoEPorta []=mensagemRecebida.substring(1,mensagemRecebida.length()).split(",");						
								clienteEnviar = new Socket(enderecoEPorta[0], Integer.parseInt(enderecoEPorta[1]));
							}							
						}																					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
				
		escutaServidor.start();
		
		aguardarConexao = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Socket s = socketServer.accept();
							clienteReceber = null;
							clienteReceber = s;
							System.out.println("Cliente recebido "+clienteReceber);
							if(!escutaVizinho.isAlive()) 
								escutaVizinho.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		 });
		
		
		escutaVizinho = new Thread(new Runnable() {			
			Scanner s = null;
			@Override
				public void run() {				
				try {
								
					while(true){													
						DataInputStream inD = new DataInputStream(clienteReceber.getInputStream());
						String mensagemRecebida;

						mensagemRecebida = inD.readUTF();
							if (mensagemRecebida.equalsIgnoreCase("fim")) {
								break;
							}else if(mensagemRecebida.indexOf("•") != -1) {
								//mensagem•id1,id2
								String ids [] = mensagemRecebida.substring(mensagemRecebida.indexOf("•")+1,mensagemRecebida.length()).split(",");
								if(Integer.parseInt(ids[0]) == index) {
									System.out.println("Mensagem chegou no processo inicial");
								}else {
									System.out.println("Mensagem recebida: "+ mensagemRecebida.substring(0, mensagemRecebida.indexOf("•")));								
									
									DataOutputStream outDS = new DataOutputStream(clienteEnviar.getOutputStream());
									outDS.writeUTF(mensagemRecebida+index+",");
								}
							}
						}																
					

				} catch (IOException e) {
					e.printStackTrace();
				}
				}
			});
		

		Scanner teclado = new Scanner(System.in);
		String snd;
		try {
			while(true){
				System.out.println("Digite uma mensagem: ");
				snd = teclado.nextLine();
				if(clienteEnviar != null) {
					if (!snd.equalsIgnoreCase("fim"))
						System.out.println(snd);
									
					DataOutputStream outDS = new DataOutputStream(clienteEnviar.getOutputStream());
					outDS.writeUTF(snd+"•"+index+",");
				}else {
					System.out.println("Aguarde a topologia anel ser criada");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}		
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {	
		new No();
	}
}


		