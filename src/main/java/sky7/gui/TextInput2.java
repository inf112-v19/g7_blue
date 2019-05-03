package sky7.gui;

import com.badlogic.gdx.Input.TextInputListener;

// Get user input
public class TextInput2 implements TextInputListener {
	String input;
	GUIold gui;
	
	public TextInput2(GUIold gui) {
	    this.gui = gui;
	}
	
	@Override
	public void input (String text) {
		input = text;
		System.out.println("Entered IP: " + input);
		gui.connectClient(input);
	}

	@Override
	public void canceled () {
	}

	public String getinput() {
		return input;
	}
}