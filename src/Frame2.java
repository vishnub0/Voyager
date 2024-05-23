import javax.swing.*;
import java.awt.*;

public class Frame2 {
    public static void main(String[] args) {
        Frame2 fr2 = new Frame2();
        fr2.run();
    }
    public void run() {
        JFrame frame = new JFrame("Credits");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        Credits cr = new Credits();
        frame.getContentPane().add(cr);
        frame.setVisible(true);
    }
}

class Credits extends JPanel {
    int y_coord = 300;
    Timer credTimer;
    String text = "Hello, this is the credits. Thank you for playing.";
    JLabel label = new JLabel(text);
    public Credits() {
        setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setBounds(200, y_coord, 100, 10000);
        credTimer = new Timer(10, e -> {y_coord--; label.setBounds(200, y_coord, 100, 10000); repaint();});
        credTimer.start();
    }
    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
    }
}