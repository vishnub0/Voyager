import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    long previous = 0;
    long current = 0;
    double diff = 0;
    int lcount = 0;
    int rcount = 0;
    int ucount = 0;
    int scount = 0;
    double velocityX = 0;
    double velocityY = 0;
    Image torpedo;
    ArrayList<Torpedo> torpedoes = new ArrayList<>();
    ArrayList<Torpedo> torpedoes2 = new ArrayList<>();
    Timer torpTimer, actTimer, spawnTimer2;
    ArrayList<EnemyShip> enemies = new ArrayList<>();
    int playerHealth = 100;
    Image explosion;
    int stage = 1;
    int enemiesLeft = 6;

    public SPanel() {
        setBackground(Color.BLACK);
        addKeyListener(this);
        torpedo = new ImageIcon("torpedo.png").getImage();
        torpTimer = new Timer(10, new TorpHandler());
        actTimer = new Timer(10, new ActHandler());
        enemies.add(new EnemyShip());
        explosion = new ImageIcon("explosion.png").getImage();
        spawnTimer2 = new Timer(10, new SpawnHandler2());
        __init__();
    }
    public void __init__() {
        torpTimer.start();
        actTimer.start();
        spawnTimer2.start();
    }
    public void stopAll() {
        torpTimer.stop();
        actTimer.stop();
        spawnTimer2.stop();
    }
    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(40, 10, playerHealth, 20);
        while(removeTorpedoes()){}
        while(checkShot());
        for (Torpedo torpedo1 : torpedoes) torpedo1.drawTorpedo(g);
        for (Torpedo torpedo1 : torpedoes2) torpedo1.drawTorpedo(g);
        for (EnemyShip enemy : enemies) {
            enemy.drawEnemy(g);
        }
        drawShip(g);
        repaint();
    }
    class SpawnHandler2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(stage < 5) {
                EnemyShip lastShip = enemies.get(enemies.size()-1);
                if(lastShip.health == 0 && System.currentTimeMillis() - lastShip.deathDate >= 5000) {
                    enemies.add(new EnemyShip());
                    if(stage == 4) enemies.add(new EnemyShip());
                    stage++;
                }
                repaint();
            }
        }
    }
    class ActHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (EnemyShip enemy : enemies) enemy.act();
        }
    }
    public Image getImage() {
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
        for (EnemyShip enemy : enemies) {
            if(enemy.enemyX > 865) enemy.enemyX = 1;
            else if(enemy.enemyX < -65) enemy.enemyX = 799;
            if(enemy.enemyY > 865) enemy.enemyY = 1;
            else if(enemy.enemyY < -65) enemy.enemyY = 799;
        }
        // drawing the image
        if(playerHealth > 0) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(Math.toRadians(rotationDeg), shipX + 15, shipY + 30);
                g2d.drawImage(getImage(), (int) shipX, (int) shipY, 30, 60, null);
                g2d.rotate(Math.toRadians(-rotationDeg), shipX + 15, shipY + 30);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else g.drawImage(explosion, (int)shipX, (int)shipY, 60, 60, null);
        // updating the previous variable
        previous = current;
    }
    class TorpHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Torpedo torpedo1 : torpedoes) {
                torpedo1.tick(0.1);
            }
            for (Torpedo torpedo1 : torpedoes2) {
                torpedo1.tick(0.1);
            }
        }
    }
    // This method is used to remove torpedoes that are off of the screen
    public boolean removeTorpedoes() {
        for (Torpedo torpedo1 : torpedoes) {
            if(torpedo1.x_coord < -20 || torpedo1.x_coord > 820 || torpedo1.y_coord < -20 || torpedo1.y_coord > 820) {
                torpedoes.remove(torpedo1);
                return true;
            }
        }
        return false;
    }
    // This method checks if a torpedo has hit the enemy
    public boolean checkShot() {
        for (Torpedo torpedo1 : torpedoes) {
            for (EnemyShip enemy : enemies) {
                double distX = torpedo1.x_coord - enemy.enemyX;
                double distY = torpedo1.y_coord - enemy.enemyY;
                double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                if(dist < 25 && enemy.health > 0) {
                    torpedoes.remove(torpedo1);
                    enemy.health -= 25;
                    if(enemy.health == 0) {
                        enemy.deathDate = System.currentTimeMillis();
                        enemiesLeft--;
                    }
                    return true;
                }
            }
        }
        for (Torpedo torpedo1 : torpedoes2) {
            double distX = torpedo1.x_coord - shipX;
            double distY = torpedo1.y_coord - shipY;
            double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
            if(dist < 25) {
                torpedoes2.remove(torpedo1);
                playerHealth -= 25;
                return true;
            }
        }
        return false;
    }
    // this class is used to represent an enemy ship and contain all info needed to draw one
    class EnemyShip {
        double enemyX, enemyY;
        double rot;
        Image kgship;
        int health = 100;
        long lastShot;
        long deathDate = -1;
        public EnemyShip() {
            enemyX = (int)(Math.random() * 661) + 70;
            enemyY = (int)(Math.random() * 661) + 70;
            rot = (int)(Math.random() * 360);
            kgship = new ImageIcon("klingonship.png").getImage();
        }
        public void drawEnemy(Graphics g) {
            long current = System.currentTimeMillis();
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(Math.toRadians(rot), enemyX, enemyY);
                if(health > 0) g2d.drawImage(kgship, (int)enemyX - 35, (int)enemyY - 20, 70, 40, null);
                else if(health == 0 && current - deathDate < 5000) g2d.drawImage(explosion, (int)enemyX - 35, (int)enemyY - 20, 40, 40, null);
                g2d.rotate(Math.toRadians(-rot), enemyX, enemyY);
            } catch(Exception e) {
                throw new RuntimeException();
            }
        }
        public void act() {
            long current = System.currentTimeMillis();
            if(health > 0) {
                double distX = shipX - enemyX;
                double distY = shipY - enemyY;
                double deg = Math.toDegrees(Math.atan2(distY, distX));
                if(Math.abs(rot - deg) <= 1) rot = deg;
                else if(rot > deg) rot--;
                else rot++;
                if(Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2)) > 200) {
                    enemyX += Math.cos(Math.toRadians(rot));
                    enemyY += Math.sin(Math.toRadians(rot));
                }
                if(Math.abs(rot - deg) < 10 && current - lastShot >= 2000 && playerHealth > 0) {
                    torpedoes2.add(new Torpedo(enemyX, enemyY, rot));
                    lastShot = current;
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 's') {
            double x1 = (shipX + 15) + 15 * Math.cos(Math.toRadians((rotationDeg - 90) + 45));
            double y1 = (shipY + 15) + 15 * Math.sin(Math.toRadians((rotationDeg - 90) + 45));
            torpedoes.add(new Torpedo(x1, y1, rotationDeg-90));
            double x2 = (shipX + 15) + 15 * Math.cos(Math.toRadians((rotationDeg - 90) - 45));
            double y2 = (shipY + 15) + 15 * Math.sin(Math.toRadians((rotationDeg - 90) - 45));
            torpedoes.add(new Torpedo(x2, y2, rotationDeg-90));
        }
        repaint();
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
// This class contains all variables and methods required to draw a torpedo
class Torpedo {
    double x_coord, y_coord;
    double rotDeg;

    public Torpedo(double x_coord, double y_coord, double rotDeg) {
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.rotDeg = rotDeg;
    }
    public void tick(double dt) {
        x_coord += 100 * dt * Math.cos(Math.toRadians(rotDeg));
        y_coord += 100 * dt * Math.sin(Math.toRadians(rotDeg));
    }
    public void drawTorpedo(Graphics g) {
        try {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(rotDeg), x_coord, y_coord);
            g2d.drawImage(new ImageIcon("torpedo.png").getImage(), (int)x_coord - 10, (int)y_coord - 2, 20, 4, null);
            g2d.rotate(Math.toRadians(-rotDeg), x_coord, y_coord);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}