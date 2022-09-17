package com.alex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SnakeMainController implements Runnable {

    static final int GRID_WIDTH = 100;
    static final int GRID_HEIGHT = 20;
    static final long timePeriod = 50;

    static boolean gameEnded = false;
    static String direction = "right";

    private final JFrame  frame = new JFrame();
    private final JTextArea playArea = new JTextArea();
    private final String[][] grid = new String[GRID_HEIGHT][GRID_WIDTH];

    SnakeSegment snakeHead;
    java.util.List<SnakeSegment> snakeBody = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(new SnakeMainController());
    }

    @Override
    public void run() {
        initializeSnake();
        initializeGrid();
        initializeGui();
        startWorkerThread();
    }

    private void initializeSnake() {
        snakeHead = new SnakeSegment(0, 0);
        snakeHead.setX(0);
        snakeHead.setY(0);
        snakeHead.setHead(true);

        //For testing purposes only, otherwise set to 0
        int initialSnakeSegments = 8;
        while(initialSnakeSegments > 0) {
            snakeBody.add(new SnakeSegment(0, 0));
            initialSnakeSegments--;
        }
    }

    private void initializeGrid() {
        for(int i=0; i < GRID_HEIGHT; i++) {
            for(int j=0; j < GRID_WIDTH; j++) {
                grid[i][j] = ".";
            }
        }
        grid[snakeHead.getY()][snakeHead.getX()] = "3";
    }

    private void loadGridToPlayArea() {

        System.out.println("Head Location: " + snakeHead.getX() + ", " + snakeHead.getY());

        playArea.setText("");

//        for(int i=0; i < GRID_HEIGHT; i++) {
//            for (int j = 0; j < GRID_WIDTH; j++) {
//                playArea.append("[" + j + "," + i + "]");
//            }
//            playArea.append("\n");
//        }

        for(String[] row : grid) {
            for(String cell : row) {
                playArea.append(cell);
            }
            playArea.append("\n");
        }
    }

    private void initializeGui() {

        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 25);

        KeyListener listener = initializeKeyListener();

        frame.setSize(250, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(playArea, BorderLayout.CENTER);

        playArea.setFont(font);
        playArea.setEditable(false);
        playArea.addKeyListener(listener);
        playArea.setForeground(Color.ORANGE);
        playArea.setBackground(Color.DARK_GRAY);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private KeyListener initializeKeyListener() {

        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                turn(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
    }

    private void moveSnake() {

        int originalHeadX = snakeHead.getX();
        int originalHeadY = snakeHead.getY();

        switch (direction) {
            case "up" -> moveHeadUp();
            case "right" -> moveHeadRight();
            case "down" -> moveHeadDown();
            case "left" -> moveHeadLeft();
        }
        moveBody(originalHeadX, originalHeadY);
        grid[snakeHead.getY()][snakeHead.getX()] = "3";
    }

    private void turn(int keyCode) {

        if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            System.out.println("up");
            direction = "up";
        }
        else if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            System.out.println("right");
            direction = "right";
        }
        else if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            System.out.println("down");
            direction = "down";
        }
        else if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            System.out.println("left");
            direction = "left";
        }
    }

    private void moveBody(int originalHeadX, int originalHeadY) {

        int segmentNewX;
        int segmentNewY;

        int segmentOldX = originalHeadX;
        int segmentOldY = originalHeadY;

        for(SnakeSegment snakeSegment : snakeBody) {

            segmentNewX = segmentOldX;
            segmentNewY = segmentOldY;

            segmentOldX = snakeSegment.getX();
            segmentOldY = snakeSegment.getY();

            snakeSegment.setX(segmentNewX);
            snakeSegment.setY(segmentNewY);

            grid[segmentOldY][segmentOldX] = ".";
            grid[segmentNewY][segmentNewX] = "=";
        }
    }

    private void moveHeadUp() {
        int nextY = snakeHead.getY() - 1;

        //If below bottom, move to top
        if(nextY < 0) {
            nextY = GRID_HEIGHT-1;
        }

        grid[snakeHead.getY()][snakeHead.getX()] = ".";
        grid[nextY][snakeHead.getX()] = "3";

        snakeHead.setY(nextY);
    }

    private void moveHeadRight() {
        int nextX = snakeHead.getX() + 1;

        //If after right limit, move to left
        if(nextX > GRID_WIDTH-1) {
            nextX = 0;
        }

        grid[snakeHead.getY()][snakeHead.getX()] = ".";
        grid[snakeHead.getY()][nextX] = "3";

        snakeHead.setX(nextX);
    }

    private void moveHeadDown() {
        int nextY = snakeHead.getY() + 1;

        //If above top, move to bottom
        if(nextY > GRID_HEIGHT-1) {
            nextY = 0;
        }

        grid[snakeHead.getY()][snakeHead.getX()] = ".";
        grid[nextY][snakeHead.getX()] = "3";

        snakeHead.setY(nextY);
    }

    private void moveHeadLeft() {
        int nextX = snakeHead.getX() - 1;

        //If after left limit, move to right
        if(nextX < 0) {
            nextX = GRID_WIDTH-1;
        }

        grid[snakeHead.getY()][snakeHead.getX()] = ".";
        grid[snakeHead.getY()][nextX] = "3";

        snakeHead.setX(nextX);
    }

    private void startWorkerThread() {

        SwingWorker sw1 = new SwingWorker() {

            @Override
            protected Object doInBackground() {
                while(!gameEnded) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(timePeriod);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    moveSnake();
                    loadGridToPlayArea();
                }
                return null;
            }
        };

        sw1.execute();
    }
}
