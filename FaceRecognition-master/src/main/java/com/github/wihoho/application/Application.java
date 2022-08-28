package com.github.wihoho.application;

import com.github.wihoho.Trainer;
import com.github.wihoho.constant.FeatureType;
import com.github.wihoho.jama.Matrix;
import com.github.wihoho.training.CosineDissimilarity;
import com.github.wihoho.training.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    ClassLoader classLoader = getClass().getClassLoader();
    private Matrix convertToMatrix(String fileAddress) throws IOException {
        File file = new File(classLoader.getResource(fileAddress).getFile());
        return vectorize(FileManager.convertPGMtoMatrix(file.getAbsolutePath()));
    }

    //Convert a m by n matrix into a m*n by 1 matrix
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

    public static void main(String[] args) throws Exception {
        Application application = new Application();

        // Build a trainer
        Trainer trainer = Trainer.builder()
                .metric(new CosineDissimilarity())
                .featureType(FeatureType.LPP)
                .numberOfComponents(3)
                .k(1)
                .build();

        List<String> johnFaces = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            johnFaces.add("faces/s1/" + i + ".pgm");
        }

        List<String> smithFaces = new ArrayList<>();
        for(int i = 1; i <= 10; i ++) {
            smithFaces.add("faces/s2/" + i +".pgm");
        }

        // add training data
        johnFaces.forEach(
                item -> {
                    try {
                        trainer.add(application.convertToMatrix(item), "john");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        smithFaces.forEach(
                item -> {
                    try {
                        trainer.add(application.convertToMatrix(item), "smith");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // train
        trainer.train();


        // new faces
        List<String> tedFaces = new ArrayList<>();
        for(int i = 1; i <= 10; i ++) {
            tedFaces.add("faces/s6/" + i +".pgm");
        }

        // add 7 faces for Ted
        for(int i = 0; i < 7; i ++) {
            trainer.addFaceAfterTraining(application.convertToMatrix(tedFaces.get(i)), "ted");
        }

        System.out.println("[RECOGNITION RESULT]:");
        System.out.println(trainer.recognize(application.convertToMatrix(tedFaces.get(7))));
        System.out.println(trainer.recognize(application.convertToMatrix(tedFaces.get(6))));
        System.out.println(trainer.recognize(application.convertToMatrix(tedFaces.get(5))));
        System.out.println(trainer.recognize(application.convertToMatrix(tedFaces.get(4))));

        System.out.println(trainer.recognize(application.convertToMatrix(smithFaces.get(1))));


    }
}
