package quickdt.integration;

import com.google.common.collect.Iterables;
import junit.framework.Assert;
import org.testng.annotations.Test;
import quickdt.*;
import quickdt.experiments.crossValidation.CrossValidator;
import quickdt.experiments.crossValidation.RMSECrossValScorer;
import quickdt.randomForest.RandomForestBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Created by ian on 3/1/14.
 */
public class DiabetesDatasetTest {
    @Test
    public void testDiabetesDataset() throws IOException {
        final List<Instance> instances = Benchmarks.loadDiabetesDataset();

        System.out.println("Total instance count: "+ Iterables.size(instances));

        CrossValidator crossValidator = new CrossValidator();
        PredictiveModelBuilder<?> predictiveModelBuilder = new RandomForestBuilder(new TreeBuilder().ignoreAttributeAtNodeProbability(0.0)).numTrees(100);
        final PredictiveModel predictiveModel = predictiveModelBuilder.buildPredictiveModel(instances);

        System.out.println("Random forest hash: " + predictiveModel.hashCode());

        final RMSECrossValScorer testResult = (RMSECrossValScorer) crossValidator.test(predictiveModelBuilder, instances);
        Assert.assertTrue(String.format("RMSE is %f, should be below 0.7", testResult.getRMSE()), testResult.getRMSE() < 0.7);
    }
}