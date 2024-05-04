import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Shooter {
    public static void main(String[] args) {
        Shooter st = new Shooter();
        st.run();
    }
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
class SHPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    Font stf;

    {
        try {
            stf = Font.createFont(Font.TRUETYPE_FONT, new File("stf.ttf")).deriveFont(35f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(stf);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Timer eTimer;
    eHandler eh;
    Image blaster = new ImageIcon("laser_blaster.png").getImage();
    double rotationRad = 0.0;
    boolean entered = false;
    ArrayList<Laser> lasers = new ArrayList<>();
    lHandler lh;
    Timer lTimer, spawnTimer, supplyTimer, countdown;
    int secondsLeft = 60;
    long prev = System.currentTimeMillis();
    ArrayList<Enemy> enemies = new ArrayList<>();
    int numShots = 7;
    boolean allDead = true;
    Image heart = new ImageIcon("heart.png").getImage();
    int lives = 3;
    public SHPanel() {
        setBackground(Color.CYAN);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        eTimer = new Timer(10, new eHandler());
        lTimer = new Timer(1, new lHandler());
        spawnTimer = new Timer(2000, new SpawnHandler());
        supplyTimer = new Timer(1000, new SuppHandler());
        countdown = new Timer(1000, new CountHandler());
    }
    public void __init__() {
        eTimer.start();
        lTimer.start();
        spawnTimer.start();
        supplyTimer.start();
        countdown.start();
    }

    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        while(checkShot()){}
        while(removeLasers()){}
        for(Enemy e : enemies) e.drawEnemy(g);
        for(Laser laser : lasers) laser.drawLaser(g);
        g.setFont(stf);
        g.setColor(new Color(211, 142, 38));
        g.drawString("" + secondsLeft, 375, 50);
        if(allDead && secondsLeft == 0) g.drawString("You win!", 375, 400);
        for(int i = 0; i < lives*25; i += 25) g.drawImage(heart, 600 + i, 10, 20, 20, null);
        g.setColor(Color.RED);
        g.fillRect(10, 10, 20 * numShots, 20);
        try {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(rotationRad+Math.PI/2, 400, 700);
            g2d.drawImage(blaster, 375, 700, 50, 50, null);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkShot() {
        for(Laser l : lasers) {
            Rectangle rect = new Rectangle((int)l.x, (int)l.y, 5, 5);
            for(Enemy e : enemies) {
                if(rect.intersects(new Rectangle(e.x_coord, e.y_coord, 75, 100)) && !e.shot && e.y_coord < 650) {
                    e.shot = true;
                    lasers.remove(l);
                    return true;
                }
            }
        }
        return false;
    }
    class SpawnHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            enemies.add(new Enemy());
            repaint();
        }
    }
    class SuppHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(numShots < 7) numShots++;
            repaint();
        }
    }
    class CountHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(secondsLeft > 0) secondsLeft--;
            if(secondsLeft == 40) {
                spawnTimer.stop();
                spawnTimer = new Timer(1000, new SpawnHandler());
                spawnTimer.start();
            } else if(secondsLeft == 20) {
                spawnTimer.stop();
                spawnTimer = new Timer(500, new SpawnHandler());
                spawnTimer.start();
            } else if(secondsLeft == 0) {

                for(Enemy e3 : enemies) {
                    if(e3.y_coord >= 650 && !e3.shot) allDead = false;
                }
            }
            repaint();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
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
        if(numShots > 0) {
            lasers.add(new Laser(Math.toDegrees(rotationRad)));
            numShots--;
        }
        repaint();
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
            for(Enemy e2 : enemies) {
                if(e2.y_coord < 650) e2.y_coord++;
                if(e2.y_coord >= 650 && !e2.shot) {
                    if(lives > 0 && !e2.alreadyCounted) {
                        lives--;
                        e2.alreadyCounted = true;
                    }
                }
                e2.check();
            }
            repaint();
        }
    }
    class lHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            long current = System.currentTimeMillis();
            for(Laser laser : lasers) {
                laser.x += 8 * Math.cos(Math.toRadians(laser.rot)) * (current-prev)/10;
                laser.y += 8 * Math.sin(Math.toRadians(laser.rot)) * (current-prev)/10;
            }
            prev = current;
        }
    }
    class Enemy {
        boolean shot = false;
        boolean alreadyCounted = false;
        int x_coord = (int)(Math.random()*751) + 25;
        int y_coord = 10;
        static Image[] imgs = new Image[5];
        long timeShot = -1;
        int current = 0;
        public Enemy() {
            for(int i = 1; i < 6; i++) imgs[i-1] = new ImageIcon("Sprite" + i + ".png").getImage();
        }
        public void drawEnemy(Graphics g) {
            g.drawImage(imgs[current], x_coord, y_coord, 75, 100, null);
        }
        public void check() {
            if(y_coord >= 650 && timeShot < 0) timeShot = System.currentTimeMillis();
            long time = System.currentTimeMillis();
            if(y_coord < 650) {
                if(!shot) current = 0;
                else current = 1;
             } else {
                if(shot && current < 4) {
                    long diff = time - timeShot;

                    if(diff < 1000) current = 2;
                    else if(diff >= 1000 && diff < 2000) current = 3;
                    else current = 4;
                }
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