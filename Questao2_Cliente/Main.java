package Questao2_Cliente;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Main {

	int cont = 0;
	JFrame jframe;
	
	Socket cliente=null;
	
	JTextField mensagem;
	JTextArea historico;
	JComboBox<String> listaDeClientes;
	int posicao = -1;
	JLabel titulo;
	public Main() {
		jframe = new JFrame("Chat Cliente");
		jframe.setPreferredSize(new Dimension(400, 500));		
		jframe.setLocationRelativeTo(null);	
		//jframe.getContentPane().setBackground(Color.white);
		jframe.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		//Definição de titulo
		titulo=new JLabel("Chat Cliente");
		titulo.setFont(new java.awt.Font("Tahoma", 1, 28));
		titulo.setBounds(100,15, 340, 40);

		jframe.add(titulo);



		//Definir layout em grid
		JPanel panel = new JPanel();
		panel.setBounds(0, 100, 400, 400);
		GridLayout experimentLayout = new GridLayout(3,1);		
		panel.setLayout(experimentLayout);               


		JPanel pnl1 = new JPanel();
		pnl1.setLayout(new GridBagLayout());		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.8;
		c.gridx = 0;
		c.gridy = 0;
		mensagem= new JTextField();
		mensagem.setFont(new java.awt.Font("Tahoma", 1, 16));		
		pnl1.add(mensagem,c);               

		listaDeClientes =new JComboBox<String>();
		pnl1.add(listaDeClientes);
		
		panel.add(pnl1);
		
		JPanel pnl2 = new JPanel();
		pnl2.setLayout(new GridBagLayout());	
		
		c.weightx = 0.1;
		c.gridx = 1;
		c.gridy = 0;
		JButton enviar = new JButton("Enviar");
		enviar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					enviar();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
		});

		pnl2.add(enviar,c);        

		c.weightx = 0.1;
		c.gridx = 1;
		c.gridy = 0;
		
		JButton enviarParaTodos = new JButton("Enviar para todos");
		enviarParaTodos.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					enviarParaTodos();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
		});
		
		pnl2.add(enviarParaTodos);
		

		panel.add(pnl2,c);

		JPanel pnl3 = new JPanel();
		pnl3.setLayout(null);				

		historico = new JTextArea();
		historico.setEditable(false); // set textArea non-editable

		JScrollPane scroll = new JScrollPane(historico, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);		
		scroll.setBounds(0, 0, 380, 220);

		pnl3.add(scroll);
		panel.add(pnl3);

		jframe.add(panel);

		jframe.pack();
		jframe.setVisible(true);
		rodarCliente();
	}

	
	private void rodarCliente() {
		try {
			cliente = new Socket("127.0.0.1", 3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress inet = cliente.getInetAddress();
		System.out.println("HostAddress = "+inet.getHostAddress());
		System.out.println("HostName = "+inet.getHostName());

		//Escuta servidor
		Thread t=new Thread(new Runnable() {
			Scanner s = null;
			@Override
			public void run() {
				try {

					s = new Scanner(cliente.getInputStream());
					String mensagemRecebida;
					//Exibe mensagem no console
					while(s.hasNextLine()){
						mensagemRecebida = s.nextLine();
						if (mensagemRecebida.equalsIgnoreCase("fim")) {
							break;
						}else if(mensagemRecebida.substring(0,1).equals("ÿ")) {							
							posicao= Integer.parseInt(mensagemRecebida.substring(1,mensagemRecebida.length()));
							titulo.setText("Chat - Cliente "+posicao);
						}else if(mensagemRecebida.substring(0,1).equals("—")) {	
							System.out.println(mensagemRecebida);
							String valores []=mensagemRecebida.substring(1,mensagemRecebida.length()).split(",");							
							List<String> list = new ArrayList<String>();
							for(int i = 0; i< valores.length ;i++) {
								System.out.println(i);								
									list.add(valores[i]);									
							}
							list.remove(posicao);
							String clientes[] = list.toArray(new String[0]);
							listaDeClientes.setModel(new DefaultComboBoxModel<String>(clientes));
						}else if(mensagemRecebida.substring(0,1).equals("•")) {	
							System.out.println(mensagemRecebida);
							String valores []=mensagemRecebida.substring(1,mensagemRecebida.length()).split(",");
							historico.setText(historico.getText()+"\nCliente "+valores[1]+": "+valores[2]);
						}else
							historico.setText(historico.getText()+"\nPara todos: "+mensagemRecebida);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		t.start();
	}

	public static void main(String [] args) {
		Main classePrincipal = new Main();	

		//classePrincipal.rodarServidor();
	}

	private void enviar() throws IOException{	
		
		//Formato da mensagem:
		//• - Indica que é uma mensagem unicast
		//Cliente destinatario,Cliente de origem,mensagem
		if(listaDeClientes.getSelectedIndex() != -1 && !mensagem.equals("")) {
			String mensagemASerEnviada ="•"+listaDeClientes.getSelectedItem()+","+posicao+","+mensagem.getText();
			if(!mensagemASerEnviada.equals("")) {				
				PrintStream saida;
				saida = new PrintStream(this.cliente.getOutputStream());
				saida.println(mensagemASerEnviada);
				
				historico.setText(historico.getText()+"\nEu: "+mensagem.getText());
				mensagem.setText("");
			}
		}else {
			JOptionPane.showMessageDialog(null, "Nenhum cliente selecionado");
		}
	}

	private void enviarParaTodos() throws IOException{		
		PrintStream saida;
		saida = new PrintStream(this.cliente.getOutputStream());
		saida.println(mensagem.getText());
		mensagem.setText("");
	}		
}


