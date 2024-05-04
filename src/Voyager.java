/*
Vishnu Bharadwaj
4/28/24
Voyager.java
This is my game, Voyager. It is a game based off of the Star Trek Original Series that uses all of my java knowledge in
a fun game. Currently, I have finished the homepage and I am working on Level 1 (you can switch between levels).
*/

import javax.swing.*; import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// This is the main class which contains the main method and method to create the JFrame.
public class Voyager {
    // This is the main method of my program which creates an object (type Voyager) and uses it to call the run method.
    public static void main(String[] args) {
        Voyager vg = new Voyager();
        vg.run();
    }
    // This is the run method which creates the JFrame and sets it to be visible. It also adds the VPanel to the JFrame.
    public void run() {
        JFrame frame = new JFrame("Voyager");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        VPanel VPanel = new VPanel();
        frame.getContentPane().add(VPanel);
        frame.setVisible(true);
    }
}

// This is the VPanel class which is the content pane of the JFrame. It contains all the code to build my game.
class VPanel extends JPanel {
    CardLayout cl;
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
    final Color GOLD = new Color(211, 142, 38);
    ArrayList<Star> stars = new ArrayList<>();
    Timer starTimer;
    Image logo = new ImageIcon("voyager-logo.png").getImage();
    Image pb_p, pb_l, pb_a, pb_y;
    Image pb_pClicked, pb_lClicked, pb_aClicked, pb_yClicked;
    long prev = System.currentTimeMillis();
    Clip clip;
    Homepage homepage;
    Level1 level1;

