import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// FollowChess class contains lot of methods which generally used in standard chess board methods such as main methods are
// pieceCanMove(), it is whether valid move for particular piece considering its surrounding
public class FollowChess {
    private char[][] board;
    public int boardLength;
    public int boardWidth;
    private List<Character> whitePieceCaptureOrder;
    private List<Character> blackPieceCaptureOrder;

    // constructor mainly used to initializing Array and ArrayLists which are gonna used in later parts of this methods
    public FollowChess() {
        board = new char[0][0];
        whitePieceCaptureOrder = new ArrayList<>();
        blackPieceCaptureOrder = new ArrayList<>();
    }

    //this is a standard load board implementation takes input from board_input.txt file and returns true, if successfully loaded board or not
    public boolean loadBoard(BufferedReader boardStream) {
        try {
            // Check for the null board stream
            if (boardStream == null){
                return false;
            }

            // counter variables for num of black and white king
            int numOfBlackKings = 0;
            int numOfWhiteKings = 0;

            // Array to store each line
            List<String> data = new ArrayList<>();
            String line;

            // iterate each line through while loop
            while ((line = boardStream.readLine()) != null && line.trim().isEmpty()) {
                return true;
            }

            // If there are no non-blank lines after skipping leading blank lines, return false
            if (line == null) {
                return false;
            }

            // loop for reading each line
            do {
                if (line.trim().isEmpty()) {
                    // Return false if a blank line is encountered after non-blank lines
                    break;
                }
                data.add(line);
            } while ((line = boardStream.readLine()) != null);

            // check for the empty data
            if (data.isEmpty()){
                return false;
            }

            // Get the num of rows and columns
            int numRows = data.size();
            int numCols = data.get(0).length();

            // Checking whether board is rectangle or not
            for (String row : data) {
                if (row.length() != numCols) {
                    return false;
                }
            }

            // this will allows board with the length of 1X2 and 2X1 size
            if (numCols == 0 || numRows == 0) {
                return false;
            }
            // Initializes board with obtained configurations
            board = new char[numRows][numCols];
            boolean hasBlackKing = false; // variable of boolean for Black king
            boolean hasWhiteKing = false; // variable of boolean for White king

            // Iteration of whole board
            for (int i = 0; i < numRows; i++) {
                String row = data.get(i);

                for (int j = 0; j < numCols; j++) {
                    char piece = row.charAt(j);

                    // check if the obtained piece is valid or not for chess game
                    if (!isValidPiece(piece)){
                        // Invalid piece
                        return false;
                    }

                    // Get the piece in 2D board array
                    board[i][j] = piece;

                    // Checking for white(k) and black(K) kings
                    if (piece == 'k') {
                        hasWhiteKing = true;
                        numOfWhiteKings++;
                    } else if (piece == 'K') {
                        hasBlackKing = true;
                        numOfBlackKings++;
                    }
                }
            }

            //Initializing board length and width according the board
            boardLength = board.length;
            boardWidth = board.length;


            // Check if the board contains one black and one white king
            if (!hasBlackKing || !hasWhiteKing) {
                // This is for its presence in board, both the kings should be present in board
                return false;
            }

            // If kings(black and white) exists, then it should be maximum 1
            if (numOfBlackKings != 1 || numOfWhiteKings != 1) {
                // Invalid board configuration
                return false;
            }

            // board loaded successfully
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // this function validate the pieces from the chess
    private boolean isValidPiece(char piece) {
        return ".kKrRbBnNqQpP".indexOf(piece) != -1;
    }

    // after the load board, printBoard function prints a loaded board using PrintWriter Class using outstream
    public boolean printBoard( PrintWriter outstream ) {

        // Unable to print if PrintWriter is null
        if(outstream == null){
            return false;
        }

        // Check if the board is not null
        if (board != null) {
            // Iterate through each row in the board
            for (char[] row : board) {
                // currentRowString variable for consisting row value
                String currentRowString = "";
                // Iterate through each character from row and
                // converting it to String
                for (Character currentChar : row){
                    currentRowString += currentChar;
                }
                // Print the row as a string
                outstream.print(currentRowString + "\n");
            }
            // clear the outstream after printing a loaded board
            // Flush the PrintWriter to make sure the output is sent
            outstream.flush();
            // board printed successfully
            return true;
        }
        // Printing was unsuccessful
        return false;

    }


    //This function is being called from main a1 class and returns a boolean value upon
    // successfully execution of moves from move sequence file
    boolean applyMoveSequence( BufferedReader moveStream ) {

        try {
            // Read the first line from the move sequence
            String line = moveStream.readLine();

            // Check for an empty stream
            if (line == null) {
                // Empty move stream
                return false;
            }

            // This variable is created to alternate a player moves such as first line
            // for white piece and second line is for black piece
            boolean isWhiteMove = true;

            do {
                // Check for empty moves
                if (line.trim().isEmpty()) {
                    // Invalid move
                    return false;
                }
                // Split the moves by a space
                String[] move = line.split(" ");

                // empty moves are not considered to be true
                if (line.trim().isEmpty()) {
                    return false;
                }

                // startposition which is a space separated first 2 value of move sequence in each line
                // suppose if move sequence is 'a 1 a 2' then start position would be "a 1" for the same
                String startPosition = move[0] + " " +move[1];

                // move length should not be more than 4
                if (move.length == 4) {
                    //obtaining variables of start row, start column and end row, end column
                    // dynamically for different size of board
                    int startRow = boardLength - (Character.getNumericValue(move[1].charAt(0)));
                    int startCol = move[0].charAt(0) - 'a';
                    int endRow = boardWidth - (Character.getNumericValue(move[3].charAt(0)));
                    int endCol = move[2].charAt(0) - 'a';

                    // checking for the conditions for the rows and column we got if they might reach
                    // out of the board boundaries
                    if (startRow < 0 || startRow >= boardLength || startCol < 0 || startCol > boardWidth
                    || endRow < 0 || endRow >= boardLength || endCol < 0 || endCol > boardWidth ){
                        return false;
                    }
                    //determining piece based on start row and end column value
                    char piece = board[startRow][startCol];

                    // checking if the piece is movable or not
                    boolean isItMovable = pieceCanMove(startPosition);
                    System.out.println(piece + " has atleast one valid move? " + isItMovable);

                    // condition for checking whose turn is it, Black or White
                    if ((isWhiteMove && Character.isLowerCase(piece)) || (!isWhiteMove && Character.isUpperCase(piece))){

                        // statement checks both conditions and whether it is valid move or not
                        if (pieceCanMove(startPosition) && isValidMoveForPiece(piece, startRow, startCol, endRow, endCol)) {
                            // valid move execution from start to destination
                            makeMove(startRow, startCol, endRow, endCol);
                            //alternate players after making one move
                            isWhiteMove = !isWhiteMove;

                        } else {
                            // Invalid move
                            return false;
                        }
                    }else{
                        return false;
                    }
                } else {
                    // Invalid move format
                    return false;
                }
                // when first move is applied read the next move sequence
                line = moveStream.readLine();
            } while (line != null);
            // looping from each line

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    // Capture order takes integer value either 0 or 1
    // and returns a List of Characters in a ordered way
    // for white piece it returns a list of captured pieces in a way they were captured
    // so does for black pieces
    // if pawn captured first and rook later List shows first pawn and then rook similarly
    // in a order manner
    List<Character> captureOrder( int player ) {

        if (player == 0) {
            // White player
            return blackPieceCaptureOrder;
            // 1 indicates black player
        } else if (player == 1) {
            // Black player
            return whitePieceCaptureOrder;
        } else {
            // returns a new empty arrayList for invalid player
            return null;
        }
    }

    // inCheck method tells whether player(white or black) is in check or not
    // takes a player similarly as capture order either 0 or 1
    public boolean inCheck(int player) {

        // if 0 then it is for white king otherwise black king
        char kingPiece = (player == 0) ? 'k' : 'K';

        // getKingRow returns a king i th position or king i.e row
        // getKingColumn returns a j th position of king i.e. column
        int kingRow = getRowOftheKing(player);
        int kingCol = getColOfTheKing(player);

        // iterate through each player and checks whether it can capture the king or not
        // after the very next move
        for (int m = 0; m < boardLength; m++) {
            for (int n = 0; n < board[0].length; n++) {
                char piece = board[m][n];

               // see if the piece belongs to opponent player and can capture the king
                if ((player == 0 && Character.isUpperCase(piece)) || (player == 1 && Character.isLowerCase(piece))) {

                    if (isValidMoveForPiece(piece, m, n, kingRow, kingCol)) {
                        return true;
                        // The king is in check
                    }
                }
            }
        }
        // King is safe
        return false;
    }

    public boolean pieceCanMove(String boardPosition)
    {
        // this breaks a given string into a spaceless string
        String[] h1 = boardPosition.split(" ");

        if (h1.length != 2){
            return false;
        }

        // This converts a first position value into a integer of a start column
        int startCol = h1[0].charAt(0) - 'a';

        // This formula gives a start row in integer
        int startRow = boardLength - (Character.getNumericValue(h1[1].charAt(0)));

        // checking if called before loading the board or not
        // piece can only be checked for a is at least move
        // after loading the board
        if (startRow < 0 || startRow >= boardLength || startCol < 0 || startCol > boardWidth){
            return false;
        }

        // Get the chess piece at specified position
        char piece = board[startRow][startCol];

        // Iterate through a all possible moves and check for at least one valid move
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int endRow = i;
                int endCol = j;

                // Check if the move is valid
                if (isValidMoveForPiece(piece, startRow, startCol, endRow, endCol)) {
                    // move is valid
                    return true;
                }
            }
        }

        // piece does not have at least one valid move
        return false;
    }

    // this function is responsible for moving piece from one position to another
    // takes four parameter starting and ending rows and column position
    private void makeMove(int startRow, int startCol, int endRow, int endCol) {

        // Get the piece from startRow and startColumn
        char piece = board[startRow][startCol];

        // final piece to be captured
        char capturedPiece = board[endRow][endCol];

        // it should not be a .
        if (capturedPiece != '.'){

            // if opponent is in uppercase then add it to whiteCapturedList
            if (Character.isUpperCase(piece)) {
                whitePieceCaptureOrder.add(capturedPiece);
            } else {
                // add it to white captured list if captured
                blackPieceCaptureOrder.add(capturedPiece);
            }
        }

            // exchange the piece from start to end position
            board[startRow][startCol] = '.';
            board[endRow][endCol] = piece;

    }


    // this functions particularly checks for a piece and iterates a through all the possible and
    // non possible moves and return false if it is not valid move
    // it takes all moves into accountant that for pieces like bishops, rooks and queens and checks
    // for its diagonal, vertical, horizontal and both the moves respectively
    boolean isValidMoveForPiece(char piece, int startRow, int startCol, int endRow, int endCol) {

        // Check if the particular move is within the range of board or not
        if (!isValidPosition(endRow, endCol)) {
            return false;
        }

        // obtaining row and column differences
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);


        // mentioned a all moves cases for all the pieces such as
        // pawn, rook, knight, bishop, queen and king
        switch (piece) {
             // White king
           case 'K': // Black king
                // conditions for king that it should move only 1 step horizontally
                // and vertically and can capture only opponent piece
                char destinationBlackPiece = board[endRow][endCol];
                if ( destinationBlackPiece == '.' || ( Character.isUpperCase(piece) && Character.isLowerCase(destinationBlackPiece))) {
                    if (Math.abs(rowDiff) <= 1 && Math.abs(colDiff) <= 1){
                        return true;
                    }
                }
                // non valid move for king
                return false;
            case 'k':
                char destinationWhitePiece = board[endRow][endCol];
                if ( destinationWhitePiece == '.' || (Character.isLowerCase(piece) && Character.isUpperCase(destinationWhitePiece))) {
                    if (Math.abs(rowDiff) <= 1 && Math.abs(colDiff) <= 1){
                        return true;
                    }
                }
                // non valid move for king
                return false;
            // logic for White pawn
            case 'p':
                if ((startRow > endRow) && (rowDiff == 1) && (colDiff == 0) && (board[endRow][endCol] == '.')) {
//                    System.out.println(startRow + " " + endRow);
                    return true; // Valid non-capturing move (one step ahead)

                } else if (startRow > endRow && rowDiff == 1 && Math.abs(colDiff) == 1 && board[endRow][endCol] != '.' && Character.isUpperCase(board[endRow][endCol])) {
//                    System.out.println(startRow + " " + endRow);
                    return true; // Valid capturing move diagonally only if there is an opponent piece
                }
                // non valid move for white pawn
                return false;

            // logic for Black pawn
            case 'P':
                if ((startRow < endRow) && (rowDiff == 1) && (colDiff == 0) && (board[endRow][endCol] == '.')) {
                    return true; // Valid non-capturing move (one step ahead)
                } else if (startRow < endRow && rowDiff == 1 && Math.abs(colDiff) == 1 && board[endRow][endCol] != '.' && Character.isLowerCase(board[endRow][endCol])) {
                    return true; // Valid capturing move diagonally only if there is an opponent piece
                }
                // Invalid move for black pawn
                return false;

            case 'q':
                // all possible calculations of moves for white queen

                // obtaining final piece for white queen
                char fPieceForWhiteQueen = board[endRow][endCol];

                // conditions for checking final piece
                // whether is it . or opponent piece
                if((fPieceForWhiteQueen == '.' || (Character.isLowerCase(piece) && Character.isUpperCase(fPieceForWhiteQueen)))){

                    // if and for condition for checking its moves diagonally and vertical moves
                    if (((startRow == endRow) || (startCol == endCol)
                            || (Math.abs(startRow - endRow) == (Math.abs(startCol - endCol))))) {

                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // checking for valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // Check if there is an obstacle in the path
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }

                        }

                        // valid move for white queen
                        return true;
                    }
                }
                // non valid move for white queen
                return false;

            case 'Q':  // Black queen:
                // obtaining final possition for black queen
                char fPieceForBlackQueen = board[endRow][endCol];

                // conditions for checking final piece
                // whether is it . or opponent piece
                if((fPieceForBlackQueen == '.' || (Character.isUpperCase(piece) && Character.isLowerCase(fPieceForBlackQueen)))){

                    // if and for condition for checking its moves diagonally and vertical moves
                    if (((startRow == endRow) || (startCol == endCol)
                            || (Math.abs(startRow - endRow) == (Math.abs(startCol - endCol))))) {

                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // Checking for valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // Check if there is an obstacle in the path
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }
                        }

                        // valid move for black queen
                        return true;
                    }
                }
                // non valid move for Black queen
                return false;

            case 'b':
                // Obtaining final position for white bishop
                char fPieceForWhiteBishop = board[endRow][endCol];

                // conditions for moving white bishop diagonally
                if((fPieceForWhiteBishop == '.' || (Character.isLowerCase(piece) && Character.isUpperCase(fPieceForWhiteBishop)))){
                    if ((Math.abs(startRow - endRow) == Math.abs(startCol - endCol))) {
                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // checking for valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // Check if obstacles exist
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }
                        }

                        // valid move for white bishop
                        return true;
                    }

                }
                // non valid move for white bishop
                return false;

            case 'B':  // Black bishop

                // obtaining final position for black bishop
                char fPieceForBishop = board[endRow][endCol];

                // conditions for black bishop to move diagonally
                if((fPieceForBishop == '.' || (Character.isUpperCase(piece) && Character.isLowerCase(fPieceForBishop)))){
                    if ((Math.abs(startRow - endRow) == Math.abs(startCol - endCol))) {
                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // checking for a valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // Check if obstacles exist
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }
                        }

                        // valid move for black bishop
                        return true;
                    }

                }
                // non valid move for black bishop
                return false;

            case 'n': // White knight

                // obtaining final position for white knight
                char fPieceForWhiteKnight = board[endRow][endCol];

                // conditions for white knight
                return (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2) && (fPieceForWhiteKnight == '.' || (Character.isLowerCase(piece) && Character.isUpperCase(fPieceForWhiteKnight)));

            case 'N': // Black knight

                // obtaining final position for black knight
                char fPieceForBlackKnight = board[endRow][endCol];

                // conditions for black knight
                return (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2) && (fPieceForBlackKnight == '.' || (Character.isUpperCase(piece) && Character.isLowerCase(fPieceForBlackKnight)));

            case 'r': // white rook

                // obtaining final position for white rook
                char fPieceForWhiteRook = board[endRow][endCol];

                // conditions for white rook that it can only capture opponent piece
                // and can move only horizontally and vertically.
                if((fPieceForWhiteRook == '.' || (Character.isLowerCase(piece) && Character.isUpperCase(fPieceForWhiteRook)))){
                    if (((startRow == endRow) || (startCol == endCol))) {
                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // Checking for a valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // checking if obstacles exist
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }
                        }

                        // valid move for white rook
                        return true;
                    }
                }

                // non valid move for white rook
                return false;

            case 'R': // Black rook
                // obtaining final position for black rook
                char fPieceForBlackRook = board[endRow][endCol];

                // conditions for black rook if at ending position piece is of opponent or not
                // and can move only horizontally and vertically
                if((fPieceForBlackRook == '.' || (Character.isUpperCase(piece) && Character.isLowerCase(fPieceForBlackRook)))){
                    if (((startRow == endRow) || (startCol == endCol))) {
                        int stepRow = (rowDiff == 0) ? 0 : ((endRow > startRow) ? 1 : -1);
                        int stepCol = (colDiff == 0) ? 0 : ((endCol > startCol) ? 1 : -1);

                        for (int i = 1; i < Math.max(rowDiff, colDiff); i++) {
                            int checkRow = startRow + i * stepRow;
                            int checkCol = startCol + i * stepCol;

                            // Checking for the valid position
                            if (!isValidPosition(checkRow, checkCol)) {
                                return false;
                            }

                            // check if any obstacles exist
                            if (board[checkRow][checkCol] != '.') {
                                return false;
                            }
                        }

                        // Valid move for black rook
                        return true;
                    }
                }

                // non valid move for black rook
                return false;

            default:
                // for none of the conditions
                // or unknown player
                return false;
        }
    }

    // it returns a boolean value for validating that ending position
    // for particular move is within a range or not
    // takes ending row and column for any piece
    boolean isValidPosition(int row, int col) {

        // generating total rows and column
        int numRows = board.length;
        int numCols = board[0].length;

        // conditions for a parsed positions are within the range or not
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    // it gives the partial position for a king
    // by giving a row of the king for player
    public int getRowOftheKing(int player) {

        // check whether a player is a white or a black
        // 0 indicates for a white player
        // 1 indicates for a black player
        // getting the king piece
        char kingPiece = (player == 0) ? 'k' : 'K';

        // iterating over all the board to find a king position
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                // condition for if king found or not.
                if (board[i][j] == kingPiece) {
                    // returns kings i'th position i.e. king row
                    return i;
                }
            }
        }

        // king not found at this position
        return -1;
    }

    // it gives the partial position for a king
    // by giving a column of the king for player
    public int getColOfTheKing(int player) {

        // check whether a player is a white or a black
        // 0 indicates for a white player
        // 1 indicates for a black player
        // getting the king piece
        char kingPiece = (player == 0) ? 'k' : 'K';

        // iterating over all the board to find a king position
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                // condition if king found or not.
                if (board[i][j] == kingPiece) {

                    // returns kings j'th position i.e. king column
                    return j;
                }
            }
        }
        return -1; // King not found
    }
}

