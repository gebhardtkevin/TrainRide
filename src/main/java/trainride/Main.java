package trainride;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        GamePanel panel = GamePanel.getInstance();
        panel.start();
        window.add(panel);
        window.show();
    }
}