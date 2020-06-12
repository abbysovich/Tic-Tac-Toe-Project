package game;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static game.Board.*;
import static game.Piece.*;

public class Main {

    /**Starts and manages the computer game.*/
    private static void playComputerGame(Board board) {
        boolean human = true;
        while (!board.isGameOver()) {
            if (human) {
                Scanner input = new Scanner(System.in);
                System.out.println("Enter move");
                String humanMove = input.nextLine();
                try {
                    board.makeMove(humanMove);
                }
                catch(IllegalArgumentException error) {
                    System.out.println("This move is illegal");
                    continue;
                }
                human = false;
            }
            else {
                findMachineMove(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
//                System.out.println(machineMove);
                board.makeMove(machineMove);
                human = true;
            }
            board.displayBoard();
        }
        board.announceWinner();
        System.exit(0);
    }

    /**Starts and manages the auto game.*/
    private static void playAutoGame(Board board) {
        boolean isTie = false;
        while (!board.isGameOver()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter Move: ");
            String humanMove = input.nextLine();
            try {
                board.makeMove(humanMove);
            }
            catch(IllegalArgumentException error) {
                System.out.println("This move is illegal.");
                continue;
            }
            board.displayBoard();
        }
        board.announceWinner();
        System.exit(0);
    }

    /**Reads the file and plays out the game as is instructed by it.*/
    public static void readFile(File file) throws IOException {
        Scanner sc = new Scanner(file);
        Board testBoard = new Board();
        while (sc.hasNextLine()) {
            if (sc.findInLine("display") != null) {
                testBoard.displayBoard();
                sc.nextLine();
                continue;
            }
            if (sc.findInLine("auto") != null) {
                playAutoGame(testBoard);
            }
            if (sc.findInLine("comp") != null) {
                playComputerGame(testBoard);
            }
            testBoard.makeMove(sc.nextLine());
        }
    }

    /** Starts the game.*/
    public static void main(String[] args) throws IOException {
        if (args[0].matches("([a-z]|[0-9])*.in")) {
            String pathname = "./test/" + args[0];
            File testFile = new File(pathname);
            try {
                readFile(testFile);
            }
            catch (IOException error) {
                throw new IOException("Teri ma ka");
            }
            System.exit(0);
        }
        Board board = new Board();
        switch (args[0]) {
        case "auto":
            playAutoGame(board);
        case "comp":
            playComputerGame(board);
        default:
            throw new IllegalArgumentException("Illegal argument. Choose between auto and comp");
        }
    }

    /** Finds and returns the machine's next move. Save the move in saveMove.
     * X is the maximising player. O is the minimizing player.*/
    private static int findMachineMove(Board board, int depth, int alpha, int beta) {
        boolean hasChanged = false;
        boolean maximising = (board.getCurrPlayer() == O) ? false : true;
        if (depth == 0 || board.isGameOver()) {
            return heuristic(board, !maximising);
        }
        int bestScore = (maximising) ? alpha: beta;
        List<String> legalMoves = board.legalMoves();
        for (String move : legalMoves) {
            Board copy = copyBoard(board);
            copy.makeMove(move);
            int score = findMachineMove(copy, depth - 1, alpha, beta);
            if (maximising) {
                if (score > bestScore) {
                    bestScore = score;
                    machineMove = move;
                    hasChanged = true;
                    alpha = Math.max(alpha, score);
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    machineMove = move;
                    hasChanged = true;
                    beta = Math.min(beta, score);
                }
            }
            if (alpha >= beta) {
                break;
            }
            if (machineMove == null || !hasChanged) {
                machineMove = legalMoves.get(0);
                hasChanged = true;
            }
        }
        return bestScore;
    }

    /** Heuristic function.*/
    private static int heuristic(Board board, boolean maximising) {
        boolean diagonalCase = false;
        if (board.isGameOver() || board.getWinner() != null) {
            if (board.inTie()) {
                return 0;
            } else if (maximising) {
                if (board.getWinner() == O) {
                    return Integer.MIN_VALUE;
                }
                return Integer.MAX_VALUE;
            } else {
                if (board.getWinner() == X) {
                    return Integer.MAX_VALUE;
                }
                return Integer.MIN_VALUE;
            }
        }

        List<String> legalMoves = board.legalMoves();
        for (String move : legalMoves) {
            Board copy = copyBoard(board);
            copy.makeMove(move);
            if (copy.isGameOver()) {
                if (copy.inTie()) {
                    int a = (maximising) ? 30 : -30;
                    return a;
                }
                if (maximising) {
                    return Integer.MIN_VALUE;
                }
                return Integer.MAX_VALUE;
            }
            if (cornerTrick(copy)) {
                if (maximising) {
                    return Integer.MIN_VALUE;
                }
                return Integer.MAX_VALUE;
            }
            if (diagonalTrick(copy)) {
                if (maximising) {
                    if (copy.getEdges().contains(X)) {
                        diagonalCase = true;
                    }
                } else {
                    if (copy.getEdges().contains(O)) {
                        diagonalCase = true;
                    }
                }
            }
        }
        if (diagonalCase) {
            if (maximising) {
                return 100;
            } else {
                return -100;
            }
        }

        int result = 0;
        if (board.getBoard()[1][1] == X) {
            result += 50;
        } else if (board.getBoard()[1][1] == O) {
            result -= 50;
        }

        if (maximising) {
            result += 20;
        } else {
            result -= 20;
        }

        return result;
    }

    /**Handles whether the user is trying to use the diagonal trick.*/
    private static boolean diagonalTrick(Board board) {
        Piece[][] diagonals = board.getDiag();
        for (Piece[] arr : diagonals) {
            if (Arrays.equals(arr,new Piece[] {X, O, X}) || Arrays.equals(arr, new Piece[] {O,X,O})) {
                return true;
            }
        }
        return false;
    }

    /**Returns true if the board is in a corner double trick situation.*/
    private static boolean cornerTrick(Board board) {
        Piece[] topLine = board.getTopLine();
        Piece[] leftLine = board.getLeftLine();
        Piece[] rightLine = board.getRightLine();
        Piece[] bottomLine = board.getBottomLine();
        if (Arrays.equals(topLine, new Piece[] {EMP, X, X}) && Arrays.equals(rightLine, new Piece[] {X, X, EMP})) {
            return true;
        }
        if (Arrays.equals(topLine, new Piece[] {X, X, EMP}) && Arrays.equals(leftLine, new Piece[] {X, X, EMP})) {
            return true;
        }
        if (Arrays.equals(bottomLine, new Piece[] {EMP, X, X}) && Arrays.equals(rightLine, new Piece[] {EMP, X, X})) {
            return true;
        }
        if (Arrays.equals(leftLine, new Piece[] {EMP, X, X}) && Arrays.equals(bottomLine, new Piece[] {X, X, EMP})) {
            return true;
        }
        return false;
    }

    /**Machine's move.*/
    private static String machineMove;

}
