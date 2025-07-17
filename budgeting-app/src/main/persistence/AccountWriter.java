package persistence;

import model.Account;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class AccountWriter {
    private static final int INDENT = 4;
    private PrintWriter printWriter;
    private String output;

    // Inspired from JsonSerializationDemo
    // EFFECTS: Gets the output String for the location where the file is being saved
    public AccountWriter(String output) {
        this.output = output;
    }

    // Inspired from open method from JsonSerializationDemo
    // EFFECTS: Opens the writer. If the output file cannot be opened, it throws a
    // FileNotFoundException
    public void openWriter() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(output));
    }

    // Inspired from saveToFile method from JsonSerializationDemo
    // EFFECTS: Writes a string to a file
    private void save(String json) {
        printWriter.print(json);
    }

    // Inspired from the write method in JsonSerializationDemo
    // EFFECTS: Writes account to file through a JSONObject
    public void write(Account account) {
        JSONObject json = account.toJson();
        save(json.toString(INDENT));
    }

    // Inspired from the close method in JsonSerializationDemo
    // EFFECTS: Closes the writer
    public void closeWriter() {
        printWriter.close();
    }
}
