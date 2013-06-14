package ga.core;

import java.util.Arrays;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is the chromosome class with generic genes
 * Any two chromosomes which have the same sequence of
 * genes are considered to be the same (this helps in
 * caching their fitness values ) - however additionally,
 * there is a purpose field which separates a chromosome
 * which is being used in the algorithm, from that
 * which is being stored separately for further analysis
 * or re-feeding into the population. This is needed
 * because once a chromosome is stored, it should not be
 * subject to any mutation.
 * Copyright 2013 Academic Free License 3.0
 */
@SuppressWarnings("rawtypes")
public abstract class Chromosome<T> implements Cloneable, Comparable{

	protected T[] genes;
	protected String purpose = "GA"; // GA for taking part in algorithm , S for storing 
	protected boolean isCostDirty = true;
	protected boolean isFitnessDirty = true;
	protected double cost = -1;
	protected double fitness = -1;
	
	public Chromosome(T[] genes){
		this.genes = genes.clone();
	}

	public Chromosome(T[] genes,String purpose){
		this.genes = genes.clone();
		this.purpose = purpose;
	}	
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	@SuppressWarnings("unchecked")
	public Chromosome(Chromosome c){
		this.genes = (T[])c.getGenes().clone();
	}	
	
	public T[] getGenes() {
		return genes;
	}

	public void setGenes(T[] genes) {
		this.genes = genes;
	}

	public void setGenes(int fromSource, int toSource,int fromDest, T[] genes ) {
		int j = fromDest ;
		for(int i = fromSource ; i < toSource ; i ++){
			this.genes[j] = genes[i];
			j++;
		}
	}
	
	public T getGeneAt(int i) {
		return genes[i];
	}

	public void setGeneAt(int i,T gene) {
		genes[i] = gene;
	}	
	

	public int getSize() {
		return genes.length;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(genes) + purpose.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Chromosome)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Chromosome<T> compareTo = (Chromosome<T>) object;
		return (Arrays.equals(genes, compareTo.genes) && purpose.equals(compareTo.purpose));
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Double.compare(this.getActualCost(), ((Chromosome)o).getActualCost());
	}

	@SuppressWarnings({ "unchecked"})
	public Chromosome<T> clone() throws CloneNotSupportedException {
        return (Chromosome) super.clone();
	}	
	public void dispose(){
		
		for(int i = 0 ; i < genes.length ; i ++){
			genes[i] = null;		
		}
		isCostDirty = true;
		isFitnessDirty = true;
	}
	
	abstract public String toString() ;
	abstract protected void mutateGene(int i)throws Exception ;
	abstract protected void mutateGene(int i,int j)throws Exception ;
	abstract public double getActualCost() ;
	abstract public double getFitness() ;
	abstract public boolean isValid() ;
	abstract public void chop() ;
	abstract public void optimize() ;
	abstract public Chromosome flip() ;

	
}
