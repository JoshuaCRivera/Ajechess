package ajechess.chess;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class PawnPromotionPanel extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	int index;
    int location;
    JPanel pawnPromotionPanel;
    Ajechess ajechess;

    public PawnPromotionPanel(Ajechess ajechess) {
        setTitle("Replace pawn with");
        this.ajechess = ajechess;
        pawnPromotionPanel = new JPanel(new GridLayout(1,4,10,0));

        int[] cmdActions = {Pieces.QUEEN,Pieces.ROOK,Pieces.BISHOP,Pieces.KNIGHT};     
        
        for(int i=0; i<cmdActions.length; i++) {
            JButton button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(cmdActions[i]+"");
            pawnPromotionPanel.add(button);
        }
        setContentPane(pawnPromotionPanel);        
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                resumeGame(Pieces.QUEEN);
            }
        }
        );
    }
    public void setIcons(boolean white) {
        Component[] components = pawnPromotionPanel.getComponents();
        Resource resource = new Resource();
        String[] resourceStrings =  {"q","r","b","n"};
        for(int i=0; i<components.length; i++) {
            JButton button = (JButton) components[i];
            button.setIcon(new ImageIcon(resource.getResource((white?"w":"b")+resourceStrings[i])));
        }
        pack();
        setLocationRelativeTo(null);
    }
    public void actionPerformed(ActionEvent event) {
        int promotePiece = Integer.parseInt(event.getActionCommand());
        setVisible(true);
        resumeGame(promotePiece);
    }
    public void resumeGame(int promotePiece) {  
        ajechess.position.playerPieces[index] = new Pieces(promotePiece,location);
        ajechess.boardPanel.repaint();
        ajechess.state = Ajechess.COMPUTER_MOVE;
    }
}
