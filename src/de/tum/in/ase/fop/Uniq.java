package de.tum.in.ase.fop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Uniq extends TextFileUtility {

    private static final String ERROR = "Something went wrong!";

    public Uniq(String[] args) throws InvalidCommandLineArgumentException {
        super(args);
    }

    @Override
    public String applyToFile(Path file) throws InvalidCommandLineArgumentException {
        if (file == null) {
            throw new InvalidCommandLineArgumentException(ERROR);
        }
        String temp = "Uniq on file " + file + ":\n";
        String input;
        try (Scanner sc = new Scanner(new File(file.toString()), StandardCharsets.UTF_8)) {
            Set<String> set = new HashSet<>();
            while (sc.hasNextLine()) {
                input = sc.nextLine();
                if (set.add(input)) {
                    temp+= input + System.lineSeparator();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File was not found.");
        } catch (IOException e) {
            System.err.println(ERROR);
        }
        System.out.println(temp);
        this.output(temp);
        return temp;
    }

    public static void main(String[] args) throws InvalidCommandLineArgumentException {
        Uniq test = new Uniq(args);
        test.applyToAll();
    }
}
