package ajechess.chess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class PreferencesPanel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JSlider levelSlider;
	JRadioButton whitePiecesButton;
	JRadioButton blackPiecesButton;
	JButton ok;
	JButton cancel;
	final static int WHITE = 0;
	final static int BLACK = 1;
	Ajechess ajechess;

	public PreferencesPanel(Ajechess ajechess) {
		super("Settings");
		this.ajechess = ajechess;
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.add(createLevelPane(), BorderLayout.WEST);
		mainPane.add(createColorPane(), BorderLayout.EAST);
		mainPane.add(createButtonPane(), BorderLayout.SOUTH);
		mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	public JPanel createLevelPane() {
		levelSlider = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);
		JPanel levelPane = new JPanel();
		levelSlider.setMajorTickSpacing(1);
		levelSlider.setPaintTicks(true);
		levelSlider.setPaintLabels(true);
		levelPane.add(levelSlider);
		levelPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5),
				BorderFactory.createTitledBorder("Difficulty Level")));
		return levelPane;
	}

	public JPanel createColorPane() {
		JPanel colorPane = new JPanel(new GridLayout(1, 2));
		whitePiecesButton = new JRadioButton("White", true);
		blackPiecesButton = new JRadioButton("Black", false);
		ButtonGroup group = new ButtonGroup();
		group.add(whitePiecesButton);
		group.add(blackPiecesButton);
		colorPane.add(whitePiecesButton);
		colorPane.add(blackPiecesButton);
		colorPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5),
				BorderFactory.createTitledBorder("Color of pieces")));
		return colorPane;
	}

	public JPanel createButtonPane() {
		JPanel buttonPane = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new GridLayout(1, 2, 5, 0));
		panel.add(ok = new JButton("OK"));
		panel.add(cancel = new JButton("Cancel"));
		buttonPane.add(panel, BorderLayout.CENTER);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 75, 5, 75));
		return buttonPane;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == ok) {
			ajechess.state = Ajechess.GAME_ENDED;
			ajechess.newGame();
		}
		setVisible(false);
	}
}
