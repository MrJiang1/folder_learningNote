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
package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a collection of miscellaneous utility methods.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Utils {
		
	/**
	 * Calculates the average hop distance given the topology size.
	 * 
	 * @param m The number of tiles in the first dimension of the topology
	 * @param n The number of tiles in the second dimension of the topology
	 * @return The average hop distance (cm)
	 */
	public static double calcHopDistance(int m, int n){
		return Math.sqrt((Configuration.getChip_size()/100)/((m-1)*(n-1)));
	}
	
	/**
	 * Adds two decibel values.
	 * 
	 * @param a The first term of the sum (dB)
	 * @param b The second term of the sum (dB)
	 * @return The sum (dB)
	 */
	public static double sumDB(double a, double b){
		return Utils.valToDb(Utils.dbToVal(a) + Utils.dbToVal(b));
	}

	/**
	 * Converts from dB
	 * 
	 * @param db a value in decibel
	 * @return the converted value
	 */
	public static double dbToVal(double db){
		return Math.pow(10, (db*0.1));
	}
	
	/**
	 * Converts into dB
	 * 
	 * @param val a value
	 * @return the converted value (dB)
	 */
	public static double valToDb(double val){
		return 10*Math.log10(val);
	}
	
	/**
	 * Calculates the signal-to-noise ratio (SNR)
	 * 
	 * @param signal_attenuation The signal power
	 * @param noise_attenuation The noise power
	 * @return The signal-to-noise ratio (SNR)
	 */
	public static final double calcSNR(double signal_attenuation, double noise_attenuation){
		if (noise_attenuation==0){
			return Double.POSITIVE_INFINITY;
		}
		return (signal_attenuation-noise_attenuation);
	}
	
	/**
	 * Clones a matrix of integer.
	 * 
	 * @param m The matrix
	 * @return The cloned matrix
	 */
	public static int[][] cloneMatrix(int m[][]) {
		int[][] newM = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				newM[i][j] = m[i][j];
			}
		}
		return newM;
	}
	
	/**
	 * Clones an ArrayList.
	 * 
	 * @param a The ArrayList to clone
	 * @return The cloned ArrayList
	 */
	public static ArrayList<BuildingBlock> cloneArrayList(List<BuildingBlock> a) {
		ArrayList<BuildingBlock> clone = new ArrayList<BuildingBlock>();
	    for(BuildingBlock item: a) clone.add(item);
	    return clone;
	}
	
	/**
	 * Calculates a unique identifier given two features 
	 * and the total number of items.
	 * 
	 * @param i The first feature
	 * @param j The second feature
	 * @param tot The total number of items
	 * @return The identifier
	 */
	public static int calcID(int i, int j, int tot){
		return i*tot+j;
	}
}
