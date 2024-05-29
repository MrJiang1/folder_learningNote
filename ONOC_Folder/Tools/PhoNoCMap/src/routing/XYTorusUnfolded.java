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

package routing;

import java.util.EmptyStackException;

import main.Globals;
import main.Routing;
import main.Topology;

/**
 * Provides the XY dimension order routing for mesh topologies.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class XYTorusUnfolded extends Routing{

	/* (non-Javadoc)
	 * @see main.Routing#calcPort(main.Topology, int, int)
	 */
	public int calcOutputPort(Topology topology, int src_tile, int dst_tile) {
		int port = -1;
		int src_x = topology.getTile(src_tile).getRow();
		int src_y = topology.getTile(src_tile).getColumn();
		int dst_x = topology.getTile(dst_tile).getRow();
		int dst_y = topology.getTile(dst_tile).getColumn();
		
		if (src_x == dst_x && src_y == dst_y){
			port = Globals.ejection;
		}
		else if (src_y != dst_y){
			if (src_y < dst_y){
				if(Math.abs(src_y-dst_y) <=((float)topology.getN()/2.0)){
					port = Globals.east;
				}
				else{
					port = Globals.west;
				}
			}
			else{
				if(Math.abs(src_y-dst_y) <=((float)topology.getN()/2.0)){
					port = Globals.west;
				}
				else{
					port = Globals.east;
				}
			}
		}
		else if (src_x != dst_x){
			if (src_x < dst_x){
				if(Math.abs(src_x-dst_x) <=((float)topology.getM()/2.0)){
					port = Globals.south;
				}
				else{
					port = Globals.north;
				}
			}
			else{
				if(Math.abs(src_x-dst_x) <=((float)topology.getM()/2.0)){
					port = Globals.north;
				}
				else{
					port = Globals.south;
				}
			}
		}
		else{
			System.out.println("Error: routing algorithm doesn't work");
        	throw new EmptyStackException();
		}
		return port;
	}
}
