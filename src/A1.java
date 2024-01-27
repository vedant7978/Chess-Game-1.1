import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class A1 {
    public static void main(String[] args) {
        try {
            // reading board input file from the given path
            BufferedReader boardReader = new BufferedReader(new FileReader("G:/Assignment 1.1/src/board_input.txt"));
            // creating the object of the FollowChess class
            FollowChess chessGame = new FollowChess();

            // Loading the board configuration
            if (chessGame.loadBoard(boardReader)) {

                // Printing the initial board
                System.out.println("Initial Board Configuration:");

                // Printing the initial board with PrintWriter class
                chessGame.printBoard(new PrintWriter(System.out));

                // Calling inCheck function before applying the move sequence to check whether king is in check or not
                // Check if the white king is in check
                System.out.println("white king is in check! "+ chessGame.inCheck(0));
                // check if the black king is in check
                System.out.println("black king is in check! " + chessGame.inCheck(1));

                // Reading the moves one by one from move sequence file
                BufferedReader moveReader = new BufferedReader(new FileReader("G:/Assignment 1.1/src/move_sequence.txt"));

                 // Apply the move sequence
                if (chessGame.applyMoveSequence(moveReader)) {

                    // Calling the inCheck() function again after applying the move sequence to check whether
                    // king is in check or not after a particular move
                    // Player 0 is for white king and player 1 is for black king!
                    System.out.println("white king is in check! "+ chessGame.inCheck(0));
                    System.out.println("black king is in check! " + chessGame.inCheck(1));

                    //Print the final board
                    System.out.println("\nFinal Board Configuration:");
                    chessGame.printBoard(new PrintWriter(System.out));

                    // After applying for a move sequence check if any piece captures any opponent piece
                    // Here 0 is for white player
                    // it returns captured black pieces in order the way they were captured
                    // Demonstrate the capture order for a white pieces
                    List<Character> whiteCaptureOrder = chessGame.captureOrder(0);
                    System.out.println("Pieces captured by white pieces " + whiteCaptureOrder);

                    // Demonstrate the capture order for black pieces
                    List<Character> blackCaptureOrder = chessGame.captureOrder(1);
                    System.out.println("Pieces captured by black pieces " + blackCaptureOrder);

                } else {
                    // The sequence given is not valid according the board configuration
                    System.out.println("Invalid move sequence. Board not updated.");
                    // Print the board till the right sequences are made
                    chessGame.printBoard(new PrintWriter(System.out));
                }
            } else {
                // Board is not valid
                System.out.println("Invalid board configuration.");
            }
        } catch (IOException e) {
            return;
        }
    }
}