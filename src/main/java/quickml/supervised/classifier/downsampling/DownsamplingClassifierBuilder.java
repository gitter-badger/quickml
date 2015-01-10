package quickml.supervised.classifier.downsampling;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickml.collections.MapUtils;
import quickml.data.AttributesMap;
import quickml.data.Instance;
import quickml.supervised.classifier.Classifier;
import quickml.supervised.PredictiveModelBuilder;
import quickml.supervised.UpdatablePredictiveModelBuilder;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ian on 4/22/14.
 */

public class DownsamplingClassifierBuilder implements UpdatablePredictiveModelBuilder<AttributesMap,DownsamplingClassifier> {
    private static final Logger logger = LoggerFactory.getLogger(DownsamplingClassifierBuilder.class);
    private final double targetMinorityProportion;
    private final UpdatablePredictiveModelBuilder<AttributesMap,? extends Classifier> predictiveModelBuilder;

    public DownsamplingClassifierBuilder(UpdatablePredictiveModelBuilder<AttributesMap, ? extends Classifier> predictiveModelBuilder, double targetMinorityProportion) {

        this.predictiveModelBuilder = predictiveModelBuilder;
        Preconditions.checkArgument(targetMinorityProportion > 0 && targetMinorityProportion < 1, "targetMinorityProportion must be between 0 and 1 (was %s)", targetMinorityProportion);
        this.targetMinorityProportion = targetMinorityProportion;
    }

    @Override
    public DownsamplingClassifier buildPredictiveModel(final Iterable<? extends Instance<AttributesMap>> trainingData) {
        final Map<Serializable, Double> classificationProportions = getClassificationProportions(trainingData);
        if (classificationProportions.size() != 2) {
            printSampleInstancesForInspection(trainingData);
        }
        Preconditions.checkArgument(classificationProportions.size() == 2, "trainingData must contain only 2 classifications, but it had %s. mapOfClassificationsToOutcomes: %s", classificationProportions.size(), classificationProportions.get(1.0), classificationProportions.toString());
        final Map.Entry<Serializable, Double> majorityEntry = MapUtils.getEntryWithHighestValue(classificationProportions).get();
        final Map.Entry<Serializable, Double> minorityEntry = MapUtils.getEntryWithLowestValue(classificationProportions).get();
        Serializable majorityClassification = majorityEntry.getKey();
        final double majorityProportion = majorityEntry.getValue();
        final double naturalMinorityProportion = 1.0 - majorityProportion;
        if (naturalMinorityProportion >= targetMinorityProportion) {
            final Classifier wrappedPredictiveModel = predictiveModelBuilder.buildPredictiveModel(trainingData);
            return new DownsamplingClassifier(wrappedPredictiveModel, majorityClassification, minorityEntry.getKey(), 0);
        }

        final double dropProbability = (naturalMinorityProportion > targetMinorityProportion)?  0 : 1.0 - ((naturalMinorityProportion - targetMinorityProportion*naturalMinorityProportion) / (targetMinorityProportion - targetMinorityProportion *naturalMinorityProportion));

        Iterable<? extends Instance<AttributesMap>> downsampledTrainingData = Iterables.filter(trainingData, new RandomDroppingInstanceFilter(majorityClassification, dropProbability));

        final Classifier wrappedPredictiveModel = predictiveModelBuilder.buildPredictiveModel(downsampledTrainingData);

        return new DownsamplingClassifier(wrappedPredictiveModel, majorityClassification, minorityEntry.getKey(), dropProbability);
    }

    private void printSampleInstancesForInspection(Iterable<? extends Instance<AttributesMap>> trainingData) {
        Iterator<? extends Instance<AttributesMap>> trainingDataIterator = trainingData.iterator();
        logger.info("length of training data" + Iterables.size(trainingData));
        for (int i = 0; i < 1000; i+=100) {
            if (trainingDataIterator.hasNext()) {
                Instance<AttributesMap> instance = trainingDataIterator.next();
                if (instance.getLabel().equals(Double.valueOf(1.0))) {
                    logger.info("instance " + i);
                    logger.info(instance.getAttributes().toString());
                    logger.info("label:" + instance.getLabel().toString());
                    logger.info("weight:" + instance.getWeight());
                }
            } else {
                break;
            }
        }
    }

    @Override
    public PredictiveModelBuilder<AttributesMap, DownsamplingClassifier> updatable(boolean updatable) {
        predictiveModelBuilder.updatable(updatable);
        return this;
    }

    @Override
    public void setID(Serializable id) {
        predictiveModelBuilder.setID(id);
    }

    private Map<Serializable, Double> getClassificationProportions(final Iterable<? extends Instance<AttributesMap>> trainingData) {
        Map<Serializable, AtomicLong> classificationCounts = Maps.newHashMap();
        long total = 0;
        for (Instance<AttributesMap>instance : trainingData) {
            AtomicLong count = classificationCounts.get(instance.getLabel());
            if (count == null) {
                count = new AtomicLong(0);
                classificationCounts.put(instance.getLabel(), count);
            }
            count.incrementAndGet();
            total++;
        }
        Map<Serializable, Double> classificationProportions = Maps.newHashMap();
        for (Map.Entry<Serializable, AtomicLong> classCount : classificationCounts.entrySet()) {
            classificationProportions.put(classCount.getKey(),  classCount.getValue().doubleValue() / (double) total);
        }
        return classificationProportions;
    }

    @Override
    public void updatePredictiveModel(DownsamplingClassifier predictiveModel, Iterable<? extends Instance<AttributesMap>> newData, boolean splitNodes) {
        if (predictiveModelBuilder instanceof UpdatablePredictiveModelBuilder) {
            Iterable<? extends Instance<AttributesMap>> downsampledNewData = Iterables.filter(newData, new RandomDroppingInstanceFilter(predictiveModel.getMajorityClassification(), predictiveModel.getDropProbability()));
            ((UpdatablePredictiveModelBuilder)predictiveModelBuilder).updatePredictiveModel(predictiveModel.wrappedClassifier, downsampledNewData, splitNodes);
        } else {
            throw new RuntimeException("Cannot update predictive model without UpdatablePredictiveModelBuilder");
        }
    }

    @Override
    public void stripData(DownsamplingClassifier predictiveModel) {
        if (predictiveModelBuilder instanceof UpdatablePredictiveModelBuilder) {
            ((UpdatablePredictiveModelBuilder) predictiveModelBuilder).stripData(predictiveModel.wrappedClassifier);
        } else {
            throw new RuntimeException("Cannot strip data without UpdatablePredictiveModelBuilder");
        }
    }
}