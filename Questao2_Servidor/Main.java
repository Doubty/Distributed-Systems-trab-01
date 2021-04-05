package Questao2_Servidor;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	
	int cont = 0;
	JFrame jframe;
	JLabel qtdLabel;
	public Main() {
		jframe = new JFrame("Servidor");
		jframe.setPreferredSize(new Dimension(400, 300));		
		jframe.setLocationRelativeTo(null);	
		jframe.getContentPane().setBackground(Color.white);
		jframe.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		//JLabel nDeClientes = JLabel();
		
		//Definir layout em grid
		GridLayout experimentLayout = new GridLayout(3,1);		
		jframe.setLayout(experimentLayout);               
		
		JPanel pnl1 = new JPanel();
		pnl1.setLayout(new GridBagLayout());
		JLabel titulo=new JLabel("Servidor de Mensagens");
		titulo.setFont(new java.awt.Font("Tahoma", 1, 28));
		
        pnl1.add(titulo);
        jframe.add(pnl1);
        
        JPanel pnl2 = new JPanel();
		pnl2.setLayout(new GridBagLayout());				
		qtdLabel=new JLabel("Numero de clientes: "+Servidor.cont);
		qtdLabel.setFont(new java.awt.Font("Tahoma", 1, 22));
		
        pnl2.add(qtdLabel);
        jframe.add(pnl2);
        
        JPanel pnl3 = new JPanel();
		pnl3.setLayout(new GridBagLayout());	
		JButton parar = new JButton("Parar servidor");
		parar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		pnl3.add(parar);
        jframe.add(pnl3);
		
        jframe.pack();
		jframe.setVisible(true);
		rodarServidor();
	}

	public static void main(String [] args) {
		Main classePrincipal = new Main();	
		
		//classePrincipal.rodarServidor();
	}

	public void rodarServidor() {
		Thread t=new Thread(new Runnable()	{

			@Override
			public void run() {
				ServerSocket server = null;
				try {
					server = new ServerSocket(3000);
					ArrayList<Socket> clientes = new ArrayList<Socket>();
					while(true){
						System.out.println("Aguardando conexão...");
						Socket con = server.accept();
						System.out.println("Num");
						Servidor.cont++;
						qtdLabel.setText("Numero de clientes:  "+Servidor.cont);
						clientes.add(con);
						System.out.println("Cliente conectado");
						Thread t= new Thread(new Servidor(con, clientes, Servidor.cont));
						t.start();	       
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}


