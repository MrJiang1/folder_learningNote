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

import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;
/**
 * The optimization module class.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 */
public class GeneticMappingOptimizerModule extends OptimizerModule{

	/* (non-Javadoc)
	 * @see org.opt4j.core.start.Opt4JModule#config()
	 */
	@Override
	protected void config() {
		GeneticMappingOptimizer.POPSIZE = populationSize;
		GeneticMappingOptimizer.OFFSIZE = offSize;
		bindIterativeOptimizer(GeneticMappingOptimizer.class);
	}
	
	/**
	 * @return The number of iterations
	 */
	public int getIterations() {
        return iterations;
	}
	/**
	 * @param iterations The number of iterations
	 */
	public void setIterations(int iterations) {
	        this.iterations = iterations;
	}
	
	/**
	 * @return The population size
	 */
	public int getPopulationSize() {
	    return populationSize;
	}
	/**
	 * @param popSize The population size
	 */
	public void setPopulationSize(int popSize) {
	    this.populationSize = popSize;
	}
	
	/**
	 * @return The offspring size
	 */
	public int getOffSize() {
	    return offSize;
	}
	/**
	 * @param offSize The offspring size
	 */
	public void setOffSize(int offSize) {
	    this.offSize = offSize;
	}

	
	/**
	 * The maximal number of iterations that must be set 
	 * by adding this property with the annotation @MaxIterations.
	 */
	@MaxIterations
	protected int iterations = 100000;
    /**
     * The population size
     */
    protected int populationSize = 100;
    /**
     * The offspring size
     */
    protected int offSize = 25;
}
