
import java.io.Serializable;

public class MyGameOutput implements Serializable {
	MyGame myGame = null;

	String sendersMsg;
	String sendersName;

	public void copyMsg(MyGameInput myInput) {
		sendersName = myInput.sendersName;
		sendersMsg = myInput.myMsg;
	}

	public String toString() {
		if (sendersName != null && sendersMsg != null)
			return sendersName + ": " + sendersMsg;
		else
			return "";
	}

	public MyGameOutput(MyGame mg) {
		myGame = mg;
	}

}
