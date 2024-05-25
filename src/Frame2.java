import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Frame2 {
    public static void main(String[] args) {
        Frame2 fr2 = new Frame2();
        fr2.run();
    }
    public void run() {
        JFrame frame = new JFrame("Credits");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        Credits cr = new Credits();
        frame.getContentPane().add(cr);
        frame.setVisible(true);
    }
}

class Credits extends JPanel {
    int y_coord = 830;
    Timer credTimer;
    ArrayList<String> creds = new ArrayList<>();
    Scanner input, input2;
    Font stf;
    final Color GOLD = new Color(211, 142, 38);
    Clip clip;
    JFrame transcript;
    public Credits() {
        setBackground(Color.BLACK);
        tryCatchIt();
        while(input.hasNext()) creds.add(input.nextLine());
        input.close();
        credTimer = new Timer(25, e -> {y_coord--; repaint();});
        credTimer.start();
        transcript = new JFrame("Java Life");
        transcript.setSize(600, 600);
        transcript.setLocation(950, 100);
        transcript.setResizable(false);
        transcript.add(new Transcript());
        transcript.setVisible(true);
        playMusic("javalife.wav", false);
    }
    class Transcript extends JPanel {
        String text;
        JTextArea trans;
        JScrollPane pane;
        public Transcript() {
            setBackground(Color.BLACK);
            setLayout(null);
            while(input2.hasNext()) text += input2.nextLine() + "\n";
            input2.close();
            trans = new JTextArea(text.substring(4));
            trans.setBackground(GOLD);
            getSTF(20f);
            trans.setFont(stf);
            pane = new JScrollPane(trans);
            pane.setBackground(GOLD);
            pane.setBounds(0, 0, 600, 600);
            add(pane);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    // TODO: remove after moving to Voyager.java
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
    // method used to initialize the scanner in order to read file with the credits
    public void tryCatchIt() {
        try {
            input = new Scanner(new File("credits.txt"));
            input2 = new Scanner(new File("transcript.txt"));
        } catch(FileNotFoundException e) {
            System.exit(1);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getSTF(25f);
        g.setFont(stf);
        g.setColor(GOLD);
        for(int i = 0; i < creds.size(); i++) {
            String txt = creds.get(i);
            int loc = y_coord + 40 * i;
            if(loc >= 0 && loc <= 830) g.drawString(txt, 100, loc);
        }
    }
}