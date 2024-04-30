import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// class where all of the crap is created
public class Shooter {
    // main method that is called. makes an instance of the class in order to run the run() method
    public static void main(String[] args) {
        Shooter st = new Shooter();
        st.run();
    }
    // run method which creates the JFrame with a content pane of the Panel class. sets frame to be visible
    public void run() {
        JFrame frame = new JFrame("Voyager Level 1: Shooter");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        SHPanel panel = new SHPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
// panel class where all of the crap is drawn on the screen
class SHPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    Enemy bob;
    Timer eTimer;
    eHandler eh;
    Image blaster = new ImageIcon("laser_blaster.png").getImage();
    double rotationRad = 0.0;
    boolean entered = false;
    ArrayList<Laser> lasers = new ArrayList<>();
    lHandler lh;
    Timer lTimer;
    long prev = System.currentTimeMillis();


    // constructor which sets the background and sets the listeners
    public SHPanel() {
        setBackground(Color.CYAN);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        bob = new Enemy();
        eh = new eHandler();
        eTimer = new Timer(10, eh);
        eTimer.start();
        lh = new lHandler();
        lTimer = new Timer(1, lh);
        lTimer.start();
    }

    // paintComponent method which draws everything
    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        bob.drawEnemy(g);
        while(removeLasers()){}
        for(Laser laser : lasers) laser.drawLaser(g);
        try {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(rotationRad+Math.PI/2, 400, 700);
            g2d.drawImage(blaster, 375, 700, 50, 50, null);
//            g2d.rotate(-Math.PI/2);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        char let = e.getKeyChar();
        if(let == 's') bob.shot = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lasers.add(new Laser(Math.toDegrees(rotationRad)));
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        entered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        entered = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(entered) {
            int x = e.getX();
            int y = e.getY();
            x -= 400;
            y -= 700;
            rotationRad = Math.atan2(y, x);
        }
    }
    public boolean removeLasers() {
        for(Laser l : lasers) {
            if(l.x < 0 || l.x > 800 || l.y < 0 || l.y > 800) {
                lasers.remove(l);
                return true;
            }
        }
        return false;
    }
    class eHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(bob.y_coord < 650) bob.y_coord++;
            bob.check();
            repaint();
        }
    }
    class lHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            long current = System.currentTimeMillis();
            for(Laser laser : lasers) {
                laser.x += Math.cos(Math.toRadians(laser.rot)) * (current-prev)/10;
                laser.y += Math.sin(Math.toRadians(laser.rot)) * (current-prev)/10;
            }
            prev = current;
        }
    }
    // class for the enemy falling from the sky
    class Enemy {
        boolean shot = false;
        boolean dead = false;
        int x_coord = (int)(Math.random()*201) + 300;
        int y_coord = 10;
        static Image[] imgs = new Image[5];
        int current = 0;
        Polygon poly = new Polygon();
        // constructor to fill the array for the sprites
        public Enemy() {
            for(int i = 1; i < 6; i++) imgs[i-1] = new ImageIcon("Sprite" + i + ".png").getImage();
        }
        // method to draw the enemy on the screen
        public void drawEnemy(Graphics g) {
            g.drawImage(imgs[current], x_coord, y_coord, 250, 125, null);
        }
        public void check() {
            if(y_coord < 650) {
                if(!shot) current = 0;
                else current = 1;
             } else {
                if(shot && current < 4) current++;
            }
        }
    }
    class Laser {
        double rot;
        double x = 400;
        double y = 700;
        public Laser(double rotRad) {
            rot = rotRad;
        }
        public void drawLaser(Graphics g) {
            ((Graphics2D)g).setStroke(new BasicStroke(10));
            g.setColor(Color.RED);
            g.drawLine(400, 700, (int)x, (int)y);
        }
    }
}