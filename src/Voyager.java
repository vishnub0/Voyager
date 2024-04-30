/*
Vishnu Bharadwaj
4/28/24
Voyager.java
This is my game, Voyager. It is a game based off of the Star Trek Original Series that uses all of my java knowledge in
a fun game. Currently, I have finished the homepage and the instructions page for level 1.
*/

import javax.swing.*; import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    class Level1 extends JPanel implements MouseListener {
        CardLayout cl2;
        boolean instructions = true;
        public Level1() {
            cl2 = new CardLayout();
            setLayout(cl2);
            addMouseListener(this);
            run2();
        }
        public void run2() {
            JPanel instructions = new JPanel();
            instructions.setBackground(Color.BLACK);
            add(instructions, "instructions");
            JPanel shooter = new JPanel();
            shooter.setBackground(Color.WHITE);
            add(shooter, "shooter");
        }
        public void paintComponent(Graphics g) {
            grabFocus();
            super.paintComponent(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(instructions) {
                cl2.next(this);
                instructions = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

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
    }
}