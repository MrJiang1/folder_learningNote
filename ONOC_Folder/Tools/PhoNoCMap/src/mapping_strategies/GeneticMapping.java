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

import java.nio.channels.ClosedByInterruptException;

import genetic_mapping.GeneticMappingModule;
import genetic_mapping.GeneticMappingOptimizerModule;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import main.Application;
import main.Configuration;
import main.MappingEvaluator;
import main.MappingSolution;
import main.MappingStrategyInterface;
import main.NoCarchitecture;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;

/**
 * Provides the genetic algorithm class for the mapping problem
 * as defined in the following paper:
 * <p>
 * E. Fusella and A. Cilardo, “Crosstalk-aware mapping for tile-based
 * optical network-on-chip,” in High Performance Computing and
 * Communications, 2015 IEEE 7th Intl Symp on Cyberspace Safety
 * and Security, 2015 IEEE 12th Intl Conf on Embedded Software and
 * Syst (HPCC, CSS, ICESS), 2015 IEEE Intl Conf on. IEEE, 2015
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class GeneticMapping implements MappingStrategyInterface{
	/**
	 * Constructs a GeneticMapping object taking the required 
	 * information from the configuration object
	 */
	public GeneticMapping(){
		this.iterationNum = Configuration.getIterations();
		this.populationSize = Configuration.getPopulationSize();
		this.offSpringSize = Configuration.getOffspringSize();
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
	public MappingSolution map(Application app, NoCarchitecture arch){
		MappingSolution map = new MappingSolution(true);
		this.iterationPerformed = this.iterationNum;
		long startTime = System.currentTimeMillis();
		if (!Configuration.getStopCondition()){
			this.evolutionarySearch(app, arch, map);
		}
		else{
			this.evolutionarySearchIncrementalStop(app, arch, map);
		}
		long endTime = System.currentTimeMillis();
		this.executionTime = endTime-startTime;
		return map;
	}

	/**
	 * Finds the best mapping solution using a stop criterion based on
	 * the total number of iterations.
	 * 
	 * @param app The application
	 * @param arch The NoC architecture
	 * @param map The mapping solution
	 */
	private void evolutionarySearch(Application app, NoCarchitecture arch, MappingSolution map) {
		final int iterations = this.iterationNum;
		GeneticMappingOptimizerModule sop = new GeneticMappingOptimizerModule();
		GeneticMappingModule mapping_problem = new GeneticMappingModule();
		sop.setIterations(iterations);
		sop.setOffSize(this.offSpringSize);
		sop.setPopulationSize(this.populationSize);
		final Opt4JTask task = new Opt4JTask(false);
		task.init(sop, mapping_problem);
		
		int[][] mapping_matrix = null;
		
		new Thread() {
		    public void run() {
		    	while(task.getIteration()<iterations){
			    	try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	final int progress = (int)((double)task.getIteration()/(double)iterationNum*100);			    	
			    	SwingUtilities.invokeLater(new Runnable() {
			    		public void run() {
	                    	progressBar.setValue(progress);
	                    }
	                });
		    	}
		    }
		}.start();
		
		try {
			task.execute();
			Archive archive = task.getInstance(Archive.class);
			for (Individual individual : archive) {
				mapping_matrix = (int[][]) individual.getPhenotype();
			}
			map.setMappingMatrix(mapping_matrix, app, arch);
			this.evaluator.evaluate(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			task.close();
		}
	}
	
	/**
	 * Finds the best mapping solution using a stop criterion based 
	 * on distance convergence. Basically, we stop the evolution 
	 * when there is no longer any appreciable improvement in the 
	 * consecutive phenotypes of the new populations that are being created
	 * for a certain number of iterations.
	 * 
	 * @param app The application
	 * @param arch The NoC architecture
	 * @param map The mapping solution
	 */
	@SuppressWarnings("deprecation")
	private void evolutionarySearchIncrementalStop(Application app, NoCarchitecture arch, MappingSolution map) {
		final int iterations = this.iterationNum;
		GeneticMappingOptimizerModule sop = new GeneticMappingOptimizerModule();
		GeneticMappingModule mapping_problem = new GeneticMappingModule();
		sop.setOffSize(this.offSpringSize);
		sop.setPopulationSize(this.populationSize);
		final Opt4JTask task = new Opt4JTask(false);
		task.init(sop, mapping_problem);
		
		int[][] mapping_matrix = null;
		
		new Thread() {
		    public void run() {
		    	while(iterations_without_gain<iterations){
			    	try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	final int progress = (int)((double)iterations_without_gain/(double)iterationNum*100);
			    	SwingUtilities.invokeLater(new Runnable() {
			    		public void run() {
	                    	progressBar.setValue(progress);
	                    }
	                });
		    	}
		    }
		}.start();
		
		Worker p = new Worker(task);
		Thread thread = new Thread(p);
		thread.start();
		
		while (task.getIteration() < 1) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		double score = Double.NEGATIVE_INFINITY;
		
		int last_iteration_with_gain = 0;
		while(iterations_without_gain < iterations){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Archive archive = task.getInstance(Archive.class);
			int[][] map_tmp = null;
			for (Individual individual : archive) {
				map_tmp = (int[][]) individual.getPhenotype();
			}
			map.setMappingMatrix(map_tmp, app, arch);
			double score_tmp = evaluator.evaluate(map);
			if(score<score_tmp){
				score = score_tmp;
				iterations_without_gain=0;
				last_iteration_with_gain=task.getIteration();
			}
			else{
				iterations_without_gain = task.getIteration()-last_iteration_with_gain;
			}
		}
		this.iterationPerformed = last_iteration_with_gain;
		
		Archive archive = task.getInstance(Archive.class);
		for (Individual individual : archive) {
			mapping_matrix = (int[][]) individual.getPhenotype();
		}
		map = new MappingSolution(mapping_matrix);
		this.evaluator.evaluate(map);
		task.close();
		thread.stop();
	}
	
	
	private class Worker implements Runnable {
		private Opt4JTask task;

		public Worker(Opt4JTask tsk) {
			task = tsk;
		}

		public void run() {
			try {
				task.execute();
			} catch (ThreadDeath e) {
				// System.out.println("** ThreadDeath from thread");
			} catch (InterruptedException e) {
				System.out.println("** InterruptedException from thread");
			} catch (RuntimeException e) {
				System.out.println("** RuntimeException from thread");
			} catch (ClosedByInterruptException e) {
				System.out.println("** ClosedByInterruptException from thread");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	 * The current number of iterations without a gain in the mapping objective
	 */
	private int iterations_without_gain = 0;
	/**
	 * The number of iterations
	 */
	private int iterationNum;
	/**
	 * The population size
	 */
	private int populationSize;
	/**
	 * The offspring size
	 */
	private int offSpringSize;
	/**
	 * The mapping evaluator
	 */
	private MappingEvaluator evaluator;
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
