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

import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import main.Application;
import main.Configuration;
import main.Globals;
import main.MappingEvaluator;
import main.MappingSolution;
import main.MappingStrategyInterface;
import main.NoCarchitecture;
import main.Tile;

/**
 * Provides the list mapping class. Basically, it tries, at each step, 
 * to make the best move as possible within a list of admitted moves, 
 * i.e. the moves consisting on swapping the tasks mapped onto two different tiles. 
 * When it arrives to a local maximum (a point not having better neighboring solutions) 
 * it records the solution and generates another random starting point 
 * in the hope of falling in a different region of attraction. 
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class ListMapping implements MappingStrategyInterface{
	/**
	 * Constructs a RandomMapping object taking the total number 
	 * of iterations from the configuration object.
	 * 
	 */
	public ListMapping(){
		this.iterationNum = Configuration.getIterations();
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
	public MappingSolution map(Application app, NoCarchitecture arch){
		int n=0;
		MappingSolution map = new MappingSolution(true);
		MappingSolution map_return = new MappingSolution(map.getMapping_matrix());
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
			boolean stop = false;
			if(Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveCrosstalk) == 0 && map.getTaskListWC().isEmpty()){
				stop = true;
			}
			while (!stop){
				stop = true;
				ArrayList<Tile> tileToMove = new ArrayList<Tile>();
				MappingSolution map_best = new MappingSolution(map.getMapping_matrix());
				tileToMove.add(map.getTileFromCore(map.getSourceCoreWC(), arch));
				tileToMove.add(map.getTileFromCore(map.getDestinationCoreWC(), arch));
				for(int i=0; i<map.getTaskListWC().size(); i++){
					tileToMove.add(map.getTileFromCore(map.getTaskListWC().get(i).getSrcCore(), arch));
					tileToMove.add(map.getTileFromCore(map.getTaskListWC().get(i).getDstCore(), arch));
				}
				for(int i=0; i<tileToMove.size(); i++){
					for(int tileID=0; tileID<arch.getTopology().getNumTiles(); tileID++){
						if (tileID != tileToMove.get(i).getId()){
							MappingSolution map_tmp = new MappingSolution(map.getMapping_matrix());
							int coreID_tmp = map_tmp.getMapping_matrix()[tileToMove.get(i).getRow()][tileToMove.get(i).getColumn()];
							map_tmp.getMapping_matrix()[tileToMove.get(i).getRow()][tileToMove.get(i).getColumn()] = 
									map_tmp.getMapping_matrix()[arch.getTopology().getTile(tileID).getRow()][arch.getTopology().getTile(tileID).getColumn()];
							map_tmp.getMapping_matrix()[arch.getTopology().getTile(tileID).getRow()][arch.getTopology().getTile(tileID).getColumn()] = coreID_tmp;
							if(this.evaluator.evaluate(map_tmp) > this.evaluator.evaluate(map_best)){
								map_best = map_tmp;
							}
						}
					}
				}
				if(this.evaluator.evaluate(map_best) > this.evaluator.evaluate(map)){
					map = map_best;
					stop = false;
				}
			}
			if(this.evaluator.evaluate(map) > this.evaluator.evaluate(map_return)){
				map_return = map;
				if (Configuration.getStopCondition()){
					n=0;
				}
			}
			map = new MappingSolution(true);
			this.evaluator.evaluate(map);
			n++;
		}
		endTime = System.currentTimeMillis();
		this.executionTime = endTime-startTime;
		return map_return;
	}
	
	/*
	 * (non-Javadoc)
	 * @see main.MappingStrategyInterface#getReadableRepresentation()
	 */
	@Override
	public String getReadableRepresentation(){
		String values = "***** Optimization *****\n";
		values+="Genetic Algorithm\n";
		values+="iterations "+iterationNum+"\n";
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
	 * The component that visually displays the progress of the mapping search.
	 */
	private JProgressBar progressBar;
	/**
	 * The execution time
	 */
	private long executionTime;
}
