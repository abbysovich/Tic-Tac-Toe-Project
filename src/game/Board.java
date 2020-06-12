package game;

import java.util.ArrayList;
import java.util.List;

import static game.Utils.*;
import static game.Piece.*;

public class Board {

    /**Default 3x3 board in case no initial configuration provided.*/
    private static Piece[][] standard = {{EMP, EMP, EMP}, {EMP, EMP, EMP}, {EMP, EMP, EMP}};

    /**Returns a standard n x n board.*/
    private static Piece[][] createStandard(int n) {
        Piece[][] board = new Piece[n][n];
        for (int col = 0; col < board.length; col += 1) {
            for (int row = 0; row < board[0].length; row += 1) {
                board[col][row] = EMP;
            }
        }
        return board;
    }

    /** 2D array representing board. Indexed as follows - _board[column][row]*/
    private Piece[][] _board;

    /** Integer length/breadth of the board.*/
    private int size;

    /**Is the board in a tie situation.*/
    private boolean tie = false;

    /**Current player. This player is next to move. X always starts.*/
    private Piece currPlayer;

    /**Winner of the game.*/
    private Piece winner;

    /**Initializes a board.*/
    private void initialize(int size, Piece[][] initConfig) {
        if (initConfig == null) {
            _board = createStandard(size);
        }
        else {
            _board = initConfig;
        }
        this.size = size;
        currPlayer = X;
    }

    /**Board copy constructor. Doesn't actually make a copy.*/
    public Board Board(Board board) {
        return new Board(board.size, board._board);
    }

    /**Board copy constructor proper.*/
    public static Board copyBoard(Board board) {
        Board result = new Board();
        for (int col = 0; col < result._board.length; col++) {
            for (int row = 0; row < result._board.length; row++) {
                result._board[col][row] = board._board[col][row];
            }
        }
        result.winner = board.winner;
        result.tie = board.tie;
        result.size = board.size;
        result.currPlayer = board.currPlayer;
        return result;
    }

    /**Constructs a board which is in state given by initConfig.*/
    public Board(int size, Piece[][] initConfig) {
        initialize(size, initConfig);
    }

    /**Constructs an empty new size x size board. All the squares will contain EMP by default.*/
    public Board(int size) {
        initialize(size, null);
    }

    /**Constructs an empty 3x3 board.*/
    public Board() {
        initialize(3, null);
    }

    /** Prints out the board in its current state */
    public void displayBoard() {
        newLine();
        for (int row = _board[0].length - 1; row >= 0; row -= 1) {
            String pair = new String();
            for (int col = 0; col < _board.length; col += 1) {
                pair += "|";
                pair += _board[col][row].abbrev();
            }
            System.out.println(pair + "|");
            newLine();
        }
    }

    /**Returns true if the move is legal. Move will be of the form "2,3".*/
    private boolean isLegal(String move) {
        if (!move.matches("[0-9]*,[0-9]*")) {
            return false;
        }

        int column = Integer.parseInt(move.substring(2)) - 1;
        int row = Integer.parseInt(move.substring(0, 1)) - 1;

        if (beyondBoard(column, row)) {
            return false;
        }

        if (isOccupied(column, row)) {
            return false;
        }

        return true;
    }

    /**Returns whether or not the specified square has already been filled in.*/
    private boolean isOccupied(int col, int row) {
        if (_board[col][row] != EMP) {
            return true;
        }
        return false;
    }

    /**Returns true if either of the coordinates of the doesn't fit on the board.*/
    private boolean beyondBoard(int col, int row) {
        if (col >= size || row >= size || col <= -1 || row <= -1) {
            return true;
        }
        return false;
    }

    /**Makes the specified move.*/
     void makeMove(String move) {
        if (!isLegal(move)) {
            throw new IllegalArgumentException("This is an illegal move.");
        }
        int column = Integer.parseInt(move.substring(2)) - 1;
        int row = Integer.parseInt(move.substring(0, 1)) - 1;
        _board[column][row] = currPlayer;
        alternatePlayer(currPlayer);
    }

    /**Alternates the value of currPlayer.*/
    private void alternatePlayer(Piece current) {
        if (currPlayer == X) {
            currPlayer = O;
        }
        else currPlayer = X;
    }

    /**Returns true if this is a winning line of pieces*/
    private boolean isWinning(Piece[] arr) {
        if (arr[0] == X) {
            for (Piece piece : arr) {
                if (piece != X) {
                    return false;
                }
            }
        }
        else {
            for (Piece piece : arr) {
                if (piece != O) {
                    return false;
                }
            }
        }
        return true;
    }

    /**Destructively tranposes THIS board.*/
    private void transpose() {
        Utils.transpose(this._board);
    }

