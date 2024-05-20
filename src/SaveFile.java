import java.io.*;

public class SaveFile implements Serializable {
    int level;

    public SaveFile(int level) {
        this.level = level;
    }
    public void save() {
        try {
            FileOutputStream f = new FileOutputStream("saveFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(this);
        } catch(IOException e) {
            System.exit(1);
        }
    }
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
