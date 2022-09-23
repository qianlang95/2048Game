/**
 * CS5004 Summer 2022 Final Project - GAME 2048
 * Controller class to bind GameWindow class and GameLogic class
 * Takes user inputs(key type), tells model what to do and view what to display
 * @author Qian Lang; Xiaodong Li
 * @version 0.0
 * 09 Aug 2022
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserAction implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {} // invoke when key is typed, output KeyChar -> e.getKeyChar()
    public void keyReleased(KeyEvent e) {} //called whenever the key is released

    /**
     * invoke the shiftUp()/shiftDown()/shiftTowardsLeft()/shiftTowardsRight() in GameLogic class based on the KeyCode of
     * key user pressed down (up/down/left/right)
     * @param e the event to be processed
     */
    public void keyPressed(KeyEvent e) {
        int keyVal = e.getKeyCode();

        switch (keyVal) {
            case KeyEvent.VK_UP:
                GameTrigger.GAME_LOGIC.shiftUp();
                break;
            case KeyEvent.VK_DOWN:
                GameTrigger.GAME_LOGIC.shiftDown();
                break;
            case KeyEvent.VK_LEFT:
                GameTrigger.GAME_LOGIC.shiftTowardsLeft();
                break;
            case KeyEvent.VK_RIGHT:
                GameTrigger.GAME_LOGIC.shiftTowardsRight();
                break;
            default:
                break;
        }

        GameTrigger.GAME_WINDOW.repaint(); //will call on paint method with param Graphics
    }

    /**
     * to add KeyListener
     */
    public void invokeKey() {
        GameTrigger.GAME_WINDOW.addKeyListener(this);
    }

    /**
     * to remove KeyListener
     */
    public void closeKey() {
        GameTrigger.GAME_WINDOW.removeKeyListener(this);
    }
}
