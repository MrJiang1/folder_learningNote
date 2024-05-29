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

import java.util.EmptyStackException;

/**
 * Is the abstract base class for all the custom routing
 * algorithms defined in the routing package. 
 * A custom routing algorithm that extends this abstract class must implement the 
 * initialize method in order to proper set the different router
 * architectures.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public abstract class Routing {
	/**
	 * Evaluates the output port ID required to reach the next hop.
	 * It should be called in every hop along the path.
	 * 
	 * @param top The topology
	 * @param src The tile of the optical router
	 * @param dst The destination tile
	 * @return The ID of the output port
	 */
	public abstract int calcOutputPort(Topology top, int src, int dst);
	
	/**
	 * Calculates the input port used by a message to reach
	 * the current hop along the path between a source and 
	 * a destination.
	 * 
	 * @param top The topology
	 * @param src The source tile of the optical router
	 * @param dst The destination tile
	 * 
	 * @return The input port
	 */
	public int calcInputPort(Topology top, int src, int dst)
	{
		return this.calcInputPortFromOutputPort(this.calcOutputPort(top, src, dst));
	}
	
	
	/**
	 * Calculates the input port connected to a certain output port
	 * 
	 * @param p_out An output port
	 * @return The input port
	 */
	private int calcInputPortFromOutputPort(int p_out){
		switch (p_out){
		case Globals.north:
			return Globals.south;
		case Globals.east:
			return Globals.west;
		case Globals.south:
			return Globals.north;
		case Globals.west:
			return Globals.east;
		default: 
			System.out.println("Error: calcInputPortFromOutputPort -> not possible for port "+p_out);
        	throw new EmptyStackException();
		}
	}
}
