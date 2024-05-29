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

import building_blocks.Waveguide;

/**
 * Is the class containing all the tile information. 
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Tile {
	
	/**
	 * Constructs a Tile object. It requires the ID and 
	 * some topological information, i.e. the row and column
	 * where the tile is located.
	 * 
	 * @param id The tile ID
	 * @param row The row
	 * @param column The column
	 */
	public Tile(int id, int row, int column){
		this.id = id;
		this.row = row;
		this.column = column;
		this.inWaveguides = new HashMap<Integer, Waveguide>();
		this.outWaveguides = new HashMap<Integer, Waveguide>();
		
	}
	
	/**
	 * @return a String object containing all the 
	 * main information about the tile.
	 */
	public String getValues(){
		String values = "";
		values+=("ID: "+this.id+"\tposition: ["+this.row+","+this.column+"] input waveguides ->"+
				"\tN: "+(this.inWaveguides.containsKey(Globals.north) == true ? this.inWaveguides.get(Globals.north).getID() : "" )+
				"\tE: "+(this.inWaveguides.containsKey(Globals.east) == true ? this.inWaveguides.get(Globals.east).getID() : "" )+
				"\tS: "+(this.inWaveguides.containsKey(Globals.south) == true ? this.inWaveguides.get(Globals.south).getID() : "" )+
				"\tW: "+(this.inWaveguides.containsKey(Globals.west) == true ? this.inWaveguides.get(Globals.west).getID() : "" ) +
				" output waveguides ->"+
				"\tN: "+(this.outWaveguides.containsKey(Globals.north) == true ? this.outWaveguides.get(Globals.north).getID() : "" )+
				"\tE: "+(this.outWaveguides.containsKey(Globals.east) == true ? this.outWaveguides.get(Globals.east).getID() : "" )+
				"\tS: "+(this.outWaveguides.containsKey(Globals.south) == true ? this.outWaveguides.get(Globals.south).getID() : "" )+
				"\tW: "+(this.outWaveguides.containsKey(Globals.west) == true ? this.outWaveguides.get(Globals.west).getID() : "" )+
				"\n");
		return values;
	}
	
	/**
	 * Prints formatted representations of the object 
	 * to a text-output stream. 
	 */
	public void print(){
		System.out.println("ID: "+this.id+"\tposition: ["+this.row+","+this.column+"] input waveguides ->"+
				"\tN: "+(this.inWaveguides.containsKey(Globals.north) == true ? this.inWaveguides.get(Globals.north).getID() : "" )+
				"\tE: "+(this.inWaveguides.containsKey(Globals.east) == true ? this.inWaveguides.get(Globals.east).getID() : "" )+
				"\tS: "+(this.inWaveguides.containsKey(Globals.south) == true ? this.inWaveguides.get(Globals.south).getID() : "" )+
				"\tW: "+(this.inWaveguides.containsKey(Globals.west) == true ? this.inWaveguides.get(Globals.west).getID() : "" ) +
				" output waveguides ->"+
				"\tN: "+(this.outWaveguides.containsKey(Globals.north) == true ? this.outWaveguides.get(Globals.north).getID() : "" )+
				"\tE: "+(this.outWaveguides.containsKey(Globals.east) == true ? this.outWaveguides.get(Globals.east).getID() : "" )+
				"\tS: "+(this.outWaveguides.containsKey(Globals.south) == true ? this.outWaveguides.get(Globals.south).getID() : "" )+
				"\tW: "+(this.outWaveguides.containsKey(Globals.west) == true ? this.outWaveguides.get(Globals.west).getID() : "" )+
				"\n");
	}
	
	/**
	 * @return The tile identifier
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return The row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @return The column
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param p The input port
	 * @return The waveguide connected to the port p
	 */
	public Waveguide getInWaveguide(int p) {
		return inWaveguides.get(p);
	}
	/**
	 * Connects the input port p with the waveguide w.
	 * 
	 * @param p The input port
	 * @param w The waveguide
	 */
	public void setInWaveguide(int p, Waveguide w) {
		this.inWaveguides.put(p, w);
	}
	/**
	 * @param p The output port
	 * @return waveguide connected to the port p
	 */
	public Waveguide getOutWaveguide(int p) {
		return outWaveguides.get(p);
	}
	/**
	 * Connects the output port p with the waveguide w.
	 * 
	 * @param p The output port
	 * @param w The waveguide
	 */
	public void setOutWaveguide(int p, Waveguide w) {
		this.outWaveguides.put(p, w);
	}
	
	/**
	 * The tile ID
	 */
	private int id;
	/**
	 * The row in the topology where this tile is located
	 */
	private int row;
	/**
	 * The column in the topology where this tile is located
	 */
	private int column;
	/**
	 * The input waveguide that are connected to this tile
	 */
	private HashMap<Integer, Waveguide> inWaveguides;
	/**
	 * The output waveguide that are connected to this tile
	 */
	private HashMap<Integer, Waveguide> outWaveguides;
}
