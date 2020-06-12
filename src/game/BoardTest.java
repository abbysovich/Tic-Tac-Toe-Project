package game;

import org.junit.Test;

import java.util.List;

import static game.Piece.*;

import static game.Board.*;

public class BoardTest {

    /** Test 3x3 board.*/
    Board testBoard = new Board(3, new Piece[][] {{X, O, EMP}, {X, X, O}, {O, EMP, O}});

    /** Empty 3x3 test board.*/
    Board emptyTest = new Board();

    /** Test 4x4 board.*/
    Board bigBoard = new Board(4);

    /** Test 3x3 winning board.*/
    Board diagWinOne = new Board(3, new Piece[][] {{O, X, X}, {X, O, O}, {EMP, EMP, O}});

    /** Test 3x3 winning board.*/
    Board horizontalWinOne = new Board(3, new Piece[][] {{EMP, O, X}, {EMP, O, X}, {EMP, EMP, X}});

    /** Test 3x3 winning board.*/
    Board verticalWinOne = new Board(3, new Piece[][] {{X, X, X}, {O, O, EMP}, {EMP, EMP, EMP}});

    /**Test 3x3 winning board.*/
    Board horizontalWinTwo = new Board(3, new Piece[][] {{EMP, O, X}, {X, O, X}, {EMP, O, EMP}});

    @Test
    public void testDisplay() {
//        testBoard.displayBoard();
//        new Board().displayBoard();
//        bigBoard.displayBoard();
        horizontalWinTwo.displayBoard();
    }

    @Test
    public void makeMove() {
        emptyTest.displayBoard();
        emptyTest.makeMove("1,1");
        emptyTest.makeMove("2,1");
        emptyTest.makeMove("1,2");
        emptyTest.makeMove("2,2");
        emptyTest.makeMove("3,3");
        emptyTest.displayBoard();
    }

    @Test
    public void testIllegalMoves() {
        emptyTest.makeMove("2,2");
        emptyTest.makeMove("2,2");
    }

    @Test
    public void winningSituations() {
        System.out.println(verticalWinOne.isGameOver());
        System.out.println(diagWinOne.isGameOver());
        System.out.println(horizontalWinOne.isGameOver());
        System.out.println(emptyTest.isGameOver());
        System.out.println(testBoard.isGameOver());
        System.out.println(horizontalWinTwo.isGameOver());
        System.out.println(new Board().isGameOver());
    }

    @Test
    public void testLegalMoves() {
//        Board boi = new Board(3, new Piece[][] {{EMP, X, O}, {EMP, EMP, O}, {X, O, X}});
//        boi.displayBoard();
//        List<String> legalMoves = boi.legalMoves();
//        System.out.println(legalMoves);
//
//        System.out.println(emptyTest.legalMoves());
//
//        Board gal = new Board(3, new Piece[][] {{EMP, X, O}, {EMP, O, X}, {EMP, X, O}});
//        gal.displayBoard();
//        System.out.println(gal.legalMoves());

        Board bhai = new Board(3, new Piece[][] {{X, O, O}, {O, X, X}, {X, X, O}});
        bhai.displayBoard();
        System.out.println(bhai.legalMoves().size());

        Board doosraBhai = new Board(3, new Piece[][] {{O, EMP, X}, {EMP, O, EMP}, {X, EMP, X}});
        doosraBhai.displayBoard();
        System.out.println(doosraBhai.legalMoves());

    }

    @Test
    public void copyTest() {
        Board boi = new Board(3, new Piece[][] {{EMP, X, O}, {EMP, EMP, O}, {X, O, X}});
        Board otherBoi = copyBoard(boi);
        otherBoi.displayBoard();
        otherBoi.makeMove("1,2");
        otherBoi.displayBoard();
        System.out.println(otherBoi.getCurrPlayer());
        System.out.println(boi.getCurrPlayer());
    }

}
