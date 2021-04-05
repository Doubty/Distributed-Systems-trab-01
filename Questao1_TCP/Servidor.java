package Questao1_TCP;


import java.io.IOException;

import java.io.PrintStream;
import java.net.Socket;




public class Servidor implements Runnable{
public Socket cliente;
Thread escutaNo;
int index;
public Servidor(Socket cliente, int index) throws IOException{
	this.index = index;
	this.cliente = cliente;
	System.out.println("No servidor: "+ServerToken.nos.size());
	iniciarServidor();
}

public void iniciarServidor() throws IOException {	
	PrintStream saida;
	saida = new PrintStream(cliente.getOutputStream());
	saida.println("—"+index);
}

@Override
public void run() {
	// TODO Auto-generated method stub
	
}

 
}