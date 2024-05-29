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
 * Provides the unfolded optimized torus topology presented in the 
 * following paper:
 * <p>
 * K. Feng, Y. Ye, and J. Xu, “A formal study on topology and 
 * floorplan characteristics of mesh and torus-based optical 
 * networks-on-chip,” Microprocessors and Microsystems, vol. 
 * 37, no. 8, pp. 941–952, 2013. 
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class TorusUnfoldedOptimized extends Topology
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
		Waveguide waveguide;
		double length = Utils.calcHopDistance(this.getM(), this.getN());

		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			int row = (int) tileID / this.getN();
			int column = tileID % this.getN();
			tiles.put(tileID, new Tile(tileID, row, column));
		}

		for (int tileID = 0; tileID < this.getNumTiles(); tileID++)
		{
			if (tiles.get(tileID).getRow() == 0 && tiles.get(tileID).getColumn() < this.getN() - 1)
			{
				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID + 1);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID).setOutWaveguide(Globals.north, waveguide);
				tiles.get(tileID + 1).setInWaveguide(Globals.west, waveguide);
				wgID++;

				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID + 1);
				waveguide.setOutputPort(tileID);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID + 1).setOutWaveguide(Globals.west, waveguide);
				tiles.get(tileID).setInWaveguide(Globals.north, waveguide);
				wgID++;
			}

			if (tiles.get(tileID).getRow() > 0)
			{
				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID - this.getN());
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				wgID++;
				tiles.get(tileID).setOutWaveguide(Globals.north, waveguide);
				tiles.get(tileID - this.getN()).setInWaveguide(Globals.south, waveguide);

				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID - this.getN());
				waveguide.setOutputPort(tileID);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				wgID++;
				tiles.get(tileID).setInWaveguide(Globals.north, waveguide);
				tiles.get(tileID - this.getN()).setOutWaveguide(Globals.south, waveguide);

				if (tiles.get(tileID).getRow() < this.getM() - 1)
				{
					wgID--;
					int csID = crossID;
					createCrosses(waveguides, crossings);
					wgID++;
					waveguide = new Waveguide(wgID, -1, csID + 1);
					waveguide.setInputPort(tileID);
					waveguides.put(wgID, waveguide);
					tiles.get(tileID).setOutWaveguide(Globals.east, waveguide);
					crossings.get(csID + 1).setWaveguideInput0(wgID);
					wgID++;

					waveguide = new Waveguide(wgID, csID + 3, -1);
					waveguide.setOutputPort(tileID);
					waveguides.put(wgID, waveguide);
					tiles.get(tileID).setInWaveguide(Globals.east, waveguide);
					crossings.get(csID + 3).setWaveguideOutput0(wgID);
					wgID++;

					if (tiles.get(tileID).getColumn() < this.getN() - 1)
					{
						waveguide = new Waveguide(wgID, csID + 2, -1);
						waveguide.setOutputPort(tileID + 1);
						waveguide.setLength(length);
						waveguides.put(wgID, waveguide);
						tiles.get(tileID + 1).setInWaveguide(Globals.west, waveguide);
						crossings.get(csID + 2).setWaveguideOutput0(wgID);
						wgID++;

						waveguide = new Waveguide(wgID, -1, csID + 4);
						waveguide.setInputPort(tileID + 1);
						waveguide.setLength(length);
						waveguides.put(wgID, waveguide);
						tiles.get(tileID + 1).setOutWaveguide(Globals.west, waveguide);
						crossings.get(csID + 4).setWaveguideInput0(wgID);
						wgID++;
					}
				}
			}

			if (tiles.get(tileID).getRow() == this.getM() - 1 && tiles.get(tileID).getColumn() < this.getN() - 1)
			{
				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID);
				waveguide.setOutputPort(tileID + 1);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID).setOutWaveguide(Globals.south, waveguide);
				tiles.get(tileID + 1).setInWaveguide(Globals.west, waveguide);
				wgID++;

				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(tileID + 1);
				waveguide.setOutputPort(tileID);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID + 1).setOutWaveguide(Globals.west, waveguide);
				tiles.get(tileID).setInWaveguide(Globals.south, waveguide);
				wgID++;
			}
		}
		// Wrap around
		for (int i = 0; i < this.getM() - 2; i++)
		{
			double l = 0;
			int tileID = this.getN() * (i + 1);
			l = (tiles.get(tileID).getRow() < ((this.getM()) / 2)) ? tiles.get(tileID).getRow() * 2 : (this.getM() - 1 - tiles.get(tileID).getRow()) * 2;
			l += (this.getN() - 1);
			l *= length;
			int csID1 = tiles.get(tileID + this.getN() - 1).getOutWaveguide(Globals.east).getOutputCrossingID();
			waveguide = new Waveguide(wgID, csID1 + 1, -1);
			waveguide.setOutputPort(tileID);
			waveguide.setLength(l);
			waveguides.put(wgID, waveguide);
			tiles.get(tileID).setInWaveguide(Globals.west, waveguide);
			crossings.get(csID1 + 1).setWaveguideOutput0(wgID);
			wgID++;

			waveguide = new Waveguide(wgID, -1, csID1 + 3);
			waveguide.setInputPort(tileID);
			waveguide.setLength(l);
			waveguides.put(wgID, waveguide);
			tiles.get(tileID).setOutWaveguide(Globals.west, waveguide);
			crossings.get(csID1 + 3).setWaveguideInput0(wgID);
			wgID++;

		}
		// Wrap around first row
		waveguide = new Waveguide(wgID);
		waveguide.setInputPort(0);
		waveguide.setOutputPort(this.getN() - 1);
		waveguide.setLength(length * (this.getN() - 1));
		waveguides.put(wgID, waveguide);
		tiles.get(0).setOutWaveguide(Globals.west, waveguide);
		tiles.get(this.getN() - 1).setInWaveguide(Globals.north, waveguide);
		wgID++;

		waveguide = new Waveguide(wgID);
		waveguide.setInputPort(this.getN() - 1);
		waveguide.setOutputPort(0);
		waveguide.setLength(length * (this.getN() - 1));
		waveguides.put(wgID, waveguide);
		tiles.get(0).setInWaveguide(Globals.west, waveguide);
		tiles.get(this.getN() - 1).setOutWaveguide(Globals.north, waveguide);
		wgID++;
		// Wrap around last row
		waveguide = new Waveguide(wgID);
		waveguide.setInputPort(this.getN() * (this.getM() - 1));
		waveguide.setOutputPort(this.getN() * this.getM() - 1);
		waveguide.setLength(length * (this.getN() - 1));
		waveguides.put(wgID, waveguide);
		tiles.get(this.getN() * (this.getM() - 1)).setOutWaveguide(Globals.west, waveguide);
		tiles.get(this.getN() * this.getM() - 1).setInWaveguide(Globals.south, waveguide);
		wgID++;

		waveguide = new Waveguide(wgID);
		waveguide.setInputPort(this.getN() * this.getM() - 1);
		waveguide.setOutputPort(this.getN() * (this.getM() - 1));
		waveguide.setLength(length * (this.getN() - 1));
		waveguides.put(wgID, waveguide);
		tiles.get(this.getN() * (this.getM() - 1)).setInWaveguide(Globals.west, waveguide);
		tiles.get(this.getN() * this.getM() - 1).setOutWaveguide(Globals.south, waveguide);
		wgID++;

		// Create columns
		if (this.getM() == 2)
		{
			for (int i = 0; i < this.getN(); i++)
			{
				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(i);
				waveguide.setOutputPort(i + this.getN());
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(i).setOutWaveguide(Globals.east, waveguide);
				tiles.get(i + this.getN()).setInWaveguide(Globals.east, waveguide);
				wgID++;

				waveguide = new Waveguide(wgID);
				waveguide.setInputPort(i + this.getN());
				waveguide.setOutputPort(i);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(i + this.getN()).setOutWaveguide(Globals.east, waveguide);
				tiles.get(i).setInWaveguide(Globals.east, waveguide);
				wgID++;
			}
		}
		else
		{

			for (int i = 0; i < this.getN(); i++)
			{
				int csID1 = tiles.get(i + this.getN()).getOutWaveguide(Globals.east).getOutputCrossingID();
				int csID2 = csID1;
				waveguide = new Waveguide(wgID, -1, csID1 + 1);
				waveguide.setInputPort(i);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(i).setOutWaveguide(Globals.east, waveguide);
				crossings.get(csID1 + 1).setWaveguideInput1(wgID);
				wgID++;

				waveguide = new Waveguide(wgID, csID1, -1);
				waveguide.setOutputPort(i);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(i).setInWaveguide(Globals.east, waveguide);
				crossings.get(csID1).setWaveguideOutput1(wgID);
				wgID++;

				int tileID1 = i + this.getN();
				int tileID2 = tileID1 + this.getN();
				for (int y = 0; y < this.getM() - 3; y++)
				{
					csID1 = tiles.get(tileID1).getOutWaveguide(Globals.east).getOutputCrossingID();
					csID2 = tiles.get(tileID2).getOutWaveguide(Globals.east).getOutputCrossingID();

					waveguide = new Waveguide(wgID, csID1 + 3, csID2 + 1);
					waveguide.setLength(length);
					waveguides.put(wgID, waveguide);
					crossings.get(csID1 + 3).setWaveguideOutput1(wgID);
					crossings.get(csID2 + 1).setWaveguideInput1(wgID);
					wgID++;

					waveguide = new Waveguide(wgID, csID2, csID1 + 2);
					waveguide.setLength(length);
					waveguides.put(wgID, waveguide);
					crossings.get(csID2).setWaveguideOutput1(wgID);
					crossings.get(csID1 + 2).setWaveguideInput1(wgID);
					wgID++;

					tileID1 = tileID2;
					tileID2 += this.getN();
				}
				waveguide = new Waveguide(wgID, csID2 + 3, -1);
				waveguide.setOutputPort(tileID2);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID2).setInWaveguide(Globals.east, waveguide);
				crossings.get(csID2 + 3).setWaveguideOutput1(wgID);
				wgID++;

				waveguide = new Waveguide(wgID, -1, csID2 + 2);
				waveguide.setInputPort(tileID2);
				waveguide.setLength(length);
				waveguides.put(wgID, waveguide);
				tiles.get(tileID2).setOutWaveguide(Globals.east, waveguide);
				crossings.get(csID2 + 2).setWaveguideInput1(wgID);
				wgID++;
			}
		}
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
