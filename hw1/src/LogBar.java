import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Log bar used in the game.
 */
public class LogBar {
	/** Panel which labels are printed onto. */
	private JPanel panel;
	/** Labels printed on the log bar. */
	private JLabel[] labels;
	/** Strings printed on the labels. */
	private ArrayList<String> log;
	/** Capacity of the entries in the log bar. */
	private int cap;

	public LogBar() {
		cap = 15;

		panel = new JPanel();
		panel.setLayout(new GridLayout(cap, 1));
		panel.setMaximumSize(new Dimension(580, cap * 20));
		panel.setBorder(BorderFactory.createLineBorder(Color.gray, 5));

		labels = new JLabel[cap];
		for (int i = 0; i < cap; i++) {
			labels[i] = new JLabel();
			panel.add(labels[i]);
		}

		log = new ArrayList<>();
	}

	/**
	 * Updates the strings printed on the labels.
	 */
	public void update() {
		for (int i = 0; i < log.size(); i++)
			labels[i].setText(log.get(i));
	}

	/**
	 * Adds new entry to the log.
	 * @param str New entry.
	 */
	public void add(String str) {
		if (log.size() >= cap) {
			for (int j = 0; j < log.size() - 1; j++)
				log.set(j, log.get(j+1));
			log.set(log.size() - 1, " " + str);
		}
		else
			log.add(" " + str);
	}

	/**
	 * Appends a string to the end of the last entry.
	 * @param str String appended to the end of the last entry.
	 */
	public void addToLastStr(String str) {
		int lastI = log.size() - 1;

		log.set(lastI, log.get(lastI) + " " + str);
	}

	public JPanel getPanel() {
		return panel;
	}

}
