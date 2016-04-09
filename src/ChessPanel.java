
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class ChessPanel extends JPanel {

	GamePlayer myGamePlayer;
	MyGameInput myGameInput = new MyGameInput();
	ChessGame chessGame = null;

	Image offScreenImage = null;
	int lastWidth = -1, lastHeight = -1;

	BoardDimensions boardDimensions = new BoardDimensions();

	public void setGamePlayer(GamePlayer gp) {
		myGamePlayer = gp;
		myGameInput.setName(myGamePlayer.getPlayerName());
	}

	public void newScreen(ChessGame myGame) {
		this.chessGame = myGame;
		repaint();
	}

	ChessPanel() {
		ChessPiece.readInImages();
		MouseClickMonitor mmh = new MouseClickMonitor();
		addMouseListener(mmh);
		addMouseMotionListener(mmh);
	}

	public void paint(Graphics screen) {
		Dimension d = getSize();
		if (d.width != lastWidth || d.height != lastHeight) {
			lastWidth = d.width;
			lastHeight = d.height;
			offScreenImage = createImage(lastWidth, lastHeight);
		}
		Graphics g = offScreenImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, d.width, d.height);

		Insets insets = getInsets();
		int top = insets.top;
		int left = insets.left;
		int square_width = (d.width - left - insets.right) / 8;
		int square_height = (d.height - top - insets.bottom) / 8;
		boardDimensions.setParms(left, top, square_width, square_height);

		// Color in the Board squares
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int x = left + j * square_width;
				int y = top + i * square_height;
				if ((i + j) % 2 == 1) {
					g.setColor(Color.green);
					g.fillRect(x, y, square_width, square_height);
				} else {
					g.setColor(Color.gray);
					g.fillRect(x, y, square_width, square_height);
				}
			}
		}// end of outer for loop

		// Draw pieces in their current location

		if (chessGame != null) {
			chessGame.drawInPosition(g, boardDimensions);
		}
		// To avoid flicker we copy the offScreenImage to the real screen
		screen.drawImage(offScreenImage, 0, 0, this);
	}

	// INNER Class
	class MouseClickMonitor extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			DPoint dpoint = boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null) {
				myGameInput.setMouseCmd(MyGameInput.MOUSE_PRESSED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}

		public void mouseReleased(MouseEvent e) {
			DPoint dpoint = boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null) {
				myGameInput.setMouseCmd(MyGameInput.MOUSE_RELEASED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}

		public void mouseDragged(MouseEvent e) {
			DPoint dpoint = boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null) {
				myGameInput.setMouseCmd(MyGameInput.MOUSE_DRAGGED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}

	}

	// End of INNER Class
}
