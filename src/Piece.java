
import java.awt.Graphics;
import java.io.Serializable;

enum ColorType {black, white};
enum PieceType {Pawn, Rook, Knight, Bishop, Queen, King};



class Piece implements Serializable
{    
    int xSquare, ySquare;
    PieceType pieceType;  
    ColorType color;
    
    
    Piece (PieceType p, ColorType c, int xSquare, int ySquare)
    {
        this.pieceType=p;
        this.color =c;
        this.xSquare = xSquare;
        this.ySquare = ySquare;
    }
    
    void moveLoc(int x, int y)
    {
        xSquare = x;
        ySquare = y;
    }
    boolean areYouHere(int xSelectLoc, int ySelectLoc)
    {
        if (xSelectLoc == xSquare && ySelectLoc == ySquare)
            return true;
        else
            return false;
    }
    
     void drawInPosition(Graphics g, BoardDimensions boardDimensions)
     {
    	 boardDimensions.boardDraw(g, pieceType, color, xSquare, ySquare);
     }
     
     // The following will be used while we are dragging piece around
     void dragDraw(Graphics g, BoardDimensions boardDimensions,
             double xDelta, double yDelta)
     {
    	 boardDimensions.boardDraw(g, pieceType, color, xSquare+xDelta, ySquare+yDelta);
     }
     
     boolean isLegalMove(DPoint pressed_point, DPoint released_point, Piece[][] pieceArray) {
    	 
    	int xPressed = (int)pressed_point.x;
 		int yPressed = (int)pressed_point.y;
 		int xReleased = (int)released_point.x;
 		int yReleased = (int)released_point.y;
		
 		if(xPressed == xReleased && yPressed == yReleased){
 			return false;
 		}
 		if(pieceType.equals(PieceType.Pawn) && color.equals(ColorType.white)){
 			if(xPressed == xReleased){
 				if(yPressed == 6 && (yReleased == yPressed - 2)){
 					if(pieceArray[xPressed][yPressed -1] == null && pieceArray[xPressed][yPressed -2] == null)
 					return true;
 				}else if (yReleased == yPressed-1 ){
 					if(pieceArray[xPressed][yPressed -1] == null)
 	 					return true;
 				}
 			}
 			if(yReleased == yPressed -1 &&(xReleased == xPressed + 1 ||xReleased == xPressed - 1)){
 				if(pieceArray[xReleased][yReleased] != null  && pieceArray[xReleased][yReleased].color == ColorType.black) {
// 					pieceArray[xReleased][yReleased].dead();
 					return true;
 				}
 			}

 		}
 		else if(pieceType.equals(PieceType.Pawn) && color.equals(ColorType.black)){
 			if(xPressed == xReleased){
 				if(yPressed == 1 && (yReleased == yPressed + 2)){
 					if(pieceArray[xPressed][yPressed + 1] == null && pieceArray[xPressed][yPressed + 2] == null)
 						return true;
 				}else if (yReleased == yPressed + 1 ){
 					if(pieceArray[xPressed][yPressed +1] == null)
 					return true;
 				}
 			}
 			
 			if(yReleased == yPressed + 1 &&(xReleased == xPressed + 1 ||xReleased == xPressed - 1)){
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == ColorType.white) {
// 					pieceArray[xReleased][yReleased].dead();
 					return true;
 				}
 			}

 		}
 		else if(pieceType.equals(PieceType.Knight)){
 			
 			if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color) {
					return false;
			}
 			
