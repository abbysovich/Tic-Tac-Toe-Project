package game;

/** Random utility functions.*/
public class Utils {

    /**Prints out a line (for the board display).*/
    public static void newLine() {
        System.out.println("-------");
    }

    /**Destructively tranposes the 2-D array provided.*/
    public static void transpose(Piece[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr[i].length; j++) {
                Piece temp = arr[j][i];
                arr[j][i] = arr[i][j];
                arr[i][j] = temp;
            }
        }
    }

}