    // This is the constructor which sets the background and layout of the VPanel. It also calls the run method.
    public VPanel() {
        setBackground(Color.BLACK);
        cl = new CardLayout();
        setLayout(cl);
        run();

    }
    // This is the paintComponent method of the VPanel which changes the background color by calling the parent's pC.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    // This is the run method which creates Panels for each of the levels and adds them to the VPanel.
    public void run() {
        homepage = new Homepage();
        homepage.setBackground(Color.BLACK);
        add(homepage, "homepage");
        level1 = new Level1();
        add(level1, "l1");
        JPanel level2 = new JPanel();
        add(level2, "l2");
        JPanel level3 = new JPanel();
        add(level3, "l3");
    }
    // This class represents a star in my background. It has all the variables and methods needed to draw the star.
    class Star {
        int x_coord = (int)(Math.random()*796);
        int y_coord = (int)(Math.random()*796);
        long creation = System.currentTimeMillis();
        long current = System.currentTimeMillis();
        // This method is used to draw the star on the screen.
        public void drawStar(Graphics g) {
            current = System.currentTimeMillis();
            g.setColor(Color.WHITE);
            int diameter = getDiameter(current-creation);
            g.fillOval(x_coord - diameter/2, y_coord - diameter/2, diameter, diameter);
        }
        // This function is used to calculate the diameter of the star based on how long the star has existed.
        public int getDiameter(long diff) {
            return (int)(8 * Math.sin(Math.PI * diff / 700));
        }
    }
    // This is a method I use to remove all the old stars in the ArrayList with diameter 0 (will come into play later).
    public boolean removeStars() {
        for(Star i : stars) {
            if(i.current - i.creation >= 700) {
                stars.remove(i);
                return true;
            }
        }
        return false;
    }
    // This class is the event handler for the star timer and calls pC in order to constantly update and redraw stars.
    class StarHandler implements ActionListener {
        // This is the method called by the starTimer which calls pC (to update the stars and add more of them).
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
    // This method is used to change which Card is being displayed on the VPanel. It switches to the next card in cl.
    public void nextPanel() {
        cl.next(this);
    }
    // This method is used to play an audio file (filename), and loops it if the parameter loop is true
    public void playMusic(String filename, boolean loop) { // plays background music
        try{
            File musicPath = new File(filename);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if(loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch(Exception e) {
            System.exit(1);
        }
    }
    // This is the JPanel for the homepage, and contains all the methods and variables to draw the homepage.
    class Homepage extends JPanel implements MouseListener, MouseMotionListener {
        boolean hovering = false;
        int width = 65, height = 130; int let_coord1 = 375, let_coord2 = 373;
        long start = System.currentTimeMillis();
        // This is the constructor of the homepage which sets the bg, uploads required images, and starts the timers.
        public Homepage() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            addMouseMotionListener(this);
            StarHandler sh = new StarHandler();
            starTimer = new Timer(10, sh);
            starTimer.start();
            pb_p = new ImageIcon("pb_p.png").getImage();
            pb_l = new ImageIcon("pb_l.png").getImage();
            pb_a = new ImageIcon("pb_a.png").getImage();
            pb_y = new ImageIcon("pb_y.png").getImage();
            pb_pClicked = new ImageIcon("pb_pClicked.png").getImage();
            pb_lClicked = new ImageIcon("pb_lClicked.png").getImage();
            pb_aClicked = new ImageIcon("pb_aClicked.png").getImage();
            pb_yClicked = new ImageIcon("pb_yClicked.png").getImage();
            LetHandler lh = new LetHandler();
            Timer letTimer = new Timer(10 , lh);
            letTimer.start();
            playMusic("theme.wav", true);
        }
        // This is the pC for homepage which draws all the components of the homepage along with updating ArrayLists.
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            long current = System.currentTimeMillis();
            if(current - prev >= 10) {
                int numStars = (int) (Math.random() * 11);
                for (int i = 0; i < numStars; i++) stars.add(new Star());
                prev = current;
            }
            while(removeStars()) {}
            for(Star star : stars) star.drawStar(g);
            g.drawImage(logo, 75, 300, 649, 130, null);
            if(!hovering) {
                g.drawImage(pb_p, let_coord1, 30 + (130-height)/2, width, height, null);
                g.drawImage(pb_l, let_coord1, 165 + (130-height)/2, width, height, null);
                g.drawImage(pb_a, 373, 300, 100, 130, null);
                g.drawImage(pb_y, let_coord2, 435 + (130-height)/2, width, height, null);
            } else {
                g.drawImage(pb_pClicked, let_coord1, 30 + (130-height)/2, width, height, null);
                g.drawImage(pb_lClicked, let_coord1, 165 + (130-height)/2, width, height, null);
                g.drawImage(pb_aClicked, 373, 300, 100, 130, null);
                g.drawImage(pb_yClicked, let_coord2, 435 + (130-height)/2, width, height, null);
            }
        }
        // This handler is used to constantly update the dimensions of the "ply" letters to make an animation.
        class LetHandler implements ActionListener {
            // This is the method called by the letTimer which constantly updates the dimensions of the letters.
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!hovering || Math.abs(width - 65) > 5) {
                    long current = System.currentTimeMillis();
                    width = Math.abs(getWidth(current - start));
                    let_coord1 = 375 + (65 - width) / 2;
                    let_coord2 = 373 + (65 - width) / 2;
                    height = Math.abs(getHeight(current - start));

                }
                repaint();
            }
            // This is a function used to calculate the width of the letters based on how long the hp has been open for.
            public int getWidth(long diff) {
                return (int)(65 * Math.cos(Math.PI * diff / 1000));
            }
            // This is a function used to calculate the height of the letters based on how long the hp has been open for.
            public int getHeight(long diff) {
                return (int)(130 * Math.cos(Math.PI * diff / 1000));
            }
        }
        // This is the method to change the panel being displayed when the user clicks their mouse in the right place.
        @Override
        public void mouseClicked(MouseEvent e) {
            if(hovering) {
                starTimer.stop();
                clip.stop();
                nextPanel();
            }
        }
        // This is the method called whenever the user's mouse is pressed (currently has nothing).
        @Override
        public void mousePressed(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse is released (currently has nothing).
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse enters the homepage (currently has nothing).
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse exits the homepage (currently has nothing).
        @Override
        public void mouseExited(MouseEvent e) {

        }
        // This is the method called whenever the user drags their mouse (currently has nothing).
        @Override
        public void mouseDragged(MouseEvent e) {

        }
        // This is the method that checks if the user is hovering over the play button when they move their mouse.
        @Override
        public void mouseMoved(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getX();
            hovering = mouseX >= 373 && mouseX <= 478 && mouseY >= 30 && mouseY <= 565;
            repaint();
        }
    }
    // This is the JPanel for the level 1, and contains all the methods and variables to draw the first level.
    class Level1 extends JPanel implements MouseListener {
        CardLayout cl2;
        boolean instructions = true;
        Shooter shooter;
        // This is the constructor which sets the layout and calls the run2 method in order to create the JPanels.
        public Level1() {
            cl2 = new CardLayout();
            setLayout(cl2);
            addMouseListener(this);
            run2();
        }
        // This is the run2 method which creates the instructions page and the page for the game.
        public void run2() {
            JPanel instructions = new JPanel();
            instructions.setBackground(Color.BLACK);
            add(instructions, "instructions");
            shooter = new Shooter();
            add(shooter, "shooter");
            SWin  win = new SWin();
            add(win, "win");
            SLose lose = new SLose();
            add(lose, "lose");
        }
        // This is the pC for Level1 which sets the background and grabs the focus.
        public void paintComponent(Graphics g) {
            grabFocus();
            super.paintComponent(g);
        }
        class Shooter extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
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
            public Shooter() {
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
            public void stopAll() {
                eTimer.stop();
                lTimer.stop();
                spawnTimer.stop();
                supplyTimer.stop();
                countdown.stop();
            }
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                while(checkShot()){}
                while(removeLasers()){}
                for(Enemy e : enemies) e.drawEnemy(g);
                for(Laser laser : lasers) laser.drawLaser(g);
                g.setFont(stf);
                g.setColor(GOLD);
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
                        if(allDead) {
                            stopAll();
                            showPanel("win");
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
                            } else if(lives <= 0) {
                                stopAll();
                                showPanel("lose");
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
        class SWin extends JPanel {
            public SWin() {
                setBackground(Color.BLACK);
            }
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You win!", 400, 400);
            }
        }
        class SLose extends JPanel {
            public SLose() {
                setBackground(Color.BLACK);
            }
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You lose!", 400, 400);
            }
        }
        public void showPanel(String name) {
            cl2.show(this, name);
        }
        // This is the method called whenever the mouse is clicked and goes to the next panel in the layout.
        @Override
        public void mouseClicked(MouseEvent e) {
            if(instructions) {
                cl2.next(this);
                shooter.__init__();
                instructions = false;
            }
        }
        // This is the method called whenever the user's mouse is pressed (currently has nothing).
        @Override
        public void mousePressed(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse is released (currently has nothing).
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse enters the homepage (currently has nothing).
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        // This is the method called whenever the user's mouse exits the homepage (currently has nothing).
        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}