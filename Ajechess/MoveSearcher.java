package ajechess.chess;

public class MoveSearcher {
    Ajechess ajechess;
    int level;
    public MoveSearcher(Ajechess ajechess) {
        this.ajechess = ajechess;
    }
    public Position alphaBeta(int player, Position position, int alpha, int beta, int depth) {
        if(depth == 0) 
        	return position;
        Position bestPosition = null;
        ChessAI move_generator = new ChessAI(position,player);
        move_generator.generateMoves();
        Position[] positions = move_generator.getPositions();
        if(positions.length == 0) 
        	return position;    
        
        ChessCalculator evaluator = new ChessCalculator();        
        for(Position piecePosition:positions) {
            if(bestPosition == null) bestPosition = piecePosition;
            if(player == Ajechess.PLAYER) {
                Position opponentPosition = alphaBeta(Ajechess.COMPUTER,piecePosition,alpha,beta,depth-1);                
                int score = evaluator.evaluate(opponentPosition); 
                if(score>alpha) {
                    bestPosition = piecePosition;
                    alpha = score;
                }
            }
            else {
                Position opponentPosition = alphaBeta(Ajechess.PLAYER,piecePosition,alpha,beta,depth-1);                
                if(new Game(opponentPosition).isChecked(Ajechess.PLAYER)) {
                    return piecePosition;
                }
                int score = evaluator.evaluate(opponentPosition);
                if(score<=alpha && level > 4) 
                	return piecePosition;
                if(score<beta) {
                    bestPosition = piecePosition;
                    beta = score;
                }              
            }
        }
        return bestPosition;
    }
}
