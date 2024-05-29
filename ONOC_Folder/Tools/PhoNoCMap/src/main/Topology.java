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

import java.util.HashMap;
import building_blocks.Crossing;
import building_blocks.Waveguide;


/**
 * Is the abstract base class for all the custom topology
 * defined in the topologies package.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public abstract class Topology {
	/**
	 * Constructs a direct topology given the number of row and column
	 * defined in the Configuration file. It calls the calcTile_list
	 * that must be implemented in every custom topology class.
	 */
	public Topology(){
		this.m = Configuration.getM();
		this.n = Configuration.getN();
		this.numTiles = this.m*this.n;
		this.tiles = new HashMap<Integer, Tile>();
		this.waveguides = new HashMap<Integer, Waveguide>();
		this.crossings = new HashMap<Integer, Crossing>();
		this.calcTile_list(this.tiles, this.waveguides, this.crossings);		
	}
	
	/**
	 * Initializes the topology object by filling all the required
	 * data structures. This method must be implemented in every 
	 * custom topology class. This method must set three class 
	 * variables that every topology should customize according
	 * to its inner architecture:
	 * <ul>
	 * <li>tiles, the map containing the tile objects
	 * <li>waveguides, the map containing the waveguide objects
	 * <li>crossings, the map containing the crossing objects
	 * </ul>
	 * 
	 * @param tiles The tile map
	 * @param waveguides The map containing the waveguides 
	 * @param crossings The map containing the waveguide crossings
	 */
	abstract public void calcTile_list(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings);

	/**
	 * @return a String object containing all the 
	 * main information about the topology.
	 */
	public String getReadableRepresentation(){
		String values = "***** Topology *****\n";
		values += ("dimensions: " + this.m + "x" + this.n+"\n");
		values += ("number of tiles: " + this.numTiles+"\n");
		values += ("Tile list:\n");
		for(int i=0; i<this.numTiles; i++){
			values += this.tiles.get(i).getValues();
		}
		return values;
	}
	
	/**
	 * Prints formatted representations of the object 
	 * to a text-output stream. 
	 */
	public void print(){
		System.out.println("Topology");
		System.out.println("dimensions: " + this.m + "x" + this.n);
		System.out.println("number of tiles: " + this.numTiles);
		System.out.println("Tile list:");
		for(int i=0; i<this.numTiles; i++){
			this.tiles.get(i).print();
		}
	}
	
	/**
	 * @return The number of tiles in the first dimension
	 */
	public int getM() {
		return m;
	}
	/**
	 * @return The number of tiles in the second dimension
	 */
	public int getN() {
		return n;
	}
	/**
	 * @return The number of tiles
	 */
	public int getNumTiles() {
		return numTiles;
	}
	/**
	 * @param id The tile ID
	 * @return A tile given its ID
	 */
	public Tile getTile(int id) {
		return tiles.get(id);
	}
	/**
	 * @return The tile map
	 */
	public HashMap<Integer, Tile> getTiles() {
		return tiles;
	}
	/**
	 * @param id The waveguide crossing ID
	 * @return A waveguide crossing given its ID
	 */
	public Crossing getCrossing(int id) {
		return crossings.get(id);
	}

	/**
	 * @param id The waveguide ID
	 * @return A waveguide given its ID
	 */
	public Waveguide getWaveguide(int id) {
		return waveguides.get(id);
	}
	
	/**
	 * The number of tiles in the first dimension
	 */
	private int m;
	/**
	 * The number of tiles in the second dimension
	 */
	private int n;
	/**
	 * The number of tiles
	 */
	private int numTiles;
	/**
	 * The map containing the tile objects
	 */
	private HashMap<Integer, Tile> tiles; 
	/**
	 * The map containing the waveguide objects
	 */
	private HashMap<Integer, Waveguide> waveguides;
	/**
	 * The map containing the crossing objects
	 */
	private HashMap<Integer, Crossing> crossings;
}
