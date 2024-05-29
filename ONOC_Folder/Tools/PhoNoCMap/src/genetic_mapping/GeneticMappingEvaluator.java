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

import main.Application;
import main.MappingEvaluator;
import main.MappingSolution;
import main.NoCarchitecture;

import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;
/**
 * The evaluator class used to determine the quality of one phenotype, 
 * i.e. the worst case power loss or SNR according to the mapping objective.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class GeneticMappingEvaluator implements Evaluator<int[][]>{

	/* (non-Javadoc)
	 * @see org.opt4j.core.problem.Evaluator#evaluate(java.lang.Object)
	 */
	public Objectives evaluate(int[][] mapping_matrix) {
		MappingSolution map = new MappingSolution(mapping_matrix);
		eval.evaluate(map);
		double score = eval.evaluate(map);
		Objectives objectives = new Objectives();
        objectives.add("objective", Sign.MAX, score);
        if (costo < score){
        	costo = score;
        	System.out.println("costo "+costo);
        }
        return objectives;
	}
	
	private NoCarchitecture arch = new NoCarchitecture();
	private Application app = new Application();
	private MappingEvaluator eval = new MappingEvaluator(arch, app);
	public static double costo = 0; 
}
