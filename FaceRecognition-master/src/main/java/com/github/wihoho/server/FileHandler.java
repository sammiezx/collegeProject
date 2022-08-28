package com.github.wihoho.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    String root;
    private PrintWriter writer;
    public List<String> list = new ArrayList<>();
    FileHandler(String root){
        this.root = root;
        load();
    }
    private void load(){
        try {
            Scanner scanner = new Scanner(new File(root + "label.csv"));
            scanner.useDelimiter("\n");
            while (scanner.hasNext())
            {
                String line = scanner.next();
                list.add(line);
            }

            writer =  new PrintWriter(root + "label.csv");
            list.forEach((e) -> {
                try {
                    writer.print(e + "\n");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void reload(){
        try {
            writer =  new PrintWriter(root + "label.csv");
            list.forEach((e) -> {
                try {
                    writer.print(e + "\n");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void append(String label){
        list.add(label);
        writer.print(label + "\n");
    }
    public void terminate(){
        writer.close();
    }
}
