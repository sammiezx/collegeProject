package com.github.wihoho.server;

import com.github.wihoho.Trainer;
import com.github.wihoho.constant.FeatureType;
import com.github.wihoho.jama.Matrix;
import com.github.wihoho.training.CosineDissimilarity;
import com.github.wihoho.training.FileManager;

import java.io.*;
import java.util.*;

public class ServerController {
    String root = "/home/samir/Desktop/face/FaceRecognition-master/src/main/resources/faces/";
    FileHandler handler = new FileHandler(root);
    Trainer trainer = Trainer.builder()
            .metric(new CosineDissimilarity())
            .featureType(FeatureType.LPP)
            .numberOfComponents(3)
            .k(1)
            .build();

    ServerController() throws Exception {
        train();
    }

    private Matrix convertToMatrix(String fileAddress) throws IOException {
        return vectorize(FileManager.convertPGMtoMatrix(fileAddress));
    }
    static Matrix vectorize(Matrix input) {
        int m = input.getRowDimension();
        int n = input.getColumnDimension();

        Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;
    }

    public boolean verifySaved(String label) throws Exception {
        int i= 0;
        while (i < 10){
            String path = root + label + '/';
            String file =  ++i + ".pgm";

            try {
                FileReader fileReader = new FileReader(path+file);
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        return true;
    }
    public String recognize() throws Exception {
        SortedMap<String, Integer> countMap = new TreeMap<>();
        SortedMap<Integer, String> invertedMap = new TreeMap<>();

        if (!verifySaved("buffer"))
            return "IMPROPER BUFFER";

        for (int j = 1; j <= 10; j++) {
            String rec = trainer.recognize(convertToMatrix(root + "buffer/" + j + ".pgm"));
            if (countMap.containsKey(rec))
                countMap.put(rec, countMap.get(rec) + 1);
            else
                countMap.put(rec, 1);
        }
        countMap.forEach((s,i) -> {
            invertedMap.put(i,s);
        });
        if (invertedMap.lastKey() > 5)
            return invertedMap.get((invertedMap.lastKey())) + " : " + invertedMap.lastKey();
        else
            return "failed";
    }

    public void train() throws Exception {
        handler.list.forEach((label) -> {
            for (int j = 1; j <= 10 ; j++) {
                String file = root + label + '/' + j + ".pgm";
                try {
                    trainer.add(convertToMatrix(file), label);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        trainer.train();
    }
    public void append(String label) throws Exception {
        handler.append(label);
        for (int j = 1; j <= 10 ; j++) {
            String file = root + label + '/' + j + ".pgm";
            try {
                trainer.add(convertToMatrix(file), label);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        trainer.train();
    }

    public static void main(String[] args) throws Exception {
        ServerController serverController = new ServerController();
        serverController.handler.terminate();
    }
}
