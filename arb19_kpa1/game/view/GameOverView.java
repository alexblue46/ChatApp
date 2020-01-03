package arb19_kpa1.game.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The view for a game over screen
 *
 */
public class GameOverView extends JPanel {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -5144384137355851424L;
	/**
	 * The label containing win/lose text
	 */
	private final JLabel lblWinLabel = new JLabel("Win Label");

	/**
	 * Create the panel.
	 * 
	 * @param isWin if this screen is displayed to a winner
	 */
	public GameOverView(boolean isWin) {
		if (isWin) {
			lblWinLabel.setText("You won!");
		} else {
			lblWinLabel.setText("You lost...");
		}
		initGUI();
	}

	/**
	 * Initialize GUI components
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		lblWinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblWinLabel.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 90));

		add(lblWinLabel);
	}

}
