import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File; import java.io.PrintWriter;
import java.util.Scanner;
public class Processor {
    Scanner input; PrintWriter pw;
    public static void main(String[] args) {
        Processor p = new Processor();
        p.run();
    }
    public void run() {
        tryCatchIt();
        ArrayList<String> sentences = new ArrayList<>();
        while(input.hasNext()) {
            sentences.add(input.nextLine());
        }
        input.close();
        for (int i = 1; i <= sentences.size(); i++) {
            String sent = sentences.get(i-1);
            String text = "wav" + i + "|" + sent.substring(sent.indexOf('\t') + 1);
            System.out.println(text);
            pw.println(text);
        }
        pw.close();
    }
    public void tryCatchIt() {
        try {
            input = new Scanner(new File("sentences_unprocessed.txt"));
            pw = new PrintWriter(new File("transcript.txt"));
        } catch(Exception e) {
            System.exit(1);
        }
    }
}
