package ajechess.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Ajechess extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	Position position;
	ChessBoardPane boardPanel;
	Resource resource = new Resource();
	Map<Integer, Image> images = new HashMap<Integer, Image>();
	Map<Integer, Icon> iconImages = new HashMap<Integer, Icon>();
	Move move = new Move();
	boolean pieceSelected;
	boolean whitePiece = true;
	int state;
	MoveSearcher moveSearcher;
	Game game;
	JLabel newGame;
	JLabel rules;
	JLabel quit;
	JPanel panel = new JPanel(new BorderLayout());
	PreferencesPanel options;
	boolean castling;
	PawnPromotionPanel pawnPromotionPanel;
	Color backGroundColor = Color.decode("#9befb7");

	public Ajechess() {
		super("Ajechess");
		setContentPane(panel);
		position = new Position();
		pawnPromotionPanel = new PawnPromotionPanel(this);

		loadMenuIcons();
		loadBoardImages();

		boardPanel = new ChessBoardPane();

		panel.add(createMenuPane(), BorderLayout.WEST);
		panel.add(boardPanel, BorderLayout.CENTER);
		panel.setBackground(backGroundColor);

		pack();
		Dimension size = getSize();
		size.height = 525;
		setSize(size);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				quit();
			}
		});
	}

	public final static int EMPTY = 55;
	public final static int ILLEGAL_MOVE = 77;
	public final static int PLAYER = 1;
	public final static int COMPUTER = 0;
	public final static int BOARD_IMAGE = 1000;
	public final static int GLOWING_TILE = 1001;
	public final static int GLOWING_DARK_TILE = 1002;
	public final static int PREPARE_ANIMATION = 1003;
	public final static int ANIMATING = 1004;
	public final static int PLAYER_MOVE = 1005;
	public final static int COMPUTER_MOVE = 1006;
	public final static int GAME_ENDED = 1007;
	public final static int DRAW = 0;
	public final static int CHECKMATE = 1;
	public final static int NEW_GAME_BUTTON = 10078;
	public final static int NEW_GAME_HOVER_BUTTON = 10079;
	public final static int QUIT_BUTTON = 10080;
	public final static int QUIT_HOVER_BUTTON = 10081;
	public final static int RULES_BUTTON = 10082;
	public final static int RULES_HOVER_BUTTON = 10083;
	public final static int PROMOTING = 10086;
	public final static int AJECHESS = 10095;

	public JPanel createMenuPane() {
		newGame = new JLabel(iconImages.get(NEW_GAME_BUTTON));
		rules = new JLabel(iconImages.get(RULES_BUTTON));
		quit = new JLabel(iconImages.get(QUIT_BUTTON));

		newGame.addMouseListener(this);
		rules.addMouseListener(this);
		quit.addMouseListener(this);

		JPanel panel = new JPanel(new GridLayout(8, 1));
		panel.add(newGame);
		panel.add(rules);
		panel.add(quit);
		panel.setBackground(backGroundColor);
		JPanel mainMenuPanel = new JPanel(new BorderLayout());
		mainMenuPanel.setBackground(backGroundColor);
		mainMenuPanel.add(panel, BorderLayout.SOUTH);
		mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 16, 0));

		return mainMenuPanel;
	}

	public void newGame() {

		whitePiece = options.whitePiecesButton.isSelected();
		move.source = -1;
		move.destination = -1;
		position = new Position();
		position.initialize(whitePiece);
		game = new Game(position);
		loadPieceImages();
		pawnPromotionPanel.setIcons(whitePiece);
		boardPanel.repaint();

		if (true == whitePiece) {
			state = PLAYER_MOVE;
		} else
			state = COMPUTER_MOVE;
		castling = false;
		moveSearcher.level = options.levelSlider.getValue();
		play();
	}

	public void play() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					switch (state) {
					case PLAYER_MOVE:
						break;
					case COMPUTER_MOVE:
						if (gameEnded(COMPUTER)) {
							state = GAME_ENDED;
							break;
						}
						move = moveSearcher.alphaBeta(COMPUTER, position, Integer.MIN_VALUE, Integer.MAX_VALUE,
								options.levelSlider.getValue()).Move;
						state = PREPARE_ANIMATION;
						break;
					case PREPARE_ANIMATION:
						prepareAnimation();
						break;
					case ANIMATING:
						animate();
						break;
					case GAME_ENDED:
						return;
					}
					try {
						Thread.sleep(3);
					} catch (Exception event) {
						event.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	public boolean gameEnded(int player) {
		int result = game.getResult(player);
		boolean endGame = false;
		String color = "";
		if (player == COMPUTER) {
			if (true == whitePiece)
				color = "White";
			else
				color = "Black";
		} else if (false == whitePiece) {
			color = "Black";
		} else {
			color = "White";
		}

		if (result == CHECKMATE) {
			showEndGameResult("Checkmate: the " + color + " player has won");
			endGame = true;
		} else if (result == DRAW) {
			showEndGameResult("Stalemate: the enemy king cannot move, but is not in check");
			endGame = true;
		}
		return endGame;
	}

	public void showEndGameResult(String message) {
		int option = JOptionPane.showOptionDialog(null, message, "Game Over", 0, JOptionPane.PLAIN_MESSAGE, null,
				new Object[] { "Play again", "Cancel" }, "Play again");
		if (option == 0) {
			options.setVisible(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		Object source = event.getSource();
		if (source == quit) {
			quit();
		} else if (source == newGame) {
			if (state == COMPUTER_MOVE) {
				return;
			}

			if (options == null) {
				options = new PreferencesPanel(this);
				moveSearcher = new MoveSearcher(this);
			}
			options.setVisible(true);
		} else if (source == rules) {
			ChessRules.createAndShowUI();
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		Object source = event.getSource();
		if (source == newGame) {
			newGame.setIcon(iconImages.get(NEW_GAME_HOVER_BUTTON));
		} else if (source == rules) {
			rules.setIcon(iconImages.get(RULES_HOVER_BUTTON));
		} else if (source == quit) {
			quit.setIcon(iconImages.get(QUIT_HOVER_BUTTON));
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		Object source = event.getSource();
		if (source == newGame) {
			newGame.setIcon(iconImages.get(NEW_GAME_BUTTON));
		} else if (source == rules) {
			rules.setIcon(iconImages.get(RULES_BUTTON));
		} else if (source == quit) {
			quit.setIcon(iconImages.get(QUIT_BUTTON));
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	public class ChessBoardPane extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;
		Image animating_image;
		int movingX;
		int movingY;
		int desX;
		int desY;
		int deltaX;
		int deltaY;

		public ChessBoardPane() {
			setPreferredSize(new Dimension(450, 500));
			setBackground(backGroundColor);
			addMouseListener(this);
		}

		public boolean validMove(int destination) {
			int source = move.source;
			int squareLocation = position.board[destination];
			if (squareLocation == ILLEGAL_MOVE)
				return false;
			if (!game.safeMove(PLAYER, source, destination))
				return false;
			boolean valid = false;
			int piece_value = position.playerPieces[position.board[source]].value;
			switch (piece_value) {
			case Pieces.PAWN:
				if (destination == source - 10 && squareLocation == EMPTY)
					valid = true;
				if (destination == source - 20 && position.board[source - 10] == EMPTY && squareLocation == EMPTY
						&& source > 80)
					valid = true;
				if (destination == source - 9 && squareLocation < 0)
					valid = true;
				if (destination == source - 11 && squareLocation < 0)
					valid = true;
				break;
			case Pieces.KNIGHT:
			case Pieces.KING:
				if (piece_value == Pieces.KING)
					valid = checkCastling(destination);
				int[] destinations = null;
				if (piece_value == Pieces.KNIGHT)
					destinations = new int[] { source - 21, source + 21, source + 19, source - 19, source - 12,
							source + 12, source - 8, source + 8 };
				else
					destinations = new int[] { source + 1, source - 1, source + 10, source - 10, source + 11,
							source - 11, source + 9, source - 9 };
				for (int i = 0; i < destinations.length; i++) {
					if (destinations[i] == destination) {
						if (squareLocation == EMPTY || squareLocation < 0) {
							valid = true;
							break;
						}
					}
				}
				break;
			case Pieces.BISHOP:
			case Pieces.ROOK:
			case Pieces.QUEEN:
				int[] deltas = null;
				if (piece_value == Pieces.BISHOP)
					deltas = new int[] { 11, -11, 9, -9 };
				if (piece_value == Pieces.ROOK)
					deltas = new int[] { 1, -1, 10, -10 };
				if (piece_value == Pieces.QUEEN)
					deltas = new int[] { 1, -1, 10, -10, 11, -11, 9, -9 };
				for (int i = 0; i < deltas.length; i++) {
					int sourceDeltas = source + deltas[i];
					valid = true;
					while (destination != sourceDeltas) {
						squareLocation = position.board[sourceDeltas];
						if (squareLocation != EMPTY) {
							valid = false;
							break;
						}
						sourceDeltas += deltas[i];
					}
					if (valid == true)
						break;
				}
				break;
			}
			return valid;
		}

		@Override
		public void paintComponent(Graphics graphics) {
			if (position.board == null)
				return;
			super.paintComponent(graphics);
			graphics.drawImage(images.get(AJECHESS), 15, 20, this);
			graphics.drawImage(images.get(BOARD_IMAGE), 20, 65, this);
			for (int i = 0; i < position.board.length - 11; i++) {
				if (position.board[i] == ILLEGAL_MOVE)
					continue;
				int xDirection = i % 10;
				int yDirection = (i - xDirection) / 10;

				if (pieceSelected && i == move.source) {
					graphics.drawImage(images.get(GLOWING_TILE), xDirection * 45 + 1, yDirection * 45 - 1, this);
				} else if (!pieceSelected && move.destination == i
						&& (position.board[i] == EMPTY || position.board[i] < 0)) {
					graphics.drawImage(images.get(GLOWING_DARK_TILE), xDirection * 45 + 1, yDirection * 45 - 1, this);
				}

				if (position.board[i] == EMPTY)
					continue;
				if (state == ANIMATING && i == move.source)
					continue;
				if (position.board[i] > 0) {
					int piece = position.playerPieces[position.board[i]].value;
					graphics.drawImage(images.get(piece), xDirection * 45, yDirection * 45, this);
				} else {
					int piece = position.aiPieces[-position.board[i]].value;
					graphics.drawImage(images.get(-piece), xDirection * 45, yDirection * 45, this);
				}
			}
			if (state == ANIMATING) {
				graphics.drawImage(animating_image, movingX, movingY, this);
			}
		}

		@Override
		public void mouseClicked(MouseEvent event) {
			if (state != PLAYER_MOVE)
				return;
			int location = boardValue(event.getY()) * 10 + boardValue(event.getX());
			if (position.board[location] == ILLEGAL_MOVE)
				return;
			if ((!pieceSelected || position.board[location] > 0) && position.board[location] != EMPTY) {
				if (position.board[location] > 0) {
					pieceSelected = true;
					move.source = location;
				}
			} else if (pieceSelected && validMove(location)) {
				pieceSelected = false;
				move.destination = location;
				state = PREPARE_ANIMATION;
			}
			repaint();
		}

		public void mousePressed(MouseEvent event) {
		}

		public void mouseReleased(MouseEvent event) {
		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

	}

	public void loadBoardImages() {
		try {
			images.put(BOARD_IMAGE, ImageIO.read(resource.getResource("chessboard")));
			images.put(GLOWING_TILE, ImageIO.read(resource.getResource("glowingTile")));
			images.put(GLOWING_DARK_TILE, ImageIO.read(resource.getResource("glowingTileDark")));
			images.put(AJECHESS, ImageIO.read(resource.getResource("title")));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public boolean checkCastling(int destination) {
		Pieces king = position.playerPieces[8];
		Pieces rightRook = position.playerPieces[6];
		Pieces leftRook = position.playerPieces[5];

		if (true == king.has_moved)
			return false;
		int source = move.source;

		if (rightRook == null && leftRook == null)
			return false;
		if (rightRook != null && rightRook.has_moved && leftRook != null && leftRook.has_moved)
			return false;

		if (whitePiece) {
			if (source != 95)
				return false;
			if (destination != 97 && destination != 93)
				return false;
			if (destination == 97) {
				if (position.board[96] != EMPTY)
					return false;
				if (position.board[97] != EMPTY)
					return false;
				if (!game.safeMove(PLAYER, source, 96))
					return false;
				if (!game.safeMove(PLAYER, source, 97))
					return false;
			} else if (destination == 93) {
				if (position.board[94] != EMPTY)
					return false;
				if (position.board[93] != EMPTY)
					return false;
				if (!game.safeMove(PLAYER, source, 94))
					return false;
				if (!game.safeMove(PLAYER, source, 93))
					return false;
			}
		} else {
			if (source != 94)
				return false;
			if (destination != 92 && destination != 96)
				return false;
			if (destination == 92) {
				if (position.board[93] != EMPTY)
					return false;
				if (position.board[92] != EMPTY)
					return false;
				if (!game.safeMove(PLAYER, source, 93))
					return false;
				if (!game.safeMove(PLAYER, source, 92))
					return false;
			} else if (destination == 96) {
				if (position.board[95] != EMPTY)
					return false;
				if (position.board[96] != EMPTY)
					return false;
				if (!game.safeMove(PLAYER, source, 95))
					return false;
				if (!game.safeMove(PLAYER, source, 96))
					return false;
			}
		}
		return castling = true;
	}

	public int boardValue(int value) {
		return value / 45;
	}

	public void prepareAnimation() {
		int imageMovement = 0;
		if (position.board[move.source] > 0) {
			imageMovement = position.playerPieces[position.board[move.source]].value;
		} else {
			imageMovement = -position.aiPieces[-position.board[move.source]].value;
		}
		boardPanel.animating_image = images.get(imageMovement);

		int xDirection = move.source % 10;
		int yDirection = (move.source - xDirection) / 10;
		boardPanel.desX = move.destination % 10;
		boardPanel.desY = (move.destination - boardPanel.desX) / 10;
		int xPlane = boardPanel.desX - xDirection;
		int yPlane = boardPanel.desY - yDirection;
		boardPanel.movingX = xDirection * 45;
		boardPanel.movingY = yDirection * 45;
		if (Math.abs(xPlane) > Math.abs(yPlane)) {
			if (yPlane == 0) {
				if (xPlane > 0) {
					boardPanel.deltaX = 1;
				} else {
					boardPanel.deltaX = -1;
				}
				boardPanel.deltaY = 0;
			} else {
				if (xPlane > 0) {
					boardPanel.deltaX = Math.abs(xPlane / yPlane);
				} else {
					boardPanel.deltaX = -(Math.abs(xPlane / yPlane));
				}
				if (yPlane > 0) {
					boardPanel.deltaY = 1;
				} else {
					boardPanel.deltaY = -1;
				}
			}
		} else {
			if (xPlane == 0) {
				if (yPlane > 0) {
					boardPanel.deltaY = 1;
				} else {
					boardPanel.deltaY = -1;
				}
				boardPanel.deltaX = 0;
			} else {
				if (xPlane > 0) {
					boardPanel.deltaX = 1;
				} else {
					boardPanel.deltaX = -1;
				}
				if (yPlane > 0) {
					boardPanel.deltaY = Math.abs(yPlane / xPlane);
				} else {
					boardPanel.deltaY = -(Math.abs(yPlane / xPlane));
				}
			}
		}
		state = ANIMATING;
	}

	public void animate() {
		if (boardPanel.movingX == boardPanel.desX * 45 && boardPanel.movingY == boardPanel.desY * 45) {
			boardPanel.repaint();
			int source_square = position.board[move.source];
			if (source_square > 0) {
				state = COMPUTER_MOVE;
			} else {
				if (move.destination > 90 && move.destination < 98
						&& position.aiPieces[-source_square].value == Pieces.PAWN)
					promoteComputerPawn();
				state = PLAYER_MOVE;
			}
			position.update(move);

			if (source_square > 0) {
				if (true == castling) {
					prepareCastlingAnimation();
					state = PREPARE_ANIMATION;
				} else if (move.destination > 20 && move.destination < 29
						&& position.playerPieces[source_square].value == Pieces.PAWN) {
					promotePlayerPawn();
				}
			} else {
				if (true == gameEnded(PLAYER)) {
					state = GAME_ENDED;
					return;
				}
			}
		}
		boardPanel.movingX += boardPanel.deltaX;
		boardPanel.movingY += boardPanel.deltaY;
		boardPanel.repaint();
	}

	public void promotePlayerPawn() {
		pawnPromotionPanel.location = move.destination;
		pawnPromotionPanel.index = position.board[move.destination];
		pawnPromotionPanel.setVisible(true);
		state = PROMOTING;
	}

	public void promoteComputerPawn() {
		int piece_index = position.board[move.source];
		position.aiPieces[-piece_index] = new Pieces(Pieces.QUEEN, move.destination);
	}

	public void prepareCastlingAnimation() {
		if (move.destination == 97 || move.destination == 96) {
			move.source = 98;
			move.destination -= 1;
		} else if (move.destination == 92 || move.destination == 93) {
			move.source = 91;
			move.destination += 1;
		}
	}

	public void loadPieceImages() {
		String[] resource_keys = { "p", "n", "b", "r", "q", "k" };
		int[] images_keys = { Pieces.PAWN, Pieces.KNIGHT, Pieces.BISHOP, Pieces.ROOK, Pieces.QUEEN, Pieces.KING };
		try {
			for (int i = 0; i < resource_keys.length; i++) {
				images.put(images_keys[i],
						ImageIO.read(resource.getResource((whitePiece ? "w" : "b") + resource_keys[i])));
				images.put(-images_keys[i],
						ImageIO.read(resource.getResource((whitePiece ? "b" : "w") + resource_keys[i])));
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void loadMenuIcons() {
		iconImages.put(NEW_GAME_BUTTON, new ImageIcon(resource.getResource("newGame")));
		iconImages.put(NEW_GAME_HOVER_BUTTON, new ImageIcon(resource.getResource("newGameHover")));
		iconImages.put(QUIT_BUTTON, new ImageIcon(resource.getResource("quit")));
		iconImages.put(QUIT_HOVER_BUTTON, new ImageIcon(resource.getResource("quitHover")));
		iconImages.put(RULES_BUTTON, new ImageIcon(resource.getResource("rules")));
		iconImages.put(RULES_HOVER_BUTTON, new ImageIcon(resource.getResource("rulesHover")));
	}

	public void quit() {
		int option = JOptionPane.showConfirmDialog(null, "Are you sure you would like to leave the game?", "Warning",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else if (option == JOptionPane.NO_OPTION) {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if (info.getName().equals("Nimbus")) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}

					Ajechess gameWindow = new Ajechess();
					gameWindow.setLocationRelativeTo(null);
					gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					gameWindow.setResizable(false);
					gameWindow.setVisible(true);
				} catch (Exception event) {
					JOptionPane.showMessageDialog(null, event.getStackTrace());
					event.printStackTrace();
				}
			}
		});
	}
}

class Move {
	int source;
	int destination;

	public Move() {
		source = -1;
		destination = -1;
	}

	public Move(int source, int destination) {
		this.source = source;
		this.destination = destination;
	}
}