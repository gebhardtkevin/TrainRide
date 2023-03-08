package trainride;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean plusPressed;
    private boolean minusPressed;
    private boolean escPressed;
    private boolean plusWasPressed;
    private boolean minusWasPressed;

    @Override
    public void keyTyped(KeyEvent e) {
            //we don't need this
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_SUBTRACT -> minusWasPressed = true;
            case KeyEvent.VK_ADD -> plusWasPressed = true;
            default -> {/*do nothing*/}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            default -> {/*do nothing*/}
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isEscPressed() {
        return escPressed;
    }

    public boolean isPlusPressed() {
        return plusPressed;
    }

    public boolean isMinusPressed() {
        return minusPressed;
    }

    public boolean plusWasPressed() {
        return this.plusWasPressed;
    }

    public void resetPlusWasPressed() {
        this.plusWasPressed = false;
    }

    public boolean minusWasPressed() {
        return this.minusWasPressed;
    }

    public  void resetMinusWasPressed(){
        this.minusWasPressed = false;
    }
}