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

/**
 * Contains some useful mnemonic values that are
 * required along all the java code.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public abstract class Globals {
	public static final int injection = 0;
	public static final int ejection = 0;
	public static final int north = 1;
	public static final int east = 2;
	public static final int south = 3;
	public static final int west = 4;
	
	public static final int destination = 0;
	public static final int source = 1;
	public static final int source_and_destination = 2;
	
	public static final int topology_mesh = 0;
	
	public static final int mapping_random = 0;
	
	public static final int routing_xy = 0;
	
	public static final int router_crux = 0;
	
	public static final int pse_type = 0;
	public static final int cse_type = 1;
	
	public static final int vertical = 0;
	public static final int horizontal = 0;
	
	public static final int in = 0;
	public static final int out = 1;
	
	public static final int port = 0;
	public static final int waveguide = 1;
	public static final int microring = 2;
	public static final int crossing = 3;
	
	public static final String geneticMapping = "GeneticMapping";
	public static final String randomMapping = "RandomMapping";
	public static final String mappingObjectiveCrosstalk = "Crosstalk";
	public static final String mappingObjectiveLoss = "Power loss";
	public static final String mappingObjectiveLaserPower = "Laser power";
}
