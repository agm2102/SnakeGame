/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package snakegame;

import br.com.snakegame.view.GameView;

/**
 *
 * @author Adrian
 */
public class SnakeGame {

    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) {
        GameView gameView = new GameView();
        
        gameView.startGame();
    }
    
}
