import java.io.Serializable;
import java.util.ArrayList;

public class MyGame extends GameNet_CoreGame implements Serializable {

	private ArrayList<String> currPlayers = new ArrayList<String>();
	ChessGame chessGame = new ChessGame();

	public Object process(Object ob) {
		MyGameInput myGameInput = (MyGameInput) ob;

		int clientIndex = getClientIndex(myGameInput.sendersName);
		chessGame.setClientIndex(clientIndex);

		if (clientIndex < 0) {
			System.out.println("Already have 2 players");
			return null; // Ignore input
		}

		switch (myGameInput.cmd) {
		case MyGameInput.MOUSE_PRESSED:
			chessGame.mousePressed(myGameInput.dpoint);
			break;
		case MyGameInput.MOUSE_RELEASED:
			chessGame.mouseReleased(myGameInput.dpoint);
			break;
		case MyGameInput.MOUSE_DRAGGED:
			chessGame.mouseDragged(myGameInput.dpoint);
			break;
		case MyGameInput.CONNECTING:
			break;
		}

		MyGameOutput myGameOutput = new MyGameOutput(this);
		myGameOutput.copyMsg(myGameInput); // Copy Input name and message to
											// Output name and Message

		return myGameOutput;
	}

	private int getClientIndex(String name) {
		// The following will return -1 if the name can't be found
		int retval = currPlayers.indexOf(name);

		if (retval < 0 && currPlayers.size() < 2) {
			retval = currPlayers.size();
			currPlayers.add(name);
			if (currPlayers.size() == 2) {
				// Game ready to go.
			}
		}
		return retval;
	}

	// If you are already in the game, your index will be returned (0 or 1)
	// Otherwise -1 is returned ... you are never added with this routine.
	private int getYourIndex(String name) {
		return currPlayers.indexOf(name);
	}

	// This returns the other Player's name if he exists. A null is returned if
	// he doesn't exist.
	private String otherPlayerName(String yourName) {
		if (currPlayers.size() < 2)
			return null;
		if (yourName.equals(currPlayers.get(0)))
			return currPlayers.get(1);
		else
			return currPlayers.get(0);
	}
}
