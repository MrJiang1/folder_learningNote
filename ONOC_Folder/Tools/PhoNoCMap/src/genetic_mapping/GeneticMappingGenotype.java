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

import main.Configuration;
import main.Utils;

import org.opt4j.core.Genotype;
/**
 * The custom genotype for the optimization
 * mapping problem.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class GeneticMappingGenotype implements Genotype {

	/**
	 * Constructs a new genotype with an empty
	 * mapping matrix
	 */
	public GeneticMappingGenotype(){
		this.num_cores = Configuration.getNum_cores();
		this.m = Configuration.getM();
		this.n = Configuration.getN();
	}

	/**
	 * Constructs a new genotype given a
	 * mapping matrix
	 * 
	 * @param mapping_matrix The mapping matrix
	 */
	public GeneticMappingGenotype(int[][] mapping_matrix) {
		this.num_cores = Configuration.getNum_cores();
		this.m = Configuration.getM();
		this.n = Configuration.getN();
    	this.genotype_mapping_matrix = Utils.cloneMatrix(mapping_matrix);
    }
	 
	/**
	 * Constructs a new genotype given a
	 * mapping matrix and optionally mutates 
	 * the matrix values
	 * 
	 * @param mapping_matrix The mapping matrix
	 * @param mutate <code>true</code> in order to randomly mutate the mapping matrix; 
	 * 			<code>false</code> otherwise
	 */
	public GeneticMappingGenotype(int[][] mapping_matrix, boolean mutate) {
		// qui si modifica un solo task non c'è for
		this.num_cores = Configuration.getNum_cores();
		this.m = Configuration.getM();
		this.n = Configuration.getN();
    	this.genotype_mapping_matrix = Utils.cloneMatrix(mapping_matrix);
	    if (mutate == true){
	    	int tile_i_x = (int)(Math.random()*this.m);
	    	int tile_i_y = (int)(Math.random()*this.n);
	    	while (this.genotype_mapping_matrix[tile_i_x][tile_i_y] == -1){
	    		tile_i_x = (int)(Math.random()*this.m);
		    	tile_i_y = (int)(Math.random()*this.n);
	    	}
	    	int tile_j_x = (int)(Math.random()*this.m);
	    	int tile_j_y = (int)(Math.random()*this.n);
	    	while (this.genotype_mapping_matrix[tile_j_x][tile_j_y] == -1 && 
	    			this.genotype_mapping_matrix[tile_i_x][tile_i_y] == this.genotype_mapping_matrix[tile_j_x][tile_j_y]){
	    		tile_j_x = (int)(Math.random()*this.m);
		    	tile_j_y = (int)(Math.random()*this.n);
	    	}
	    	int tmp = this.genotype_mapping_matrix[tile_i_x][tile_i_y];
	    	this.genotype_mapping_matrix[tile_i_x][tile_i_y] = this.genotype_mapping_matrix[tile_j_x][tile_j_y];
	    	this.genotype_mapping_matrix[tile_j_x][tile_j_y] = tmp;
	    }
	}
 
	
	/**
	 * Constructs a new genotype given two
	 * mapping matrices and using the cycle crossover.
	 * 
	 * @param phenotype_parent_i The mapping matrix of the first parent
	 * @param phenotype_parent_j The mapping matrix of the second parent
	 */
	public GeneticMappingGenotype(int[][] phenotype_parent_i, int[][] phenotype_parent_j) {
		this.num_cores = Configuration.getNum_cores();
		this.m = Configuration.getM();
		this.n = Configuration.getN();
		int numTiles = this.m*this.n;
		int[] parenti = new int[this.m*this.n];
		int[] parentj = new int[this.m*this.n];
		for(int i=0; i<this.m; i++){
			for(int j=0; j<this.n; j++){
				parenti[Math.max(this.m, this.n)*i+j] = phenotype_parent_i[i][j];
				parentj[Math.max(this.m, this.n)*i+j] = phenotype_parent_j[i][j];
			}
		}
		int[] child = new int[this.m*this.n];
		for(int i=0; i<numTiles; i++){
			child[i] = -2;
		}
		int cycle_index = 0;
		int first_core_index = 0;
		while(first_core_index < numTiles){
			child[first_core_index] = (cycle_index%2==0) ? parenti[first_core_index] : parentj[first_core_index];
			int index = first_core_index;
			while(parentj[index] != parenti[first_core_index]){
				int corej = parentj[index];
				while(parenti[index]!=corej || child[index]!=-2){
					index = (index+1)%numTiles;
				}
				if(cycle_index%2==0){
					child[index] = parenti[index];
				}
				else{
					child[index] = parentj[index];
				}
			}
			cycle_index++;
			while(first_core_index<numTiles && child[first_core_index]!=-2){
				first_core_index++;
			}
		}
		
		this.genotype_mapping_matrix = new int[this.m][this.n];
		for(int i=0; i<numTiles; i++){
			this.genotype_mapping_matrix[i/Math.max(this.m, this.n)][i%Math.max(this.m, this.n)] = child[i];
		}
		
	}

	/* (non-Javadoc)
	 * @see org.opt4j.core.Genotype#newInstance()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GeneticMappingGenotype newInstance(){
		this.genotype_mapping_matrix = new int[this.m][this.n];
		for(int i=0; i<this.m; i++){
			for(int j=0; j<this.n; j++){
				this.genotype_mapping_matrix[i][j] = -1;
			}
		}
		//levare che si inizia sempre dal core 0
		int core = 0;
		while (core < this.num_cores){
			int tile_row = (int)(Math.random()*this.m);
			int tile_column = (int)(Math.random()*this.n);
			if (this.genotype_mapping_matrix[tile_row][tile_column] == -1){
				this.genotype_mapping_matrix[tile_row][tile_column] = core;
				core++;
			}
		}
        return this;
	}

	 /* (non-Javadoc)
	 * @see org.opt4j.core.Genotype#size()
	 */
	@Override
	 public int size(){
		 return 0;
	}
	
	/**
	 * @return The mapping matrix
	 */
	public int[][] getGenotype_mapping_matrix() {
		return genotype_mapping_matrix;
	}
	
	public void printMatrix(){
		for(int i=0; i<this.m; i++){
			for(int j=0; j<this.n; j++){
				System.out.print(genotype_mapping_matrix[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	/**
	 * The number of cores
	 */
	private int num_cores;
	/**
	 * The number of tiles along the first dimension
	 */
	private int m;
	/**
	 * The number of tiles along the second dimension
	 */
	private int n;	
	/**
	 * The mapping matrix
	 */
	private int[][] genotype_mapping_matrix;
	
}
