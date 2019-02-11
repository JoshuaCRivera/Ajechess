package ajechess.chess;

import javax.swing.JLabel;

public class Game {
	Position position;
	Pieces playerKing;
	Pieces aiKing;
	JLabel inCheck;

	public Game(Position position) {
		playerKing = position.playerPieces[8];
		aiKing = position.aiPieces[8];
		this.position = position;
	}

	public int getResult(int player) {
		int state = -1;
		ChessAI ChessAI = new ChessAI(position, player);
		ChessAI.generateMoves();
		Position[] positions = ChessAI.getPositions();
		if (positions.length == 0) {
			if (isChecked(player)) {
				state = Ajechess.CHECKMATE;
			} else {
				state = Ajechess.DRAW;
			}
		}
		return state;
	}

	public boolean safeMove(int player, int source, int destination) {
		Move _move = new Move(source, destination);
		Position _position = new Position(position, _move);
		Game game = new Game(_position);
		return !game.isChecked(player);
	}

	public boolean isChecked(int player) {
		boolean checked = false;

		Pieces king;

		if (player == Ajechess.PLAYER) {
			king = playerKing;
		} else {
			king = aiKing;
		}

		if (king == null) {
			return false;
		}
		checked = checkedByPawn(king);
		if (!checked) {
			checked = checkedByKnight(king);
		}
		if (!checked) {
			checked = checkedByBishop(king);
		}
		if (!checked) {
			checked = checkedByRook(king);
		}
		if (!checked) {
			checked = checkedByQueen(king);
		}
		if (!checked) {
			checked = desSquareAttackedByKing(king);
		}
		return checked;
	}

	private boolean checkedByPawn(Pieces king) {
		boolean checked = false;
		int location = king.location;
		if (king == playerKing) {
			int right_square = position.board[location - 9];
			int left_square = position.board[location - 11];
			if (right_square == Ajechess.ILLEGAL_MOVE || left_square == Ajechess.ILLEGAL_MOVE)
				return false;
			if (right_square < 0 && position.aiPieces[-right_square].value == Pieces.PAWN)
				checked = true;
			if (left_square < 0 && position.aiPieces[-left_square].value == Pieces.PAWN)
				checked = true;
		} else {
			int right_square = position.board[location + 11];
			int left_square = position.board[location + 9];
			if (right_square != Ajechess.ILLEGAL_MOVE) {
				if (right_square > 0 && right_square != Ajechess.EMPTY
						&& position.playerPieces[right_square].value == Pieces.PAWN)
					checked = true;
			}
			if (left_square != Ajechess.ILLEGAL_MOVE) {
				if (left_square > 0 && left_square != Ajechess.EMPTY
						&& position.playerPieces[left_square].value == Pieces.PAWN)
					checked = true;
			}
		}
		return checked;
	}

	private boolean checkedByKnight(Pieces king) {
		boolean checked = false;
		int location = king.location;
		int[] destinations = { location - 21, location + 21, location + 19, location - 19, location - 12, location + 12,
				location - 8, location + 8 };
		for (int destination : destinations) {
			int des_square = position.board[destination];
			if (des_square == Ajechess.ILLEGAL_MOVE)
				continue;
			if (king == playerKing) {
				if (des_square < 0 && position.aiPieces[-des_square].value == Pieces.KNIGHT) {
					checked = true;
					break;
				}
			} else {
				if (des_square > 0 && des_square != Ajechess.EMPTY
						&& position.playerPieces[des_square].value == Pieces.KNIGHT) {
					checked = true;
					break;
				}
			}
		}
		return checked;
	}

	private boolean desSquareAttackedByKing(Pieces king) {
		boolean checked = false;
		int location = king.location;
		int[] destinations = { location + 1, location - 1, location + 10, location - 10, location + 11, location - 11,
				location + 9, location - 9 };
		for (int destination : destinations) {
			int des_square = position.board[destination];
			if (des_square == Ajechess.ILLEGAL_MOVE)
				continue;
			if (king == playerKing) {
				if (des_square < 0 && position.aiPieces[-des_square].value == Pieces.KING) {
					checked = true;
					break;
				}
			} else {
				if (des_square > 0 && des_square != Ajechess.EMPTY
						&& position.playerPieces[des_square].value == Pieces.KING) {
					checked = true;
					break;
				}
			}
		}
		return checked;
	}

	private boolean checkedByBishop(Pieces king) {
		boolean checked = false;
		int[] deltas = { 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int delta = king.location + deltas[i];
			while (true) {
				int des_square = position.board[delta];
				if (des_square == Ajechess.ILLEGAL_MOVE) {
					checked = false;
					break;
				}
				if (king == playerKing) {
					if (des_square < 0 && position.aiPieces[-des_square].value == Pieces.BISHOP) {
						checked = true;
						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				} else if (king == aiKing) {
					if (des_square > 0 && des_square != Ajechess.EMPTY
							&& position.playerPieces[des_square].value == Pieces.BISHOP) {
						checked = true;
						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				}
				delta += deltas[i];
			}
			if (checked)
				break;
		}
		return checked;
	}

	private boolean checkedByRook(Pieces king) {
		boolean checked = false;
		int[] deltas = { 1, -1, 10, -10 };
		for (int i = 0; i < deltas.length; i++) {
			int delta = king.location + deltas[i];
			while (true) {
				int des_square = position.board[delta];
				if (des_square == Ajechess.ILLEGAL_MOVE) {
					checked = false;
					break;
				}
				if (king == playerKing) {
					if (des_square < 0 && position.aiPieces[-des_square].value == Pieces.ROOK) {
						checked = true;
						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				} else if (king == aiKing) {
					if (des_square > 0 && des_square != Ajechess.EMPTY
							&& position.playerPieces[des_square].value == Pieces.ROOK) {
						checked = true;
						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				}
				delta += deltas[i];
			}
			if (checked)
				break;
		}
		return checked;
	}

	private boolean checkedByQueen(Pieces king) {
		boolean checked = false;
		int[] deltas = { 1, -1, 10, -10, 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int delta = king.location + deltas[i];
			while (true) {
				int des_square = position.board[delta];
				if (des_square == Ajechess.ILLEGAL_MOVE) {
					checked = false;
					break;
				}
				if (king == playerKing) {
					if (des_square < 0 && position.aiPieces[-des_square].value == Pieces.QUEEN) {
						checked = true;

						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				} else if (king == aiKing) {
					if (des_square > 0 && des_square != Ajechess.EMPTY
							&& position.playerPieces[des_square].value == Pieces.QUEEN) {
						checked = true;
						break;
					} else if (des_square != Ajechess.EMPTY)
						break;
				}
				delta += deltas[i];
			}
			if (checked)
				break;
		}
		return checked;
	}
}
