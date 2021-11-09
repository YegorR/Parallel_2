package ru.yegorr.parallel_second;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    private static final int ATTEMPTS = 3;

    public static void main(String[] args) {
        ImageReader imageReader1;
        ImageReader imageReader2;
        ImageReader imageReader3;

        try {
            imageReader1 = new ImageReader("1.jpg");
            imageReader2 = new ImageReader("2.jpg");
            imageReader3 = new ImageReader("3.jpg");
        } catch (Exception ex) {
            System.err.println("ImageReader error");
            ex.printStackTrace();
            return;
        }

        ResultSaver resultSaver1 = new ResultSaver(imageReader1.getHeight(), imageReader1.getWidth());
        ResultSaver resultSaver2 = new ResultSaver(imageReader2.getHeight(), imageReader2.getWidth());
        ResultSaver resultSaver3 = new ResultSaver(imageReader3.getHeight(), imageReader3.getWidth());

        for (int threadPoolSize = 1; threadPoolSize <= 16; ++threadPoolSize) {
            double avgTime = 0;
            for (int attempt = 0; attempt < ATTEMPTS; ++attempt) {



                ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
                List<Callable<Void>> tasks = new ArrayList<>();
                for (int i = 0; i < threadPoolSize; ++i) {
                    tasks.add(new CalculatingTask(i, threadPoolSize, imageReader1, imageReader2, imageReader3, resultSaver1, resultSaver2, resultSaver3));
                }
                long startTime = System.nanoTime();
                try {

                    executorService.invokeAll(tasks);
                    executorService.shutdown();
                } catch (InterruptedException ignored) {
                    System.err.println("InterruptedException should not be thrown");
                }
                long endTime = System.nanoTime();
                avgTime += (endTime - startTime) / 1_000_000.0;
            }
            System.out.printf("ThreadPoolSize #%2d  avgTime %f%n", threadPoolSize, avgTime / ATTEMPTS);
        }
    }
}
