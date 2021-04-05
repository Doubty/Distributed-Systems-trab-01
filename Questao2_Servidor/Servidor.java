package Questao2_Servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor implements Runnable{
	public Socket cliente;
	static ArrayList<Socket> listaDeClientes;
	public static int cont = 0;

	public Servidor(Socket cliente, ArrayList<Socket> listaDeClientes, int pos) throws IOException{
		this.cliente = cliente;
		this.listaDeClientes = listaDeClientes;
		
		String listString = "";

		//Encaminhar mensagem com a posição do cliente na lista
		encaminharPos("ÿ"+(cont-1));
				
		if(cont > 1) {
		for (int i =0; i<listaDeClientes.size(); i++)
		{			
			if(i != cont)
		    listString += i + ",";
		}				
		
		//Encaminhar lista de clientes
		encaminharBroadcast("—"+listString);
		
		}
	}
	
	@Override
	public void run() {
		try {
			Scanner scan = null;
			scan = new Scanner(this.cliente.getInputStream());
			String mensagem;
		    //Exibe mensagem no console
			while(scan.hasNextLine()){
				mensagem = scan.nextLine();
				if (mensagem.equalsIgnoreCase("fim"))
					break;
				else if(mensagem.substring(0,1).equals("•")) {	
					System.out.println(mensagem);
					String valores []=mensagem.substring(1,mensagem.length()).split(",");
					System.out.println(mensagem);
					encaminhar(mensagem, Integer.parseInt(valores[0]));
				}else {
					encaminharBroadcast(mensagem);
				}
		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void encaminharPos(String mensagem) throws IOException {
		PrintStream saida = new PrintStream(listaDeClientes.get(cont-1).getOutputStream());
		 saida.println(mensagem);
	}
	
	 public void encaminhar(String mensagem, int indexCliente) throws IOException {		 		
			 PrintStream saida = new PrintStream(listaDeClientes.get(indexCliente).getOutputStream());
			 saida.println(mensagem);		 
	 }
	 
	 public void encaminharBroadcast(String mensagem) throws IOException {
		 for(int i=0; i< listaDeClientes.size(); i++) {
			 PrintStream saida = new PrintStream(listaDeClientes.get(i).getOutputStream());
			 saida.println(mensagem);
		 }
	 }
	
}
