package ajechess.chess;

public class Pieces  {
    public final static int PAWN = 100;
    public final static int KNIGHT = 360;
    public final static int BISHOP = 325;
    public final static int ROOK = 500;
    public final static int QUEEN = 900;
    public final static int KING = 1000000;       
    int value;
    int location;
    boolean has_moved;
    public Pieces(int value,int location) {
        this(value,location,true);
    }
    public Pieces(int value,int location,boolean hasMoved) {
        this.value = value;
        this.location = location;
        this.has_moved = hasMoved;
    }
    @Override
    public Pieces clone() {
        return new Pieces(value,location,has_moved);
    }
}
