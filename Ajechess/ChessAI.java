package ajechess.chess;

import java.util.ArrayList;
import java.util.List;

public class ChessAI {
	Position position;
	int player;
	List<Position> positions = new ArrayList<Position>();
	Game game;

	public ChessAI(Position position, int player) {
		this.position = position;
		this.player = player;
		this.game = new Game(position);
	}

	public Position[] getPositions() {
		return positions.toArray(new Position[positions.size()]);
	}

	public void generateMoves() {
		if (player == Ajechess.PLAYER) {
			for (int i = 1; i < position.playerPieces.length; i++) {
				Pieces piece = position.playerPieces[i];
				if (piece == null)
					continue;
				if (piece.value == Pieces.PAWN) {
					playerPawnMoves(piece);
				} else if (piece.value == Pieces.KNIGHT) {
					playerKnightMoves(piece);
				} else if (piece.value == Pieces.KING) {
					playerKingMoves(piece);
				} else if (piece.value == Pieces.BISHOP) {
					playerBishopMoves(piece);
				} else if (piece.value == Pieces.ROOK) {
					playerRookMoves(piece);
				} else if (piece.value == Pieces.QUEEN) {
					playerQueenMoves(piece);
				}
			}
		} else {
			for (int i = 1; i < position.aiPieces.length; i++) {
				Pieces piece = position.aiPieces[i];
				if (piece == null)
					continue;
				if (piece.value == Pieces.PAWN) {
					aiPawnMoves(piece);
				} else if (piece.value == Pieces.KNIGHT) {
					aiKnightMoves(piece);
				} else if (piece.value == Pieces.KING) {
					aiKingMoves(piece);
				} else if (piece.value == Pieces.BISHOP) {
					aiBishopMoves(piece);
				} else if (piece.value == Pieces.ROOK) {
					aiRookMoves(piece);
				} else if (piece.value == Pieces.QUEEN) {
					aiQueenMoves(piece);
				}
			}
		}
	}

	public void playerPawnMoves(Pieces pawn) {
		int location = pawn.location;
		int forwardPiece = position.board[location - 10];
		if (forwardPiece != Ajechess.ILLEGAL_MOVE) {
			if (forwardPiece == Ajechess.EMPTY && game.safeMove(Ajechess.PLAYER, location, location - 10)) {
				positions.add(new Position(position, new Move(location, location - 10)));

			}
		}

		if (location > 80 && forwardPiece == Ajechess.EMPTY && position.board[location - 20] == Ajechess.EMPTY
				&& game.safeMove(Ajechess.PLAYER, location, location - 20)) {
			positions.add(new Position(position, new Move(location, location - 20)));
		}

		int rightPiece = position.board[location - 9];
		if (rightPiece != Ajechess.ILLEGAL_MOVE) {
			if (rightPiece < 0 && game.safeMove(Ajechess.PLAYER, location, location - 9))
				positions.add(new Position(position, new Move(location, location - 9)));
		}
		int leftPiece = position.board[location - 11];
		if (leftPiece != Ajechess.ILLEGAL_MOVE) {
			if (leftPiece < 0 && game.safeMove(Ajechess.PLAYER, location, location - 11))
				positions.add(new Position(position, new Move(location, location - 11)));
		}
	}

	public void aiPawnMoves(Pieces pawn) {
		int location = pawn.location;
		int forwardPiece = position.board[location + 10];
		if (forwardPiece != Ajechess.ILLEGAL_MOVE) {
			if (forwardPiece == Ajechess.EMPTY && game.safeMove(Ajechess.COMPUTER, location, location + 10)) {
				positions.add(new Position(position, new Move(location, location + 10)));
			}
		}

		if (location < 39 && forwardPiece == Ajechess.EMPTY && position.board[location + 20] == Ajechess.EMPTY
				&& game.safeMove(Ajechess.COMPUTER, location, location + 20)) {
			positions.add(new Position(position, new Move(location, location + 20)));
		}

		int rightPiece = position.board[location + 11];
		if (rightPiece != Ajechess.ILLEGAL_MOVE) {
			if (rightPiece > 0 && rightPiece != Ajechess.EMPTY
					&& game.safeMove(Ajechess.COMPUTER, location, location + 11))
				positions.add(new Position(position, new Move(location, location + 11)));
		}
		int leftPiece = position.board[location + 9];
		if (leftPiece != Ajechess.ILLEGAL_MOVE) {
			if (leftPiece > 0 && leftPiece != Ajechess.EMPTY
					&& game.safeMove(Ajechess.COMPUTER, location, location + 9))
				positions.add(new Position(position, new Move(location, location + 9)));

		}
	}

