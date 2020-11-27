package Test;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import localApp.LocalSystemConfig;

public class MainClass {

	public static void main(String[] args) {
		 /*
		LocalSystemConfig.openUDPServer();
		System.out.println(LocalSystemConfig.get_UDP_port());
		
		Scanner s = new Scanner(System.in);
		String t = s.next();
		
		if(t.equals("receive")) {
			
			DatagramSocket link;
			try {
				byte[] b0 = new byte[256];
				DatagramPacket p0 = new DatagramPacket(b0, b0.length);
				link = LocalSystemConfig.m_UDP_socket;
				link.receive(p0);
				String s0 = new String(p0.getData(), StandardCharsets.UTF_8);
				System.out.println("Port src = " + p0.getPort() + "link port = " + link.getPort());
				
			} catch (IOException e) {
				
			}
			
			
			
			
		}
		
		if(t.equals("send")) {
			for(int i=LocalSystemConfig.START_UDP_PORT; i <= LocalSystemConfig.END_UDP_PORT; ++i) {
				
				System.out.print("Emission sur le port " + i + " : ");
				
				try {
					DatagramSocket link = LocalSystemConfig.m_UDP_socket;
					byte[] b1 = "coucou".getBytes();
					
					DatagramPacket p1 = new DatagramPacket(b1, b1.length, InetAddress.getLocalHost(), i);
					
					link.send(p1);
					
					System.out.println("Paquet envoyé !");
					
				} catch (IOException e) {
					System.out.println("PAS DE REPONSE");
				}
			}
		}
	
		
		*/
		
		
	}

}
