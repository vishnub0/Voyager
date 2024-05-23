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

        Level3 bf = new Level3();
        frame.getContentPane().add(bf);
        frame.setVisible(true);
    }
}

// This is the JPanel for the third level and contains all the methods and variables to draw the level.
class Level3 extends JPanel {
    CardLayout cl4;
    public Level3() {
        setBackground(Color.BLACK);
        cl4 = new CardLayout();
        setLayout(cl4);
        runL3();
    }
    public void runL3() {
        Bossfight bf = new Bossfight();
        bf.init();
        add(bf, "boss");
    }
    // This is the class that draws the actual bossfight
    class Bossfight extends JPanel implements KeyListener {
        Image[] sprites = new Image[6];
        Image[] esprites = new Image[3];
        int currentIndex = 0;
        int khanIndex = 0;
        Timer actTimer;
        Kirk kirk = new Kirk();
        double kirkX = 300, kirkY = 500;
        boolean moveDone = true;
        double dist = 200;
        Image punchingBag = load("punchingbag");
        double pb_rotDeg = 90;
        long lastHit = -1;
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
            esprites[2] = load("k_block");
            actTimer = new Timer(10, new ActHandler());
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
                if(current - lastHit <= 500) pb_rotDeg = calcDeg(0, current - lastHit);
//                if(khanIndex == 1 && currentIndex == 4) {
//                    if(current - kirk.timeSinceMove <= 1000) {
//                        khanX = khan.prevX + (double) (current - kirk.timeSinceMove) / 10;
//                        if(khanX > 700) khanX = 700;
//                        khanY = 500 - 200 * Math.sin(Math.PI * (current - kirk.timeSinceMove) / 1000);
//                    }
//                    else khanIndex = 0;
//                } else if(khanIndex >= 1 && current - kirk.timeSinceMove > 450) khanIndex = 0;
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
            drawPB(g);
            g.setColor(Color.RED);
            g.fillRect(10, 10, kirk.health, 20);
            g.setColor(Color.BLACK);
            ((Graphics2D)g).setStroke(new BasicStroke(2));
            g.drawRect(10, 10, 100, 20);
            g.drawRect(580, 10, 200, 20);
            for(int i = 0; i <= 100; i += 25) g.drawLine(10 + i, 10, 10 + i, 30);
            for(int i = 0; i <= 200; i += 25) g.drawLine(580 + i, 10, 580 + i, 30);
            ((Graphics2D)g).setStroke(new BasicStroke(1));
        }
        // this method is used to draw the punching bag on the screen
        public void drawPB(Graphics g) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.fillOval(398, 303, 4, 4);
                g2d.rotate(Math.toRadians(pb_rotDeg), 400, 305);
                g2d.drawImage(punchingBag, 400, 260, 450, 90, null);
                g2d.rotate(-Math.toRadians(pb_rotDeg), 400, 305);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        // this method is used to calculate the degree of the punching bag
        public double calcDeg(int move, long time) {
            return 90 - (90 * Math.sin(Math.PI * time / 500));
        }

        class Kirk {
            int health = 100;
            long timeSinceMove = 0;

            // method used to move the player
            public void move(int right) {
                double distance = kirkX - 100;
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
            public void jab() {
                currentIndex = 2;
                timeSinceMove = System.currentTimeMillis();
                moveDone = false;
                boolean blocked = (int)(Math.random() * 10) + 1 < 7;
                if(dist < 10) {
                    if (!blocked) {
                        khanIndex = 1;
                        lastHit = System.currentTimeMillis();
                    } else {
                        khanIndex = 2;
                    }
                }
            }
            // second move of the player
            public void hook() {
                currentIndex = 3;
                timeSinceMove = System.currentTimeMillis();
                moveDone = false;
                boolean blocked = (int)(Math.random() * 10) + 1 < 5;
                if(dist < 10) {
                    if(!blocked) {
                        //khan.health -= 30;
                        khanIndex = 1;
                    } else {
                        khanIndex = 2;
                    }
                }

            }
            // third move of the player
            public void uppercut() {
                currentIndex = 4;
                timeSinceMove = System.currentTimeMillis();
                moveDone = false;
                boolean blocked = (int)(Math.random() * 10) + 1 < 3;
                if(dist < 10) {
                    if(!blocked) {
                        //khan.health -= 20;
                        khanIndex = 1;
                        //khan.prevX = khanX;
                    } else {
                        khanIndex = 2;
                    }
                }

            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            if(moveDone) {
                //dist = Math.abs(kirkX + 100 - khanX);
                switch(keycode) {
                    case KeyEvent.VK_UP -> kirk.jump();
                    case KeyEvent.VK_RIGHT -> kirk.move(1);
                    case KeyEvent.VK_LEFT -> kirk.move(-1);
                    case 67 -> kirk.jab();
                    case 88 -> kirk.hook();
                    case 90 -> kirk.uppercut();
                }
            }
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}