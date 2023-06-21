package de.tum.in.ase.fop;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Fold extends TextFileUtility {

    private int width;
    private static final String ERROR = "Something went wrong!";

    public Fold(String[] args) throws InvalidCommandLineArgumentException {
        super(args);
        if (args == null) {
            throw new InvalidCommandLineArgumentException(ERROR);
        }
        width = Integer.parseInt(parseOption(args, "w", "80"));
    }

    @Override
    public String applyToFile(Path file) throws InvalidCommandLineArgumentException {
        if (file == null) {
            throw new InvalidCommandLineArgumentException(ERROR);
        }
        StringBuilder temp = new StringBuilder("Fold on file " + file + ":\n");
        int count = 0;
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                for (char ch : line.toCharArray()) {
                    temp.append(ch);
                    count++;
                    if (count + 1 > width) {
                        temp.append(System.lineSeparator());
                        count = 0;
                    }
                }
                temp.append(System.lineSeparator());
                count = 0;
            }
            System.out.println(temp);
            this.output(temp.toString());
        } catch (IOException e) {
            System.err.println(ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InvalidCommandLineArgumentException {
        Fold test = new Fold(args);
        test.applyToAll();
    }

}
