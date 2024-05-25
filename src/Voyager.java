/*
Vishnu Bharadwaj
4/28/24
Voyager.java
This is my game, Voyager. It is a game based off of the Star Trek Original Series that uses all of my java knowledge in
a fun game. Currently, I have finished level 2 and am working on level 3 + JoeTTS.
*/

import javax.swing.*; import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import java.util.Random;
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
    final Color GOLD2 = new Color(238, 167, 59);
    final Color SILVER = new Color(192,192,192);
    final Color AZURE = new Color(55, 198, 255);
    ArrayList<Star> stars = new ArrayList<>();
    Timer starTimer;
    Image logo = new ImageIcon("voyager-logo.png").getImage();
    Image pb_p, pb_l, pb_a, pb_y;
    Image pb_pClicked, pb_lClicked, pb_aClicked, pb_yClicked;
    long prev = System.currentTimeMillis();
    Clip clip;
    Homepage homepage;
    Level1 level1;
    Image sadJoe;
    Cutscene1 cutscene1;
    public static SaveFile sf = SaveFile.load();
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
        //Scene1 sc1 = new Scene1();
        //add(sc1);
        homepage = new Homepage();
        homepage.setBackground(Color.BLACK);
        add(homepage, "homepage");
        Levels levels = new Levels();
        add(levels, "levels");
        cutscene1 = new Cutscene1();
        add(cutscene1, "cutscene1");
        level1 = new Level1();
        add(level1, "l1");
        Level2 level2 = new Level2();
        add(level2, "l2");
        sadJoe = new ImageIcon("sadjoe.png").getImage();
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
    // This method is used by the levels panel to show a certain panel in the CardLayout
    public void showPanel(String name) {
        cl.show(this, name);
    }
    // This method is used to play an audio file (filename), and loops it if the parameter loop is true
    public void playMusic(String filename, boolean loop) {
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
    // This is the JPanel for the first scene (Joe Bob Kirk's monologue)
    class Scene1 extends JPanel {
        long creation = System.currentTimeMillis();
        Timer repTimer;
        // This is the constructor of the first scene which plays the monologue
        public Scene1() {
            setBackground(Color.BLACK);
            playMusic("finalfrontier.wav", false);
            repTimer = new Timer(10, i -> repaint());
            repTimer.start();
        }
        // This is the pC of the first scene which displays the text that JoeTTS is saying
        public void paintComponent(Graphics g) {
            long current = System.currentTimeMillis();
            getSTF(25f);
            g.setFont(stf);
            g.setColor(GOLD);
            g.drawString("Space, the final frontier.", 150, 400);
            g.drawString("These are the voyages of the starship enterprise.", 150, 430);
            g.drawString("Its continuing mission, to explore strange new worlds.", 150, 460);
            g.drawString("To seek out new life and new civilizations.", 150, 490);
            g.drawString("- Captain Kirk", 275, 600);
            getSTF(35f);
            g.setFont(stf);
            g.drawString("To boldly go where no man has gone before.", 150, 550);
            if(current - creation > 21000) {
                repTimer.stop();
                nextPanel();
            }
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
            //playMusic("theme.wav", true);
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
                //clip.stop();
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
    // This is the JPanel used to select the level to play, contains everything required to draw it
    class Levels extends JPanel implements MouseListener, MouseMotionListener {
        Image lock = new ImageIcon("lock.png").getImage();
        boolean hovering1 = false, hovering2 = false;
        // Constructor used to set background of level page and add mouse listener
        public Levels() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            addMouseMotionListener(this);
        }
        // pC used to draw all the text and level options on the screen
        public void paintComponent(Graphics g) {
            grabFocus();
            super.paintComponent(g);
            getSTF(125f);
            g.setFont(stf);
            g.setColor(GOLD);
            g.drawString("LEVELS", 250, 130);
            int level = sf.level;
            if (hovering1) {
                g.setColor(AZURE);
                g.fillRect(145, 295, 160, 160);
            } else {
                g.setColor(SILVER);
                g.fillRect(150, 300, 150, 150);
            }
            g.setColor(GOLD);
            g.drawString("1", 200, 430);
            if (hovering2) {
                g.setColor(AZURE);
                g.fillRect(495, 295, 160, 160);
            } else {
                g.setColor(SILVER);
                g.fillRect(500, 300, 150, 150);
            }
            g.setColor(GOLD);
            g.drawString("2", 550, 430);
            if(level < 2) g.drawImage(lock, 500, 300, 150, 150, null);
        }
        // this method is called whenever the user clicks their mouse (currently empty)
        @Override
        public void mouseClicked(MouseEvent e) {

        }
        // this method is used to check if the user clicks on one of the buttons on the levels page and transports them
        // to that panel
        @Override
        public void mousePressed(MouseEvent e) {
            int x_coord = e.getX();
            int y_coord = e.getY();
            if(x_coord >= 150 && x_coord <= 300 && y_coord >= 300 && y_coord <= 450) {
                showPanel("cutscene1");
                cutscene1.scene01.textTimer.start();
                hovering1 = false;
            } else if(x_coord >= 500 && x_coord <= 650 && y_coord >= 300 && y_coord <= 450 && sf.level >= 2) {
                showPanel("l2");
                hovering2 = false;
            }
        }
        // this method is called whenever the user releases their mouse (currently empty)
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        // this method is called whenever the user's mouse enters the screen (currently empty)
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        // this method is called whenever the user's mouse exits the screen (currently empty)
        @Override
        public void mouseExited(MouseEvent e) {

        }
        // this method is called whenever the user drags their mouse (currently empty)
        @Override
        public void mouseDragged(MouseEvent e) {

        }
        // this method is used to check if the user is hovering over a button and if they are the text changes color
        @Override
        public void mouseMoved(MouseEvent e) {
            int x_coord = e.getX();
            int y_coord = e.getY();
            if(x_coord >= 150 && x_coord <= 300 && y_coord >= 300 && y_coord <= 450) hovering1 = true;
            else hovering1 = false;
            if(x_coord >= 500 && x_coord <= 650 && y_coord >= 300 && y_coord <= 450 && sf.level >= 2) hovering2 = true;
            else hovering2 = false;
        }
    }
    // This is the JPanel for the first cutscene of the game
    class Cutscene1 extends JPanel {
        CardLayout clc1;
        Scene01 scene01;
        Scene02 scene02;
        Scene03 scene03;
        Scene04 scene04;
        Scene05 scene05;
        // constructor which creates all of the
        public Cutscene1() {
            clc1 = new CardLayout();
            setLayout(clc1);
            scene01 = new Scene01();
            add(scene01, "sc1");
            scene02 = new Scene02();
            add(scene02, "sc2");
            scene03 = new Scene03();
            add(scene03, "sc3");
            scene04 = new Scene04();
            add(scene04, "sc4");
            scene05 = new Scene05();
            add(scene05, "sc5");
        }
        // method to go to the next panel in the cardlayout.
        public void nextPanel(int scene) {
            clc1.next(this);
            if(scene == 2) scene02.textTimer.start();
            else if(scene == 3) scene03.textTimer.start();
            else if(scene == 4) scene04.textTimer.start();
            else if(scene == 5) scene05.textTimer.start();
        }
        // class for the first scene in the cutscene introducing the Gorn.
        class Scene01 extends JPanel {
            String text = "We have intel that the Gorn are delivering more\nresources to their soldiers. If this continues,\nwe will lose the war.     ";
            String displayedText = "";
            JTextArea bubble = new JTextArea();
            Timer textTimer;
            // constructor that creates the timer needed to display the text
            public Scene01() {
                setLayout(null);
                setBackground(Color.LIGHT_GRAY);
                bubble.setBounds(10, 10, 300, 250);
                add(bubble);
                textTimer = new Timer(50, e -> {
                    if(displayedText.length() < text.length()) {
                        displayedText = text.substring(0, displayedText.length() + 1);
                        bubble.setText(displayedText);
                    } else {
                        nextPanel(2);
                        textTimer.stop();
                    }
                    repaint();
                });
            }
            // paintComponent which draws the image of the briefcase on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(new ImageIcon("briefcase.png").getImage(), 350, 100, 430, 314, null);
            }
        }
        // class for the second scene where Kirk gives Spock the order.
        class Scene02 extends JPanel {
            String text = "That is why I have assigned you, Spock,\nthe mission of stopping the\nreinforcements.     ";
            String displayedText = "";
            JTextArea bubble = new JTextArea();
            Timer textTimer;
            // constructor that creates the timer needed to display the text
            public Scene02() {
                setLayout(null);
                setBackground(Color.LIGHT_GRAY);
                bubble.setBounds(10, 10, 270, 200);
                add(bubble);
                textTimer = new Timer(50, e -> {
                    if(displayedText.length() < text.length()) {
                        displayedText = text.substring(0, displayedText.length() + 1);
                        bubble.setText(displayedText);
                    } else {
                        nextPanel(3);
                        textTimer.stop();
                    }
                    repaint();
                });
            }
            // paintComponent which draws the image of captain kirk on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(new ImageIcon("kirk1.jpeg").getImage(), 0, 0, 800, 800, null);
            }
        }
        // class for the third scene where Spock acknowledges the mission.
        class Scene03 extends JPanel {
            String text = "Yes sir. I will intercept\nthem before they are able to\ntouch the ground.     ";
            String displayedText = "";
            JTextArea bubble = new JTextArea();
            Timer textTimer;
            // constructor that creates the timer needed to display the text
            public Scene03() {
                setLayout(null);
                setBackground(Color.LIGHT_GRAY);
                bubble.setBounds(10, 10, 230, 230);
                add(bubble);
                textTimer = new Timer(50, e -> {
                    if(displayedText.length() < text.length()) {
                        displayedText = text.substring(0, displayedText.length() + 1);
                        bubble.setText(displayedText);
                    } else {
                        nextPanel(4);
                        textTimer.stop();
                    }
                    repaint();
                });
            }
            // paintComponent which draws the image of spock on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(new ImageIcon("spocksalute.jpeg").getImage(), 0, 0, 800, 800, null);
            }
        }
        // class for the fourth scene where the blimp is shown
        class Scene04 extends JPanel {
            String text = "In the middle of nowhere...\nThe Gorn Blimp     ";
            String displayedText = "";
            JTextArea bubble = new JTextArea();
            Timer textTimer;
            // constructor that creates the timer needed to display the text
            public Scene04() {
                setLayout(null);
                setBackground(Color.LIGHT_GRAY);
                bubble.setBounds(10, 210, 230, 70);
                add(bubble);
                textTimer = new Timer(50, e -> {
                    if(displayedText.length() < text.length()) {
                        displayedText = text.substring(0, displayedText.length() + 1);
                        bubble.setText(displayedText);
                    } else {
                        nextPanel(5);
                        textTimer.stop();
                    }
                    repaint();
                });
            }
            // paintComponent which draws the image of the blimp on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(new ImageIcon("level1.png").getImage(), 0, 0, 800, 800, null);
            }
        }
        // class for the fifth scene where Spock is looking through his binoculars.
        class Scene05 extends JPanel {
            String text = "Showtime.                              ";
            String displayedText = "";
            JTextArea bubble = new JTextArea();
            Timer textTimer;
            // constructor that creates the timer needed to display the text
            public Scene05() {
                setLayout(null);
                setBackground(Color.LIGHT_GRAY);
                bubble.setBounds(560, 10, 230, 50);
                add(bubble);
                textTimer = new Timer(50, e -> {
                    if(displayedText.length() < text.length()) {
                        displayedText = text.substring(0, displayedText.length() + 1);
                        bubble.setText(displayedText);
                    } else {
                        showPanel("l1");
                        textTimer.stop();
                    }
                    repaint();
                });
            }
            // paintComponent which draws the image of spock on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(new ImageIcon("spockbinoc.jpeg").getImage(), 0, 0, 800, 800, null);
            }
        }
    }
    // This is the JPanel for the level 1, and contains all the methods and variables to draw the first level.
    class Level1 extends JPanel implements MouseListener, MouseMotionListener {
        CardLayout cl2;
        boolean instructions = true;
        Shooter shooter;
        boolean hovering = false;
        // This is the constructor which sets the layout and calls the run2 method in order to create the JPanels.
        public Level1() {
            cl2 = new CardLayout();
            setLayout(cl2);
            addMouseListener(this);
            addMouseMotionListener(this);
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
                g.setFont(stf);
                if(hovering) {
                    g.setColor(AZURE);
                    g.fillRect(605, 645, 160, 70);
                } else {
                    g.setColor(SILVER);
                    g.fillRect(610, 650, 150, 60);
                }
                g.setColor(GOLD);
                g.drawString("CONTINUE", 620, 700);
                g.setColor(Color.RED);
                g.fillRect(50, 450, 140, 20);
                g.setColor(new Color(136, 8, 8));
                for(int i = 0; i < 10; i++) g.fillRect(450, 725 - 20 * i, 20, 20);
                g.setColor(Color.BLACK);
                ((Graphics2D)g).setStroke(new BasicStroke(2));
                g.drawRect(50, 450, 140, 20);
                for(int i = 0; i <= 7; i++) g.drawLine(50 + 20 * i, 450, 50 + 20 * i, 470);
                for(int i = 0; i <= 10; i++) g.drawLine(450, 525 + 20 * i, 470, 525 + 20 * i);
                g.drawImage(new ImageIcon("redskull.png").getImage(), 440, 505, 40, 40, null);
            }
        }
        // This is the class that draws the level 1 game and takes care of the entire shooter
        class Shooter extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
            Timer eTimer;
            Image blaster = new ImageIcon("laser_blaster.png").getImage();
            double rotationRad = 0.0;
            boolean entered = false;
            ArrayList<Laser> lasers = new ArrayList<>();
            Timer lTimer, spawnTimer, supplyTimer, countdown, superTimer, freezeTimer;
            int secondsLeft = 60;
            long prev = System.currentTimeMillis();
            ArrayList<Enemy> enemies = new ArrayList<>();
            int numShots = 7;
            Image heart = new ImageIcon("heart.png").getImage();
            int lives = 3;
            ArrayList<Particle> particles = new ArrayList<>();
            Image recharge, freeze;
            Image eyeball, bloodyheart, bloodyskull;
            boolean frozen = false;
            int superCharge = 0;
            Image skull, redskull;
            Image bg;
            Image bgbg;
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
                eyeball = new ImageIcon("eyeball.png").getImage();
                bloodyheart = new ImageIcon("bloodyheart.png").getImage();
                bloodyskull = new ImageIcon("bloodyskull.png").getImage();
                bgbg = new ImageIcon("level1bg.jpg").getImage();
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
                g.drawImage(bgbg, 0, 0, 1375, 915, null);
                g.drawImage(bg, -5, 60, 765, 800, null);
                while(checkShot()){}
                while(removeLasers()){}
                while(removeParticles()){}
                while(removeEnemies()){}
                for(Enemy e : enemies) e.drawEnemy(g);
                for (Particle particle : particles) {
                    if(particle.type == 1.1) {
                        g.drawImage(eyeball, (int)particle.posX, (int)particle.posY, 30, 30, null);
                    } else if(particle.type == 1.2) {
                        g.drawImage(bloodyheart, (int)particle.posX, (int)particle.posY, 30, 30, null);
                    } else if(particle.type == 1.3) {
                        g.drawImage(bloodyskull, (int)particle.posX, (int)particle.posY, 30, 30, null);
                    } else if(particle.type == 0) {
                        g.setColor(new Color(136, 8, 8));
                        if(particle.velY > 0) g.fillOval((int)particle.posX, (int)particle.posY, 5, 5);
                        else g.fillOval((int)particle.posX-5, (int)particle.posY-5, 10, 10);
                    }
                }
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
                    g2d.drawImage(blaster, 360, 700, 80, 80, null);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // This method is used to check if a laser hits an enemy or particle, in which case it is deleted and the
            // respective action is performed
            public boolean checkShot() {
                for(Laser l : lasers) {
                    Rectangle rect = new Rectangle((int)l.x, (int)l.y, 5, 5);
                    for(Enemy e : enemies) {
                        if(rect.intersects(new Rectangle(e.x_coord, e.y_coord, 75, 100)) && !e.shot && e.y_coord < 650) {
                            e.shot = true;
                            lasers.remove(l);
                            enemies.remove(e);
                            if(superCharge < 10) superCharge++;
                            Random rand = new Random();
                            for(int i = 0; i < (int)(Math.random()*101) + 50; i++) {
                                boolean bloody = Math.random() > 0.01;
                                double vel1 = rand.nextDouble(20) - 10;
                                double vel2 = rand.nextDouble(20) - 10;
                                int x = (int)(Math.random() * 21) + e.x_coord + 40;
                                int y = (int)(Math.random() * 21) + e.y_coord + 40;
                                particles.add(new Particle(x, y, vel1, vel2, bloody));
                            }
                            return true;
                        }
                    }
                    for(Particle p : particles) {
                        if(p.isColliding(l.x, l.y) && (p.type == 2 || p.type == 3)) {
                            if(p.type == 2) numShots = 7;
                            else {
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
                // This is the method which "thaws" all the enemies once after 3 seconds of being frozen, it then stops
                // the timer and calls pC
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
                        sf.level = 2;
                        sf.save();
                    }
                    repaint();
                }
            }
            // This is the keyTyped method which is called whenever the user types a key (currently empty)
            @Override
            public void keyTyped(KeyEvent e) {

            }
            // This is the keyPressed method which checks if the user used their super move if they pressed 's'
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                if(keycode == 83 && superCharge == 10) {
                    for(Enemy enemy : enemies) {
                        enemy.shot = true;
                        Random rand = new Random();
                        for(int i = 0; i < (int)(Math.random()*101) + 50; i++) {
                            boolean bloody = Math.random() > 0.01;
                            double vel1 = rand.nextDouble(20) - 10;
                            double vel2 = rand.nextDouble(20) - 10;
                            int x = (int)(Math.random() * 21) + enemy.x_coord + 40;
                            int y = (int)(Math.random() * 21) + enemy.y_coord + 40;
                            particles.add(new Particle(x, y, vel1, vel2, bloody));
                        }
                    }
                    superCharge = 0;
                    enemies = new ArrayList<>();
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
            // This is the mouseMoved method which updates the rotationRad var to determine where the laser is fired
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
                long current = System.currentTimeMillis();
                for(Particle p : particles) {
                    if(p.type <= 1.3 && p.posY >= p.splatterY) {
                        particles.remove(p);
                        particles.add(new Particle(p.posX, p.posY, p.type));
                        return true;
                    }
                    if(p.posX < -20 || p.posX > 820 || p.posY < -20 || p.posY > 820 || p.type <= 1 && current - p.creation > 15000) {
                        particles.remove(p);
                        return true;
                    }
                }
                return false;
            }
            // method used to remove enemies that have been on the screen for longer than 10 seconds
            public boolean removeEnemies() {
                for (Enemy enemy : enemies) {
                    if(enemy.shot && enemy.timeShot > 0 && System.currentTimeMillis() - enemy.timeShot > 10000) {
                        enemies.remove(enemy);
                        return true;
                    }
                }
                return false;
            }
            // This handler class is responsible for updating the locations of the enemies and the particles every 10
            // milliseconds
            class eHandler implements ActionListener {
                // This method is used to update the location of the enemies and the particles, also updating which
                // sprite is displayed
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
            // This is the enemy class which contains all the information required to create and draw an enemy
            class Enemy {
                boolean shot = false;
                boolean alreadyCounted = false;
                int x_coord = (int)(Math.random()*576) + 25;
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
                    g.drawImage(imgs[current], x_coord, y_coord, 100, 100, null);
                    if(frozen) {
                        g.setColor(new Color(200, 255, 255, 153));
                        g.fillRect(x_coord, y_coord, 100, 100);
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
        class SWin extends JPanel implements KeyListener {
            // This constructor sets the bg of the win panel to be black
            public SWin() {
                setBackground(Color.BLACK);
                addKeyListener(this);
            }
            // This method displays the text "You win!" on the win panel
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You win!", 400, 400);
                getSTF(35f);
                g.drawString("PRESS SPACE TO CONTINUE", 200, 700);
            }
            // this method is called whenever the user types a key on the SWin panel (currently empty)
            @Override
            public void keyTyped(KeyEvent e) {

            }
            // this method is called whenever the user presses a key, and if they press the space key it goes to the
            // level 2 panel
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                if(keycode == 32) nextPanel();
            }
            // this method is called whenever the user releases a key on the SWin panel (currently empty)
            @Override
            public void keyReleased(KeyEvent e) {

            }
        }
        // This class extends JPanel and the user is transported here if they lose on level 1
        class SLose extends JPanel implements MouseListener, MouseMotionListener {
            boolean hoveringL = false;
            // This constructor sets the bg of the lose panel to be black
            public SLose() {
                setBackground(Color.BLACK);
                addMouseListener(this);
                addMouseMotionListener(this);
            }
            // This method displays the text "You lose!" on the lose panel
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(sadJoe, 100, 200, 600, 600, null);
                g.setFont(stf);
                if(hoveringL) {
                    g.setColor(AZURE);
                    g.fillRect(365, 650, 120, 65);
                } else {
                    g.setColor(SILVER);
                    g.fillRect(370, 655, 110, 55);
                }
                g.setColor(GOLD);
                g.drawString("You lose!", 200, 150);
                g.drawString("Retry", 380, 700);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(hoveringL) {
                    shooter.rotationRad = 0.0;
                    shooter.lasers = new ArrayList<>();
                    shooter.superCharge = 0;
                    shooter.enemies = new ArrayList<>();
                    shooter.particles = new ArrayList<>();
                    shooter.numShots = 7;
                    shooter.secondsLeft = 60;
                    shooter.frozen = false;
                    shooter.lives = 3;
                    shooter.entered = false;
                    shooter.prev = System.currentTimeMillis();
                    shooter.__init__();
                    showPanel("shooter");
                    hoveringL = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if(mouseX >= 370 && mouseX <= 480 && mouseY >= 655 && mouseY <= 710) hoveringL = true;
                else hoveringL = false;
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
            int x_coord = e.getX();
            int y_coord = e.getY();
            if(instructions && x_coord >= 660 && x_coord <= 760 && y_coord >= 650 && y_coord <= 710) {
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
        // this method is called whenever the user drags their mouse on the screen (currently not being used)
        @Override
        public void mouseDragged(MouseEvent e) {

        }
        // this method is called in order to check if the user moves their mouse over the next button (color change)
        @Override
        public void mouseMoved(MouseEvent e) {
            int x_coord = e.getX();
            int y_coord = e.getY();
            // g.fillRect(610, 650, 150, 60);
            if(instructions && x_coord >= 610 && x_coord <= 760 && y_coord >= 650 && y_coord <= 710) hovering = true;
            else hovering = false;
            repaint();
        }
    }
    // This is the JPanel for the second level and contains all the methods and variables to draw the level.
    class Level2 extends JPanel implements MouseListener, MouseMotionListener {
        CardLayout cl3;
        SpaceBattle sb;
        boolean instructions = true;
        boolean hovering = false;
        // this is the constructor for the level 2 panel which sets the layout, adds listeners, and starts star timer
        public Level2() {
            cl3 = new CardLayout();
            setLayout(cl3);
            addMouseListener(this);
            addMouseMotionListener(this);
            run3();
            StarHandler sh = new StarHandler();
            starTimer = new Timer(10, sh);
            starTimer.start();
        }
        // this method is used to create JPanels for the instructions page, game page, and winning/losing page
        public void run3() {
            Instructions is = new Instructions();
            sb = new SpaceBattle();
            WinPanel wp = new WinPanel();
            LosePanel lp = new LosePanel();
            add(is, "instructions");
            add(sb, "sb");
            add(wp, "win");
            add(lp, "lose");
        }
        // this is the pC of the level 2 class which sets the background and grabs the focus of the user
        public void paintComponent(Graphics g) {
            grabFocus();
            super.paintComponent(g);
        }

        // This is the instructions panel which teaches the user how to steer their ship and fire torpedoes.
        class Instructions extends JPanel {
            // This is the constructor of the instructions panel which sets the background
            public Instructions() {
                setBackground(Color.BLACK);
            }
            // this is the pC of the instructions panel which draws instructions for playing level 2
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
                getSTF(40f);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("INSTRUCTIONS: SPACE FIGHT", 225, 45);
                g.drawString("OBJECTIVE: SHOOT DOWN ALL KLINGONS", 100, 145);
                getSTF(35f);
                g.setFont(stf);
                g.drawString("Use up arrow to move", 100, 220);
                g.drawString("Use up right/left to rotate", 100, 260);
                g.drawString("Use up space to brake", 100, 300);
                g.drawString("Use up 's' to shoot torpedoes", 100, 340);
                g.drawString("Use torpedoes to destroy Klingon ships", 100, 400);
                g.drawString("They can withstand more than one", 100, 440);
                g.drawString("Avoid enemy torpedoes!", 100, 500);
                getSTF(40f);
                g.setFont(stf);
                if(hovering) {
                    g.setColor(AZURE);
                    g.fillRect(605, 645, 160, 70);
                } else {
                    g.setColor(SILVER);
                    g.fillRect(610, 650, 150, 60);
                }
                g.setColor(GOLD);
                g.drawString("CONTINUE", 620, 700);
                g.drawImage(new ImageIcon("spaceship.png").getImage(), 600, 180, 60, 120, null);
                g.drawImage(new ImageIcon("torpedo.png").getImage(), 600, 320, 80, 16, null);
                g.drawImage(new ImageIcon("klingonship.png").getImage(), 600, 480, 140, 80, null);
            }
        }
        // This is the class for the actual space battle
        class SpaceBattle extends JPanel implements KeyListener {
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
            int playerHealth = 250;
            Image explosion;
            int stage = 1;
            int enemiesLeft = 6;
            // This constructor initializes timers and loads images
            public SpaceBattle() {
                setBackground(Color.BLACK);
                addKeyListener(this);
                torpedo = new ImageIcon("torpedo.png").getImage();
                torpTimer = new Timer(10, new TorpHandler());
                actTimer = new Timer(10, new ActHandler());
                enemies.add(new EnemyShip());
                explosion = new ImageIcon("explosion.png").getImage();
                spawnTimer2 = new Timer(10, new SpawnHandler2());
            }
            // this method is used to add a ship to the screen
            public void addShip() {
                enemies.add(new EnemyShip());
            }
            // This method is used to start the timers and is called when the user switches to the panel
            public void initialize() {
                torpTimer.start();
                actTimer.start();
                spawnTimer2.start();
            }
            // This method is used to stop the timers once the user wins/loses
            public void stopAll() {
                torpTimer.stop();
                actTimer.stop();
                spawnTimer2.stop();
            }
            // this method draws all the stars, ships, and torpedoes on the screen
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                long current = System.currentTimeMillis();
                if(current - prev >= 10) {
                    int numStars = (int) (Math.random() * 11);
                    for (int i = 0; i < numStars; i++) stars.add(new Star());
                    prev = current;
                }
                while(removeStars()) {}
                for(Star star : stars) star.drawStar(g);
                getSTF(30f);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("HP:", 10, 40);
                g.drawString("Enemies left: " + enemiesLeft, 320, 40);
                g.setColor(Color.RED);
                g.drawRect(50, 20, 250, 20);
                for(int i = 0; i <= 250; i += 25) g.drawLine(50 + i, 20, 50 + i, 40);
                g.fillRect(50, 20, playerHealth, 20);
                while(removeTorpedoes()){}
                while(checkShot());
                crash();
                for (Torpedo torpedo1 : torpedoes) torpedo1.drawTorpedo(g);
                for (Torpedo torpedo1 : torpedoes2) torpedo1.drawTorpedo(g);
                for (EnemyShip enemy : enemies) {
                    enemy.drawEnemy(g);
                }
                drawShip(g);
                repaint();
            }
            // this class is used to add more enemy ships to the screen
            class SpawnHandler2 implements ActionListener {
                // this method is called and adds ships to the screen if needed
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
            // this class is used to update the position of the enemy
            class ActHandler implements ActionListener {
                // this method is used to call the act method of the enemies which updates the coordinates of the enemy
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (EnemyShip enemy : enemies) enemy.act();
                }
            }
            // this method is used to load an image of the USS Enterprise
            public Image getImage() {
                return new ImageIcon("spaceship.png").getImage();
            }
            // This method is used to update the position of the user and draw the enterprise on the screen
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
            // method used to update the location of the torpedoes
            class TorpHandler implements ActionListener {
                // method that calls the tick methods of the torpedoes to update coords
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
                for (Torpedo torpedo1 : torpedoes2) {
                    if(torpedo1.x_coord < -20 || torpedo1.x_coord > 820 || torpedo1.y_coord < -20 || torpedo1.y_coord > 820) {
                        torpedoes2.remove(torpedo1);
                        return true;
                    }
                }
                return false;
            }
            // This method checks if a torpedo has hit the enemy using Euclidean distance
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
                                if(enemiesLeft == 0) {
                                    stopAll();
                                    showPanel("win");
                                }
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
                        if(playerHealth == 0) {
                            stopAll();
                            showPanel("lose");
                        }
                        return true;
                    }
                }
                return false;
            }
            // This method checks if the player crashes into the enemy
            public void crash() {
                for (EnemyShip enemy : enemies) {
                    double distX = (shipX + 15) - enemy.enemyX;
                    double distY = (shipY + 30) - enemy.enemyY;
                    double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                    if(dist < 30 && enemy.health > 0) {
                        enemy.health = 0;
                        enemy.deathDate = System.currentTimeMillis();
                        enemiesLeft--;
                        playerHealth = 0;
                        stopAll();
                        showPanel("lose");
                        break;
                    }
                }
            }
            // this class is used to represent an enemy ship and contain all info needed to draw one
            class EnemyShip {
                double enemyX, enemyY;
                double rot;
                Image kgship;
                int health = 100;
                long lastShot;
                long deathDate = -1;
                // this constructor randomly initializes the coordinates and rotation of the enemy ship
                public EnemyShip() {
                    enemyX = (int)(Math.random() * 661) + 70;
                    enemyY = (int)(Math.random() * 661) + 70;
                    rot = (int)(Math.random() * 360);
                    kgship = new ImageIcon("klingonship.png").getImage();
                }
                // method used to draw enemy on screen
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
                // method used to update the coordinates + rotation of enemy and fire torpedoes based on the positon of
                // the user
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
            // method that checks if the user presses 's' and shoots a torpedo if necessary
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if(key == 's' && playerHealth > 0) {
                    double x1 = (shipX + 15) + 15 * Math.cos(Math.toRadians((rotationDeg - 90) + 45));
                    double y1 = (shipY + 15) + 15 * Math.sin(Math.toRadians((rotationDeg - 90) + 45));
                    torpedoes.add(new Torpedo(x1, y1, rotationDeg-90));
                    double x2 = (shipX + 15) + 15 * Math.cos(Math.toRadians((rotationDeg - 90) - 45));
                    double y2 = (shipY + 15) + 15 * Math.sin(Math.toRadians((rotationDeg - 90) - 45));
                    torpedoes.add(new Torpedo(x2, y2, rotationDeg-90));
                }
                repaint();
            }
            // method used to check if the user presses an arrow key and if so updates the position of the user
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(playerHealth > 0) {
                    switch (key) {
                        case KeyEvent.VK_UP -> ucount++;
                        case KeyEvent.VK_LEFT -> lcount++;
                        case KeyEvent.VK_RIGHT -> rcount++;
                        case 32 -> scount++;
                    }
                }
                repaint();
            }
            // method used to check if the user stops pressing an arrow key, stopping user movement
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if(playerHealth > 0) {
                    switch (key) {
                        case KeyEvent.VK_UP -> ucount = 0;
                        case KeyEvent.VK_LEFT -> lcount = 0;
                        case KeyEvent.VK_RIGHT -> rcount = 0;
                        case 32 -> scount = 0;
                    }
                }
                repaint();
            }
        }
        // This is the page the user is transported to if they win the space battle
        class WinPanel extends JPanel {
            // this is the constructor of the win panel which sets the background to black
            public WinPanel() {
                setBackground(Color.BLACK);
            }
            // this is the pC of the win panel which displays the text "You win!" in STF
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                getSTF(35f);
                g.setFont(stf);
                g.setColor(GOLD);
                g.drawString("You win!", 375, 400);
            }
        }
        // This is the page the user is transported to if they lose the space battle
        class LosePanel extends JPanel implements MouseListener, MouseMotionListener {
            boolean hoveringL = false;
            // this is the constructor of the lose panel which sets the background to black
            public LosePanel() {
                setBackground(Color.BLACK);
                addMouseListener(this);
                addMouseMotionListener(this);
            }
            // this is the pC of the lose panel which displays the text "You win!" in STF
            public void paintComponent(Graphics g) {
                grabFocus();
                super.paintComponent(g);
                g.drawImage(sadJoe, 100, 200, 600, 600, null);
                getSTF(35f);
                g.setFont(stf);
                if(hoveringL) {
                    g.setColor(AZURE);
                    g.fillRect(365, 650, 120, 65);
                } else {
                    g.setColor(SILVER);
                    g.fillRect(370, 655, 110, 55);
                }
                g.setColor(GOLD);
                g.drawString("You lose!", 200, 150);
                g.drawString("Retry", 380, 700);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(hoveringL) {
                    sb.shipX = 400;
                    sb.shipY = 400;
                    sb.rotationDeg = 0.0;
                    sb.previous = 0;
                    sb.current = 0;
                    sb.diff = 0;
                    sb.lcount = 0;
                    sb.rcount = 0;
                    sb.ucount = 0;
                    sb.scount = 0;
                    sb.velocityX = 0;
                    sb.velocityY = 0;
                    sb.playerHealth = 250;
                    sb.stage = 1;
                    sb.enemiesLeft = 6;
                    sb.torpedoes = new ArrayList<>();
                    sb.torpedoes2 = new ArrayList<>();
                    sb.enemies = new ArrayList<>();
                    sb.addShip();
                    sb.initialize();
                    showPanel("sb");
                    hoveringL = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if(mouseX >= 370 && mouseX <= 480 && mouseY >= 655 && mouseY <= 710) hoveringL = true;
                else hoveringL = false;
            }
        }
        // this method is called whenever the user drags their mouse on the screen (currently empty)
        @Override
        public void mouseDragged(MouseEvent e) {

        }
        // this method is called whenever the user moves their mouse and checks if the user hovers over the next button
        @Override
        public void mouseMoved(MouseEvent e) {
            int x_coord = e.getX();
            int y_coord = e.getY();
            if(instructions && x_coord >= 610 && x_coord <= 760 && y_coord >= 650 && y_coord <= 710) hovering = true;
            else hovering = false;
            repaint();
        }
        // This class contains all variables and methods required to draw a torpedo
        class Torpedo {
            double x_coord, y_coord;
            double rotDeg;
            // constructor to initialize coordinates and rotation of the torpedo
            public Torpedo(double x_coord, double y_coord, double rotDeg) {
                this.x_coord = x_coord;
                this.y_coord = y_coord;
                this.rotDeg = rotDeg;
            }
            // method called every 0.1 ms to update the location of the torpedo
            public void tick(double dt) {
                x_coord += 100 * dt * Math.cos(Math.toRadians(rotDeg));
                y_coord += 100 * dt * Math.sin(Math.toRadians(rotDeg));
            }
            // method used to draw the torpedo on the screen
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
        // this method is used to show a specific level 2 panel (either win or lose)
        public void showPanel(String name) {
            cl3.show(this, name);
        }
        // this method is called whenever the user clicks their mouse (currently empty)
        @Override
        public void mouseClicked(MouseEvent e) {

        }
        // this method is used to transport the user to the game page after they have read the instructions
        @Override
        public void mousePressed(MouseEvent e) {
            int x_coord = e.getX();
            int y_coord = e.getY();
            if(instructions && x_coord >= 660 && x_coord <= 760 && y_coord >= 650 && y_coord <= 710) {
                cl3.next(this);
                sb.initialize();
                instructions = false;
            }
        }
        // this method is called whenever the user releases their mouse (currently empty)
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        // this method is called whenever the user's mouse enters the screen (currently empty)
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        // this method is called whenever the user's mouse exits the screen (currently empty)
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    // This class contains all the information needed to create particles such as powerups, and later on, blood and guts
    class Particle {
        double posX, posY, velX, velY;
        double type;
        // type = 0: blood, type = 1: organ, type = 2: recharge, type = 3: freeze
        double splatterY = 820;
        long creation;
        boolean gravity;
        // This constructor is used to randomly initialize the location and type of particle (recharge or freeze)
        public Particle() {
            posX = (int)(Math.random()*686) + 25;
            posY = 60;
            velX = 0;
            velY = 30;
            gravity = true;
            type = (int)(Math.random() * 2) + 2;
            creation = System.currentTimeMillis();
        }
        // constructor for a moving blood/organ particle
        public Particle(double posX, double posY, double velX, double velY, boolean isBlood) {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
            if(isBlood) this.type = 0;
            else this.type = ((int)(Math.random() * 3) + 11)/10.0;
            this.gravity = true;
            splatterY = (Math.random() * 116) + 675;
            creation = System.currentTimeMillis();
        }
        // constructor for blood/organ particles that aren't moving
        public Particle(double posX, double posY, double type) {
            this.posX = posX;
            this.posY = posY;
            this.type = type;
            this.velX = 0;
            this.velY = 0;
            this.gravity = false;
            creation = System.currentTimeMillis();
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
            posY += velY * dt;
            if(gravity) velY += 1;
        }
    }
}

// this class is used to save the level the user is on (currently not being used)
class SaveFile implements Serializable {
    int level;
    // this constructor is used to create a SaveFile object and set the level of the object
    public SaveFile(int level) {
        this.level = level;
    }
    // This method uses FileInputStreams in order to this class as a .ser file, it is called whenever the user's level changes
    public void save() {
        try {
            FileOutputStream f = new FileOutputStream("saveFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(this);
        } catch(IOException e) {
            System.exit(1);
        }
    }
    // this method is called at the beginning of the program to check if the user has already played the game by reading
    // the .ser file. If the user hasn't played the game before, it creates a .ser file for them
    public static SaveFile load() {
        FileInputStream f = null;
        try {
            f = new FileInputStream("saveFile.ser");
            ObjectInputStream ois = new ObjectInputStream(f);
            SaveFile sf = (SaveFile) ois.readObject();
            return sf;
        } catch (IOException | ClassNotFoundException e) {
            SaveFile sf = new SaveFile(1);
            sf.save();
            return sf;
        }
    }
}