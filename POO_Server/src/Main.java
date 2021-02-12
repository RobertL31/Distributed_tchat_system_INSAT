import network.NetworkManager;

public class Main {

	public static void main(String[] args) {
		NetworkManager manager = new NetworkManager();
		
		manager.init();
		
		System.out.println("Server launched !");
		
		while(true);
	}

}
