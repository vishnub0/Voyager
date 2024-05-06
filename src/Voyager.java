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
    // This method is used to update the font size of STF, my custom Star Trek font given the size param
    public void getSTF(float size) {
        try {
            stf = Font.createFont(Font.TRUETYPE_FONT, new File("stf.ttf")).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(stf);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        // This is the method called whenever the user's mouse is clicked (currently has nothing).
        @Override
        public void mouseClicked(MouseEvent e) {

        }
        // This is the method to change the panel being displayed when the user presses their mouse in the right place.
        @Override
        public void mousePressed(MouseEvent e) {
            if(hovering) {
                starTimer.stop();
                clip.stop();
                nextPanel();
            }
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
        // This is the run2 method which creates the instructions page, page for the game, and the win/lose pages
        public void run2() {
            Instructions instructions = new Instructions();
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
        // This is the class for the instructions which draws the instruction panel for level 1
        class Instructions extends JPanel {
            // This is the constructor of the Instructions page which sets the background to black
            public Instructions() {
                setBackground(Color.BLACK);
            }
            // This is the pC method of Instructions which draws all text + images to teach the user how to play level 1
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                getSTF(45f);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("LEVEL 1: SHOOTER", 250, 50);
                g.drawString("OBJECTIVE: SHOOT DOWN ALL ENEMIES", 100, 150);
                getSTF(35f);
                g.setFont(stf);
                g.drawString("Click on the screen to", 75, 300);
                g.drawString("shoot a laser there", 75, 340);
                g.drawString("Your gun can hold 7 lasers", 50, 400);
                g.drawString("Recharges every second", 50, 440);
                g.drawString("POWERUPS", 500, 300);
                g.drawString("Shoot to activate", 485, 340);
                g.drawImage(new ImageIcon("recharge.png").getImage(), 500, 360, 75, 75, null);
                g.drawImage(new ImageIcon("freeze.png").getImage(), 685, 360, 75, 75, null);
                g.drawString("Recharge", 485, 460);
                g.drawString("Freeze", 670, 460);
                g.drawString("SUPER MOVE", 100, 550);
                getSTF(30f);
                g.setFont(stf);
                g.drawString("Every enemy you kill charges", 85, 600);
                g.drawString("up your super move (kills all", 85, 635);
                g.drawString("enemies on screen).", 85, 670);
                g.drawString("Type 's' to use.", 85, 705);
                getSTF(40f);
                g.drawString("Press mouse to", 600, 700);
                g.drawString("go to game", 600, 740);
                g.setColor(Color.RED);
                g.fillRect(50, 450, 140, 20);
                g.setColor(new Color(136, 8, 8));
                for(int i = 0; i < 10; i++) g.fillRect(450, 725 - 20 * i, 20, 20);
                g.setColor(Color.BLACK);
                ((Graphics2D)g).setStroke(new BasicStroke(2));
                g.drawLine(50, 450, 150, 450);
                g.drawLine(50, 470, 150, 470);
                for(int i = 0; i <= 7; i++) g.drawLine(50 + 20 * i, 450, 50 + 20 * i, 470);
                for(int i = 0; i <= 10; i++) g.drawLine(450, 525 + 20 * i, 470, 525 + 20 * i);
                g.drawImage(new ImageIcon("redskull.png").getImage(), 440, 505, 40, 40, null);
            }
        }
        // This is the class that draws the level 1 game and takes care of the entire shooter
        class Shooter extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
            Timer eTimer;
            eHandler eh;
            Image blaster = new ImageIcon("laser_blaster.png").getImage();
            double rotationRad = 0.0;
            boolean entered = false;
            ArrayList<Laser> lasers = new ArrayList<>();
            lHandler lh;
            Timer lTimer, spawnTimer, supplyTimer, countdown, superTimer, freezeTimer;
            int secondsLeft = 60;
            long prev = System.currentTimeMillis();
            ArrayList<Enemy> enemies = new ArrayList<>();
            int numShots = 7;
            Image heart = new ImageIcon("heart.png").getImage();
            int lives = 3;
            ArrayList<Particle> particles = new ArrayList<>();
            Image recharge, freeze;
            boolean frozen = false;
            int superCharge = 0;
            Image skull, redskull;
            Image bg;
            // This is the constructor of the level 1 game page which sets bg, initializes timers, and loads images
            public Shooter() {
                setBackground(Color.CYAN);
                getSTF(35f);
                addMouseListener(this);
                addMouseMotionListener(this);
                addKeyListener(this);
                eTimer = new Timer(10, new eHandler());
                lTimer = new Timer(1, new lHandler());
                spawnTimer = new Timer(2000, new SpawnHandler());
                supplyTimer = new Timer(1000, new SuppHandler());
                countdown = new Timer(1000, new CountHandler());
                superTimer = new Timer((int)(Math.random() * 5001) + 5000, new SuperHandler());
                freezeTimer = new Timer(3000, new FreezeHandler());
                recharge = new ImageIcon("recharge.png").getImage();
                freeze = new ImageIcon("freeze.png").getImage();
                skull = new ImageIcon("skull.png").getImage();
                redskull = new ImageIcon("redskull.png").getImage();
                bg = new ImageIcon("level1.png").getImage();
            }
            // This method is used to start all the timers when the user switches to the game page
            public void __init__() {
                eTimer.start();
                lTimer.start();
                spawnTimer.start();
                supplyTimer.start();
                countdown.start();
                superTimer.start();
            }
            // This method is used to stop all the timers once the user is finished with playing the game
            public void stopAll() {
                eTimer.stop();
                lTimer.stop();
                spawnTimer.stop();
                supplyTimer.stop();
                countdown.stop();
            }
            // This is the pC method of Shooter which draws all the enemies, lasers, powerups, etc. for the game
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(bg, -5, 60, 765, 800, null);
                while(checkShot()){}
                while(removeLasers()){}
                while(removeParticles()){}
                for(Enemy e : enemies) e.drawEnemy(g);
                for(Laser laser : lasers) laser.drawLaser(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("" + secondsLeft, 375, 35);
                for(int i = 0; i < lives*25; i += 25) g.drawImage(heart, 600 + i, 10, 20, 20, null);
                g.setColor(Color.RED);
                g.fillRect(10, 10, 20 * numShots, 20);
                g.setColor(new Color(136, 8, 8));
                for(int i = 0; i < superCharge; i++) g.fillRect(765, 350 - 30 * i, 30, 30);
                if(superCharge == 10) g.drawImage(redskull, 754, 30, 52, 52, null);
                else g.drawImage(skull, 754, 30, 52, 52, null);
                g.setColor(Color.BLACK);
                ((Graphics2D)g).setStroke(new BasicStroke(2));
                g.drawLine(10, 10, 10 + 20 * numShots, 10);
                g.drawLine(10, 30, 10 + 20 * numShots, 30);
                for(int i = 0; i <= numShots; i++) g.drawLine(10 + 20 * i, 10, 10 + 20 * i, 30);
                g.drawLine(765, 80, 765, 380);
                g.drawLine(795, 80, 795, 380);
                for(int i = 0; i <= 10; i++) g.drawLine(765, 80 + 30 * i, 795, 80 + 30 * i);
                ((Graphics2D)g).setStroke(new BasicStroke(5));
                g.drawLine(0, 60, 760, 60);
                g.drawLine(760, 60, 760, 800);
                for(Particle p : particles) {
                    if(p.type == 2) {
                        g.drawImage(recharge, (int)p.posX - 20, (int)p.posY - 20, 40, 40, null);
                    } else if(p.type == 3) {
                        g.drawImage(freeze, (int)p.posX - 20, (int)p.posY - 20, 40, 40, null);
                    }
                }
                try {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.rotate(rotationRad+Math.PI/2, 400, 700);
                    g2d.drawImage(blaster, 375, 700, 50, 50, null);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // This method is used to check if a laser hits an enemy or particle, in which case it is deleted and the respective action is performed
            public boolean checkShot() {
                for(Laser l : lasers) {
                    Rectangle rect = new Rectangle((int)l.x, (int)l.y, 5, 5);
                    for(Enemy e : enemies) {
                        if(rect.intersects(new Rectangle(e.x_coord, e.y_coord, 75, 100)) && !e.shot && e.y_coord < 650) {
                            e.shot = true;
                            lasers.remove(l);
                            if(superCharge < 10) superCharge++;
                            /* blood particles, currently not being used
                            for(int i = 0; i < (int)(Math.random()*101) + 50; i++) {
                                boolean bloody = (int)(Math.random()*10) + 1 > 3;
                                particles.add(new Particle(e, bloody));
                            }*/
                            return true;
                        }
                    }
                    for(Particle p : particles) {
                        if(p.isColliding(l.x, l.y)) {
                            if(p.type == 2) numShots = 7;
                            else if(p.type == 3) {
                                frozen = true;
                                freezeTimer.start();
                            }
                            particles.remove(p);
                            lasers.remove(l);
                            return true;
                        }
                    }
                }
                return false;
            }
            // This handler class is called whenever the user freezes the enemies, unfreezes enemies after 3 seconds
            class FreezeHandler implements ActionListener {
                // This is the method which "thaws" all the enemies once after 3 seconds of being frozen, it then stops the timer and calls pC
                @Override
                public void actionPerformed(ActionEvent e) {
                    frozen = false;
                    freezeTimer.stop();
                    repaint();
                }
            }
            // This handler class generates the recharge and freeze powerups every 5-10 seconds
            class SuperHandler implements ActionListener {
                // This method is used to add new powerups to the ArrayList and randomly reinitialize the superTimer
                @Override
                public void actionPerformed(ActionEvent e) {
                    particles.add(new Particle());
                    superTimer.stop();
                    superTimer = new Timer((int)(Math.random() * 5001) + 5000, new SuperHandler());
                    superTimer.start();
                    repaint();
                }
            }
            // This handler class is responsible for spawning new enemies (unless they are frozen)
            class SpawnHandler implements ActionListener {
                // This method is used to add new enemies to the ArrayList and update the screen via pC
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!frozen) enemies.add(new Enemy());
                    repaint();
                }
            }
            // This handler class is responsible for recharging the user's lasers (1 laser/second)
            class SuppHandler implements ActionListener {
                // This method is used to recharge the laser blaster of the user every second
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(numShots < 7) numShots++;
                    repaint();
                }
            }
            // This handler class is responsible for updating the countdown every second and changing the rate at which enemies spawn
            class CountHandler implements ActionListener {
                // This method is used to update the seconds left for the user and helps make the game more difficult as the user progresses
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
                        stopAll();
                        showPanel("win");
                    }
                    repaint();
                }
            }
            // This is the keyTyped method which is called whenever the user types a key (currently empty)
            @Override
            public void keyTyped(KeyEvent e) {

            }
            // This is the keyPressed method which checks if the user used their super move if the pressed s
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                if(keycode == 83 && superCharge == 10) {
                    for(Enemy enemy : enemies) {
                        enemy.shot = true;
                    }
                    superCharge = 0;
                    repaint();
                }
            }
            // This is the keyReleased method which is called whenever the user releases a key (currently empty)
            @Override
            public void keyReleased(KeyEvent e) {

            }
            // This is the mouseClicked method which is called whenever the user clicks their mouse (currently empty)
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            // This is the mousePressed method which is creates lasers whenever the user presses their mouse
            @Override
            public void mousePressed(MouseEvent e) {
                if(numShots > 0) {
                    lasers.add(new Laser(Math.toDegrees(rotationRad)));
                    numShots--;
                }
                repaint();
            }
            // This is the mouseReleased method which is called whenever the user releases their mouse (currently empty)
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            // This is the mouseEntered method which is called whenever the user enters the screen, updating a variable
            @Override
            public void mouseEntered(MouseEvent e) {
                entered = true;
            }
            // This is the mouseExited method which is called whenever the user exits the screen, updating a variable
            @Override
            public void mouseExited(MouseEvent e) {
                entered = false;
            }
            // This is the mouseDragged method which is called whenever the user drags their mouse (currently empty)
            @Override
            public void mouseDragged(MouseEvent e) {

            }
            // This is the mouseMoved method which updates the rotationRad var to determine where the laser will be fired
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
            // This is the removeLasers method which is removes lasers that are off of the screen, called in a while(){}
            public boolean removeLasers() {
                for(Laser l : lasers) {
                    if(l.x < 0 || l.x > 800 || l.y < 0 || l.y > 800) {
                        lasers.remove(l);
                        return true;
                    }
                }
                return false;
            }
            // This method is used to remove all particles that are off of the screen, called in a while(){}
            public boolean removeParticles() {
                for(Particle p : particles) {
                    if(p.posX < -20 || p.posX > 820 || p.posY < -20 || p.posY > 820) {
                        particles.remove(p);
                        return true;
                    }
                }
                return false;
            }
            // This handler class is responsible for updating the locations of the enemies and the particles every 10 milliseconds
            class eHandler implements ActionListener {
                // This method is used to update the location of the enemies and the particles, also updating which sprite is displayed
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!frozen) {
                        for (Enemy e2 : enemies) {
                            if (e2.y_coord < 650) {
                                if(secondsLeft < 20) e2.y_coord += 1.3;
                                else e2.y_coord++;
                            }
                            if (e2.y_coord >= 650 && !e2.shot) {
                                if (lives > 0 && !e2.alreadyCounted) {
                                    lives--;
                                    e2.alreadyCounted = true;
                                } else if (lives <= 0) {
                                    stopAll();
                                    showPanel("lose");
                                }
                            }
                            e2.check();
                        }
                        for (Particle particle : particles) {
                            particle.tick(0.01);
                        }
                    }
                    repaint();
                }
            }
            // This handler class is responsible for updating the coordinates of the lasers using the unit circle
            class lHandler implements ActionListener {
                // This method is responsible for updating the location of the laser using trig and delta time
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
            // This is the enemy class which contains all of the information required to create and draw an enemy
            class Enemy {
                boolean shot = false;
                boolean alreadyCounted = false;
                int x_coord = (int)(Math.random()*686) + 25;
                int y_coord = 60;
                static Image[] imgs = new Image[5];
                long timeShot = -1;
                int current = 0;
                // This constructor is used to load all the sprites for the enemy and stores them in an array
                public Enemy() {
                    for(int i = 1; i < 6; i++) imgs[i-1] = new ImageIcon("Sprite" + i + ".png").getImage();
                }
                // This method is used to draw the enemy on the screen
                public void drawEnemy(Graphics g) {
                    g.drawImage(imgs[current], x_coord, y_coord, 75, 100, null);
                    if(frozen) {
                        g.setColor(new Color(200, 255, 255, 153));
                        g.fillRect(x_coord, y_coord, 75, 100);
                    }
                }
                // This method is used to determine which sprite should be displayed based on if they have been shot
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
            // This is the class for the laser which contains all variables and methods required to draw the laser
            class Laser {
                double rot;
                double x = 400;
                double y = 700;
                // This constructor is used to create a laser and store the rotation in radians
                public Laser(double rotRad) {
                    rot = rotRad;
                }
                // this method is used to draw the laser on the screen
                public void drawLaser(Graphics g) {
                    ((Graphics2D)g).setStroke(new BasicStroke(10));
                    g.setColor(Color.RED);
                    g.drawLine(400, 700, (int)x, (int)y);
                }
            }
        }
        // This class extends JPanel and the user is transported here if they beat level 1
        class SWin extends JPanel {
            // This constructor sets the bg of the win panel to be black
            public SWin() {
                setBackground(Color.BLACK);
            }
            // This method displays the text "You win!" on the win panel
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You win!", 400, 400);
            }
        }
        // This class extends JPanel and the user is transported here if they lose on level 1
        class SLose extends JPanel {
            // This constructor sets the bg of the lose panel to be black
            public SLose() {
                setBackground(Color.BLACK);
            }
            // This method displays the text "You lose!" on the lose panel
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You lose!", 400, 400);
            }
        }
        // This method shows the respective panel in the CardLayout of level 1
        public void showPanel(String name) {
            cl2.show(this, name);
        }
        // This is the method called whenever the user's mouse is clicked (currently has nothing).
        @Override
        public void mouseClicked(MouseEvent e) {

        }
        // This is the method called whenever the mouse is pressed and goes to the next panel in the layout.
        @Override
        public void mousePressed(MouseEvent e) {
            if(instructions) {
                cl2.next(this);
                shooter.__init__();
                instructions = false;
            }
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
    // This class contains all the information needed to create particles such as powerups, and later on, blood and guts
    class Particle {
        double posX, posY, velX, velY;
        int type;
        // type = 0: blood, type = 1: organ, type = 2: recharge, type = 3: freeze
        boolean gravity;
        // This constructor is used to randomly initialize the location and type of particle
        public Particle() {
            posX = (int)(Math.random() * 761) + 20;
            posY = 30;
            velX = 0;
            velY = 10;
            gravity = true;
            type = (int)(Math.random() * 2) + 2;
        }
        // This method is used to check if anything collides with the particle
        public boolean isColliding(double positionX, double positionY) {
            if(type == 0 || type == 1) return false;
            else if(Math.sqrt(Math.pow(posX - positionX, 2) + Math.pow(posY - positionY, 2)) <= 15) return true;
            return false;
        }
        // This method is used to update the x and y coordinates of the particle, taking gravity into account
        public void tick(double dt) {
            posX += velX * dt;
            posY += velX * dt;
            if(gravity) posY += 1;
        }
    }
}