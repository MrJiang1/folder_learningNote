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
 * Provides the unfolded torus topology.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class TorusUnfolded extends Topology
{

	private Integer	wgID;
	private Integer	crossID;

	/*
	 * (non-Javadoc)
	 * @see main.Topology#calcTile_list(java.util.HashMap, java.util.HashMap, java.util.HashMap)
	 */
	@Override
	public void calcTile_list(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		wgID = 0;
		crossID = 0;

		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			int row = (int) tileID / this.getN();
			int column = tileID % this.getN();
			tiles.put(tileID, new Tile(tileID, row, column));
		}

		createFrame(tiles, waveguides, crossings);
		createRows(tiles, waveguides, crossings);
		createColumns(tiles, waveguides, crossings);
		createColumns2(tiles, waveguides, crossings);
		createRows2(tiles, waveguides, crossings);
		closeRings(tiles, waveguides, crossings);
	}

	/**
	 * Creates the elements between tiles in a row
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 */
	private void createRows(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int i_tile = 0;
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 0; i < this.getM(); i++)
		{
			wgxTile(tiles, waveguides, i_tile, i_tile + 1, Globals.east, length);
			for (int y = 1; y < this.getN() - 1; y++)
			{
				Integer cID = crossID;
				wgID--;
				createCrosses(waveguides, crossings);
				wgID++;
				wgxTilexCrosses(tiles, waveguides, crossings, i_tile + y, i_tile + y + 1, cID, Globals.east, 0, length);
			}
			i_tile += this.getN();
		}
	}

	/**
	 * Creates the elements of the wrap arounds, by row
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 */
	private void createRows2(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		Integer i_tile = this.getN();
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 1; i < this.getM() - 1; i++)
		{
			Integer y_tile = i_tile + 1;

			Integer csID1 = tiles.get(i_tile).getOutWaveguide(Globals.south).getOutputCrossingID() - 2;
			Integer csID2 = tiles.get(y_tile).getOutWaveguide(Globals.south).getOutputCrossingID() - 2;

			wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.east, length);

			for (int y = 1; y < this.getN() - 1; y++)
			{
				csID1 = csID2;
				csID2 = tiles.get(y_tile).getInWaveguide(Globals.east).getInputCrossingID();
				csID2 = crossings.get(csID2).getWaveguideInput1();
				csID2 = waveguides.get(csID2).getInputCrossingID() - 1;
				wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.east, 0);
				y_tile++;
				csID1 = csID2;
				csID2 = tiles.get(y_tile).getOutWaveguide(Globals.south).getOutputCrossingID() - 2;
				wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.east, length);
			}

			i_tile += this.getN();
		}
	}

	/**
	 * Creates the elements between the tiles in a column
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 */
	private void createColumns(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int i_tile = 0;
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 0; i < this.getN(); i++)
		{
			wgxTile(tiles, waveguides, i_tile, i_tile + this.getN(), Globals.south, length);
			for (int y = 1; y < this.getM() - 1; y++)
			{
				Integer cID = crossID;
				wgID--;
				createCrosses(waveguides, crossings);
				wgID++;
				wgxTilexCrosses(tiles, waveguides, crossings, i_tile + (this.getN() * y), i_tile + (this.getN() * (y + 1)), cID, Globals.south, 0, length);
			}
			i_tile += 1;
		}
	}

	/**
	 * Creates the elements of wrap arounds, by column
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 */
	private void createColumns2(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 1; i < this.getN() - 1; i++)
		{
			Integer y_tile = i + this.getN();

			Integer csID1 = tiles.get(i).getOutWaveguide(Globals.east).getOutputCrossingID() - 1;
			Integer csID2 = tiles.get(y_tile).getOutWaveguide(Globals.east).getOutputCrossingID() - 1;

			wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.south, length);

			for (int y = 1; y < this.getM() - 1; y++)
			{
				y_tile += this.getN();
				csID1 = csID2;
				csID2 = crossID;
				wgID--;
				createCrosses(waveguides, crossings);
				wgID++;
				wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.south, 0);
				csID1 = csID2;
				csID2 = tiles.get(y_tile).getOutWaveguide(Globals.east).getOutputCrossingID() - 1;
				wgxCrosses2(waveguides, crossings, csID1, csID2, Globals.south, length);
			}
		}
	}

	/**
	 * Closes all rings
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 */
	private void closeRings(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		Integer i_tile;
		Integer y_tile = 1 + ((this.getM() - 1) * this.getN());

		for (int i = 1; i < this.getN() - 1; i++)
		{
			i_tile = i;
			Integer csID2 = tiles.get(i_tile).getOutWaveguide(Globals.east).getOutputCrossingID();
			Integer csID1 = csID2 + 1;
			wgxCrossxTile(tiles, waveguides, crossings, i_tile, csID1, csID2, Globals.north, false);

			csID1 = tiles.get(y_tile).getInWaveguide(Globals.east).getInputCrossingID();
			csID2 = csID1 + 1;
			wgxCrossxTile(tiles, waveguides, crossings, y_tile, csID1, csID2, Globals.south, false);
			y_tile++;
		}

		i_tile = this.getN();
		y_tile = i_tile + this.getN() - 1;

		for (int i = 1; i < this.getM() - 1; i++)
		{
			Integer csID1 = tiles.get(i_tile).getInWaveguide(Globals.south).getInputCrossingID();
			Integer csID2 = csID1 + 2;
			wgxCrossxTile(tiles, waveguides, crossings, i_tile, csID1, csID2, Globals.west, false);

			csID2 = tiles.get(y_tile).getOutWaveguide(Globals.south).getOutputCrossingID();
			csID1 = csID2 + 2;
			wgxCrossxTile(tiles, waveguides, crossings, y_tile, csID1, csID2, Globals.east, false);

			i_tile += this.getN();
			y_tile += this.getN();
		}
	}

	/**
	 * Closes a wrap around
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 * @param tileID
	 *            ID of the tile at which connect the waveguides
	 * @param crossID1
	 *            ID of the waveguide crossing to which arrives the waveguide
	 *            coming out from the tile
	 * @param crossID2
	 *            ID of the waveguide crossing from which starts the waveguide
	 *            coming in the tile
	 * @param port
	 *            port of enter/exit of the waveguides
	 */
	private void wgxCrossxTile(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, Integer tileID, Integer crossID1, Integer crossID2,
			Integer port, boolean border)
	{
		waveguides.put(wgID, new Waveguide(wgID, -1, crossID1));
		waveguides.get(wgID).setInputPort(tileID);
		tiles.get(tileID).setOutWaveguide(port, waveguides.get(wgID));
		if ((port == Globals.north || port == Globals.south))
		{
			if (!border)
				crossings.get(crossID1).setWaveguideInput1(wgID);
			else
				crossings.get(crossID1).setWaveguideInput0(wgID);

		}
		else
		{
			if (!border)
				crossings.get(crossID1).setWaveguideInput0(wgID);
			else
				crossings.get(crossID1).setWaveguideInput1(wgID);
		}
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, crossID2, -1));
		waveguides.get(wgID).setOutputPort(tileID);
		tiles.get(tileID).setInWaveguide(port, waveguides.get(wgID));
		if ((port == Globals.north || port == Globals.south))
		{
			if (!border)
				crossings.get(crossID2).setWaveguideOutput1(wgID);
			else
				crossings.get(crossID2).setWaveguideOutput0(wgID);
		}
		else
		{
			if (!border)
				crossings.get(crossID2).setWaveguideOutput0(wgID);
			else
				crossings.get(crossID2).setWaveguideOutput1(wgID);

		}
		wgID++;
	}

	/**
	 * Creates the elements of the wrap arounds surrounding the torus
	 * 
	 * @param tiles
	 *            The tile map
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The waveguide crossing map
	 */
	private void createFrame(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		// Create and link together the crossings sets
		createCrosses(waveguides, crossings);
		createCrosses(waveguides, crossings);
		createCrosses(waveguides, crossings);
		createCrosses(waveguides, crossings);
		wgID++;

		wgxCrossxTile(tiles, waveguides, crossings, 0, 4, 2, Globals.north, true);
		wgxCrossxTile(tiles, waveguides, crossings, 0, 3, 4, Globals.west, true);

		wgxCrossxTile(tiles, waveguides, crossings, this.getN() - 1, 5, 7, Globals.north, true);
		wgxCrossxTile(tiles, waveguides, crossings, this.getN() - 1, 7, 8, Globals.east, true);

		wgxCrossxTile(tiles, waveguides, crossings, this.getM() * this.getN() - 1, 9, 11, Globals.south, true);
		wgxCrossxTile(tiles, waveguides, crossings, this.getM() * this.getN() - 1, 10, 9, Globals.east, true);

		wgxCrossxTile(tiles, waveguides, crossings, this.getM() * this.getN() - this.getN(), 16, 14, Globals.south, true);
		wgxCrossxTile(tiles, waveguides, crossings, this.getM() * this.getN() - this.getN(), 14, 13, Globals.west, true);

		wgxCrosses(waveguides, crossings, 1, 6, 5, 2, Globals.north, length * (this.getN() - 1));
		wgxCrosses(waveguides, crossings, 6, 12, 10, 8, Globals.east, length * (this.getM() - 1));
		wgxCrosses(waveguides, crossings, 12, 15, 16, 11, Globals.south, length * (this.getN() - 1));
		wgxCrosses(waveguides, crossings, 15, 1, 3, 13, Globals.east, length * (this.getM() - 1));
	}

	/**
	 * Creates two waveguides that link a pair of tiles
	 * 
	 * @param tiles
	 *            HashMap of the tiles
	 * @param waveguides
	 *            HashMap of the waveguides
	 * @param tile_src
	 *            tile source
	 * @param tile_dest
	 *            tile destination
	 * @param output_port
	 *            exit port of the first waveguide. The second waveguide arrive
	 *            in the same port
	 */
	private void wgxTile(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, Integer tile_src, Integer tile_dest, Integer output_port, double length)
	{
		Integer input_port = 0;
		switch (output_port)
		{
			case Globals.north:
				input_port = Globals.south;
				break;
			case Globals.east:
				input_port = Globals.west;
				break;
			case Globals.south:
				input_port = Globals.north;
				break;
			case Globals.west:
				input_port = Globals.east;
				break;
			default:
				break;
		}

		waveguides.put(wgID, new Waveguide(wgID, -1, -1));
		waveguides.get(wgID).setInputPort(tile_src);
		waveguides.get(wgID).setOutputPort(tile_dest);
		waveguides.get(wgID).setLength(length);
		tiles.get(tile_src).setOutWaveguide(output_port, waveguides.get(wgID));
		tiles.get(tile_dest).setInWaveguide(input_port, waveguides.get(wgID));
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, -1, -1));
		waveguides.get(wgID).setInputPort(tile_dest);
		waveguides.get(wgID).setOutputPort(tile_src);
		waveguides.get(wgID).setLength(length);
		tiles.get(tile_dest).setOutWaveguide(input_port, waveguides.get(wgID));
		tiles.get(tile_src).setInWaveguide(output_port, waveguides.get(wgID));
		wgID++;
	}

	/**
	 * Connects two tiles with the four waveguide crossings between them
	 * 
	 * @param tiles
	 *            HashMap of the tiles
	 * @param waveguides
	 *            HashMap of the waveguides
	 * @param crossings
	 *            HashMap of the crossings
	 * @param tile_src
	 *            tile source
	 * @param tile_dest
	 *            tile destination
	 * @param cID
	 *            The offset of the crosses ID
	 * @param output_port
	 *            exit port of the first waveguide. The second waveguide arrive
	 *            in the same port
	 */
	private void wgxTilexCrosses(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, Integer tile_src, Integer tile_dest, Integer cID,
			Integer output_port, double length1, double length2)
	{
		Integer input_port = 0;
		Integer cross_dest1 = 0;
		Integer cross_dest2 = 0;
		Integer cross_src1 = 0;
		Integer cross_src2 = 0;

		switch (output_port)
		{
			case Globals.east:
				input_port = Globals.west;
				cross_dest1 = cID + 1;
				cross_dest2 = cID + 4;
				cross_src1 = cID + 2;
				cross_src2 = cID + 3;
				break;
			case Globals.south:
				input_port = Globals.north;
				cross_dest1 = cID + 2;
				cross_dest2 = cID + 3;
				cross_src1 = cID + 4;
				cross_src2 = cID + 1;
				break;
			default:
				break;
		}

		waveguides.put(wgID, new Waveguide(wgID, -1, cross_dest1));
		waveguides.get(wgID).setInputPort(tile_src);
		waveguides.get(wgID).setLength(length1);
		tiles.get(tile_src).setOutWaveguide(output_port, waveguides.get(wgID));
		if ((cross_dest1 == cID + 1) || (cross_dest1 == cID + 4))
			crossings.get(cross_dest1).setWaveguideInput0(wgID);
		else
			crossings.get(cross_dest1).setWaveguideInput1(wgID);
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, cross_src1, -1));
		waveguides.get(wgID).setOutputPort(tile_dest);
		waveguides.get(wgID).setLength(length2);
		tiles.get(tile_dest).setInWaveguide(input_port, waveguides.get(wgID));
		if ((cross_src1 == cID + 2) || (cross_src1 == cID + 3))
			crossings.get(cross_src1).setWaveguideOutput0(wgID);
		else
		{
			crossings.get(cross_src1).setWaveguideOutput1(wgID);
		}
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, cross_src2, -1));
		waveguides.get(wgID).setOutputPort(tile_src);
		waveguides.get(wgID).setLength(length1);
		tiles.get(tile_src).setInWaveguide(output_port, waveguides.get(wgID));
		if ((cross_src2 == cID + 2) || (cross_src2 == cID + 3))
			crossings.get(cross_src2).setWaveguideOutput0(wgID);
		else
		{
			crossings.get(cross_src2).setWaveguideOutput1(wgID);
		}
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, -1, cross_dest2));
		waveguides.get(wgID).setInputPort(tile_dest);
		waveguides.get(wgID).setLength(length2);
		tiles.get(tile_dest).setOutWaveguide(input_port, waveguides.get(wgID));
		if ((cross_dest2 == cID + 1) || (cross_dest2 == cID + 4))
			crossings.get(cross_dest2).setWaveguideInput0(wgID);
		else
			crossings.get(cross_dest2).setWaveguideInput1(wgID);
		wgID++;

	}

	/**
	 * Creates two waveguides that connect two pair of waveguide crossings
	 * 
	 * @param waveguides
	 *            The waveguide map
	 * @param crossings
	 *            The crossing map
	 * @param cID1
	 *            Offset of the waveguide crossing ID which the first waveguide
	 *            comes out
	 * @param cID2
	 *            Offset of the waveguide crossing ID which the first waveguide
	 *            arrives
	 * @param out_dir
	 *            direction of the first waveguide. The second one arrives from
	 *            the opposite side
	 */
	private void wgxCrosses2(HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, Integer cID1, Integer cID2, Integer out_dir, double length)
	{
		Integer cross_src1 = 0;
		Integer cross_dst1 = 0;
		Integer cross_src2 = 0;
		Integer cross_dst2 = 0;

		switch (out_dir)
		{
			case Globals.south:
				cross_src1 = cID1 + 4;
				cross_dst1 = cID2 + 2;
				cross_src2 = cID2 + 1;
				cross_dst2 = cID1 + 3;
				break;

			case Globals.east:
				cross_src1 = cID1 + 2;
				cross_dst1 = cID2 + 1;
				cross_src2 = cID2 + 3;
				cross_dst2 = cID1 + 4;
				break;

			default:
				break;
		}

		wgxCrosses(waveguides, crossings, cross_src1, cross_dst1, cross_src2, cross_dst2, out_dir, length);
	}

	/**
	 * Creates two waveguides that connect two pair of waveguide crossings
	 * 
	 * @param waveguides
	 * @param crossings
	 * @param cID1
	 *            Offset of the waveguide crossing which the first waveguide
	 *            comes out
	 * @param cID2
	 *            Offset of the waveguide crossing which the first waveguide
	 *            arrives
	 * @param cID3
	 *            Offset of the waveguide crossing which the second waveguide
	 *            comes out
	 * @param cID4
	 *            Offset of the waveguide crossing which the first waveguide
	 *            arrives
	 * @param out_dir
	 *            direction of the first waveguide. The second one arrives from
	 *            the opposite direction
	 */
	private void wgxCrosses(HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, Integer cID1, Integer cID2, Integer cID3, Integer cID4, Integer out_dir, double length)
	{
		waveguides.put(wgID, new Waveguide(wgID, cID1, cID2));
		waveguides.get(wgID).setLength(length);
		if (out_dir == Globals.south || out_dir == Globals.north)
		{
			crossings.get(cID1).setWaveguideOutput1(wgID);
			crossings.get(cID2).setWaveguideInput1(wgID);
		}
		else
		{
			crossings.get(cID1).setWaveguideOutput0(wgID);
			crossings.get(cID2).setWaveguideInput0(wgID);
		}
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, cID3, cID4));
		waveguides.get(wgID).setLength(length);
		if (out_dir == Globals.south || out_dir == Globals.north)
		{
			crossings.get(cID3).setWaveguideOutput1(wgID);
			crossings.get(cID4).setWaveguideInput1(wgID);
		}
		else
		{
			crossings.get(cID3).setWaveguideOutput0(wgID);
			crossings.get(cID4).setWaveguideInput0(wgID);
		}
		wgID++;
	}

	/**
	 * Creates four waveguide crossings and links them together with four
	 * waveguides
	 * 
	 * @param waveguides
	 *            HashMap of the waveguides
	 * @param crossings
	 *            HashMap of the crosses
	 */
	private void createCrosses(HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		crossings.put((1 + crossID), new Crossing(1 + crossID, -1, 4 + wgID, 1 + wgID, -1));
		crossings.put((2 + crossID), new Crossing(2 + crossID, 1 + wgID, -1, -1, 2 + wgID));
		crossings.put((3 + crossID), new Crossing(3 + crossID, 3 + wgID, -1, -1, 4 + wgID));
		crossings.put((4 + crossID), new Crossing(4 + crossID, -1, 2 + wgID, 3 + wgID, -1));

		waveguides.put(1 + wgID, new Waveguide(1 + wgID, 1 + crossID, 2 + crossID));
		waveguides.put(2 + wgID, new Waveguide(2 + wgID, 2 + crossID, 4 + crossID));
		waveguides.put(3 + wgID, new Waveguide(3 + wgID, 4 + crossID, 3 + crossID));
		waveguides.put(4 + wgID, new Waveguide(4 + wgID, 3 + crossID, 1 + crossID));

		wgID += 4;
		crossID += 4;
	}
}
