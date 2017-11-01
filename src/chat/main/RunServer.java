package chat.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import chat.host.Server;

public class RunServer {

	public static void main(String[] args) {
		String host,port;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter DNS IP Adress: ");
		host = sc.nextLine();
		System.out.println("Enter Server Port: ");
		port = sc.nextLine();
		
			try {
				new Server(host,Integer.parseInt(port));
			} catch (NumberFormatException e) {
				System.out.println("Make sure That the port number is correct and try again.");
			} catch (UnknownHostException e) {
				System.out.println("Check The IP adress and try again");
			} catch (IOException e) {
				System.out.println("Check Input and try again");
			}
		
		
	}
}
