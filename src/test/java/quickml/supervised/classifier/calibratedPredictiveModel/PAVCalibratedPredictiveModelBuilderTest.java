package quickml.supervised.classifier.calibratedPredictiveModel;


/**
 * Created by Chris on 5/14/2014.
 */
public class PAVCalibratedPredictiveModelBuilderTest {
/*
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testError3ClassificationsInDataSet() throws Exception {
        final List<InstanceWithMapOfRegressors> instances = new LinkedList<>();
        instances.add(InstanceWithMapOfRegressors.create(1, "2", "2"));
        instances.add(InstanceWithMapOfRegressors.create(2, "2", "2"));
        instances.add(InstanceWithMapOfRegressors.create(3, "2", "2"));
        final PAVCalibratedPredictiveModelBuilder cpmb = new PAVCalibratedPredictiveModelBuilder(new TreeBuilder());
        cpmb.buildPredictiveModel(instances);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testErrorNonNumericClassificationsInDataSet() throws Exception {
        final List<InstanceWithMapOfRegressors> instances = new LinkedList<>();
        instances.add(InstanceWithMapOfRegressors.create("a", "2", "2"));
        instances.add(InstanceWithMapOfRegressors.create("b", "2", "2"));
        final PAVCalibratedPredictiveModelBuilder cpmb = new PAVCalibratedPredictiveModelBuilder(new TreeBuilder());
        cpmb.buildPredictiveModel(instances);
    }

    @Test
    public void simpleBmiTest() throws Exception {
        final List<InstanceWithMapOfRegressors> instances = TreeBuilderTestUtils.getIntegerInstances(10000);
        final TreeBuilder tb = new TreeBuilder(new SplitDiffScorer());
        final RandomForestBuilder rfb = new RandomForestBuilder(tb);
        final PAVCalibratedPredictiveModelBuilder cpmb = new PAVCalibratedPredictiveModelBuilder(rfb, 1);
        final long startTime = System.currentTimeMillis();
        final CalibratedPredictiveModel calibratedPredictiveModel = cpmb.buildPredictiveModel(instances);
        final RandomForest randomForest = (RandomForest) calibratedPredictiveModel.wrappedPredictiveModel;

        TreeBuilderTestUtils.serializeDeserialize(randomForest);

        final List<Tree> trees = randomForest.trees;
        final int treeSize = trees.size();
        Assert.assertTrue(treeSize < 400, "Forest size should be less than 400");
        Assert.assertTrue((System.currentTimeMillis() - startTime) < 20000,"Building this node should take far less than 20 seconds");
    }

    @Test
    public void simpleBmiTestSplit() throws Exception {
        final List<InstanceWithMapOfRegressors> instances = TreeBuilderTestUtils.getIntegerInstances(1000);
        final PredictiveModelWithDataBuilder<CalibratedPredictiveModel> wb = getWrappedUpdatablePredictiveModelBuilder();
        wb.splitNodeThreshold(1);
        final long startTime = System.currentTimeMillis();
        final CalibratedPredictiveModel calibratedPredictiveModel = wb.buildPredictiveModel(instances);
        final RandomForest randomForest = (RandomForest) calibratedPredictiveModel.wrappedPredictiveModel;

        TreeBuilderTestUtils.serializeDeserialize(randomForest);

        final List<Tree> trees = randomForest.trees;
        final int treeSize = trees.size();
        final int firstTreeNodeSize = trees.get(0).node.size();
        Assert.assertTrue(treeSize < 400, "Forest size should be less than 400");
        Assert.assertTrue((System.currentTimeMillis() - startTime) < 20000,"Building this node should take far less than 20 seconds");

        final List<InstanceWithMapOfRegressors> newInstances = TreeBuilderTestUtils.getIntegerInstances(1000);
        final CalibratedPredictiveModel newCalibratedPredictiveModel = wb.buildPredictiveModel(newInstances);
        final RandomForest newRandomForest = (RandomForest) newCalibratedPredictiveModel.wrappedPredictiveModel;
        Assert.assertTrue(calibratedPredictiveModel == newCalibratedPredictiveModel, "Expect same tree to be updated");
        Assert.assertEquals(treeSize, newRandomForest.trees.size(), "Expected same number of trees");
        Assert.assertNotEquals(firstTreeNodeSize, newRandomForest.trees.get(0).node.size(), "Expected new nodes");
    }

    @Test
    public void simpleBmiTestNoSplit() throws Exception {
        final List<InstanceWithMapOfRegressors> instances = TreeBuilderTestUtils.getIntegerInstances(1000);
        final PredictiveModelWithDataBuilder<CalibratedPredictiveModel> wb = getWrappedUpdatablePredictiveModelBuilder();
        final long startTime = System.currentTimeMillis();
        final CalibratedPredictiveModel calibratedPredictiveModel = wb.buildPredictiveModel(instances);
        final RandomForest randomForest = (RandomForest) calibratedPredictiveModel.wrappedPredictiveModel;

        TreeBuilderTestUtils.serializeDeserialize(randomForest);

        final List<Tree> trees = randomForest.trees;
        final int treeSize = trees.size();
        final int firstTreeNodeSize = trees.get(0).node.size();
        Assert.assertTrue(treeSize < 400, "Forest size should be less than 400");
        Assert.assertTrue((System.currentTimeMillis() - startTime) < 20000,"Building this node should take far less than 20 seconds");

        final List<InstanceWithMapOfRegressors> newInstances = TreeBuilderTestUtils.getIntegerInstances(1000);
        final CalibratedPredictiveModel newCalibratedPredictiveModel = wb.buildPredictiveModel(newInstances);
        final RandomForest newRandomForest = (RandomForest) newCalibratedPredictiveModel.wrappedPredictiveModel;
        Assert.assertTrue(calibratedPredictiveModel == newCalibratedPredictiveModel, "Expect same tree to be updated");
        Assert.assertEquals(treeSize, newRandomForest.trees.size(), "Expected same number of trees");
        Assert.assertEquals(firstTreeNodeSize, newRandomForest.trees.get(0).node.size(), "Expected same nodes");
    }

    private PredictiveModelWithDataBuilder<CalibratedPredictiveModel> getWrappedUpdatablePredictiveModelBuilder() {
        final TreeBuilder tb = new TreeBuilder(new SplitDiffScorer());
        final RandomForestBuilder urfb = new RandomForestBuilder(tb);
        final PAVCalibratedPredictiveModelBuilder ucpmb = new PAVCalibratedPredictiveModelBuilder(urfb, 1);
        return new PredictiveModelWithDataBuilder<>(ucpmb);
    }*/
}