package ajechess.chess;

public class ChessCalculator {
	public int evaluate(Position position) {
		int human_score = 0;
		int computer_score = 0;
		for (int i = 1; i < position.playerPieces.length; i++) {
			if (position.playerPieces[i] != null) {
				Pieces piece = position.playerPieces[i];
				int value = piece.value;
				human_score += value;
				int location = piece.location;
				int col = (location % 10) - 1;
				int row = ((location - col) / 10) - 2;

				if (value == Pieces.PAWN) {

					if (row < 4) {
						human_score += (8 - row);
					}
					if (col > 4) {
						human_score -= ((col - 4));
					}
					else if (col < 3) {
						human_score -= ((3 - col));
					}
					if (col > 1 && col < 6 && row > 1) {
						human_score += 2;
					}
					if (row == 0) {
						human_score += Pieces.QUEEN;
					}
				}
				else if (value == Pieces.KNIGHT) {
					if (row == 7) {
						human_score -= 1;
					}
					if (col == 0 || col == 7) {
						human_score -= 1;
					}
					if (col > 1 && col < 6 && row > 1 && row < 6) {
						human_score += 1;
					}
				}
				else if (value == Pieces.BISHOP) {
					if (row == 7) {
						human_score -= 1;
					}
					if (col == 0 || col == 7) {
						human_score -= 1;
					}
					if (col > 0 && col < 7 && row > 0 && row < 7) {
						human_score += 1;
					}

				}
			}
			if (position.aiPieces[i] != null) {
				Pieces piece = position.aiPieces[i];
				int value = piece.value;
				computer_score += value;
				int location = piece.location;
				int col = (location % 10) - 1;
				int row = ((location - col) / 10) - 2;
				if (value == Pieces.PAWN) {
					if (row > 3)
						computer_score += row;
					if (col > 4) {
						computer_score -= ((col - 4));
					}
					else if (col < 3) {
						computer_score -= ((3 - col));
					}
					if (col > 1 && col < 6 && row > 1)
						computer_score += 2;
					if (row == 7)
						computer_score += Pieces.QUEEN;
				}
				else if (value == Pieces.KNIGHT) {
					if (row == 0)
						computer_score -= 1;
					if (col == 0 || col == 7)
						computer_score -= 1;
					if (col > 1 && col < 6 && row > 1 && row < 6)
						computer_score += 1;
				}

				else if (value == Pieces.BISHOP) {
					if (row == 0)
						computer_score -= 1;
					if (col == 0 || col == 7)
						computer_score -= 1;
					if (col > 0 && col < 7 && row > 0 && row < 7)
						computer_score += 1;

				}
			}
		}
		return human_score - computer_score;
	}
}
