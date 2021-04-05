package Questao1_UDP;


import java.io.DataOutputStream;
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
	DataOutputStream outDS = new DataOutputStream(cliente.getOutputStream());
	outDS.writeUTF("—"+index);
}

@Override
public void run() {
	// TODO Auto-generated method stub
	
}

 
}