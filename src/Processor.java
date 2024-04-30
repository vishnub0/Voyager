import java.util.ArrayList;
import java.util.Scanner; import java.io.PrintWriter;
import java.io.File; import java.io.FileNotFoundException;
public class Processor {
    Scanner input; PrintWriter pw;
    ArrayList<String> stuff = new ArrayList<>();
    public static void main(String[] args) {
        Processor ps = new Processor();
        ps.run();
    }
    public void run() {
        tryCatchIt();
        String val;
        while(input.hasNext()) {
            val = input.nextLine();
            if(!stuff.contains(val)) {
                pw.println(val);
                stuff.add(val);
            }
        }
        input.close();
        pw.close();
        for(String i : stuff) System.out.println(i);
    }
    public void tryCatchIt() {
        File inFile = new File("sentences.txt");
        File outFile = new File("sentences-processed.txt");
        try {
            input = new Scanner(inFile);
            pw = new PrintWriter(outFile);
        }
        catch(FileNotFoundException e) {
            System.exit(1);
        }
    }
}
