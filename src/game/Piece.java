package game;

public enum Piece {
    /** The three different kinds of pieces that can populate any square on the board.*/
    O, X, EMP;

    /** Returns the one-character denotation of this piece.*/
    public String abbrev() {
        switch (this) {
        case O:
            return "o";
        case X:
            return "x";
        case EMP:
            return " ";
        default:
                throw new IllegalArgumentException("piece name unknown");
        }
    }

    /**Alternates.*/
    public Piece alternate() {
        if (this == X) {
            return O;
        } else {
            return X;
        }
    }
}
