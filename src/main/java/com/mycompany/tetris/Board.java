/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author puntual
 */
public class Board extends javax.swing.JPanel {

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (canMove(currentShape, currentRow, currentCol - 1)) {
                        currentCol--;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (canMove(currentShape, currentRow, currentCol + 1)) {
                        currentCol++;
                    }
                    break;
                case KeyEvent.VK_Z:
                    Shape newShape = currentShape.getCopy();
                    newShape.rotateRight();
                    if (!shapeHitsMatrix(newShape, currentRow, currentCol)) {
                        currentShape = newShape;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (canMove(currentShape, currentRow + 1, currentCol)) {
                        currentRow++;
                    }
                    break;
                case KeyEvent.VK_UP:
                    dropShape();
                    break;
                default:
                    break;
            }
            repaint();
        }
    }

    public static final int NUM_ROWS = 22;
    public static final int NUM_COLS = 10;
    private Shape currentShape;
    private int currentRow;
    private int currentCol;
    private Timer timer;
    private MyKeyAdapter keyAdapter;
    private Tetrominoes[][] matrix;
    private ScoreInterface score;

    /**
     * Creates new form Board
     */
    public Board() {
        initComponents();
        initMatrix();
        keyAdapter = new MyKeyAdapter();
        addKeyListener(keyAdapter);
        setFocusable(true);
    }
    
    public void initGame() {
        initMatrix();
        createNewCurrentShape();
        int deltaTime = ConfigData.getInstance().getDeltaTime();
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(deltaTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        timer.start();
    }
    
    
    public void dropShape() {
        int row = currentRow;
        while (canMove(currentShape, row + 1, currentCol)) {
            row++;
        }
        currentRow = row;
    }
    
    
    public void setScoreInterface(ScoreInterface scoreInterface) {
        this.score = scoreInterface;
    }
    
    public void initMatrix() {
        matrix = new Tetrominoes[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] = Tetrominoes.NoShape;
            }
        }
    }
    
    public boolean canMove(Shape shape, int row, int col) {
        if (col + shape.getMinX() < 0 || col + shape.getMaxX() >= NUM_COLS) {
            return false;
        }
        if (col + shape.getMaxX() >= NUM_ROWS) {
            return false;
        }
        if (row + shape.getMaxY() >= NUM_ROWS) {
            return false;
        }
        if (shapeHitsMatrix(shape, row, col)) {
            return false;
        }
        return true;
    }
    
    private boolean shapeHitsMatrix(Shape shape, int row, int col) {
        for (int i = 0; i < 4; i++) {
            int rr = row + shape.getY(i);
            int cc = col + shape.getX(i);
            if (cc < 0 || cc >= NUM_COLS) {
                return true;
            }
            if (rr >= 0) {
                if (matrix[rr][cc] != Tetrominoes.NoShape) {
                return true;
                }
            }
        }
        return false;
    }

    public void tick() {
        if (shapeHitsMatrix(currentShape, 0, currentCol)) {
            processGameOver();
            return;
        }
        if (canMove(currentShape, currentRow + 1, currentCol)) {
            currentRow++;
        } else {
            copyCurrentShapeToMatrix();
            checkCompletedRows();
            createNewCurrentShape();
        }
        repaint();

    }
    
    
    public void checkCompletedRows() {
        int row = NUM_ROWS - 1;
        while(row >= 0) {
            if (isRowCompleted(row)) {
                deleteRow(row); 
                score.incrementScore();
            } else {
                row--;
            }
        }
    }
    
    public boolean isRowCompleted(int row) {
        for (int col = 0; col < NUM_COLS; col++) {
            if (matrix[row][col] == Tetrominoes.NoShape) {
                return false;
            }
        }
        return true;
    }
    
    public void deleteRow(int row) {
        for (int r = row; r > 0; r--) {
            for (int col = 0; col < NUM_COLS; col++) {
                matrix[r][col] = matrix[r - 1][col];
            }
        }
        for (int col = 0; col < NUM_COLS; col++) {
            matrix[0][col] = Tetrominoes.NoShape;
        }
    }
   
    
    public void copyCurrentShapeToMatrix() {
        for (int i = 0; i < 4; i++) {
            int row = currentRow + currentShape.getY(i);
            int col = currentCol + currentShape.getX(i);
            if (row >= 0) {
                matrix[row][col] = currentShape.getShape();
            }
        }
    }
    
    private void createNewCurrentShape() {
        currentShape = new Shape();
        currentRow = 0;
        currentCol = NUM_COLS / 2;
                
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBorder(g);
        paintMatrix(g);
        if (currentShape != null) {
            paintCurrentShape(g);
        }
    }
    
    public void paintMatrix(Graphics g) {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                drawSquare(g, row, col, matrix[row][col]);
            }
        }
    }
    
    public void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(1);
        g2d.setStroke(bs);
        g2d.drawRect(0, 0, NUM_COLS * getSquareWidth() - 2, 
                NUM_ROWS * getSquareHeight() - 2);
        
    }

    private void paintCurrentShape(Graphics g) {
        for (int i = 0; i < 4; i++) {
            if (currentShape.getY(i) + currentRow >= 0) {
                drawSquare(g, currentRow + currentShape.getY(i),
                        currentCol + currentShape.getX(i),
                        currentShape.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int row, int col,
            Tetrominoes shape) {
        Color colors[] = {new Color(0, 0, 0),
            new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
        };
        int x = col * getSquareWidth();
        int y = row * getSquareHeight();
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, getSquareWidth() - 2,
                getSquareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + getSquareHeight() - 1, x, y);
        g.drawLine(x, y, x + getSquareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + getSquareHeight() - 1,
                x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1,
                y + getSquareHeight() - 1,
                x + getSquareWidth() - 1, y + 1);
    }

    private int getSquareWidth() {
        return getWidth() / NUM_COLS;
    }

    private int getSquareHeight() {
        return getHeight() / NUM_ROWS;
    }
    
    public void processGameOver() {
        timer.stop();
        removeKeyListener(keyAdapter);
        currentShape = null;
        fillMatrixWithGameOver();
            
    }
    
    public void pauseGame() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            removeKeyListener(keyAdapter);
        }
    }
    
    public void resumeGame() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
            addKeyListener(keyAdapter);
        }
    }
    
    public static int rowGO;
    public static int colGO;
    private static Timer timerGO;
    
    private void fillMatrixWithGameOver() {
        rowGO = 0;
        colGO = 0;
        timerGO = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                matrix[rowGO][colGO] = Tetrominoes.LineShape;
                repaint();
                colGO++;
                if (colGO >= NUM_COLS) {
                    colGO = 0;
                    rowGO++;
                }
                if (colGO >= NUM_COLS) {
                    timer.stop();
                }
            }
        });
        timerGO.start();
        /*
        try {
            Thread.sleep(10000); // 10000 milisegundos = 10 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
    



    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
