package trainride;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Component;

public class Window {
    JFrame mainWindow;

    public Window() {
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setTitle("TrainRide!");
    }

    public void show(){
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    public void add(Component component) {
        mainWindow.add(component);
    }
}