	public void playerKnightMoves(Pieces knight) {
		int location = knight.location;
		int[] destinations = { location - 21, location + 21, location + 19, location - 19, location - 12, location + 12,
				location - 8, location + 8 };
		for (int i = 0; i < destinations.length; i++) {
			int locationDeltas_piece_index = position.board[destinations[i]];
			if (locationDeltas_piece_index != Ajechess.ILLEGAL_MOVE
					&& (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index < 0)
					&& game.safeMove(Ajechess.PLAYER, location, destinations[i]))
				positions.add(new Position(position, new Move(location, destinations[i])));

		}
	}

	public void aiKnightMoves(Pieces knight) {
		int location = knight.location;
		int[] destinations = { location - 21, location + 21, location + 19, location - 19, location - 12, location + 12,
				location - 8, location + 8 };

		for (int i = 0; i < destinations.length; i++) {
			int locationDeltas_piece_index = position.board[destinations[i]];
			if (locationDeltas_piece_index != Ajechess.ILLEGAL_MOVE
					&& (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index > 0)
					&& game.safeMove(Ajechess.COMPUTER, location, destinations[i])) {
				positions.add(new Position(position, new Move(location, destinations[i])));
			}
		}
	}

	public void playerKingMoves(Pieces king) {
		int location = king.location;
		int[] destinations = { location + 1, location - 1, location + 10, location - 10, location + 11, location - 11,
				location + 9, location - 9 };

		for (int i = 0; i < destinations.length; i++) {
			int locationDeltas_piece_index = position.board[destinations[i]];
			if (locationDeltas_piece_index != Ajechess.ILLEGAL_MOVE
					&& (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index < 0)
					&& game.safeMove(Ajechess.PLAYER, location, destinations[i])) {
				positions.add(new Position(position, new Move(location, destinations[i])));
			}
		}
	}

	public void aiKingMoves(Pieces king) {
		int location = king.location;
		int[] destinations = { location + 1, location - 1, location + 10, location - 10, location + 11, location - 11,
				location + 9, location - 9 };
		for (int i = 0; i < destinations.length; i++) {
			int locationDeltas_piece_index = position.board[destinations[i]];
			if (locationDeltas_piece_index != Ajechess.ILLEGAL_MOVE
					&& (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index > 0)
					&& game.safeMove(Ajechess.COMPUTER, location, destinations[i])) {
				positions.add(new Position(position, new Move(location, destinations[i])));
			}
		}
	}

	public void playerBishopMoves(Pieces bishop) {
		int location = bishop.location;
		int[] deltas = { 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.PLAYER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index < 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY || !safe_move) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index > 0 && locationDeltas_piece_index != Ajechess.EMPTY) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}

	public void aiBishopMoves(Pieces bishop) {
		int location = bishop.location;
		int[] deltas = { 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.COMPUTER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index > 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY || !safe_move) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index < 0) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}

	public void playerRookMoves(Pieces rook) {
		int location = rook.location;
		int[] deltas = { 1, -1, 10, -10 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.PLAYER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index < 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index > 0 && locationDeltas_piece_index != Ajechess.EMPTY) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}

	public void aiRookMoves(Pieces rook) {
		int location = rook.location;
		int[] deltas = { 1, -1, 10, -10 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.COMPUTER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index > 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index < 0) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}

	public void playerQueenMoves(Pieces queen) {
		int location = queen.location;
		int[] deltas = { 1, -1, 10, -10, 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.PLAYER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index < 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index > 0 && locationDeltas_piece_index != Ajechess.EMPTY) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}

	public void aiQueenMoves(Pieces queen) {
		int location = queen.location;
		int[] deltas = { 1, -1, 10, -10, 11, -11, 9, -9 };
		for (int i = 0; i < deltas.length; i++) {
			int locationDeltas = location + deltas[i];
			while (true) {
				int locationDeltas_piece_index = position.board[locationDeltas];
				if (locationDeltas_piece_index == Ajechess.ILLEGAL_MOVE) {
					break;
				}
				boolean safe_move = game.safeMove(Ajechess.COMPUTER, location, locationDeltas);
				if (locationDeltas_piece_index == Ajechess.EMPTY || locationDeltas_piece_index > 0) {
					if (safe_move) {
						positions.add(new Position(position, new Move(location, locationDeltas)));
						if (locationDeltas_piece_index != Ajechess.EMPTY) {
							break;
						}
					} else if (locationDeltas_piece_index != Ajechess.EMPTY)
						break;
				} else if (locationDeltas_piece_index < 0) {
					break;
				}
				locationDeltas += deltas[i];
			}
		}
	}
}
