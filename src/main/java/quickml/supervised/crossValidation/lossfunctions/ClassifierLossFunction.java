package quickml.supervised.crossValidation.lossfunctions;

import quickml.supervised.crossValidation.PredictionMapResults;

public abstract class ClassifierLossFunction implements LossFunction<PredictionMapResults> {

    public abstract Double getLoss(PredictionMapResults results);

    public abstract String getName();

}
