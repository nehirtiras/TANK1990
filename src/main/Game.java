package main;

import javax.swing.JFrame;
import core.GamePanel;
//oyun penceresi oluşturulur
public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tank 1990");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
