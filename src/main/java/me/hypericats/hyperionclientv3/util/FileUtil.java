package me.hypericats.hyperionclientv3.util;

import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static File MCDir = MinecraftClient.getInstance().runDirectory;
    public static File HypCv3Dir = new File(MCDir.getAbsolutePath() + "\\HyperionClientV3");

    public static File createFile(String filename, File directory) {
        File file = new File(directory.getAbsolutePath() + "\\" + filename);
        if (file.exists()) return null;
        try {
            file.createNewFile();
        } catch (IOException exception) {
            return null;
        }
        return file;
    }
    public static File createFile(File file) {
        if (file.exists()) return null;
        try {
            file.createNewFile();
        } catch (IOException exception) {
            return null;
        }
        return file;
    }
    public static File createDir(String directoryName, File directory) {
        File file = new File(directory.getAbsolutePath() + "\\" + directoryName);
        if (file.exists()) return null;
        file.mkdir();
        return file;
    }
    public static File createDir(File directory) {
        if (directory.exists()) return null;
        directory.mkdir();
        return directory;
    }
    public static void appendLineToFile(String line, File f1) {
        try {
            FileWriter fw = new FileWriter(f1.getAbsolutePath(), true);
            fw.write(line);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteToFile(String str, File f1) {
        try {
            FileWriter fw = new FileWriter(f1.getAbsolutePath());
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearFile(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> readFromFile(File file) {
        List<String> contents = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
            return contents;
        } catch (FileNotFoundException exception) {
            return null;
        }
    }
}
