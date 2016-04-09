
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

class ChessPiece {
	static PieceType[] pieceType = PieceType.values();
	static ColorType[] colorType = ColorType.values();
	static ChessPiece[][] chessPieces = new ChessPiece[colorType.length][pieceType.length];

	static void readInImages() {
		for (int i = 0; i < colorType.length; i++)
			for (int j = 0; j < pieceType.length; j++)
				chessPieces[i][j] = new ChessPiece(i, j);
	}

	Image pieceImg;
	int width, height;

	private Image loadImage(String fileName) {
		return new ImageIcon(this.getClass().getResource(fileName)).getImage();
	}

	ChessPiece(int colorIndex, int pieceIndex) {
		String imageName = "/" + colorType[colorIndex].toString()
				+ pieceType[pieceIndex].toString() + ".gif";
		System.out.println(imageName);
		pieceImg = loadImage(imageName);
		width = pieceImg.getWidth(null);
		height = pieceImg.getHeight(null);
	}

	void draw(Graphics g, int x, int y, int wSpace, int hSpace) {
		double wfraction = (double) width / (double) wSpace;
		double hfraction = (double) height / (double) hSpace;
		int h, w;

		if (hfraction > wfraction) {
			h = (int) (height / hfraction);
			w = (int) (width / hfraction);
			x = Math.max(0, x + wSpace / 2 - w / 2);
			g.drawImage(pieceImg, x, y, w, h, null);
		} else {
			w = (int) (width / wfraction);
			h = (int) (height / wfraction);
			y = Math.max(0, y + hSpace / 2 - h / 2);
			g.drawImage(pieceImg, x, y, w, h, null);
		}
	}

}
