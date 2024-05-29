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
package topologies;

import java.util.HashMap;

import building_blocks.Crossing;
import building_blocks.Waveguide;
import main.Globals;
import main.Tile;
import main.Topology;
import main.Utils;

/**
 * Provides the mesh topology.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Mesh extends Topology
{

	/*
	 * (non-Javadoc)
	 * @see main.Topology#calcTile_list(java.util.HashMap, java.util.HashMap, java.util.HashMap)
	 */
	@Override
	public void calcTile_list(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int waveguideID = 0;
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			int row = (int) tileID / this.getN();
			int column = tileID % this.getN();
			tiles.put(tileID, new Tile(tileID, row, column));
		}
		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			if (tiles.get(tileID).getRow() > 0)
			{
				Waveguide waveguide = new Waveguide(waveguideID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID - this.getN());
				waveguide.setLength(length);
				waveguides.put(waveguideID, waveguide);
				waveguideID++;
				tiles.get(tileID).setOutWaveguide(Globals.north, waveguide);
				tiles.get(tileID - this.getN()).setInWaveguide(Globals.south, waveguide);
			}
			if (tiles.get(tileID).getColumn() < this.getN() - 1)
			{
				Waveguide waveguide = new Waveguide(waveguideID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID + 1);
				waveguide.setLength(length);
				waveguides.put(waveguideID, waveguide);
				waveguideID++;
				tiles.get(tileID).setOutWaveguide(Globals.east, waveguide);
				tiles.get(tileID + 1).setInWaveguide(Globals.west, waveguide);
			}
			if (tiles.get(tileID).getRow() < this.getM() - 1)
			{
				Waveguide waveguide = new Waveguide(waveguideID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID + this.getN());
				waveguide.setLength(length);
				waveguides.put(waveguideID, waveguide);
				waveguideID++;
				tiles.get(tileID).setOutWaveguide(Globals.south, waveguide);
				tiles.get(tileID + this.getN()).setInWaveguide(Globals.north, waveguide);
			}
			if (tiles.get(tileID).getColumn() > 0)
			{
				Waveguide waveguide = new Waveguide(waveguideID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID - 1);
				waveguide.setLength(length);
				waveguides.put(waveguideID, waveguide);
				waveguideID++;
				tiles.get(tileID).setOutWaveguide(Globals.west, waveguide);
				tiles.get(tileID - 1).setInWaveguide(Globals.east, waveguide);
			}
		}
	}
}
