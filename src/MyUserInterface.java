
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyUserInterface extends JFrame implements ActionListener,
		GameNet_UserInterface {
	GamePlayer myGamePlayer;
	MyGameInput myGameInput = new MyGameInput();
	ChessGame chessGame = null;

	ChessPanel chessPanel = new ChessPanel();

	JLabel currentPlayer;
	JLabel currentColor;
	JTextArea textArea;
	JTextField chatInputTextField;
	private String lineSeparator = System.getProperty("line.separator");

	JLabel[] northLabels = new JLabel[8];
	JLabel[] westLabels = new JLabel[8];

	String[] westNumber = { "1", "2", "3", "4", "5", "6", "7", "8" };
	String[] northLetter = { "A", "B", "C", "D", "E", "F", "G", "H" };

	MyUserInterface() {
		super(
				"The chessboard is the world, the chessPieces are the phenomena of the universe");

		setSize(950, 600);
		addWindowListener(new Termination());
		myLayout();

		// setVisible(true);
	}

	private void myLayout() {

		setLayout(new BorderLayout());

		// Drawing in the Center
		add(chessPanel, BorderLayout.CENTER);

		JPanel north = new JPanel();
		north.setLayout(new GridLayout(1, 10));

		add(north, BorderLayout.NORTH);

		JPanel west = new JPanel();
		west.setLayout(new GridLayout(0, 1));

		// Create the Color radio buttons on the West

		JLabel jb = new JLabel();
		jb.setSize(new Dimension(0, 0));
		north.add(jb);
		for (int i = 0; i < 8; i++) {
			westLabels[i] = new JLabel(westNumber[i]);
			westLabels[i].setPreferredSize(new Dimension(15, 10));
			west.add(westLabels[i]);

			northLabels[i] = new JLabel(northLetter[i]);
			north.add(northLabels[i]);
		}
		JLabel lb = new JLabel();
		north.add(lb);
		north.add(new JLabel());

		add(west, BorderLayout.WEST);

		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));

		textArea = new JTextArea(50, 20);
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);

		currentPlayer = new JLabel("Player name: ");
		east.add(currentPlayer);
		currentColor = new JLabel("Color: ");
		east.add(currentColor);
		east.add(textArea);

		chatInputTextField = new JTextField(20);
		chatInputTextField.setBackground(Color.yellow);
		east.add(chatInputTextField);

		add(east, BorderLayout.EAST);

		chatInputTextField.addActionListener(this);
	}

	public void startUserInterface(GamePlayer player) {

		myGamePlayer = player;
		chessPanel.setGamePlayer(player);

		String playerName = player.getPlayerName();
		currentPlayer.setText((playerName == null) ? "Your name: "
				: "Your name: " + playerName + "");

		currentColor.setText("Color: White");
		// myGameInput = new MyGameInput();
		myGameInput.setName(playerName);
		myGameInput.setMsg("just entered chat room");
		myGamePlayer.sendMessage(myGameInput); // Default is the
												// "Connecting command"
		setVisible(true);

	}

	public void receivedMessage(Object ob) {
		MyGameOutput myGameOutput = (MyGameOutput) ob;
		chessGame = myGameOutput.myGame.chessGame;

		chessPanel.newScreen(chessGame);

		int playerIndex = chessGame.getClientIndex();
		if (playerIndex == 1) {
			currentColor.setText("Color: Black");
		} else if (playerIndex == 0) {
			currentColor.setText("Color: White");
		}
		if (ob.toString() != "") {
			textArea.setText(ob.toString() + lineSeparator + textArea.getText());
		}

		if (chessGame.getWinner() != null) {

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane
					.showConfirmDialog(null,
							"Game OVer: " + chessGame.getWinner()
									+ " Win. Exit the Game?", "Game Over",
							dialogButton);
			if (dialogResult == 0) {

				System.exit(0);

			} else {
				System.out.println("No Option");
			}
		}

		repaint();
	}

	public void actionPerformed(ActionEvent actionEvent) {

		String str = chatInputTextField.getText();
		if (!myGamePlayer.GameIsAlive() || "quit".equals(str)) {
			// exitProgram();
		} else {
			myGameInput.setMsg(str);
			myGamePlayer.sendMessage(myGameInput);
			chatInputTextField.setText("");
		}

	}

	class Termination extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			myGamePlayer.doneWithGame();
			System.exit(0);
		}
	}

}
