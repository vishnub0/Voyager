import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StarryBackground {
    public static void main(String[] args) {
        StarryBackground sb = new StarryBackground();
        sb.run();
    }
    public void run() {
        JFrame frame = new JFrame("Star Trek Starry BG");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        Panel panel = new Panel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}

class Panel extends JPanel {
    ArrayList<Star> stars = new ArrayList<Star>();
    Timer starTimer;

    public Panel() {
        setBackground(Color.BLACK);
        StarHandler sh = new StarHandler();
        starTimer = new Timer(10, sh);
        starTimer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int numStars = (int)(Math.random()*11);
        for(int i = 0; i < numStars; i++) stars.add(new Star());
        while(removeStars()) {}
        for(Star star : stars) star.drawStar(g);
    }
    public boolean removeStars() {
        for(Star i : stars) {
            if(i.timesDrawn >= 7) {
                stars.remove(i);
                return true;
            }
        }
        return false;
    }
    class Star {
        double timesDrawn = 0;
        int x_coord = (int)(Math.random()*796);
        int y_coord = (int)(Math.random()*796);
        public void drawStar(Graphics g) {
            g.setColor(Color.WHITE);
            int diameter = getDiameter();
            g.fillOval(x_coord - diameter/2, y_coord - diameter/2, diameter, diameter);
            timesDrawn += 0.1;
        }
        public int getDiameter() {
            return (int)(Math.sin((Math.PI * timesDrawn) / 7) * 8);
        }
    }
    class StarHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}