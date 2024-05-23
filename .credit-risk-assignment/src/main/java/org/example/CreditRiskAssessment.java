package org.example;

import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CreditRiskAssessment {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        SparkSession spark = SparkSession.builder()
                .appName("Random Forest Example")
                .getOrCreate();

        // Load training data
        Dataset<Row> training = spark.read().format("libsvm").load("/opt/spark/data/mllib/sample_libsvm_data.txt");

        // Repartitioning to distribute data across executors (crucial for parallelism)
        training = training.repartition(spark.sparkContext().defaultParallelism()); // Or specify a desired number of partitions
        
        // Increase dataset size for more memory usage
        for (int i = 0; i < 1; i++) {  // Replicate data 10 times
            training = training.union(training);
        }

        // Get numTrees from command line (default to 10 if not provided)
        int numTrees = 10;
        if (args.length > 0) {
            try {
                numTrees = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numTrees value, using default of 10.");
            }
        }

        // Train a RandomForest model with increased complexity
        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol("label")
                .setFeaturesCol("features")
                .setNumTrees(numTrees)
                .setMaxDepth(20)
                .setMaxBins(100)
                .setFeatureSubsetStrategy("all"); // Use all features for more computation

        // Fit the model
        RandomForestClassificationModel rfModel = rf.fit(training);

        // Assuming you have a separate testData DataFrame for predictions:
        Dataset<Row> testData = spark.read().format("libsvm").load("/opt/spark/data/mllib/sample_lda_libsvm_data.txt");
        Dataset<Row> predictions = rfModel.transform(testData);


        long endTime = System.currentTimeMillis();
        System.out.println("Time to run: " + (endTime - startTime) + " milliseconds");

        spark.stop();
    }
}