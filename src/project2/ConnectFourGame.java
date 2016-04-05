package project2;

/**************************************************************************
 *Connect Four game, this class handles all of the game operations.
 *
 * @Author Jason Bensel
 * @version Project2, Fall2015
 *************************************************************************/

import java.util.ArrayList;

public class ConnectFourGame {
    /** 2-dimensional array that holds current player values */
    private int[][] board;

    /** Enumerated type that holds status of the game */
    private GameStatus status;

    /** Row size of game board*/
    private int row_size;

    /** Column size of game board*/
    private int col_size;

    /** Value of current player*/
    private int current_player;

    /** Value of total players*/
    private int total_players;

    /** Value of most recently selected row*/
    private int lastSelectedRow;

    /** Value of most recently selected column*/
    private int lastSelectedCol;

    /** array that holds total wins for each player*/
    private int[] player_wins;

    /** ArrayList that stores last selected row and column*/
    private ArrayList<Integer> undoSelection;

    /** Default Constructor */
    public ConnectFourGame(){

    }

    /**********************************************************************
     * Constructor that sets the size of the Game Board, total number of
     * players, sets game to in-progress and starts ArrayList for undo
     * function
     * @param row the row size of Game Board
     * @param col the column size of Game Board
     * @param players number of players to play the game
     *********************************************************************/
    public ConnectFourGame(int row, int col, int players){
        row_size = row;
        col_size = col;
        total_players = players;
        status = GameStatus.INPROGRESS;
        board = new int[row][col];
        current_player = 1;
        player_wins = new int[total_players];
        undoSelection = new ArrayList<>();
    }

    /**********************************************************************
     * Helper method to update current player. (used in multiple classes)
     *********************************************************************/
    public void changePlayer(){
        if(current_player < total_players){
            current_player++;
        }else
            current_player = 1;
    }

    /**********************************************************************
     * Uses col to specify where the chip will fall, then searches through
     * the row to find furthest available space. Adds selected row and col
     * to ArrayList for undo function.
     * @param col column decided by player(decides where chip will fall)
     * @return The row in which the chip will stay
     *********************************************************************/
    public int selectCol(int col) {
        int row = row_size -1;
        while(board[row][col] != 0){
            row--;
            if(row < 0){
                row = -1;
                return row;
            }
        }
        board[row][col] = current_player;
        //Store row and col number for undo
        undoSelection.add(row);
        undoSelection.add(col);
        return row;
    }

    /**********************************************************************
     * Scans the 2-dimensional array: GameBoard and sets all values of
     * board to 0, then clears undo ArrayList.
     *********************************************************************/
    public void reset(){
        for(int row = 0; row < row_size; row++){
            for(int col = 0; col < col_size; col++){
                board[row][col] = 0;
            }
        }
        undoSelection.clear();
    }

    /**********************************************************************
     * Helper method that uses a selected spot to scan vertically and
     * horizontally for 4 chips of same player.
     * @param r incoming row position
     * @param c incoming column position
     * @param n number required to get a win
     * @param p current player
     * @return true or false based on if win is found or not found.
     *********************************************************************/
    private boolean checkForWinner(int r, int c, int n, int p){
        //Check for Horizontal win
        int count = 0;
        int tempCol;
        for(int i = 0; i < n; i++){
            if(c+i >= col_size) {
                tempCol = (c + i) % row_size;
                if (board[r][tempCol] == p) {
                    count++;
                }
            }else if(board[r][c+i]== p){
                count++;
            }
        }if(count == n){
            return true;
        }else count = 0;

        //Check for Vertical win
        int tempRow;
        for(int i = 0; i < n; i++){
            if(r+i >= row_size) {
                tempRow = (r + i) % row_size;
                if (board[tempRow][c] == p) {
                    count++;
                }
            }else if(board[r+i][c]== p){
                count++;
            }
        }if(count == n){
            return true;
        }
        return false;
    }

