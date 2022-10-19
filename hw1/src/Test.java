public class Test {

	public static void main(String[] args) {

		GameBoard game = new GameBoard();
		game.run();

/*
		JFrame frame = new JFrame("2d game");
		frame.setSize(600,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		// init panel
		JPanel panel = new JPanel();
		panel.setBackground(Color.lightGray);

		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel p1 = new JPanel();
		p1.setMaximumSize(new Dimension(400,200));
		p1.setBackground(Color.red);
		JPanel p2 = new JPanel();
		p2.setMaximumSize(new Dimension(400,300));
		p2.setLayout(new GridLayout(6,9));

		Component fill1 = Box.createRigidArea(new Dimension(400, 100));
		Component fill2 = Box.createRigidArea(new Dimension(400, 100));

		panel.add(fill1);
		panel.add(p1);
		panel.add(fill2);
		panel.add(p2);


		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				p2.add(new ColorBox(Game.getRandomColor()));
			}
		}



		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		*/

	}
}
