package ajechess.chess;

public class Position  {
    Move Move;
    int[] board = new int[120];
    Pieces[] playerPieces = new Pieces[17];
    Pieces[] aiPieces = new Pieces[17];
    
    public Position() {
        for(int i=0; i<board.length; i++) {
            board[i] = Ajechess.EMPTY;
        }
    }
    public Position(Position position) {
        this(position,null);
    }
    public Position(Position position, Move Move) {
        System.arraycopy(position.board, 0, this.board, 0, board.length);
        for(int i=1; i<playerPieces.length; i++) {
            if(position.playerPieces[i] != null) {
                this.playerPieces[i] = position.playerPieces[i].clone();
            }
            if(position.aiPieces[i] != null) {
                this.aiPieces[i] = position.aiPieces[i].clone();
            }
        }
        if(Move != null) update(Move);
    }    
    public void initialize(boolean humanWhite) {         
        playerPieces[1] = new Pieces(Pieces.KNIGHT,92);
        playerPieces[2] = new Pieces(Pieces.KNIGHT,97);
        playerPieces[3] = new Pieces(Pieces.BISHOP,93);
        playerPieces[4] = new Pieces(Pieces.BISHOP,96);
        playerPieces[5] = new Pieces(Pieces.ROOK,91);
        playerPieces[6] = new Pieces(Pieces.ROOK,98);
        playerPieces[7] = new Pieces(Pieces.QUEEN,humanWhite?94:95);
        playerPieces[8] = new Pieces(Pieces.KING,humanWhite?95:94);
        
        aiPieces[1] = new Pieces(Pieces.KNIGHT,22);
        aiPieces[2] = new Pieces(Pieces.KNIGHT,27);
        aiPieces[3] = new Pieces(Pieces.BISHOP,23);
        aiPieces[4] = new Pieces(Pieces.BISHOP,26);
        aiPieces[5] = new Pieces(Pieces.ROOK,21);
        aiPieces[6] = new Pieces(Pieces.ROOK,28);
        aiPieces[7] = new Pieces(Pieces.QUEEN,humanWhite?24:25);
        aiPieces[8] = new Pieces(Pieces.KING,humanWhite?25:24); 
        
        int j = 81;
        for(int i=9; i<playerPieces.length; i++) {
            playerPieces[i] = new Pieces(Pieces.PAWN,j);
            aiPieces[i] = new Pieces(Pieces.PAWN,j-50);
            j++;
        }                      
        board = new int[] {
            Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.EMPTY,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,
            Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE,Ajechess.ILLEGAL_MOVE
        };        
        for(int i=0; i<board.length; i++) {                        
            for(int k=1; k<playerPieces.length; k++) {
                if(i==playerPieces[k].location) {
                    board[i] = k;
                }
                else if(i==aiPieces[k].location) {
                    board[i] = -k;
                }
            }
        }
    }    
    public void update(Move move) {
        this.Move = move;   
        int index = board[move.source];
        int destination_index = board[move.destination];  
        if(index>0) {
            playerPieces[index].has_moved = true;
            playerPieces[index].location = move.destination;
            if(destination_index<0) {                
                aiPieces[-destination_index] = null;
            }            
        }
        else {
            aiPieces[-index].has_moved = true;
            aiPieces[-index].location = move.destination;
            if(destination_index>0 && destination_index != Ajechess.EMPTY) {                
                playerPieces[destination_index] = null;
            }            
        }
        board[move.source] = Ajechess.EMPTY;
        board[move.destination] = index;
    }
}
