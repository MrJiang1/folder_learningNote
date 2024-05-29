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
 * Provides the folded torus topology.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class TorusFolded extends Topology
{
	int	wgID;
	int	crossID;

	/*
	 * (non-Javadoc)
	 * @see main.Topology#calcTile_list(java.util.HashMap, java.util.HashMap, java.util.HashMap)
	 */
	@Override
	public void calcTile_list(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		wgID = 0;
		crossID = -1;

		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			int row = (int) tileID / this.getN();
			int column = tileID % this.getN();
			tiles.put(tileID, new Tile(tileID, row, column));
		}

		createRows(tiles, waveguides, crossings);
		createColumns(tiles, waveguides, crossings);
	}

	
	private void createFirstRow(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{

		int csOffset;
		int[] csOff1;
		int[] csOff2;
		int tileOffset = (this.getN() / 2) - 1 - ((this.getN() + 1) % 2);
		double l = Utils.calcHopDistance(this.getM(), this.getN());
		double[] lengths = new double[4];

		// First line
		lengths[0] = 0;
		lengths[1] = 2 * l;

		for (int i = 0; i < (this.getN() / 2) - ((this.getN() + 1) % 2); i++)
		{
			csOffset = this.crossID;
			wgxTileByRow(tiles, waveguides, crossings, 1, Globals.east, Globals.west, i, i + 1, lengths);
			wgxTilexCross(tiles, waveguides, crossings, i, csOffset + 2, csOffset + 1, Globals.north, 0);
		}

		// Right turn
		lengths[1] = l;
		lengths[2] = 0;
		wgxTileByRow(tiles, waveguides, crossings, 2, Globals.east, Globals.east, (this.getN() / 2) - ((this.getN() + 1) % 2), (this.getN() / 2) + (this.getN() % 2), lengths);
		csOffset = getCrossIDOffsetByRow(tiles, waveguides, crossings, (this.getN() / 2) - ((this.getN() + 1) % 2), Globals.east, 1)[0];
		wgxTilexCross(tiles, waveguides, crossings, (this.getN() / 2) - ((this.getN() + 1) % 2), csOffset + 2, csOffset + 1, Globals.south, 0);

		// Second line
		lengths[0] = l;
		lengths[1] = 0;
		lengths[2] = l;
		lengths[3] = 0;
		for (int i = 0; i < (this.getN() / 2) - 1; i++)
		{
			wgxTileByRow(tiles, waveguides, crossings, 3, Globals.west, Globals.east, (this.getN() / 2) + (this.getN() % 2) + i, (this.getN() / 2) + (this.getN() % 2) + i + 1, lengths);
			csOffset = getCrossIDOffsetByRow(tiles, waveguides, crossings, (this.getN() / 2) + (this.getN() % 2) + i, Globals.east, 1)[0];
			wgxTilexCross(tiles, waveguides, crossings, (this.getN() / 2) + (this.getN() % 2) + i, csOffset + 2, csOffset + 1, Globals.north, 0);

			csOff1 = getCrossIDOffsetByRow(tiles, waveguides, crossings, tileOffset - i, Globals.east, 1);
			csOff2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, (this.getN() / 2) + (this.getN() % 2) + i, Globals.west, 2);
			wgxCrosses(waveguides, crossings, csOff1[0] + 4, csOff2[0] + 2, csOff2[0] + 1, csOff1[0] + 3, 1, 0);
			wgxTilexCross(tiles, waveguides, crossings, tileOffset - i, csOff2[1] + 2, csOff2[1] + 1, Globals.south, 0);
		}

		csOffset = getCrossIDOffsetByRow(tiles, waveguides, crossings, this.getN() - 1, Globals.east, 1)[0];
		wgxTilexCross(tiles, waveguides, crossings, this.getN() - 1, csOffset + 2, csOffset + 1, Globals.north, 0);

		// Left turn
		if (this.getN() % 2 == 0)
			wgxTile(tiles, waveguides, this.getN() - 1, 0, Globals.west, Globals.west, l);
		else
		{
			lengths[0] = l;
			lengths[1] = 0;
			lengths[2] = 0;
			wgxTileByRow(tiles, waveguides, crossings, 2, Globals.west, Globals.west, this.getN() - 1, 0, lengths);
			csOff1 = getCrossIDOffsetByRow(tiles, waveguides, crossings, 0, Globals.east, 1);
			csOff2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, 0, Globals.west, 2);
			wgxCrosses(waveguides, crossings, csOff1[0] + 4, csOff2[1] + 2, csOff2[1] + 1, csOff1[0] + 3, 1, 0);
			wgxTilexCross(tiles, waveguides, crossings, 0, csOff2[0] + 2, csOff2[0] + 1, Globals.south, 0);
		}
	}

	private void createLastRow(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int offset = (this.getM() - 1) * this.getN();
		int[] csID;
		int[] csID2;
		double l = Utils.calcHopDistance(this.getM(), this.getN());
		double[] lengths = new double[4];

		// First line
		if (this.getM() % 2 == 0)
		{
			lengths[0] = l;
			lengths[1] = 0;
			lengths[2] = l;
			lengths[3] = 0;
		}
		else
		{
			lengths[0] = 0;
			lengths[1] = l;
			lengths[2] = 0;
			lengths[3] = l;
		}

		for (int i = 0; i < (this.getN() / 2) - ((this.getN() + 1) % 2); i++)
			wgxTileByRow(tiles, waveguides, crossings, 3, Globals.east, Globals.west, offset + i, offset + i + 1, lengths);

		// Right turn
		lengths[0] = 0;
		lengths[1] = l;
		lengths[2] = 0;
		if (this.getM() % 2 == 0)
			wgxTile(tiles, waveguides, offset + (this.getN() / 2) - ((this.getN() + 1) % 2), offset + (this.getN() / 2) + (this.getN() % 2), Globals.east, Globals.east, l);
		else
			wgxTileByRow(tiles, waveguides, crossings, 2, Globals.east, Globals.east, offset + (this.getN() / 2) - ((this.getN() + 1) % 2), offset + (this.getN() / 2) + (this.getN() % 2), lengths);

		// Second line
		if (this.getM() % 2 == 0)
		{
			lengths[0] = 0;
			lengths[1] = 2 * l;
		}
		else
		{
			lengths[0] = 2 * l;
			lengths[1] = 0;
		}
		for (int i = 0; i < (this.getN() / 2) - 1; i++)
			wgxTileByRow(tiles, waveguides, crossings, 1, Globals.west, Globals.east, offset + (this.getN() / 2) + (this.getN() % 2) + i, offset + (this.getN() / 2) + (this.getN() % 2) + i + 1,
					lengths);

		// Left turn
		if (this.getN() % 2 == 0)
		{

			if (this.getM() % 2 == 0)
			{
				lengths[0] = 0;
				lengths[1] = l;
				lengths[2] = 0;
			}
			else
			{
				lengths[0] = 0;
				lengths[1] = 0;
				lengths[2] = l;
			}
			wgxTileByRow(tiles, waveguides, crossings, 2, Globals.west, Globals.west, offset + this.getN() - 1, offset, lengths);

		}
		else
		{
			lengths[0] = 0;
			lengths[1] = l;
			lengths[2] = 0;
			if (this.getM() % 2 == 0)
				wgxTileByRow(tiles, waveguides, crossings, 2, Globals.west, Globals.west, offset + (this.getN() - 1), offset, lengths);
			else
				wgxTile(tiles, waveguides, offset + (this.getN() - 1), offset, Globals.west, Globals.west, l);
		}

		// Close wrap around paths
		if (this.getM() % 2 == 0)
		{
			for (int i = 0; i < this.getN() / 2; i++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + (this.getN() % 2) + i, Globals.west, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + (this.getN() % 2) + i, csID[0] + 3, csID[0] + 4, Globals.south, 0);
			}
			for (int i = 0; i < this.getN() / 2 - ((this.getN() + 1) % 2); i++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 - ((this.getN() + 1) % 2) - i, Globals.west, 3);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 + (this.getN() % 2) + i, Globals.west, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() / 2 + (this.getN() % 2) + i, csID2[0] + 3, csID2[0] + 4, Globals.south, 0);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() / 2 + (this.getN() % 2) + i, csID[1] + 3, csID[1] + 4, Globals.north, 0);
				wgxCrosses(waveguides, crossings, csID[2] + 4, csID2[0] + 2, csID2[0] + 1, csID[2] + 3, 1, 0);
			}

			if (this.getN() % 2 == 0)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.west, 2);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() - 1, csID[1] + 3, csID[1] + 4, Globals.north, 0);
			}
			else
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() - 1, Globals.west, 2);
				wgxTilexCross(tiles, waveguides, crossings, offset, csID[1] + 2, csID[1] + 1, Globals.south, 0);
			}

		}
		else
		{

			for (int i = 0; i < (this.getN() / 2) - ((this.getN() + 1) % 2); i++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + i, Globals.east, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + i, csID[0] + 3, csID[0] + 4, Globals.south, 0);
			}
			csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + (this.getN() / 2) - ((this.getN() + 1) % 2), Globals.east, 1);
			wgxTilexCross(tiles, waveguides, crossings, offset + (this.getN() / 2) - ((this.getN() + 1) % 2), csID[0] + 2, csID[0] + 1, Globals.south, 0);

			for (int y = 0; y < this.getN() / 2; y++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 - ((this.getN() + 1) % 2) - y, Globals.west, 2);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 + ((this.getN()) % 2) + y, Globals.east, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() / 2 + (this.getN() % 2) + y, csID2[0] + 3, csID2[0] + 4, Globals.south, 0);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() / 2 + (this.getN() % 2) + y, csID[1] + 3, csID[1] + 4, Globals.north, 0);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, 0);
			}

		}
	}

	private void createRows(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int offset2 = (this.getN() / 2) + (this.getN() % 2);
		int[] csID;
		int[] csID2;
		double l = Utils.calcHopDistance(this.getM(), this.getN());
		double[] lengths = new double[4];

		createFirstRow(tiles, waveguides, crossings);
		for (int i = 1; i < this.getM() - 1; i++)
		{
			int offset = this.getN() * i;

			// First line
			if (i % 2 != 0)
			{
				lengths[0] = l;
				lengths[1] = 0;
				lengths[2] = l;
				lengths[3] = 0;
			}
			else
			{
				lengths[0] = 0;
				lengths[1] = l;
				lengths[2] = 0;
				lengths[3] = l;
			}
			for (int y = 0; y < (this.getN() / 2) - ((this.getN() + 1) % 2); y++)
				wgxTileByRow(tiles, waveguides, crossings, 3, Globals.east, Globals.west, y + offset, y + offset + 1, lengths);

			// Right turn
			if (i % 2 != 0)
			{
				lengths[0] = 0;
				lengths[1] = 0;
				lengths[2] = l;
			}
			else
			{
				lengths[0] = 0;
				lengths[1] = l;
				lengths[2] = 0;
			}
			wgxTileByRow(tiles, waveguides, crossings, 2, Globals.east, Globals.east, (this.getN() / 2) - ((this.getN() + 1) % 2) + offset, offset2 + offset, lengths);

			// Second line
			if (i % 2 != 0)
			{
				lengths[0] = 0;
				lengths[1] = l;
				lengths[2] = 0;
				lengths[3] = l;
			}
			else
			{
				lengths[0] = l;
				lengths[1] = 0;
				lengths[2] = l;
				lengths[3] = 0;
			}
			for (int y = 0; y < (this.getN() / 2) - 1; y++)
				wgxTileByRow(tiles, waveguides, crossings, 3, Globals.west, Globals.east, offset2 + y + offset, offset2 + y + offset + 1, lengths);

			// Left turn

			if (i % 2 != 0)
			{
				lengths[0] = 0;
				lengths[1] = l;
				lengths[2] = 0;
			}
			else
			{
				lengths[0] = 0;
				lengths[1] = 0;
				lengths[2] = l;
			}
			wgxTileByRow(tiles, waveguides, crossings, 2, Globals.west, Globals.west, offset + this.getN() - 1, offset, lengths);

			if (this.getN() % 2 == 0)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() - 1, Globals.west, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() - 1, csID[0] + 3, csID[0] + 4, Globals.north, 0);
				int offset3 = offset + this.getN() - 2;

				for (int z = 0; z < (this.getN() - 2) / 2; z++)
				{
					csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + z, Globals.west, 1);
					csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset3 - z, Globals.west, 3);
					wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[2] + 2, csID2[2] + 1, csID[0] + 3, 1, 0);
					wgxTilexCross(tiles, waveguides, crossings, offset + z, csID2[1] + 2, csID2[1] + 1, Globals.south, 0);
					csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + z, Globals.east, 2);
					wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, 0);
					wgxTilexCross(tiles, waveguides, crossings, offset3 - z, csID[1] + 3, csID[1] + 4, Globals.north, 0);
				}

			}
			else
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 2);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.west, 2);
				int offset3 = offset + this.getN() - 1;
				wgxTilexCross(tiles, waveguides, crossings, offset, csID2[0] + 2, csID2[0] + 1, Globals.south, 0);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[1] + 2, csID2[1] + 1, csID[0] + 3, 1, 0);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() - 1, csID[1] + 3, csID[1] + 4, Globals.north, 0);

				for (int z = 1; z < ((this.getN() - 3) / 2) + 1; z++)
				{
					csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + z, Globals.west, 1);
					csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset3 - z, Globals.west, 3);
					wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[2] + 2, csID2[2] + 1, csID[0] + 3, 1, 0);
					wgxTilexCross(tiles, waveguides, crossings, offset + z, csID2[1] + 2, csID2[1] + 1, Globals.south, 0);
					csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + z, Globals.east, 2);
					wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, 0);
					wgxTilexCross(tiles, waveguides, crossings, offset3 - z, csID[1] + 3, csID[1] + 4, Globals.north, 0);
				}
			}
			csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 - ((this.getN() + 1) % 2), Globals.west, 1);
			csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN() / 2 - ((this.getN() + 1) % 2), Globals.east, 2);
			wgxTilexCross(tiles, waveguides, crossings, offset + this.getN() / 2 - ((this.getN() + 1) % 2), csID2[0] + 2, csID2[0] + 1, Globals.south, 0);
			wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[1] + 2, csID2[1] + 1, csID[0] + 3, 1, 0);
		}
		createLastRow(tiles, waveguides, crossings);
	}

	private void createColumns(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int offset = (this.getN() % 2 == 0) ? 0 : 1;
		int offset2 = (this.getN() % 2 == 0) ? this.getN() - 2 : this.getN() - 1;

		createFirstColumn(tiles, waveguides, crossings);
		for (int i = 0; i < this.getN() - 2; i++)
		{
			if (i % 2 == 0)
			{
				if (this.getN() % 2 == 0)
				{
					columnA(tiles, waveguides, crossings, offset++);
				}
				else
				{
					columnB(tiles, waveguides, crossings, offset2--);
				}
			}
			else
			{
				if (this.getN() % 2 == 0)
				{
					columnB(tiles, waveguides, crossings, offset2--);
				}
				else
				{
					columnA(tiles, waveguides, crossings, offset++);
				}
			}
		}
		createLastColumn(tiles, waveguides, crossings);
	}

	private void createFirstColumn(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int offset = (this.getN() % 2 == 0) ? this.getN() - 1 : 0;
		int[] csID;
		int[] csID2;
		double l = Utils.calcHopDistance(this.getM(), this.getN());

		if (this.getN() % 2 == 0)
		{
			for (int i = 0; i < this.getM() / 2 - ((this.getM() + 1) % 2); i++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 1);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.west, 1);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, l);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + (2 * this.getN()), Globals.west, 2);
				wgxTilexCross(tiles, waveguides, crossings, offset, csID2[0] + 2, csID2[0] + 1, Globals.south, 2 * l);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN(), csID2[1] + 2, csID2[1] + 1, Globals.south, l);
				offset += 2 * this.getN();
			}

			if (this.getM() % 2 == 0)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 1);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.west, 1);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, l);
				wgxTile(tiles, waveguides, offset, offset + this.getN(), Globals.south, Globals.south, l);
			}
		}
		else
		{
			for (int i = 0; i < this.getM() / 2 - ((this.getM() + 1) % 2); i++)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.west, 2);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN(), csID[1] + 3, csID[1] + 4, Globals.north, l);
				wgxTilexCross(tiles, waveguides, crossings, offset + (2 * this.getN()), csID[0] + 3, csID[0] + 4, Globals.north, 2 * l);
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.west, 1);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + (2 * this.getN()), Globals.east, 1);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, l);
				offset += 2 * this.getN();
			}

			if (this.getM() % 2 == 0)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.west, 2);
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.west, 1);
				wgxTilexCross(tiles, waveguides, crossings, offset + this.getN(), csID[1] + 3, csID[1] + 4, Globals.north, l);
				wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 3, csID2[0] + 4, csID[0] + 3, 1, l);
			}
		}
	}

	private void columnA(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, int offset)
	{
		int csID;
		int csID2;
		int offset2 = offset + this.getN();
		double l = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 0; i < this.getM() - 1; i++)
		{
			if (i % 2 == 0)
			{
				csID = tiles.get(offset).getOutWaveguide(Globals.south).getOutputCrossingID() - 2;
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset2, Globals.west, 1)[0];
				wgxCrosses(waveguides, crossings, csID + 4, csID2 + 2, csID2 + 1, csID + 3, 1, l);
				csID -= 4;
				wgxTilexCross(tiles, waveguides, crossings, offset2, csID + 3, csID + 4, Globals.north, l);
				offset += 2 * this.getN();
			}
			else
			{
				csID = tiles.get(offset2).getOutWaveguide(Globals.south).getOutputCrossingID() - 2;
				csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 1)[0];
				wgxCrosses(waveguides, crossings, csID + 4, csID2 + 2, csID2 + 1, csID + 3, 1, l);
				csID += 4;
				wgxTilexCross(tiles, waveguides, crossings, offset, csID + 3, csID + 4, Globals.north, l);
				offset2 += 2 * this.getN();
			}
		}
	}

	private void columnB(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, int offset)
	{
		int csID;
		int csID2;
		int offset2 = offset + this.getN();
		double l = Utils.calcHopDistance(this.getM(), this.getN());

		for (int i = 0; i < this.getM() - 1; i++)
		{
			if (i % 2 == 0)
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 1)[0];
				csID2 = tiles.get(offset2).getOutWaveguide(Globals.north).getOutputCrossingID() - 3;
				wgxCrosses(waveguides, crossings, csID + 4, csID2 + 2, csID2 + 1, csID + 3, 1, l);
				csID2 -= 4;
				wgxTilexCross(tiles, waveguides, crossings, offset, csID2 + 2, csID2 + 1, Globals.south, l);
				offset += 2 * this.getN();
			}
			else
			{
				csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset2, Globals.west, 1)[0];
				csID2 = tiles.get(offset).getOutWaveguide(Globals.north).getOutputCrossingID() - 3;
				wgxCrosses(waveguides, crossings, csID + 4, csID2 + 2, csID2 + 1, csID + 3, 1, l);
				csID2 += 4;
				wgxTilexCross(tiles, waveguides, crossings, offset2, csID2 + 2, csID2 + 1, Globals.south, l);
				offset2 += 2 * this.getN();
			}
		}
	}

	private void createLastColumn(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings)
	{
		int offset = this.getN() / 2 - ((this.getN() + 1) % 2);
		int[] csID;
		int[] csID2;
		double l = Utils.calcHopDistance(this.getM(), this.getN());

		wgxTile(tiles, waveguides, offset, offset + this.getN(), Globals.north, Globals.north, l);
		csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 1);
		csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.west, 1);
		wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, l);
		offset += this.getN();
		for (int i = 0; i < this.getM() / 2 - 1; i++)
		{
			csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 2);
			wgxTilexCross(tiles, waveguides, crossings, offset + this.getN(), csID[1] + 3, csID[1] + 4, Globals.north, l);
			wgxTilexCross(tiles, waveguides, crossings, offset + (2 * this.getN()), csID[0] + 3, csID[0] + 4, Globals.north, 2 * l);
			csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.east, 1);
			csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + (2 * this.getN()), Globals.west, 1);
			wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 2, csID2[0] + 1, csID[0] + 3, 1, l);
			offset += 2 * this.getN();
		}

		if (this.getM() % 2 == 1)
		{
			csID = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset, Globals.east, 2);
			csID2 = getCrossIDOffsetByRow(tiles, waveguides, crossings, offset + this.getN(), Globals.east, 1);
			wgxTilexCross(tiles, waveguides, crossings, offset + this.getN(), csID[1] + 3, csID[1] + 4, Globals.north, l);
			wgxCrosses(waveguides, crossings, csID[0] + 4, csID2[0] + 3, csID2[0] + 4, csID[0] + 3, 1, l);

		}
	}

	private void wgxTileByRow(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, int num, int inp, int outp, int src, int dst,
			double[] lengths)
	{
		int csID = this.crossID;

		this.wgID--;
		for (int i = 0; i < num; i++)
			createCrosses(waveguides, crossings);
		this.wgID++;

		if ((inp == Globals.east && outp == Globals.west) || (inp == Globals.west && outp == Globals.west && this.getN() % 2 == 0))
		{
			for (int i = 0; i < num - 1; i++)
				wgxCrosses(waveguides, crossings, csID + 2 + (4 * i), csID + 1 + (4 * (i + 1)), csID + 3 + (4 * (i + 1)), csID + 4 + (4 * i), 0, lengths[i + 1]);

			wgxTilexCross(tiles, waveguides, crossings, src, csID + 1, csID + 3, inp, lengths[0]);
			wgxTilexCross(tiles, waveguides, crossings, dst, csID + 4 + (4 * (num - 1)), csID + 2 + (4 * (num - 1)), outp, lengths[num]);
		}
		else if ((inp == Globals.west && outp == Globals.east) || (inp == Globals.east && outp == Globals.east) || (inp == Globals.west && outp == Globals.west))
		{
			for (int i = 0; i < num - 1; i++)
				wgxCrosses(waveguides, crossings, csID + 2 + (4 * (i + 1)), csID + 1 + (4 * i), csID + 3 + (4 * i), csID + 4 + (4 * (i + 1)), 0, lengths[i + 1]);

			wgxTilexCross(tiles, waveguides, crossings, src, csID + 4, csID + 2, inp, lengths[0]);
			wgxTilexCross(tiles, waveguides, crossings, dst, csID + 1 + (4 * (num - 1)), csID + 3 + (4 * (num - 1)), outp, lengths[num]);
		}

	}

	private int[] getCrossIDOffsetByRow(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, int tile, int port, int num)
	{
		int[] id = new int[num];
		id[0] = tiles.get(tile).getOutWaveguide(port).getOutputCrossingID();
		int csID = id[0];
		int tmp = (waveguides.get(crossings.get(csID).getWaveguideOutput0()).getOutputCrossingID() == csID + 1) ? -1 : -4;

		for (int i = 0; i < 2 * (num - 1); i++)
		{
			csID = waveguides.get(crossings.get(csID).getWaveguideOutput0()).getOutputCrossingID();
			if (i % 2 == 1)
				id[(i + 1) / 2] = csID;
		}
		for (int i = 0; i < num; i++)
			id[i] += tmp;
		return id;
	}

	private void wgxTilexCross(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, Integer tileID, Integer crossID1, Integer crossID2,
			Integer port, double length)
	{
		waveguides.put(wgID, new Waveguide(wgID, -1, crossID1));
		waveguides.get(wgID).setInputPort(tileID);
		waveguides.get(wgID).setLength(length);
		tiles.get(tileID).setOutWaveguide(port, waveguides.get(wgID));
		if ((port == Globals.north || port == Globals.south))
			crossings.get(crossID1).setWaveguideInput1(wgID);
		else
			crossings.get(crossID1).setWaveguideInput0(wgID);
		wgID++;

		waveguides.put(wgID, new Waveguide(wgID, crossID2, -1));
		waveguides.get(wgID).setOutputPort(tileID);
		waveguides.get(wgID).setLength(length);
		tiles.get(tileID).setInWaveguide(port, waveguides.get(wgID));
		if ((port == Globals.north || port == Globals.south))
			crossings.get(crossID2).setWaveguideOutput1(wgID);
		else
			crossings.get(crossID2).setWaveguideOutput0(wgID);
		wgID++;
	}

	private void wgxTile(HashMap<Integer, Tile> tiles, HashMap<Integer, Waveguide> waveguides, Integer tile_src, Integer tile_dest, int input_port, int output_port, double length)
	{
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

	// dir == 1 -> in1/out1 else in0/out0
	private void wgxCrosses(HashMap<Integer, Waveguide> waveguides, HashMap<Integer, Crossing> crossings, int cID1, int cID2, int cID3, int cID4, int dir, double length)
	{
		if (dir == 1)
		{
			waveguides.put(wgID, new Waveguide(wgID, cID1, cID2));
			waveguides.get(wgID).setLength(length);
			crossings.get(cID1).setWaveguideOutput1(wgID);
			crossings.get(cID2).setWaveguideInput1(wgID);
			wgID++;

			waveguides.put(wgID, new Waveguide(wgID, cID3, cID4));
			waveguides.get(wgID).setLength(length);
			crossings.get(cID3).setWaveguideOutput1(wgID);
			crossings.get(cID4).setWaveguideInput1(wgID);
			wgID++;
		}
		else
		{
			waveguides.put(wgID, new Waveguide(wgID, cID1, cID2));
			waveguides.get(wgID).setLength(length);
			crossings.get(cID1).setWaveguideOutput0(wgID);
			crossings.get(cID2).setWaveguideInput0(wgID);
			wgID++;

			waveguides.put(wgID, new Waveguide(wgID, cID3, cID4));
			waveguides.get(wgID).setLength(length);
			crossings.get(cID3).setWaveguideOutput0(wgID);
			crossings.get(cID4).setWaveguideInput0(wgID);
			wgID++;
		}
	}

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
