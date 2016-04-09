import java.io.*;
import java.net.*;

public class GamePlayer extends Thread implements Serializable {
	GameNet_ConnectionBrokenInterface gameConnectionBrokenObj = null;
	private String playerName;
	private GameControl gameControl = null;

	Socket gameSocket = null;
	ObjectInputStream socketInput = null;
	ObjectOutputStream socketOutput = null;
	GameNet_UserInterface userInterface = null;

	boolean socketAlive = true;

	/**
	 * The GamePlayer constructor needs the name of the player and the
	 * GameControl class to connect to.
	 * 
	 */
	GamePlayer(String playerName, GameControl game, GameNet_UserInterface r) {
		this.playerName = playerName;
		this.gameControl = game;
		userInterface = r;

		joinGame();
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setGameConnectionBroken(GameNet_ConnectionBrokenInterface gcb) {
		gameConnectionBrokenObj = gcb;
	}

	public void run() {
		Object outputFromSocket;
		try {
			// Read from Socket and write to Screen
			while ((outputFromSocket = socketInput.readObject()) != null) // Read
																			// from
																			// Socket
			{
				receivedMessage(outputFromSocket);
			}
		} catch (ClassNotFoundException e) {
			System.out
					.println("GamePlayer.run Class Not Found Exception: " + e);
			e.printStackTrace(System.out);
		} catch (IOException e) {
			System.out.println("GamePlayer.run Exception: " + e);
		}
		// It's easier for the socket reader to detect that the socket
		// is gone. We need to set a flag so that the socket writer
		// will know that it's time to give up.

		socketAlive = false;
		if (gameConnectionBrokenObj != null)
			gameConnectionBrokenObj.gameConnectionBroken();
		System.out.println("GamePlayer.run Thread terminating ");

	}

	public boolean GameIsAlive() {
		return socketAlive;
	}

	void joinGame() {
		ObjectOutputStream tempSocketOutput = null;
		if (gameControl == null)
			throw new RuntimeException("joinGame called on a null gameControl");

		try {
			gameSocket = new Socket(gameControl.getIpAddress(),
					gameControl.getPortNum());
			if (gameSocket == null)
				throw new RuntimeException("joinGame gameSocket null error");

			// Create in/out classes associated with the Open Socket
			tempSocketOutput = new ObjectOutputStream(
					gameSocket.getOutputStream());

			socketInput = new ObjectInputStream(gameSocket.getInputStream());
			if (socketInput == null)
				throw new RuntimeException("joinGame socketInput null error");

			try {
				Thread.sleep(500);// Sleep for 1/2 second
			} catch (InterruptedException e) {
			}

			// Start up a Thread to read from the socket and write
			// the contents to the screen

			this.start();
			try {
				Thread.sleep(500);// Sleep for 1/2 second
			} catch (InterruptedException e) {
			}

		} catch (UnknownHostException e) {
			System.out.println("GamePlayer.joinGame Cant find host: " + e);
		} catch (IOException e) {
			System.out.println("GamePlayer.joinGame IOException: " + e);
			e.printStackTrace(System.out);
		}
		socketOutput = tempSocketOutput;

	}

	/**
	 * exitGame is called when you want to terminate the connection to the
	 * GameControl
	 * 
	 */
	void exitGame() {
		exitGame(null);
	}

	void exitGame(Object ob) {
		if (ob != null)
			sendMessage(ob);

		System.out.println("GamePlayer.exitGame " + playerName);
		try {
			if (socketOutput != null)
				socketOutput.close(); // Close output stream side of the socket
			if (socketInput != null)
				socketInput.close(); // Close input stream side of socket
			if (gameSocket != null)
				gameSocket.close(); // Close the socket
		} catch (IOException e) {
		}
	}

	/*
	 * Some useful routines contained in GamePlayer void
	 * sendMessage(MyGameInput); // Send to GameControl boolean GameIsAlive();
	 * //Tests if socket is alive
	 */
	public void doneWithGame() {
		exitGame(); // Our GamePlayer object disconnects from the gameControl
		gameControl.endGame(); // If we own the server, it will shutdown
	}

	/**
	 * sendMessage provides a way to send an object to the GameControl.
	 * 
	 * @param objectToSend
	 *            to pass to the GameControl.
	 */
	public void sendMessage(Object objectToSend) {
		try {
			if (socketOutput != null) {
				socketOutput.writeObject(objectToSend); // Write to socket
				socketOutput.reset();
			}
		} catch (IOException e) {
			System.out.println("GamePlayer.sendMessage Exception: " + e);
			e.printStackTrace(System.out);
		}
	}

	protected void receivedMessage(Object objectReceived) {
		userInterface.receivedMessage(objectReceived);
	}

}
