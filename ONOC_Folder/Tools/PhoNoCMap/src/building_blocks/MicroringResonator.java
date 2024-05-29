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
package building_blocks;

import main.BuildingBlock;

/**
 * Provides the microring resonator building block that provides
 * the necessary switching functions. There are two types of microring
 * resonators: parallel, when placed between two parallel waveguides; 
 * crossing, when placed at the intersection of two waveguides (crossing).
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class MicroringResonator extends BuildingBlock{
	
	/**
	 * Constructs a microring resonator object connecting two waveguides.
	 * 
	 * @param id the microring resonator building block identifier number
	 * @param type the microring resonator type
	 * @param inputWaveguideID the input waveguide ID
	 * @param outputWaveguideID the output waveguide ID
	 * @param directionON the port that is reached in case of ON
	 * resonance state
	 */
	public MicroringResonator(int id, int type, int inputWaveguideID, int outputWaveguideID, int directionON){
		super(id);
		this.type = type;
		this.inputWaveguideID = inputWaveguideID;
		this.outputWaveguideID = outputWaveguideID;
		this.directionON = directionON;
	}

	/**
	 * @return the microring resonator type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @return the input waveguide ID
	 */
	public int getInputWaveguideID() {
		return inputWaveguideID;
	}
	/**
	 * @return the output waveguide ID
	 */
	public int getOutputWaveguideID() {
		return outputWaveguideID;
	}
	/**
	 * @return the port that is reached in case of ON
	 * resonance state
	 */
	public int getDirectionON() {
		return directionON;
	}
	
	/**
	* The microring resonator type: 0 for parallel PSE and
	* 1 for crossing PSE
	*/
	private int type;
	/**
	* The input waveguide identifier number
	*/
	private int inputWaveguideID;
	/**
	* The output waveguide identifier number
	*/
	private int outputWaveguideID;
	/**
	* The port that is reached in case of ON resonance state
	*/
	private int directionON;
}