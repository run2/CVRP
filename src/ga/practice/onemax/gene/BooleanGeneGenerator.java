package ga.practice.onemax.gene;

import ga.core.GeneGenerator;

import java.util.Random;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class BooleanGeneGenerator implements GeneGenerator<Boolean> {
	boolean reInitialize = false;
	Random random = new Random(System.currentTimeMillis()); 
	
	/** {@inheritDoc} */
	@Override
	public Boolean getNewRandomGene() {
		return random.nextBoolean();
	}

	@Override
	public void reInitialize() {
		// TODO Auto-generated method stub
		reInitialize = false;	
	}
	
}
