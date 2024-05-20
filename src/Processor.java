import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File; import java.io.PrintWriter;
import java.util.Scanner;
public class Processor {
    Scanner input;
    public static void main(String[] args) {
        Processor p = new Processor();
        p.run();
    }
    public void run() {
        String chars = "";
        tryCatchIt();
        ArrayList<String> sentences = new ArrayList<>();
        while(input.hasNext()) {
            sentences.add(input.nextLine());
        }
        input.close();
        for (String sentence : sentences) {
            for(int i = 0; i < sentence.length(); i++) {
                char c = sentence.charAt(i);
                if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) && !chars.contains("" + c)) chars += c;
            }
        }
        System.out.println(chars);
    }
    public void tryCatchIt() {
        try {
            input = new Scanner(new File("transcript.txt"));
        } catch(Exception e) {
            System.exit(1);
        }
    }
}
