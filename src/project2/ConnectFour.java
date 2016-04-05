package project2;

/**************************************************************************
 * GUI
 *
 * @author Jason Bensel
 * @version Project 2, Fall 2015
 *************************************************************************/

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFour {
    JFrame title;
    ConnectFourPanel panel;
    JMenuItem newGame;
    JMenuBar menuBar;
    JMenu gameMenu;

    public ConnectFour() {
        title = new JFrame("Connect Four " +
                "(Player 1: Blue / Player 2: Red)");
        title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        newGame = new JMenuItem("New Game");
        panel = new ConnectFourPanel();


        title.setJMenuBar(menuBar);
        menuBar.add(gameMenu);
        gameMenu.add(newGame);

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s = JOptionPane.showInputDialog(null,
                        "Enter a board size:");
                int bSize = Integer.parseInt(s);
                if (bSize > 10 || bSize < 4) {
                    String k = JOptionPane.showInputDialog(null,
                            "Number is either too large or too small, " +
                                    "try again. (3 - 10)");
                    bSize = Integer.parseInt(k);
                    panel.setGameGrid(bSize);
                    title.revalidate();
                    title.repaint();
                }else
                    panel.setGameGrid(bSize);
                    title.revalidate();
                    title.repaint();
            }
        });

        title.getContentPane().add(panel);
        title.setSize(650, 680);
        title.setVisible(true);
    }
    public static void main(String[] args){
        ConnectFour c = new ConnectFour();
    }
}
