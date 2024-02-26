package com.Task_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;



public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private final int BOX_SIZE = 25;
    private final int BOARD_WIDTH = 20;
    private final int BOARD_HEIGHT = 20;
    private final int GAME_SPEED = 130; // milliseconds

    private ArrayList<Point> snake;
    private Point food;
    private Timer timer;
    private boolean gameOver;
    private int score;
    private int direction = KeyEvent.VK_RIGHT; // Default direction

    // Method SnakeGame
    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BOX_SIZE, BOARD_HEIGHT * BOX_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake = new ArrayList<>();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)); // Initial snake position
        food = generateFoodPosition();

        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * BOX_SIZE, point.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);

        // Display score and game over message
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
        if (gameOver) {
            g.drawString("Game Over! Press R to restart.", 100, BOARD_HEIGHT * BOX_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollisions();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
        }

        // Add new head
        snake.add(0, newHead);

        // Remove tail if snake didn't eat food
        if (!newHead.equals(food)) {
            snake.remove(snake.size() - 1);
        } else {
            food = generateFoodPosition();
            score++;
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Check if snake hits walls
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver = true;
            timer.stop();
            return;
        }

        // Check if snake hits itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                timer.stop();
                return;
            }
        }
    }

    private Point generateFoodPosition() {
        Random random = new Random();
        int x = random.nextInt(BOARD_WIDTH);
        int y = random.nextInt(BOARD_HEIGHT);
        return new Point(x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                && Math.abs(direction - key) != 2) {
            direction = key;
        } else if (key == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void restartGame() {
        snake.clear();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        food = generateFoodPosition();
        score = 0;
        gameOver = false;
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