    /**********************************************************************
     * Helper method that uses last selected spot to check for diagonal
     * 4 chips of same player.
     * @param r incoming row position
     * @param c incoming col position
     * @param n incoming num required for win
     * @param p value of current player
     * @return true or false based on if win is found or not found.
     *********************************************************************/
    private boolean checkForDiagWinner(int r, int c, int n, int p){
        int count = 0;
        for(int i = 0; i < n; i++){
            if(r-i <= 0 || c+i >= col_size){
                return false;
            }else if(board[r-i][c+i] == p){
                count++;
            }if(count == n+1){
                return true;
            }
        }for(int i = 0; i < n; i++){
            if(r-i <= 0 || c-i <= 0){
                return false;
            }else if(board[r-i][c-i] == p){
                count++;
            }if(count == n+1){
                return true;
            }
        }
        return false;
    }

    /**********************************************************************
     * Scans the board for and uses helper methods to determine a winner.
     * Returns an enumerated type depending on the current status of the
     * game.
     * @return status of game PLAYER1WON,PLAYER2WON, INPROGRESS, CATS(all
     * spots filled with no winner)
     *********************************************************************/
    public GameStatus getGameStatus(){
        for(int row = 0; row < row_size; row++) {
            for (int col = 0; col < col_size; col++) {
                // need to check for cats
                if (checkForWinner(row, col, 4, current_player) ||
                        checkForDiagWinner(row, col, 4, current_player)
                            == true) {
                    if(current_player == 1){
                        return status.PLAYER1WON;
                    }else{
                        return status.PLAYER2WON;
                    }
                }
            }
        }
        int r = 0;
        int c = 0;
        while(board[r][c] != 0 && (r < row_size)&&(c < col_size)){
            r++;
            c++;
            if(r == row_size && c == col_size){
                return status.CATS;
            }
        }
        return status.INPROGRESS;
    }

    /**********************************************************************
     * Method to help the panel class update the player win Jlabel.
     * @param player enum associated with player 1 or player 2
     * @return a incremented value of respective player if player win is
     * found, returns 0 if no players have won.
     *********************************************************************/
    public int updatePlayerWins(Enum player){
        if(player == GameStatus.PLAYER1WON){
            player_wins[0]++;
            return player_wins[0];
        }else if(player == GameStatus.PLAYER2WON){
            player_wins[1]++;
            return player_wins[1];
        }
        return 0;
    }

    /**********************************************************************
     * Allows player to undo any steps as long as there has not been a win.
     * @throws ArrayIndexOutOfBoundsException protects undo from removing
     * items from an empty list.
     *********************************************************************/
    public void undo() throws ArrayIndexOutOfBoundsException{
        if(undoSelection.size() == 0){
            throw new ArrayIndexOutOfBoundsException();
        }else {
            lastSelectedRow = undoSelection.get(undoSelection.size() - 2);
            lastSelectedCol = undoSelection.get(undoSelection.size() - 1);
            board[lastSelectedRow][lastSelectedCol] = 0;
            undoSelection.remove(undoSelection.size() - 1);
            undoSelection.remove(undoSelection.size() - 1);
        }
    }

    /**********************************************************************
     * Get current player
     * @return value of current player
     *********************************************************************/
    public int getCurrent_player(){
        return current_player;
    }

    /**********************************************************************
     * Set current player
     * @param player value of player
     *********************************************************************/
    public void setCurrent_player(int player){
        current_player = player;
    }

    /**********************************************************************
     * Get row size
     * @return value of row size
     *********************************************************************/
    public int getRow_size(){
        return row_size;
    }

    /**********************************************************************
     * Get column size
     * @return value of column size
     *********************************************************************/
    public int getCol_size(){
        return col_size;
    }

    /**********************************************************************
     * Set row size
     * @param row value of desired row size
     *********************************************************************/
    public void setRow_size(int row){
        row_size = row;
    }

    /**********************************************************************
     * Set column size
     * @param col value of desired column size
     *********************************************************************/
    public void setCol_size(int col){
        col_size = col;
    }

    /**********************************************************************
     * Get value of the most recently selected row.
     * @return value of last selected row
     *********************************************************************/
    public int getLastSelectedRow(){
        return lastSelectedRow;
    }

    /**********************************************************************
     * Get value of the most recently selected column.
     * @return value of last selected column
     *********************************************************************/
    public int getLastSelectedCol(){
        return lastSelectedCol;
    }
}
