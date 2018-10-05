package ajechess.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessRules extends JPanel {

	private static final long serialVersionUID = 1L;
	public ChessRules() {
        setLayout(new BorderLayout()
        		);
        JPanel northPane = new NorthPane();       
        JPanel centerPane = new JPanel(new GridBagLayout()
        		);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();        
        gridBagConstraints.insets = new Insets(4,4,4,4);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;        

        String[][] values = new String[][] {
        	 {"King\u2654","Moves only one step in any direction at a time."},
        	 {"Queen\u2655","Moves any number of spaces in any direction."},
             {"Rook\u2656","Moves any number of horizontal or vertical steps."},
             {"Bishop\u2657","Moves any number of steps diagonally in any direction ."},
             {"Knight\u2658","Moves two steps in a straight line then one to the left or right, in an L shape."},
             {"Pawn\u2659","Moves one step foward and attack diagonally one, it's first move may be double."},
             {"Check","The King is in a position where it could be attacked and must move or be defended"},
             {"Checkmate","The king is in check, cannot be defended and the game is over"},
             {"Stalemate","The opponent has no available moves ,but their king is not in check"}
        };
        for(int i=0; i<values.length; i++) {
            JLabel pieceType = new JLabel(values[i][0]+": ");
            pieceType.setFont(new Font(pieceType.getFont().getName(),Font.BOLD,17)
            		);
            JLabel pieceDescription = new JLabel(values[i][1]);
            pieceDescription.setFont(new Font(pieceDescription.getFont().getName(),Font.BOLD,15)
            		);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            centerPane.add(pieceType,gridBagConstraints);
            gridBagConstraints.gridx = 1;
            centerPane.add(pieceDescription,gridBagConstraints);
        }
        centerPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)
        		);

        add(northPane,BorderLayout.NORTH);
        add(centerPane,BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10)
        		);
    }    
    public static void createAndShowUI() {                   
        JFrame frame = new JFrame("The Rules of Chess");
        ChessRules ChessRules = new ChessRules();
        frame.getContentPane().add(ChessRules);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    class NorthPane extends JPanel {

		private static final long serialVersionUID = 1L;
		NorthPane() {          
            JLabel label = new JLabel("The Rules of Chess",JLabel.CENTER);
            label.setFont(new Font(label.getFont().getName(),Font.BOLD+Font.ITALIC+Font.HANGING_BASELINE,25));
            label.setForeground(Color.decode("#000000"));
            add(label);
        }
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int width = this.getWidth()-5;
            int height = this.getHeight() - 1;
            graphics.drawLine(0, height, width, height);
        }
    }
}
