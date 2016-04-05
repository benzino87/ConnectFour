package project2;

/**************************************************************************
 * Connect Four Panel that sets all the visuals for the game
 *
 * @author Jason Bensel
 * @version Project 2, Fall 2015
 *************************************************************************/

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFourPanel extends JPanel {
    private JLabel[][] board;
    private JButton[] selection;
    private JButton reset;
    private JButton undo;
    private JPanel buttonPanel;
    private JPanel gameGrid;
    private JPanel topPanel;
    private JLabel playerWinOne, playerWinTwo;
    private ConnectFourGame game;
    private ImageIcon blue, red;

    public ConnectFourPanel() {

        // Seperate panels for buttons and grid
        buttonPanel = new JPanel();
        gameGrid = new JPanel();
        gameGrid.setLayout(new GridLayout(10, 10));
        game = new ConnectFourGame(10, 10, 2);
        topPanel = new JPanel();

        // Button Listener
        ButtonListener listener = new ButtonListener();

        // Size of gameGrid[][]/Buttons[]
        selection = new JButton[game.getRow_size()];
        board = new JLabel[game.getRow_size()][game.getCol_size()];
        playerWinOne = new JLabel("Player 1 Wins:");
        playerWinTwo = new JLabel("Player 2 Wins:");

        // Addition buttons for functionality
        reset = new JButton("Reset Game");
        reset.addActionListener(listener);
        undo = new JButton("Undo");
        undo.addActionListener(listener);

        // Get images and resize them
        blue = new ImageIcon(getClass().getResource("blue.png"));
        Image blueOne = blue.getImage();
        Image newBlue = blueOne.getScaledInstance(50, 50,
                Image.SCALE_SMOOTH);
        blue = new ImageIcon(newBlue);
        red = new ImageIcon(getClass().getResource("red.png"));
        Image redOne = red.getImage();
        Image newRed = redOne.getScaledInstance(48, 48,
                Image.SCALE_SMOOTH);
        red = new ImageIcon(newRed);

        // Add game board labels to Panel
        for (int row = 0; row < game.getRow_size(); row++) {
            for (int col = 0; col < game.getCol_size(); col++) {
                board[row][col] = new JLabel();
               // board[row][col].setPreferredSize(new Dimension(5, 2));
                board[row][col].setBorder
                        (new BevelBorder(1, Color.DARK_GRAY, Color.BLACK));
                gameGrid.add(board[row][col]);
            }
        }

        // Add Player win labels to panels
        playerWinOne.setPreferredSize(new Dimension(200, 50));
        playerWinOne.setBorder(new BevelBorder(1, Color.DARK_GRAY,
                Color.BLACK));
        topPanel.add(playerWinOne, BorderLayout.NORTH);
        playerWinTwo.setPreferredSize(new Dimension(200, 50));
        playerWinTwo.setBorder(new BevelBorder(1, Color.DARK_GRAY,
                Color.BLACK));
        topPanel.add(playerWinTwo, BorderLayout.NORTH);

        // Create each JButton, set size, add listener
        for (int col = 0; col < selection.length; col++) {
            selection[col] = new JButton("");
            selection[col].setPreferredSize(new Dimension(55, 20));
            selection[col].addActionListener(listener);
            buttonPanel.add(selection[col]);

        }

        // Add buttons to panels
        buttonPanel.setBackground(Color.DARK_GRAY);
        topPanel.add(reset, BorderLayout.NORTH);
        topPanel.add(undo, BorderLayout.NORTH);

        // Set default size for 10x10 game grid
        gameGrid.setPreferredSize(new Dimension(600, 500));

        // Add different types of panels to main panel
        add(topPanel, BorderLayout.NORTH);
        add(gameGrid, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Change color of Panel
        setBackground(Color.DARK_GRAY);

    }

    /**********************************************************************
     * Helper method to associate ImageIcons with player value.
     * @param currentPlayer value of current player
     * @return color of image depending on currentPlayer
     *********************************************************************/
    private ImageIcon getPlayerColor(int currentPlayer){
        if(currentPlayer == 1){
            return this.blue;
        }else
            return this.red;
    }

    /**********************************************************************
     * Method to reset the size of the game grid to desired input
     * @param row size of desired row col
     *********************************************************************/
    public void setGameGrid(int row){
        game.setRow_size(row);
        game.setCol_size(row);
        gameGrid.setLayout(new GridLayout(row, row));
        revalidate();
        repaint();
    }

    /**********************************************************************
     * Method that clears all JLabels on the game grid.
     *********************************************************************/
    private void clearGameBoard(){
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col].setIcon(null);
                board[row][col].revalidate();
            }
        }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object button = actionEvent.getSource();

            //Selects spot for chip and checks for full column
            for (int col = 0; col < selection.length; col++) {
                if (button == selection[col]) {
                    int row = game.selectCol(col);
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Column" +
                                " full! Select another column");

                        //Checks for player wins then resets board if true
                    } else {
                        board[row][col].setIcon(getPlayerColor
                                (game.getCurrent_player()));
                        if(game.getGameStatus() == GameStatus.PLAYER1WON) {
                            playerWinOne.setText("Player 1 wins: " +
                                    game.updatePlayerWins
                                            (GameStatus.PLAYER1WON));
                            JOptionPane.showMessageDialog
                                    (null, "" + game.getGameStatus());
                            int choice = JOptionPane.showConfirmDialog(null,
                                    "SECOND CHANCE! Would you like to " +
                                            "undo last move?");
                            if(choice == JOptionPane.YES_OPTION) {
                                game.undo();
                                playerWinOne.setText("Player 1 wins: " +
                                        (game.updatePlayerWins
                                                (GameStatus.PLAYER1WON)-1));
                                board[game.getLastSelectedRow()]
                                        [game.getLastSelectedCol()]
                                        .setIcon(null);
                                board[game.getLastSelectedRow()]
                                        [game.getLastSelectedCol()]
                                        .revalidate();
                            }else {
                                clearGameBoard();
                                game.reset();
                            }
                        }else if(game.getGameStatus() ==
                                GameStatus.PLAYER2WON) {
                            playerWinTwo.setText("Player 2 wins: " +
                                    game.updatePlayerWins
                                            (GameStatus.PLAYER2WON));
                            JOptionPane.showMessageDialog
                                    (null, "" + game.getGameStatus());
                            int choice = JOptionPane.showConfirmDialog(null,
                                    "SECOND CHANCE! Would you like to " +
                                            "undo last move?");
                            if(choice == JOptionPane.YES_OPTION){
                                game.undo();
                                playerWinTwo.setText("Player 2 wins: " +
                                        (game.updatePlayerWins
                                                (GameStatus.PLAYER2WON)-1));
                                board[game.getLastSelectedRow()]
                                        [game.getLastSelectedCol()]
                                        .setIcon(null);
                                board[game.getLastSelectedRow()]
                                        [game.getLastSelectedCol()]
                                        .revalidate();
                            }else {
                                clearGameBoard();
                                game.reset();
                            }
                        }
                        game.changePlayer();
                    }
                }
            }
            //Rests board from game class to 0.
            if (button == reset) {
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Are you sure?");

                    if (choice == JOptionPane.YES_OPTION) {
                        game.reset();
                        game.setCurrent_player(1);
                        clearGameBoard();
                    }

            }
            //Allows player to undo any steps as long as there is no win
            if (button == undo){
                try{
                    game.undo();
                    board[game.getLastSelectedRow()]
                            [game.getLastSelectedCol()].setIcon(null);
                    board[game.getLastSelectedRow()]
                            [game.getLastSelectedCol()].revalidate();
                }catch (ArrayIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "You cannot undo any further.");
                }
            }
        }
    }
}

