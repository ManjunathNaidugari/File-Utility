package de.tum.in.ase.fop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public abstract class TextFileUtility {

    private final List<Path> inputPaths;
    private final Path outputFile;
    private List<Path> list = new LinkedList<>();
    private Path path;
    private static final int BEGIN = 3;
    private static final String ERROR = "Something went wrong!";
    private static final String CHECK_THE_CLASS = "de.tum.in.ase.fop.Uniq";

    public TextFileUtility(String[] args) throws InvalidCommandLineArgumentException {
        if (args != null) {
            for (String string : args) {
                checkForFolder(string);
                if (!string.contains("-") && string.contains("txt")) {
                    list.add(Paths.get(string));
                }
                if (string.contains("-o=")) {
                    StringBuilder builder = new StringBuilder();
                    int i = BEGIN;
                    while (i < string.length()) {
                        builder.append(string.charAt(i));
                        i++;
                    }
                    path = Paths.get(builder.toString());
                }
            }
            this.inputPaths = list;
            this.outputFile = path;
        } else {
            throw new InvalidCommandLineArgumentException(ERROR);
        }
    }

    public void checkForFolder(String str) {
        if (str == null) {
            return;
        }
        if (!str.contains("-") && !str.contains("txt")) {
            File folder = new File(str);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                return;
            }
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    list.add(file.toPath());
                }
            }
        }
    }

    public static String parseOption(String[] args, String option, String defaultValue) {
        StringBuilder bld = new StringBuilder();
        for (String str : args) {
            if (str.contains(option)) {
                int i = BEGIN;
                while (i < str.length()) {
                    bld.append(str.charAt(i));
                    i++;
                }
                return bld.toString();
            }
        }
        return defaultValue;

    }

    public void output(String str) {
        if (outputFile == null) {
            System.err.println(ERROR);
            System.out.println(str);
            return;
        }
        String className = new Exception().getStackTrace()[1].getClassName();
        try {
            File out = new File(outputFile.toString());
            if (out.createNewFile()) {
                System.out.println("New File created: " + out.getName());
            }
            if (CHECK_THE_CLASS.equals(className)) {
                try (BufferedWriter myWriter = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(out, true), StandardCharsets.UTF_8))) {
                    myWriter.append(str);
                }
            } else {
                if (inputPaths.contains(out.toPath())) {
                    try (BufferedWriter myWriter = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(out, false), StandardCharsets.UTF_8))) {
                        myWriter.append(str);
                    }
                } else {
                    try (BufferedWriter myWriter = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(out, true), StandardCharsets.UTF_8))) {
                        myWriter.append(str);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(ERROR);
        }
    }

    public abstract String applyToFile(Path file) throws InvalidCommandLineArgumentException;

    public void applyToAll() throws InvalidCommandLineArgumentException {
        for (Path inputPath : inputPaths) {
            this.applyToFile(inputPath);
        }
    }

}
