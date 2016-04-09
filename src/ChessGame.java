
import java.awt.Graphics;
import java.io.Serializable;

public class ChessGame implements Serializable {

	private Piece[] pieces = new Piece[32];

	private int selectedIndex = -1;
	private int nextTurn = 0;
	private int startingTurn = 0;
	private int clientIndex;

	private DPoint pressed_point = new DPoint();
	private DPoint dragged_point = new DPoint();
	protected static Piece[][] pieceArray = new Piece[8][8];

	private String winner;

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public ChessGame() {

		// Fill in all the pieces and their locations
		int index = 0;

		for (int i = 0; i < 8; i++) // black pawns in row 1
		{
			pieces[index] = new Piece(PieceType.Pawn, ColorType.black, i, 1);
			pieceArray[i][1] = pieces[index];
			index++;
		}
		for (int i = 0; i < 8; i++) // white pawns in row 6
		{
			pieces[index] = new Piece(PieceType.Pawn, ColorType.white, i, 6);
			pieceArray[i][6] = pieces[index];
			index++;
		}

		PieceType[] startingKingRow = { PieceType.Rook, PieceType.Knight,
				PieceType.Bishop, PieceType.Queen, PieceType.King,
				PieceType.Bishop, PieceType.Knight, PieceType.Rook };

		for (int i = 0; i < 8; i++) // black king row in row 0
		{
			pieces[index] = new Piece(startingKingRow[i], ColorType.black, i, 0);
			pieceArray[i][0] = pieces[index];
			index++;
		}
		for (int i = 0; i < 8; i++) // white king row in row 7
		{
			pieces[index] = new Piece(startingKingRow[i], ColorType.white, i, 7);
			pieceArray[i][7] = pieces[index];
			index++;
		}

	}

	// Draws all pieces into their current location.
	public void drawInPosition(Graphics g, BoardDimensions chessBoard) {
		for (int i = 0; i < pieces.length; i++) {
			if (i != selectedIndex)
				pieces[i].drawInPosition(g, chessBoard);
			else {
				// The selected piece is still being dragged around
				DPoint delta = pressed_point.deltaPoint(dragged_point);
				pieces[i].dragDraw(g, chessBoard, delta.x, delta.y);
			}
		}
	}

	void mousePressed(DPoint dpoint) {
		pressed_point = dpoint;
		dragged_point = dpoint;

		int xSelectLoc = (int) dpoint.x;
		int ySelectLoc = (int) dpoint.y;

		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i].areYouHere(xSelectLoc, ySelectLoc)) {
				if (clientIndex == nextTurn) {
					if ((nextTurn % 2 == 0 && pieces[i].color
							.equals(ColorType.white))
							|| (nextTurn % 2 == 1 && pieces[i].color
									.equals(ColorType.black))) {

						selectedIndex = i;
						break;
					}

				}
			}
		}

	}

	void mouseDragged(DPoint dpoint) {
		dragged_point = dpoint;
	}

	void mouseReleased(DPoint dpoint) {
		// pressed_point = dpoint;
		if (selectedIndex >= startingTurn) {
			int xSelectLoc = (int) dpoint.x;
			int ySelectLoc = (int) dpoint.y;
			if (xSelectLoc >= 0
					&& ySelectLoc >= 0
					&& pieces[selectedIndex].isLegalMove(pressed_point, dpoint,
							pieceArray)) {
				if (pieceArray[xSelectLoc][ySelectLoc] != null) {
					if (pieceArray[xSelectLoc][ySelectLoc].pieceType
							.equals(PieceType.King)) {

						if (pieces[selectedIndex].color.equals(ColorType.white)) {
							winner = "White";
						} else {
							winner = "Balck";
						}

					} else {
						pieceArray[xSelectLoc][ySelectLoc].dead();
					}

				}
				pieces[selectedIndex].moveLoc(xSelectLoc, ySelectLoc);
				pieceArray[(int) pressed_point.x][(int) pressed_point.y] = null;
				pieceArray[xSelectLoc][ySelectLoc] = pieces[selectedIndex];

				nextTurn = (nextTurn + 1) % 2;

			}
			selectedIndex = -1;
		}
	}

	public int getClientIndex() {
		return clientIndex;
	}

	public void setClientIndex(int clientIndex) {
		this.clientIndex = clientIndex;
	}

}
