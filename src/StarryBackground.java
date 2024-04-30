import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StarryBackground {
    public static void main(String[] args) {
        StarryBackground sb = new StarryBackground();
        sb.run();
    }
    public void run() {
        JFrame frame = new JFrame("Star Trek Starry BG");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        VPanel VPanel = new VPanel();
        frame.getContentPane().add(VPanel);
        frame.setVisible(true);
    }
}

class Panel extends JPanel implements MouseListener, MouseMotionListener {
    ArrayList<Star> stars = new ArrayList<>();
    Timer starTimer;
    Image logo = new ImageIcon("voyager-logo.png").getImage();
    Image[] ships = new Image[3];
    ArrayList<BGShip> bgships = new ArrayList<>();
    Image pb_p, pb_l, pb_a, pb_y;
    Image pb_pClicked, pb_lClicked, pb_aClicked, pb_yClicked;
    Image pb_p_reversed, pb_l_reversed, pb_y_reversed;
    boolean hovering = false;
    boolean reversed = false;
    int width = 100; int let_coord1 = 378, let_coord2 = 373;
    long start = System.currentTimeMillis();
    String rev = "";

    public Panel() {
        setBackground(Color.BLACK);
        addMouseListener(this);
        addMouseMotionListener(this);
        StarHandler sh = new StarHandler();
        starTimer = new Timer(10, sh);
        starTimer.start();
        for(int i = 0; i < 2; i++) ships[i] = new ImageIcon("spaceship" + i + ".png").getImage();
        pb_p = new ImageIcon("pb_p.png").getImage();
        pb_l = new ImageIcon("pb_l.png").getImage();
        pb_a = new ImageIcon("pb_a.png").getImage();
        pb_y = new ImageIcon("pb_y.png").getImage();

        pb_pClicked = new ImageIcon("pb_pClicked.png").getImage();
        pb_lClicked = new ImageIcon("pb_lClicked.png").getImage();
        pb_aClicked = new ImageIcon("pb_aClicked.png").getImage();
        pb_yClicked = new ImageIcon("pb_yClicked.png").getImage();

        pb_p_reversed = new ImageIcon("pb_p_reversed.png").getImage();
        pb_l_reversed = new ImageIcon("pb_l_reversed.png").getImage();
        pb_y_reversed = new ImageIcon("pb_y_reversed.png").getImage();

        LetHandler lh = new LetHandler();
        Timer letTimer = new Timer(10 , lh);
        letTimer.start();
    }
    public void paintComponent(Graphics g) {
        grabFocus();
        super.paintComponent(g);
        int numStars = (int)(Math.random()*11);
        for(int i = 0; i < numStars; i++) stars.add(new Star());
        while(removeStars()) {}
        for(Star star : stars) star.drawStar(g);
        g.drawImage(logo, 75, 300, 649, 130, null);
        if(!hovering) {
            if(!reversed) {
                g.drawImage(pb_p, 378, 30, width, 130, null);
                g.drawImage(pb_l, 378, 165, width, 130, null);
                g.drawImage(pb_y, 373, 435, width, 130, null);
            } else {
                g.drawImage(pb_p_reversed, 378, 30, width, 130, null);
                g.drawImage(pb_l_reversed, 378, 165, width, 130, null);
                g.drawImage(pb_y_reversed, 373, 435, width, 130, null);
            }
            g.drawImage(pb_a, 373, 300, 100, 130, null);
        } else {
            g.drawImage(pb_pClicked, 378, 30, width, 130, null);
            g.drawImage(pb_lClicked, 378, 165, width, 130, null);
            g.drawImage(pb_aClicked, 373, 300, 100, 130, null);
            g.drawImage(pb_yClicked, 373, 435, width, 130, null);
        }
        bgships.add(new BGShip());
        for(BGShip ship : bgships) ship.drawShip(g);
    }
    public boolean removeStars() {
        for(Star i : stars) {
            if((i.last - i.creation)/1000 >= 3) {
                stars.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getX();
        if(mouseX >= 373 && mouseX <= 478 && mouseY >= 30 && mouseY <= 565) hovering = true;
        else hovering = false;
        repaint();
    }

    class BGShip {
        int image = (int)(Math.random()*3);
        boolean upper = (int)(Math.random()*2) == 1;
        boolean right = (int)(Math.random()*2) == 1;
        double x_coord;
        double y_coord;
        public BGShip() {
        }
        int rotationDeg = (int)(Math.random()*361);
        public void drawShip(Graphics g) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(Math.toRadians(rotationDeg), x_coord + 20, y_coord + 20);
                g2d.drawImage(ships[image], (int)x_coord, (int)y_coord, 40, 40, null);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    class Star {
        long creation = System.currentTimeMillis();
        long last = System.currentTimeMillis();
        int x_coord = (int)(Math.random()*796);
        int y_coord = (int)(Math.random()*796);
        public void drawStar(Graphics g) {
            last = System.currentTimeMillis();
            g.setColor(Color.WHITE);
            int diameter = getDiameter();
            g.fillOval(x_coord - diameter/2, y_coord - diameter/2, diameter, diameter);
        }
        public int getDiameter() {
            long diff = (last-creation)/1000;
            return (int)(8 * Math.sin((Math.PI * diff / 3)));
        }
    }
    class StarHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
    class LetHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!hovering) {
                long current = System.currentTimeMillis();
                int val = getWidth((int)((current - start)/1000));
                reversed = val < 0;
                width = Math.abs(val);
            }
            repaint();
        }
        public int getWidth(int diff) {
            return (int)(100 * Math.sin(Math.PI * diff / 5));
        }
    }
}