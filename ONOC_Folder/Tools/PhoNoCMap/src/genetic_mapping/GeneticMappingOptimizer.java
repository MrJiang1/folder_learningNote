/*******************************************************************************
 * Copyright (c) 2015, Edoardo Fusella
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in
 *         the documentation and/or other materials provided with the
 *         distribution.
 *       * Neither the name of the University of Naples Federico II nor the names of its
 *         contributors may be used to endorse or promote products derived
 *         from this software without specific prior written permission.
 *   
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *   IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *   TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *   HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 *   TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package genetic_mapping;

import java.util.Collection;
import java.util.Random;

import org.opt4j.core.Individual;
import org.opt4j.core.IndividualFactory;
import org.opt4j.core.optimizer.IterativeOptimizer;
import org.opt4j.core.optimizer.Population;
import org.opt4j.core.optimizer.TerminationException;
import org.opt4j.optimizers.ea.Selector;

import com.google.inject.Inject;
/**
 * The optimizer class that is based on mutation only. 
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class GeneticMappingOptimizer implements IterativeOptimizer{
	
	/**
	 * Constructs a GeneticMappingOptimizer object given an 
	 * individualFactory, a selector and a population.
	 * The constructor is annotated with @Inject that is 
	 * used to indicate which constructor will be used 
	 * when the instance is created.
	 * 
	 * @param population The initial population
	 * @param individualFactory The creator for Individuals.
	 * @param selector The selector that allow to determine the worst solutions of a population
	 */
	@Inject
    public GeneticMappingOptimizer(Population population, IndividualFactory individualFactory, Selector selector) {
		this.individualFactory = individualFactory;
        this.selector = selector;
        this.population = population;
    }
	
	/* (non-Javadoc)
	 * @see org.opt4j.core.optimizer.IterativeOptimizer#initialize()
	 */
	public void initialize() throws TerminationException {
		for (int i = 0; i < OFFSIZE + POPSIZE; i++) {
            population.add(individualFactory.create());
		}
	}
	/* (non-Javadoc)
	 * @see org.opt4j.core.optimizer.IterativeOptimizer#next()
	 */
	public void next() throws TerminationException {
		iter++;
		System.out.println("iter "+iter);
		Random rand = new Random();
        Collection<Individual> lames = selector.getLames(OFFSIZE, population);
        population.removeAll(lames);
        Collection<Individual> parents = selector.getParents(OFFSIZE, population);
        
        int[][] phenotype_parent_i = (int [][])((Individual) parents.toArray()[parents.size()-1]).getPhenotype();
        for (Individual parent : parents) {
        	int[][] phenotype_parent_j = ((int [][])parent.getPhenotype());
        	
        	//crossover
        	GeneticMappingGenotype genotype = new GeneticMappingGenotype(phenotype_parent_i, phenotype_parent_j);
        	
        	//mutate with prob 0.2
            if(rand.nextInt(5) == 0){
            	genotype = new GeneticMappingGenotype((int [][])genotype.getGenotype_mapping_matrix(), true);
            }
            
            Individual child = individualFactory.create(genotype);
            population.add(child);
            
            phenotype_parent_i=phenotype_parent_j;
        }
	}
	
	/**
	 * The creator for Individuals.
	 */
	protected final IndividualFactory individualFactory;
	/**
	 * The selector that allow to determine the worst solutions of a population 
	 */
	protected final Selector selector;
	/**
	 * The population
	 */
	private final Population population;
	/**
	 * The population size
	 */
	public static int POPSIZE = 100;
	/**
	 * The offspring size 
	 */
	public static int OFFSIZE = 25;

	public static int iter = 0;
}
