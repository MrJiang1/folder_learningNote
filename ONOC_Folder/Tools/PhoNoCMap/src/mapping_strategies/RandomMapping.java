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
package mapping_strategies;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import main.Application;
import main.Configuration;
import main.Globals;
import main.MappingEvaluator;
import main.MappingSolution;
import main.MappingStrategyInterface;
import main.NoCarchitecture;

/**
 * Provides the random search class. Basically, at each iteration,
 * a new mapping solution is calculated and evaluated and 
 * at the end the best candidate is chosen.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class RandomMapping implements MappingStrategyInterface{
	/**
	 * Constructs a RandomMapping object taking the total number 
	 * of iterations from the configuration object
	 */
	public RandomMapping(){
		this.iterationNum = Configuration.getIterations();
		this.scoreList = new String();
	}
	
	/* (non-Javadoc)
	 * @see main.MappingStrategyInterface#initialize(main.MappingEvaluator, javax.swing.JProgressBar)
	 */
	@Override
	public void initialize(MappingEvaluator eval, JProgressBar progressBar) {
		this.evaluator = eval;
		this.progressBar = progressBar;
	}
	
	/* (non-Javadoc)
	 * @see main.MappingStrategyInterface#map(main.Application, main.NoCarchitecture)
	 */
	@Override
	public MappingSolution map(Application app, NoCarchitecture arch) {
		int n=0;
		MappingSolution map = new MappingSolution(true);
		this.evaluator.evaluate(map);
		int progress_tmp = 0;
		int progress = 0;
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		while (n<iterationNum){
			progress = (int)((double)(n+1)/(double)iterationNum*100);
			if (progress != progress_tmp){
				progress_tmp = progress;
				final int value = progress;
				SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	progressBar.setValue(value+1);
                    }
                  });
			}
			MappingSolution map_tmp = new MappingSolution(true);
			double score = this.evaluator.evaluate(map_tmp);
			this.scoreList += score +"\n";
			if (Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveCrosstalk) == 0 && map.getSnrWC() < map_tmp.getSnrWC()){
				map = map_tmp;
				if (Configuration.getStopCondition()){
					n=0;
				}
			}
			else if (Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveLoss) == 0 && map.getSignalAttenuationWC() < map_tmp.getSignalAttenuationWC()){
				map = map_tmp;
				if (Configuration.getStopCondition()){
					n=0;
				}
			}
			n++;
			iterationPerformed++;
		}
		endTime = System.currentTimeMillis();
		this.executionTime = endTime-startTime;
		return map;
	}
	
	/**
	 * @return The list of all the evaluations 
	 * of the mapping solutions.
	 */
	public String getScoreList() {
		return scoreList;
	}

	/*
	 * (non-Javadoc)
	 * @see main.MappingStrategyInterface#getReadableRepresentation()
	 */
	@Override
	public String getReadableRepresentation(){
		String values = "***** Optimization *****\n";
		values+="Genetic Algorithm\n";
		values+="iterations "+iterationPerformed+"\n";
		values+="execution time "+executionTime+"\n";
		return values;
	}
	
	/**
	 * The number of iterations
	 */
	private int iterationNum;
	/**
	 * The mapping evaluator
	 */
	private MappingEvaluator evaluator;
	/**
	 * A String object containing all the evaluations 
	 * of the mapping solutions found during the 
	 * random mapping search. It is useful to find the 
	 * distribution of this values (power loss or SNR).
	 */
	private String scoreList;
	/**
	 * The component that visually displays the progress of the mapping search.
	 */
	private JProgressBar progressBar;
	/**
	 * The execution time
	 */
	private long executionTime;
	/**
	 * The number of performed iterations
	 */
	private long iterationPerformed;
}