    /**Returns the diagonals in a 2D array.*/
    public Piece[][] getDiag() {
        Piece[][] result = new Piece[2][3];
        for (int i = 0; i < this.size; i++) {
            result[0][i] = _board[i][i];
        }
        for (int j = 0; j < this.size; j++) {
            result[1][j] = _board[j][size - 1 - j];
        }
        return result;
    }

    /**Returns true if the game is over. To do this, you just need
     * to check if there is a complete line along n^2 + 2 possible
     * lines - the two diagonals, n horizontals, and n verticals*/
    public boolean isGameOver() {
        if (winner == null) {
            Piece[] diagOne = new Piece[this.size];
            Piece[] diagTwo = new Piece[this.size];
            for (int col = 0; col < _board.length; col += 1) {
                diagOne[col] = _board[col][col];
                diagTwo[col] = _board[col][this.size - 1 - col];
                if (isWinning(_board[col])) {
                    alternatePlayer(currPlayer);
                    winner = currPlayer;
                    return true;
                }
            }
            this.transpose();
            for (int row = 0; row < _board[0].length; row += 1) {
                if (isWinning(this._board[row])) {
                    this.transpose();
                    alternatePlayer(currPlayer);
                    winner = currPlayer;
                    return true;
                }
            }

            this.transpose();

            if (isWinning(diagOne) || isWinning(diagTwo)) {
                alternatePlayer(currPlayer);
                winner = currPlayer;
                return true;
            }
            if (checkTie()) {
                return true;
            }
        }
        return false;
    }

    /**Returns the winner.*/
    public Piece getWinner() {
        return winner;
    }

    /**Returns true if the board is in a tie.*/
    public boolean inTie() {
        return this.tie;
    }

    /**Returns true if the current state of the board is a tie*/
    public boolean checkTie() {
        if (winner == null) {
            int counter = 0;
            for (int col = 0; col < _board.length; col += 1) {
                for (int row = 0; row < _board.length; row += 1) {
                    if (_board[col][row] == EMP) {
                        counter += 1;
                    }
                }
            }
            if (counter == 0) {
                tie = true;
                return true;
            }
        }
        return false;
    }


    /**Announces winner.*/
    public void announceWinner() {
        if (this.tie) {
            System.out.println("Tie Game!!!");
            return;
        }
        System.out.println(winner.abbrev().toUpperCase() + " has won!!!");
    }

    /**Returns a list of legal moves.*/
    public List<String> legalMoves() {
        List<String> result = new ArrayList<>();
        for (int col = 1; col < _board.length + 1; col += 1) {
            for (int row = 1; row < _board.length + 1; row += 1) {
                String move = row + "," + col;
                if (isLegal(move)) {
                    result.add(move);
                }
            }
        }
        return result;
    }

    /**Return the current Player who is next to play.*/
    public Piece getCurrPlayer() {
        return this.currPlayer;
    }

    /**Get the topmost horizontal line. Returns it from left-to-right.*/
    public Piece[] getTopLine() {
        Piece[] topLine = new Piece[3];
        for (int i = 0; i < this.getBoard().length; i += 1) {
            topLine[i] = this.getBoard()[i][2];
        }
        return topLine;
    }

    /**Get the leftmost vertical line. Returns it from top-to-bottom.*/
    public Piece[] getLeftLine() {
        Piece[] leftLine = new Piece[3];
        for (int i = 0; i < this._board.length; i += 1) {
            leftLine[i] = _board[0][2 - i];
        }
        return leftLine;
    }

    /**Get the rightmost vertical line. Returns it from top-to-bottom.*/
    public Piece[] getRightLine() {
        Piece[] rightLine = new Piece[3];
        for (int i = 0; i < this._board.length; i += 1) {
            rightLine[i] = _board[2][2-i];
        }
        return rightLine;
    }

    /**Get the bottommost horizontal line. Returns it from left-to-right.*/
    public Piece[] getBottomLine() {
        Piece[] bottomLine = new Piece[3];
        for (int i = 0; i < _board.length; i += 1) {
            bottomLine[i] = _board[i][0];
        }
        return bottomLine;
    }

    /**Returns an array of the edges of the 3x3 board.*/
    public List<Piece> getEdges() {
        List<Piece> result = new ArrayList<>();
        result.add(_board[0][1]);
        result.add(_board[1][0]);
        result.add(_board[1][2]);
        result.add(_board[2][1]);
        return result;
    }

    /** Getter method for board.*/
    public Piece[][] getBoard() {
        return _board;
    }

    /** Returns whether or not this board is empty.*/
    public boolean isEmpty() {
        for (int col = 0; col < _board.length; col += 1) {
            for (int row = 0; row < _board.length; row += 1) {
                if (_board[col][row] != EMP) {
                    return false;
                }
            }
        }
        return true;
    }

}
