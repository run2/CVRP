package ga.practice.tsp.reproduce;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.Node;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;
import ga.practice.tsp.gene.EdgeGene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/PMXCrossoverOperator.aspx
 * Copyright 2013 Academic Free License 3.0
 */
public class PMXEdgeCrossOverWithEdgeGene implements Operator<TSPChromosomeWithEdgeGene>{
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	
	double reproduceProbability;
	public PMXEdgeCrossOverWithEdgeGene(double probability){
		this.reproduceProbability = probability;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Population<TSPChromosomeWithEdgeGene> operate(
			Population<TSPChromosomeWithEdgeGene> population) {
		// TODO Auto-generated method stub
		Iterator<TSPChromosomeWithEdgeGene> it = population.getAllChromosomes().iterator();
		Population<TSPChromosomeWithEdgeGene> output = new Population<TSPChromosomeWithEdgeGene>();
		Random random = new Random(System.nanoTime());

		while(it.hasNext()){
			Set<Chromosome> allowedIntoNewPopulation = new HashSet<Chromosome>();
			TSPChromosomeWithEdgeGene parent1 = it.next();
			TSPChromosomeWithEdgeGene parent2 = it.next();

			int crossOverPoint1 = 0;
			int crossOverPoint2 = 0;
			int temp = 0;
			
			if (random.nextDouble() < reproduceProbability|| (allowedIntoNewPopulation.contains(parent1)||allowedIntoNewPopulation.contains(parent2) )) {
				while(crossOverPoint1>(parent1.getSize()-2) || crossOverPoint2==0 || crossOverPoint2>=(parent1.getSize()-1) || crossOverPoint2==crossOverPoint1){
					crossOverPoint1 = random.nextInt(parent1.getSize());
					crossOverPoint2 = random.nextInt(parent1.getSize());
					if(crossOverPoint1>crossOverPoint2){
						temp = crossOverPoint2;
						crossOverPoint2 = crossOverPoint1;
						crossOverPoint1 = temp;
					}
				}
				
				
				//logger.info("Couples chosen to reproduce are " + parent1.toString() + " and " + parent2.toString());

				TSPChromosomeWithEdgeGene child1 ;
				TSPChromosomeWithEdgeGene child2 ;
				
				child1 = doPMX(parent1,parent2,crossOverPoint1,crossOverPoint2);
				CommonGA.logger.info("Child 1" + child1.toString());
				output.addChromosome(child1);
				child2 = doPMX(parent2,parent1,crossOverPoint1,crossOverPoint2);
				CommonGA.logger.info("Child 2" + child2.toString());
				output.addChromosome(child2);
			}else{
				CommonGA.logger.info("Couple retained in next generation ");
				
				allowedIntoNewPopulation.add(parent1);
				allowedIntoNewPopulation.add(parent2);
				output.addChromosome(parent1);
				output.addChromosome(parent2);
			}
			
		}
		return output;
	}
	public TSPChromosomeWithEdgeGene doPMX(TSPChromosomeWithEdgeGene parent1,TSPChromosomeWithEdgeGene parent2,int crossOverPoint1,int crossOverPoint2){
		// First get the genes between crossOverPoint1 and crossOverPoint2 of Parent1 into the child gene
		EdgeGene[] pathGenes = new EdgeGene[parent1.getSize()];
		for(int i = crossOverPoint1 ; i <= crossOverPoint2 ; i++){
			pathGenes[i] = new EdgeGene(parent1.getGenes()[i].getNodeFrom(),null);// removed .clone()
		}
		
		// Now check for genes in parent2 which are not present in pathgenes
		boolean matchFound = false;
		int[] mismatches = new int[crossOverPoint2-crossOverPoint1+1];
		int k = 0;
		for(int i = crossOverPoint1 ; i <= crossOverPoint2 ; i++,k++){
			for(int j = crossOverPoint1 ; j <= crossOverPoint2 ; j++){
				if(parent2.getGenes()[i].getNodeFrom().equals(pathGenes[j].getNodeFrom())){
					matchFound = true;
					break;
				}
			}
			if(!matchFound){
				mismatches[k] = i;
			}else{
				mismatches[k] = -1;
			}
			matchFound = false;
			
		}	
		// now we have all the nodefroms in parent2 which do not figure as nodefroms within
		// the copied portion . We need to find there position
		int lookup = 0;
		int positionFound = 0;
		for(int i = 0 ; i < k ; i++){
			lookup = mismatches[i];
			positionFound = lookup;
			if(lookup!=-1){
				do{
					for(int j=0; j < parent2.getSize() ; j++){
						if(parent2.getGenes()[j].getNodeFrom().equals(parent1.getGenes()[lookup].getNodeFrom())){
							positionFound = j;
							lookup = j;
							break;
						}
					}
				}while(positionFound<=crossOverPoint2&&positionFound>=crossOverPoint1);
				
				pathGenes[positionFound] = new EdgeGene();
				pathGenes[positionFound].setNodeFrom((Node)(parent2.getGenes()[mismatches[i]].getNodeFrom()));// removed .clone()
				
			}
			
		}
		// One done - copy the rest of paths from parent2
		for(int i = 0 ; i < pathGenes.length ; i++){
			if(null==pathGenes[i]){
				pathGenes[i] = new EdgeGene(parent2.getGenes()[i].getNodeFrom(), null) ;// removed .clone()
			}
		}
		for(int i = 0 ; i < pathGenes.length ; i++){
			if(i <pathGenes.length-1 && null!=pathGenes[i+1] && null!=pathGenes[i+1].getNodeFrom())
				pathGenes[i].setNodeTo(pathGenes[i+1].getNodeFrom());
			if(i>=1 && null!=pathGenes[i-1] )
				pathGenes[i-1].setNodeTo(pathGenes[i].getNodeFrom());
		}
		pathGenes[parent1.getSize()-1].setNodeTo(parent1.getGeneAt(parent1.getSize()-1).getNodeTo()); // set the last node to
		return  new TSPChromosomeWithEdgeGene(pathGenes);
	}

}
