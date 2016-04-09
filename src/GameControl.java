public class GameControl {
	int serverPortNum = 54321; // Starting point for Server Port
	GameServer gameServer = null;
	private String ipAddr = null;
	GameNet_CoreGame coreGame = null;
	GameCreator gameCreator = null;

	/**
	 * This is method returns the IP address used for this game.
	 * 
	 * @return IP address for this game
	 */
	String getIpAddress() {
		return ipAddr;
	}

	/**
	 * This is method returns the port number used for this game.
	 * 
	 * @return port number for this game
	 */
	int getPortNum() {
		return serverPortNum;
	}

	GameControl(GameCreator gc) {
		gameCreator = gc;
	}

	void connect_to_server(String ipAddr, int serverPortNum) {
		this.ipAddr = ipAddr;
		this.serverPortNum = serverPortNum;
	}
	public void putMsgs(Object objectOutput) {
		if (gameServer != null)
			gameServer.putOutputMsgs(objectOutput);
	}

	/**
	 * Use this constructor if the Server for this game lives in this program.
	 * Note that the actual variable createServer is not actually used.
	 * 
	 */
	void startServer() {
		try {
			coreGame = gameCreator.createGame();
			gameServer = new GameServer(serverPortNum, coreGame);
			gameServer.start();

			coreGame.startGame(this);

		

			serverPortNum = gameServer.getPortNum();
			ipAddr = gameServer.inetAddress;

			System.out.println("Starting GameControl Server ipAddress("
					+ ipAddr + ")  portNum (" + serverPortNum + ")");

		} catch (RuntimeException e) {
			System.out.println("GameControl: Runtime Exception:" + e);
			e.printStackTrace(System.out);
		}

	}

	/**
	 * endGame will shutdown a server.
	 * 
	 */

	void endGame() {

		if (gameServer != null) {
			System.out.println("endGame ");
			gameServer.stopServer();
		}

	}

}
