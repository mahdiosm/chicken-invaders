package MainFrame;

import javax.swing.JPanel;

public class Panel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel lastFrame;

	public JPanel getLastPanel() {
		return lastFrame;
	}

	public void setLastPanel(JPanel lastFrame) {
		this.lastFrame = lastFrame;
	}

}
