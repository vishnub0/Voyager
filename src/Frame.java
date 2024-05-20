import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame {
    public static void main(String[] args) {
        Frame fr = new Frame();
        fr.run();
    }
    public void run() {
        JFrame frame = new JFrame("Bossfight");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        Bossfight bf = new Bossfight();
        frame.getContentPane().add(bf);
        frame.setVisible(true);
    }
}

// This is the class that draws the actual bossfight
class Bossfight extends JPanel implements KeyListener {
    Image[] sprites = new Image[6];
    Image[] esprites = new Image[2];
    int currentIndex = 0;
    int khanIndex = 0;
    Timer actTimer;
    Kirk kirk = new Kirk();
    double kirkX = 300, kirkY = 500;
    Khan khan = new Khan();
    double khanX = 500, khanY = 500;
    boolean moveDone = true;
    double dist = 200;

    // constructor that sets the background, adds the KeyListener, and loads all the sprite images
    public Bossfight() {
        setBackground(Color.PINK);
        addKeyListener(this);
        sprites[0] = load("standing");
        sprites[1] = load("jumping");
        sprites[2] = load("jab");
        sprites[3] = load("hook");
        sprites[4] = load("uppercut");
        sprites[5] = load("knockback");
        esprites[0] = load("k_standing");
        esprites[1] = load("k_knockback");
        actTimer = new Timer(10, new ActHandler());
        init();
    }
    // method that is used to start all timers when the user goes to the screen
    public void init() {
        actTimer.start();
    }
    // handler class that is used to determine the appropriate user sprite
    class ActHandler implements ActionListener {
        // method that constantly checks which user sprite should be drawn on the screen
        @Override
        public void actionPerformed(ActionEvent e) {
            long current = System.currentTimeMillis();
            if(currentIndex == 1 || currentIndex == 4) {
                if(current - kirk.timeSinceMove <= 1000) {
                    kirkY = 500 - 200 * Math.sin(Math.PI * (current - kirk.timeSinceMove) / 1000);
                } else {
                    currentIndex = 0;
                    moveDone = true;
                }
            } else if(currentIndex == 2) {
                if(current - kirk.timeSinceMove > 125) {
                    currentIndex = 0;
                    moveDone = true;
                }
            } else if(currentIndex == 3) {
                if(current - kirk.timeSinceMove > 175) {
                    currentIndex = 0;
                    moveDone = true;
                }
            } else {
                currentIndex = 0;
            }
            if(khanIndex == 1 && currentIndex == 4) {
                if(current - kirk.timeSinceMove <= 1000) {
                    khanX = khan.prevX + (double) (current - kirk.timeSinceMove) / 10;
                    if(khanX > 700) khanX = 700;
                    khanY = 500 - 200 * Math.sin(Math.PI * (current - kirk.timeSinceMove) / 1000);
                }
                else khanIndex = 0;
            }
            if(khanIndex == 1 && currentIndex < 4 && current - kirk.timeSinceMove > 450) khanIndex = 0;
            repaint();
        }
    }

    // method that is used to quickly load all the images required
    public Image load(String filename) {
        return new ImageIcon(filename + ".png").getImage();
    }
    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        g.drawImage(sprites[currentIndex], (int)kirkX, (int)kirkY, 100, 250, null);
        g.drawImage(esprites[khanIndex], (int)khanX, (int)khanY, 100, 250, null);
        g.drawString("Kirk health: " + kirk.health, 50, 100);
        g.drawString("Khan health: " + khan.health, 300, 100);
    }

    class Kirk {
        int health = 100;
        long timeSinceMove = 0;

        // method used to move the player
        public void move(int right) {
            double distance = khanX - kirkX - 100;
            if(right == 1) {
                if (distance >= 20 && kirkX + 20 <= 700) kirkX += 20;
                else if(distance < 20 && kirkX + distance <= 700) kirkX += distance;
            }
            else {
                if(kirkX - 20 >= 0) kirkX -= 20;
            }
            currentIndex = 0;
            timeSinceMove = System.currentTimeMillis();
        }
        // jump move of the player
        public void jump() {
            currentIndex = 1;
            timeSinceMove = System.currentTimeMillis();
            moveDone = false;
        }
        // first move of the player
        public void jab(Khan khan) {
            currentIndex = 2;
            if(dist < 10) {
                khan.health -= 20;
                khanIndex = 1;
            }
            timeSinceMove = System.currentTimeMillis();
            moveDone = false;
        }
        // second move of the player
        public void hook(Khan khan) {
            currentIndex = 3;
            if(dist < 10) {
                khan.health -= 30;
                khanIndex = 1;
            }
            timeSinceMove = System.currentTimeMillis();
            moveDone = false;
        }
        // third move of the player
        public void uppercut(Khan khan) {
            currentIndex = 4;
            if(dist < 10) {
                khan.health -= 20;
                khanIndex = 1;
                khan.prevX = khanX;
            }
            timeSinceMove = System.currentTimeMillis();
            moveDone = false;
        }
    }

    class Khan {
        int health = 200;
        double prevX = 500;
        /*
        // first move of Khan
        public void hellHornet(Player player) {
            player.health -= 15 - player.blockPower;
            System.out.println("Khan used Hell Hornet.");
            if(player.blockPower > 0) System.out.println("You blocked the attack.");
            Tools.checkPlayerHealth(player);
        }
        // second move of Khan
        public void searingDamnation(Player player) {
            player.health -= 20 - player.blockPower;
            System.out.println("Khan used Searing Damnation.");
            Tools.checkPlayerHealth(player);
        }
        // third move of Khan
        public void infernalSwarm(Player player) {
            player.health -= Tools.checkkhanDamage(player);
            System.out.println("Khan used Infernal Swarm.");
            if(player.blockPower > 0) System.out.println("You blocked the attack.");
            else {
                System.out.println("You will take burn damage for the next 4 turns.");
                burnTurns = 4;
            }
            Tools.checkPlayerHealth(player);
        }
        // fourth move of Khan
        public void lordOfTheFlames(Player player) {
            player.health -= 35 - player.blockPower;
            System.out.println("Khan is using his special move...");
            System.out.println("Lord of the Flames.");
            lotfCooldown = 3;
            System.out.println("Khan won't be able to use his special move for the next 3 turns.");
            Tools.checkPlayerHealth(player);
        }
         */
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if(moveDone) {
            dist = Math.abs(kirkX + 100 - khanX);
            switch(keycode) {
                case KeyEvent.VK_UP -> kirk.jump();
                case KeyEvent.VK_RIGHT -> kirk.move(1);
                case KeyEvent.VK_LEFT -> kirk.move(-1);
                case 67 -> kirk.jab(khan);
                case 88 -> kirk.hook(khan);
                case 90 -> kirk.uppercut(khan);
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}