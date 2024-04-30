import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ship {
    public static void main(String[] args) {
        Ship ship = new Ship();
        ship.run();
    }
    public void run() {
        JFrame frame = new JFrame("Star Trek Ship");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        SPanel panel = new SPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}

class SPanel extends JPanel implements KeyListener {
    double shipX = 400; double shipY = 400;
    double rotationDeg = 0.0;
    double dt = 0.0;
    long previous = 0;
    long current = 0;
    double diff = 0;
    int lcount = 0;
    int rcount = 0;
    int ucount = 0;
    int scount = 0;
    double velocityX = 0;
    double velocityY = 0;
    public SPanel() {
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        drawShip(g);
        // calling paintComponent again
        repaint();
    }
    public Image getImage() throws Exception {
        return new ImageIcon("spaceship.png").getImage();
    }
    public void drawShip(Graphics g) {
        // finding how many seconds passed since the last paintComponent
        current = System.currentTimeMillis();
        diff = ((double) current - previous)/1000;
        // 125 degrees per second
        if(lcount > 1) rotationDeg -= diff * 125;
        if(rcount > 1) rotationDeg += diff * 125;
        rotationDeg = rotationDeg % 360;
        if(rotationDeg < 0) rotationDeg += 360;
        if(ucount > 0) {
            // using the unit circle to move the ship
            velocityX += diff * 50 * Math.cos(Math.toRadians(rotationDeg-90));
            velocityY += diff * 50 * Math.sin(Math.toRadians(rotationDeg-90));
        }
        shipX += velocityX * diff;
        shipY += velocityY * diff;
        if(scount > 0) {
            double newVelX = velocityX * 5 * diff;
            double newVelY = velocityY * 5 * diff;
            velocityX -= newVelX;
            velocityY -= newVelY;
        }
        if(shipX > 865) shipX = 1;
        else if(shipX < -65) shipX = 799;
        if(shipY > 865) shipY = 1;
        else if(shipY < -65) shipY = 799;
        // drawing the image

        // updating the previous variable
        previous = current;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_UP -> ucount++;
            case KeyEvent.VK_LEFT -> lcount++;
            case KeyEvent.VK_RIGHT -> rcount++;
            case 32 -> scount++;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_UP -> ucount = 0;
            case KeyEvent.VK_LEFT -> lcount = 0;
            case KeyEvent.VK_RIGHT -> rcount = 0;
            case 32 -> scount = 0;
        }
        repaint();
    }
}