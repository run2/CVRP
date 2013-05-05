package ga.core.select;

import ga.core.evaluate.FitnessAndCost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Selector generic interface
 * Copyright 2013 Academic Free License 3.0
 */
public abstract class Selector<T> {
	final FitnessAndCost<T> fitness;
	List<Double> selectivePressureWheel; 
	public Selector(FitnessAndCost<T> fitness) {
		this.fitness = fitness;
	}

	Comparator<T> comparator = new Comparator<T>() {
		public int compare(T o1, T o2) {
			Double v1 = fitness.computeFitness(o1);
			Double v2 = fitness.computeFitness(o2);
			return Double.compare(v2, v1);
		}
	};
	
	@SuppressWarnings("unused")
	protected List<Double> getSelectivePressure(List<T> chromosomes){
	    
	    double sumFitNessValues = 0;
	    double fitNessValue = 0;
	    selectivePressureWheel = new ArrayList<Double>();
	    for (T chromosome : chromosomes){
	    	fitNessValue = fitness.computeFitness(chromosome);
	    	sumFitNessValues += fitNessValue;
	    	selectivePressureWheel.add(fitNessValue);
	    }
	    // now we divide by sum and get the percentage
	    int i = 0;
	    double runningValue = 0;
	    for (T chromosome : chromosomes){
	    	runningValue = runningValue + selectivePressureWheel.get(i)/sumFitNessValues;
	    	selectivePressureWheel.set(i, runningValue);
	    	i++;
	    }
		return selectivePressureWheel;
	}
	// Can be improved
	protected int getIndexOfParentFromSelectionPressureDistribution(double value){
		//int index = 1;
		int i ;
		for(i = 0 ; i < selectivePressureWheel.size() ; i++ ){
			if(selectivePressureWheel.get(i) >= value)
				break;
		}
		if(i==selectivePressureWheel.size())
			i = selectivePressureWheel.size()-1;
		return i ;
	}
}
