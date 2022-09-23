/**
 * CS5004 Summer 2022 Final Project - GAME 2048
 * This class elaborates on the view
 * This class is a panel which draw the background, logo, scoreboard, and the gameboard
 * @author Qian Lang; Xiaodong Li
 * @version 0.0
 * 09 Aug 2022
 */


import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.util.HashMap;

public class GamePanel extends JPanel {
    private static final int TILE_ARC = 15;
    private static final int BOARD_MARGIN = 20;
    private static final int TILE_SIZE = 65;
    private static final int TILE_MARGIN = 15;
    private static final String FONT = "Arial";

    public GamePanel(){}

    /**
     * overriding paint() to define what we want to paint/show to the screen
     * @param graphics  the <code>Graphics</code> context in which to paint
     */
    public void paint(Graphics graphics){
        Graphics2D graph = ((Graphics2D) graphics); // cast graphics to type Graphics2D which has more method than its parent class
        paintBackground(graph);
        setLogo(graph);
        showScoreBoard(graph);
        drawGameBoard(graph);
        graph.dispose(); // free memory
    }

    /**
     * draw logo
     * @param graph context in which to paint
     */
    private void setLogo(Graphics graph) {
        graph.setFont( new Font(FONT, Font.BOLD, 38) );
        graph.setColor( new Color (0X776E65));
        graph.drawString("Fun2048!", BOARD_MARGIN, 50);
    }

    /**
     * draw background
     * @param graph context in which to paint
     */
    private void paintBackground(Graphics graph){
        graph.setColor(new Color (0XFAF8EF));
        graph.fillRect(0,0, GameTrigger.GAME_WINDOW.getWidth(), GameTrigger.GAME_WINDOW.getHeight());
    }

    /**
     * draw score board
     * @param graph context in which to paint
     */
    private void showScoreBoard(Graphics graph){
        int widthOfScoreBoard = 100;
        int heightOfScoreBoard = 80;
        int x = GameTrigger.GAME_WINDOW.getWidth() - BOARD_MARGIN - widthOfScoreBoard;
        int y = 10;

        //draw score board
        graph.setColor(new Color (0X776E65));
        graph.fillOval(x, y, widthOfScoreBoard, heightOfScoreBoard);
        graph.setFont( new Font(FONT, Font.BOLD, 15) );
        graph.setColor(new Color(0XFFFFFF));
        graph.drawString("SCORE: ", x + 20, y + 35);

        //display current score to the score board
        graph.setFont( new Font(FONT, Font.BOLD, 18) );
        String score = String.valueOf(GameTrigger.GAME_LOGIC.getTotalScore());
        graph.drawString(score, x + 40, y + 55);


    }

    /**
     * draw gameboard
     * @param graph context in which to paint
     */
    private static void drawGameBoard(Graphics graph){
        //set the game board
        graph.translate(BOARD_MARGIN, 100); //place the left-top of game board and all tiles at this coordination
        int widthOfBoard = GameTrigger.GAME_WINDOW.getWidth() - 2 * BOARD_MARGIN;
        int heightOfBoard = TILE_SIZE * 4  + (4 + 1) * TILE_MARGIN;
        graph.setColor(new Color (0XBBADA0));
        graph.fillRect(0,0, widthOfBoard, heightOfBoard );

        //lay out 4*4 tiles on the game board
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                drawTile(graph, GameTrigger.GAME_LOGIC.getTile(row, col), col, row);
            }
        }
    }

    /**
     * draw each tile on the game board
     * @param graph context in which to paint
     * @param tile the tile at position(row, col)
     * @param x x coordinate of tile
     * @param y y coordinate of tile
     */
    private static void drawTile(Graphics graph, Tile tile, int x, int y) {
        int value = 0;
        if (tile != null){
            value = tile.getValue();}

        //draw each tile(without value)
        int xOffset = x * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
        int yOffset = y * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
        graph.setColor(getTileColor(value));
        graph.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, TILE_ARC, TILE_ARC);

        //display value of each tile at the center of each tile
        graph.setColor(new Color (0X776E65));
        final Font font = new Font(FONT, Font.BOLD, 40);
        graph.setFont(font);

        final FontMetrics fm = graph.getFontMetrics(font);
        String s = String.valueOf(value);
        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, graph).getBaselineOffsets()[2];
        if (value != 0) {
            graph.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
        }

        //if game is over, display 'win' or 'lost' to the window
        if (GameTrigger.GAME_LOGIC.isGameOver()) {
            graph.setColor(new Color(255, 255, 255, 40));
            graph.fillRect(0, 0, GameTrigger.GAME_WINDOW.getWidth(), GameTrigger.GAME_WINDOW.getWidth());
            graph.setColor(new Color (0X776E65));
            graph.setFont(new Font(FONT, Font.BOLD, 40));
            if(GameTrigger.GAME_LOGIC.isWinWith2048()){
            graph.drawString("Congrats!You WIN!", 2 * BOARD_MARGIN, 100 + BOARD_MARGIN);}
            else{
                graph.drawString("Sorry!You Lose!", 2 * BOARD_MARGIN, 100 + BOARD_MARGIN);}
            GameTrigger.USER_ACTION.closeKey(); //player cannot play game anymore
            }

        }

    /**
     * get the color of tile based on the value of the tile
     * @param value the value of tile
     * @return the color of the tile
     */
    public static Color getTileColor(int value) {
        HashMap<Integer, Color> tileColor = new HashMap<>();
        tileColor.put(0,		new Color (238, 228, 218, 90));
        tileColor.put(2,		new Color (0XEEE4DA));
        tileColor.put(4,		new Color (0XEDE0C8));
        tileColor.put(8,		new Color (0XF2B179));
        tileColor.put(16,		new Color (0XF59563));
        tileColor.put(32,		new Color (0XF67C5F));
        tileColor.put(64,		new Color (0XF65E3B));
        tileColor.put(128,		new Color (0XEDCF72));
        tileColor.put(256,		new Color (0XEDCC61));
        tileColor.put(512,		new Color (0XEDC850));
        tileColor.put(1024, 	new Color (0XEDC53F));
        tileColor.put(2048, 	new Color (0XEDC22E));
        return tileColor.get(value);
    }

    }

