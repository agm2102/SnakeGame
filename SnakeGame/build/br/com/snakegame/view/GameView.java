/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.snakegame.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Adrian
 */
public class GameView extends JFrame implements KeyListener {

    private final JPanel jpanelFrente;
    private final JPanel jpanelFundo;
    private final ArrayList<JPanel> snake;
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = toolkit.getScreenSize();
    private Timer timerRunGame, timerFoodSpan;
    private Random random;
    private int x, y;
    private float speedX, speedY;
    private int quantityOfFood = 0;
    private int controllerSpeedSpanFood = 6000;
    private final int WIDTH_VIEW = screenSize.width / 2;
    private final int HEIGHT_VIEW = screenSize.height / 2;
    private final int SIZE_OF_PIECE_SNAKE = 12;
    private float SpeedSnake = 2f;

    public GameView() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jpanelFrente = new JPanel();
        jpanelFundo = new JPanel();
        random = new Random();
        snake = new ArrayList<>();

        setSize(WIDTH_VIEW, HEIGHT_VIEW);
        setLayout(null);
        setLocationRelativeTo(null);

        jpanelFundo.setLayout(null);
        jpanelFundo.setBackground(Color.black);
        jpanelFundo.setSize(WIDTH_VIEW, HEIGHT_VIEW);

        jpanelFrente.setLayout(null);
        jpanelFrente.setBackground(Color.white);
        jpanelFrente.setSize(WIDTH_VIEW - ((WIDTH_VIEW * 4) / 100), HEIGHT_VIEW - ((HEIGHT_VIEW * 10) / 100));

        add(jpanelFrente);
        add(jpanelFundo);

        jpanelFrente.setLocation(((getWidth() - jpanelFrente.getWidth()) / 2) - 8, ((getHeight() - jpanelFrente.getHeight()) / 2) - 20);

        updateView();

        addBodySnake();
        addKeyListener((KeyListener) this);
        runTimers();
    }

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            new GameView().setVisible(true);
        });
    }

    private void showGameOverScreen() {
        if (JOptionPane.showConfirmDialog(null, "Deseja Continuar? ", "Game Over", 1) == 0) {
            this.dispose();
            startGame();
        }
        else{
            dispose();
        }
    }

    private void moveSnake() {

        x = (int) (x + speedX);
        y = (int) (y + speedY);

        snake.get(0).setLocation(x, y);

        int oldPositionX = snake.get(0).getX();
        int oldPositionY = snake.get(0).getY();

        for (int i = 1; i < snake.size(); i++) {
            int tempX = snake.get(i).getX();
            int tempY = snake.get(i).getY();

            snake.get(i).setLocation(oldPositionX, oldPositionY);
            oldPositionX = tempX;
            oldPositionY = tempY;
        }
        updateView();
    }

    private void checkIfHitBorder() {
        if (x < 0 || x > jpanelFrente.getWidth() - snake.get(0).getWidth()) {
            timerFoodSpan.stop();
            timerRunGame.stop();
            showGameOverScreen();
        }
        if (y < 0 || y > jpanelFrente.getHeight() - snake.get(0).getHeight()) {
            timerFoodSpan.stop();
            timerRunGame.stop();
            showGameOverScreen();
        }
    }

    private void spanFoods() {

        random = new Random();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/m.png"));
        JLabel food = new JLabel();

        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(newImage);

        food.setIcon(newImageIcon);
        food.setBounds(random.nextInt(0, WIDTH_VIEW), random.nextInt(0, HEIGHT_VIEW), 20, 20);

        quantityOfFood++;
        addComponents(jpanelFrente, food);
        updateView();
    }

    private void snakeEatFoods() {
        if (quantityOfFood > 0) {
            for (Component comp : jpanelFrente.getComponents()) {
                if (comp instanceof JLabel) {
                    if (snake.get(0).getBounds().intersects(comp.getBounds())) {
                        jpanelFrente.remove(comp);
                        quantityOfFood--;
                        SpeedSnake = SpeedSnake + 0.3f;
                        increasesSpeedSpanFoods();
                        addBodySnake();
                        addBodySnake();
                        updateView();
                    }
                }
            }
        }
    }
    //

    private void runTimers() {
        x = 1;
        y = 1;
        speedX = 0;
        speedY = 0;

        timerFoodSpan = new Timer(random.nextInt(controllerSpeedSpanFood / 2, controllerSpeedSpanFood), (ActionEvent e) -> {
            if (quantityOfFood <= 10 && (speedX != 0 || speedY != 0)) {
                spanFoods();
            }
        });

        timerRunGame = new Timer(17, (ActionEvent e) -> {

            moveSnake();
            checkIfHitBorder();
            snakeEatFoods();
        });
        timerFoodSpan.start();
        timerRunGame.start();
    }

    private void increasesSpeedSpanFoods() {
        if (controllerSpeedSpanFood >= 3000) {
            controllerSpeedSpanFood = controllerSpeedSpanFood - 500;
        }
    }

    private void addComponents(JPanel comp1, Component comp2) {
        comp1.add(comp2);
        updateView();
    }

    private void updateView() {
        jpanelFrente.revalidate();
        jpanelFrente.repaint();
    }

    private void addBodySnake() {

        if (snake.isEmpty()) {
            JPanel headSnake = new JPanel();

            snake.add(headSnake);
            snake.get(0).setSize(SIZE_OF_PIECE_SNAKE, SIZE_OF_PIECE_SNAKE);
            snake.get(0).setBackground(Color.black);

            addComponents(jpanelFrente, snake.get(0));
        } else {
            JPanel pieceOfBody1 = new JPanel();
            JPanel pieceOfBody2 = new JPanel();

            pieceOfBody1.setSize(SIZE_OF_PIECE_SNAKE, SIZE_OF_PIECE_SNAKE);
            pieceOfBody2.setSize(SIZE_OF_PIECE_SNAKE, SIZE_OF_PIECE_SNAKE);

            pieceOfBody1.setLocation(snake.get(0).getX(), snake.get(0).getY());
            snake.add(pieceOfBody1);

            pieceOfBody2.setBounds(snake.get(1).getBounds());
            snake.add(pieceOfBody2);

            for (int i = 1; i < snake.size(); i++) {
                addComponents(jpanelFrente, snake.get(i));
            }
            pieceOfBody1.setBackground(Color.red);
            pieceOfBody2.setBackground(Color.red);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (null != KeyEvent.getKeyText(e.getKeyCode())) {
            switch (KeyEvent.getKeyText(e.getKeyCode())) {
                case "Right" -> {
                    if (speedX >= 0) {
                        speedY = 0;
                        speedX = SpeedSnake;
                    }
                    break;
                }
                case "Left" -> {
                    if (speedX <= 0) {
                        speedY = 0;
                        speedX = -SpeedSnake;
                    }
                    break;
                }
                case "Up" -> {
                    if (speedY <= 0) {
                        speedX = 0;
                        speedY = -SpeedSnake;
                    }
                    break;
                }
                case "Down" -> {
                    if (speedY >= 0) {
                        speedX = 0;
                        speedY = SpeedSnake;
                    }
                    break;
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
