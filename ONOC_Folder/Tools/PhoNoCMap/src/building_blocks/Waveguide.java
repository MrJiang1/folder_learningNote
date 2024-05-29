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

import java.util.ArrayList;

import main.BuildingBlock;

/**
 * Provides the waveguide building block. A waveguide is placed between 
 * two crossings or between a crossing and an input/output port. The waveguide 
 * could be an imput waveguide of a parallel PSE. In such a case,
 * the microring resonator ID should be specified.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Waveguide extends BuildingBlock{

	/**
	 * Constructs a waveguide object given an identifier number.
	 * 
	 * @param id the waveguide building block identifier number
	 */
	public Waveguide(int id){
		this(id,-1,-1);
	}
	
	/**
	 * Constructs a waveguide object connecting two waveguide crossings.
	 * 
	 * @param id the waveguide building block identifier number
	 * @param inputCrossingID the input waveguide crossings ID
	 * @param outputCrossingID the output waveguide crossings ID
	 */
	public Waveguide(int id, int inputCrossingID, int outputCrossingID){
		super(id);
		this.inputCrossingID = inputCrossingID;
		this.outputCrossingID = outputCrossingID;
		this.microringPPSE = null;
		this.inputPort = -1;
		this.outputPort = -1;
		this.length = 0;
	}
	
	/**
	 * Constructs a waveguide object connecting two waveguide crossings.
	 * 
	 * @param id the waveguide building block identifier number
	 * @param inputCrossingID the input waveguide crossings ID
	 * @param outputCrossingID the output waveguide crossings ID
	 * @param length the length of this waveguide. This value is used to calculate the propagation loss.
	 */
	public Waveguide(int id, int inputCrossingID, int outputCrossingID, double length)
	{
		this(id, inputCrossingID, outputCrossingID);
		this.length = length;
	}

	/**
	 * @return the input waveguide crossings ID
	 */
	public int getInputCrossingID() {
		return inputCrossingID;
	}
	/**
	 * @return the output waveguide crossings ID
	 */
	public int getOutputCrossingID() {
		return outputCrossingID;
	}
	/**
	 * 
	 * @param index The index of the microring in the arrayList
	 * @return the ID of the microring resonator 
	 * used for a parallel PSE given its index
	 */
	public int getMicroringPPSE(int index) {
		if (microringPPSE != null)
			return microringPPSE.get(index);
		else
			return -1;
	}
	/**
	 * @param microringPPSE the ID of the microring resonator 
	 * used for a parallel PSE
	 */
	public void setMicroringPPSE(int microringPPSE) {
		if (this.microringPPSE == null)
			this.microringPPSE = new ArrayList<Integer>();

		this.microringPPSE.add(microringPPSE);
	}
	/**
	 * @return the number of microring resonators used for the 
	 * PPSEs where this waveguide acts as input/output
	 */
	public int getNumMicroringPPSE()
	{
		if (!(microringPPSE == null))
			return this.microringPPSE.size();
		else
			return 0;
	}
	/**
	 * @return the input port in case of an 
	 * input waveguide
	 */
	public int getInputPort() {
		return inputPort;
	}
	/**
	 * @param input_port the input port in case of an 
	 * input waveguide
	 */
	public void setInputPort(int input_port) {
		this.inputPort = input_port;
	}
	/**
	 * @return the output port in case of an 
	 * output waveguide
	 */
	public int getOutputPort() {
		return outputPort;
	}
	/**
	 * @param output_port the output port in case of an 
	 * output waveguide
	 */
	public void setOutputPort(int output_port) {
		this.outputPort = output_port;
	}
	
	/**
	 * @return the length of this waveguide
	 */
	public double getLength()
	{
		return this.length;
	}

	/**
	 * @param length the value to assign for the length of this waveguide
	 */
	public void setLength(double length)
	{
		this.length = length;
	}
	
	/**
	* The input waveguide crossings ID
	*/
	private int inputCrossingID;
	/**
	* The output waveguide crossings ID
	*/
	private int outputCrossingID;
	/**
	* The microring resonators 
	* used for a parallel PSE where this waveguide acts as
	* input or output waveguide.
	*/
	private ArrayList<Integer>	microringPPSE;
	/**
	* The input port connected to this waveguide
	*/
	private int inputPort;
	/**
	* The output port connected to this waveguide
	*/
	private int outputPort;
	/**
	 * The length of this waveguide
	 */
	private double length;
}
