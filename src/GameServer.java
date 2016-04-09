import java.net.*;
import java.io.*;
import java.util.*;

class GameServer extends Thread {
	public String inetAddress = null;
	LinkedList<GamePlayerProcess1> gamePlayers = new LinkedList<GamePlayerProcess1>();
	int portNum;
	boolean serverStarted = false;
	boolean listening = true;
	ServerSocket serverSocket = null;
	GameNet_CoreGame coreGame = null;

	synchronized int getPortNum() {
		try {
			if (!serverStarted)
				wait();
		} catch (InterruptedException e) {
		}
		System.out.println(" getPortNum = " + portNum);
		return portNum;
	}

	synchronized void markServerStarted() {
		serverStarted = true;
		System.out.println(" markServerStarted");
		notifyAll();
	}

	GameServer(int port, GameNet_CoreGame gi) {
		portNum = port;
		coreGame = gi;
	}

	synchronized void putInputMsgs(Object ob) {
		Object ob2 = coreGame.process(ob);
		if (ob2 != null)
			putOutputMsgs(ob2);
	}

	synchronized void putOutputMsgs(Object ob) {
		for (int i = 0; i < gamePlayers.size(); i++) {
			GamePlayerProcess1 p = gamePlayers.get(i);
			p.put(ob);
		}
	}

	synchronized void removeMe(int index) {
		for (int i = 0; i < gamePlayers.size(); i++) {
			GamePlayerProcess1 c = gamePlayers.get(i);
			if (index == c.myIndex) {
				c.stopGamePlayer();
				gamePlayers.remove(c);
				break;
			}
		}
	}

	public void run() {
		InetAddress iaddr = null;
		Socket nextSock;
		int nThreadCount = 0;

		try {
			for (int i = 0; i <= 20; i++) {
				if (i == 20)
					throw new RuntimeException(
							"GameServer.run I Give up after 20 different port numbers");
				try {
					serverSocket = new ServerSocket(portNum);
					break;
				} catch (IOException e) {
					System.out.println("GameServer.run Exception :" + e);
					portNum += 1;
					System.out
							.println("Assume that we hit a used port, try again with port number="
									+ portNum);
				}
			}
			iaddr = InetAddress.getLocalHost();

			inetAddress = iaddr.getHostAddress(); // Make available
			serverSocket.setReuseAddress(true); // Makes port available sooner
												// even if port is being timed
												// out

			markServerStarted();

			while (listening) {
				nextSock = serverSocket.accept();
				System.out.println(nThreadCount + " Another Thread Created");

				// Create a thread to process incoming connection
				GamePlayerProcess1 gamePlayerChild = new GamePlayerProcess1(
						nextSock, this, nThreadCount++);
				gamePlayers.add(gamePlayerChild);
				gamePlayerChild.start();
			}

			serverSocket.close();

		} catch (IOException e) {
			System.out.println("GameServer.run Exception:" + e);
		}

	}

	void stopServer() {
		listening = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
		}

		for (int i = gamePlayers.size() - 1; i >= 0; i--) {
			GamePlayerProcess1 p = gamePlayers.get(i);
			p.stopGamePlayer();
		}

	}
}
