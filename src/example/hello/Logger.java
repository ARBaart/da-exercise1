package example.hello;

import java.io.FileWriter;
import java.io.Writer;

public class Logger {
    Writer log;

    Logger() {
        String filename = "log.txt";

        try {
            log = new FileWriter(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String text) {
        try {
            System.out.println(text);
            log.write(text);
            log.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        try {log.close(); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
