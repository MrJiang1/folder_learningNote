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
 * Provides the crossing building block that is required
 * to enable the intersection between four waveguides. 
 * Each Crossing object must connect two input and two output 
 * waveguides. Optionally, a crossing could have one or two 
 * microring resonators allowing non default paths.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Crossing extends BuildingBlock{
	
	/**
	 * Constructs a Crossing object connecting four waveguides.
	 * 
	 * @param id the crossing building block identifier number
	 * @param waveguideInput0 the first input waveguide ID
	 * @param waveguideInput1 the second input waveguide ID
	 * @param waveguideOutput0 the first output waveguide ID
	 * @param waveguideOutput1 the second output waveguide ID
	 */
	public Crossing(int id, int waveguideInput0, int waveguideInput1, int waveguideOutput0, int waveguideOutput1){
		super(id);
		this.waveguideInput0 = waveguideInput0;
		this.waveguideInput1 = waveguideInput1;
		this.waveguideOutput0 = waveguideOutput0;
		this.waveguideOutput1 = waveguideOutput1;
		this.microringIn0Out1 = -1;
		this.microringIn1Out0 = -1;
	}

	/**
	 * @return the first input waveguide ID
	 */
	public int getWaveguideInput0() {
		return waveguideInput0;
	}
	/**
	 * @return the second input waveguide ID
	 */
	public int getWaveguideInput1() {
		return waveguideInput1;
	}
	/**
	 * @return the first output waveguide ID
	 */
	public int getWaveguideOutput0() {
		return waveguideOutput0;
	}
	/**
	 * @return the first output waveguide ID
	 */
	public int getWaveguideOutput1() {
		return waveguideOutput1;
	}
	/**
	 * @return the ID of the microring resonator between 
	 * the first input waveguide and the second output waveguide
	 */
	public int getMicroringIn0Out1() {
		return microringIn0Out1;
	}
	/**
	 * @return the ID of the microring resonator between 
	 * the second input waveguide and the first output waveguide
	 */
	public int getMicroringIn1Out0() {
		return microringIn1Out0;
	}
	/**
	 * @param microringIn0Out1 the ID of the microring resonator 
	 * between the first input waveguide and the 
	 * second output waveguide to set
	 */
	public void setMicroringIn0Out1(int microringIn0Out1) {
		this.microringIn0Out1 = microringIn0Out1;
	}
	/**
	 * @param microringIn1Out0 the ID of the microring resonator 
	 * between the second input waveguide
	 * and the first output waveguide to set
	 */
	public void setMicroringIn1Out0(int microringIn1Out0) {
		this.microringIn1Out0 = microringIn1Out0;
	}
	/**
	 * @param wgID the first input waveguide ID
	 */
	public void setWaveguideInput0(int wgID)
	{
		this.waveguideInput0 = wgID;
	}
	/**
	 * @param wgID the second input waveguide ID
	 */
	public void setWaveguideInput1(int wgID)
	{
		this.waveguideInput1 = wgID;
	}
	/**
	 * @param wgID the first output waveguide ID
	 */
	public void setWaveguideOutput0(int wgID)
	{
		this.waveguideOutput0 = wgID;
	}
	/**
	 * @param wgID the second output waveguide ID
	 */
	public void setWaveguideOutput1(int wgID)
	{
		this.waveguideOutput1 = wgID;
	}
	/**
	 * The first input waveguide identifier number
	 */
	private int waveguideInput0;
	/**
	 * The second input waveguide identifier number
	 */
	private int waveguideInput1;
	/**
	 * The first output waveguide identifier number
	 */
	private int waveguideOutput0;
	/**
	 * The second output waveguide identifier number
	 */
	private int waveguideOutput1;
	/**
	 * The identifier number of the microring resonator between 
	 * the first input waveguide and the second output waveguide
	 */
	private int microringIn0Out1;
	/**
	 * The identifier number of the microring resonator between 
	 * the second input waveguide and the first output waveguide
	 */
	private int microringIn1Out0;
}