 			if(Math.abs(xPressed-xReleased) ==1 && Math.abs(yPressed-yReleased) == 2){
// 				if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 				return true;
 			}
 			else if(Math.abs(xPressed-xReleased) == 2 && Math.abs(yPressed-yReleased) == 1){
// 				if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 				return true;
 			}
 		}
 		else if(pieceType.equals(PieceType.Bishop)){
 			if(Math.abs(xPressed-xReleased) == Math.abs(yPressed-yReleased)){
 				
 				if(xReleased > xPressed && yReleased > yPressed){
 					for(int i = xPressed +1, j = yPressed + 1; i < xReleased && j < yReleased; i++, j++){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased > xPressed && yReleased < yPressed){
 					for(int i = xPressed +1, j = yPressed - 1; i < xReleased && j > yReleased; i++, j--){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased < xPressed && yReleased < yPressed){
 					for(int i = xPressed -1, j = yPressed - 1; i > xReleased && j > yReleased; i--, j--){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased < xPressed && yReleased > yPressed){
 					for(int i = xPressed -1, j = yPressed + 1; i > xReleased && j < yReleased; i--, j++){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color){
 					return false;
 				} 				
// 				else if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 			return true;
 			}
 		}
 		
 		else if(pieceType.equals(PieceType.Queen)){
 			if(Math.abs(xPressed-xReleased) == Math.abs(yPressed-yReleased)){
 				if(xReleased > xPressed && yReleased > yPressed){
 					for(int i = xPressed +1, j = yPressed + 1; i < xReleased && j < yReleased; i++, j++){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased > xPressed && yReleased < yPressed){
 					for(int i = xPressed +1, j = yPressed - 1; i < xReleased && j > yReleased; i++, j--){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased < xPressed && yReleased < yPressed){
 					for(int i = xPressed -1, j = yPressed - 1; i > xReleased && j > yReleased; i--, j--){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				else if(xReleased < xPressed && yReleased > yPressed){
 					for(int i = xPressed -1, j = yPressed + 1; i > xReleased && j < yReleased; i--, j++){
 	 					if(pieceArray[i][j] != null) {
 	 						return false;
 	 					}
 	 				}
 				}
 				
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color){
 					return false;
 				}
 				
// 				if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 			return true;
 			}
 			else if(xPressed == xReleased || yPressed == yReleased) {
 				if(yPressed > yReleased) {
 					for(int i = yReleased + 1; i < yPressed; i++) {
 						if(pieceArray[xPressed][i] != null){
 							return false;
 						}
 					}
 				}
 				else if(yPressed < yReleased) {
 					for(int i = yPressed + 1; i < yReleased; i++) {
 						if(pieceArray[xPressed][i] != null){
 							return false;
 						}
 					}
 				}
 				else if(xPressed > xReleased) {
 					for(int i = xReleased + 1; i < xPressed; i++) {
 						if(pieceArray[i][yPressed] != null){
 							return false;
 						}
 					}
 				}
 				else if(xPressed < xReleased) {
 					for(int i = xPressed + 1; i < xReleased; i++) {
 						if(pieceArray[i][yPressed] != null){
 							return false;
 						}
 					}
 				}
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color){
 					return false;
 				} 				
// 				else if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 			return true;
 			}
 		
 		}
 		else if(pieceType.equals(PieceType.King)){
 			
 			if((Math.abs(xReleased - xPressed) == 0 ||Math.abs(xReleased - xPressed) == 1) &&
 					(Math.abs(yReleased - yPressed) == 0 ||Math.abs(yReleased - yPressed) == 1)){
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color){
 					return false;
 				}
 				
// 				if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 			return true;
 			}
 						
 		}
 		else if(pieceType.equals(PieceType.Rook)){
 			if(xPressed == xReleased || yPressed == yReleased) {
 				if(yPressed > yReleased) {
 					for(int i = yReleased + 1; i < yPressed; i++) {
 						
 						
 						if(pieceArray[xPressed][i] != null){
 							return false;
 						}
 					}
 				}
 				else if(yPressed < yReleased) {
 					for(int i = yPressed + 1; i < yReleased; i++) {
 						
 						if(pieceArray[xPressed][i] != null){
 							return false;
 						}
 					}
 				}
 				else if(xPressed > xReleased) {
 					for(int i = xReleased + 1; i < xPressed; i++) {
 						
 						if(pieceArray[i][yPressed] != null){
 							return false;
 						}
 					}
 				}
 				else if(xPressed < xReleased) {
 					for(int i = xPressed + 1; i < xReleased; i++) {
 						
 						if(pieceArray[i][yPressed] != null){
 							return false;
 						}
 					}
 				}
 				if(pieceArray[xReleased][yReleased] != null && pieceArray[xReleased][yReleased].color == this.color){
 					return false;
 				} 				
// 				else if(pieceArray[xReleased][yReleased] != null) {
// 					pieceArray[xReleased][yReleased].dead();
// 				}
 			return true;
 			}
 			
 		}
    	 
    	 return false;
    	 
     }
     
     void dead() {
    	 
    	 
    	 this.xSquare = 99;
         this.ySquare = 99;
     }
     
}

