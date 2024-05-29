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

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import building_blocks.Crossing;
import building_blocks.Waveguide;

/**
 * Is the class containing the NoC architecture description and all its features
 * including:
 * <ul>
 * <li>The routing strategy
 * <li>The optical router architecture
 * <li>The network topology
 * </ul>
 * <p>
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class NoCarchitecture {

	/**
	 * Constructs a NoC architecture including the routing strategy, the optical
	 * router architecture and the network topology.
	 */
	public NoCarchitecture() {
		try {
			this.routing = (Routing) Class.forName(
					"routing." + Configuration.getRouting_type()).newInstance();
			this.router = (Router) Class.forName(
					"routers." + Configuration.getRouter_type()).newInstance();
			this.topology = (Topology) Class.forName(
					"topologies." + Configuration.getTopology_type())
					.newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.setValidCommunications();
		this.setLossMatrixNetworkLevel();
		this.setCrosstalkMatrixNetworkLevel();
	}

	/**
	 * Constructs a NoC architecture including the routing strategy, the optical
	 * router architecture and the network topology. It also print in the text
	 * area inside the GUI some information about the architecture configuration
	 * loading.
	 * 
	 * @param textArea
	 *            textArea the multi-line area used in the GUI to displays the
	 *            information about the architecture configuration loading.
	 */
	public NoCarchitecture(final JTextArea textArea) {
		boolean print_error = false;
		// loading routing
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append("Loading architecture configuration... \n");
				textArea.append("Routing -> ");
			}
		});
		try {
			this.routing = (Routing) Class.forName(
					"routing." + Configuration.getRouting_type()).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			if (!print_error) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						textArea.append("ERROR: class not found\n");
					}
				});
			}
			print_error = true;
			e.printStackTrace();
		}

		// loading router
		if (!print_error) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append("OK\n");
				}
			});
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append("Router -> ");
				}
			});
		}
		try {
			this.router = (Router) Class
					.forName("routers." + Configuration.getRouter_type())
					.getConstructor(JTextArea.class).newInstance(textArea);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			if (!print_error) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						textArea.append("ERROR: class not found\n");
					}
				});
			}
			print_error = true;
			e.printStackTrace();
		}
		if (!print_error) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append("OK\n");
				}
			});
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append("Topology -> ");
				}
			});
		}
		try {
			this.topology = (Topology) Class.forName(
					"topologies." + Configuration.getTopology_type())
					.newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			if (!print_error) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						textArea.append("ERROR: class not found\n");
					}
				});
			}
			print_error = true;
			e.printStackTrace();
		}
		if (!print_error) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append("OK\n");
				}
			});
		}

		this.setValidCommunications();
		this.setLossMatrixNetworkLevel();
		this.setCrosstalkMatrixNetworkLevel();
	}

	private double removeLoss(ArrayList<Tile> tileList, int tile, int ind) {
		int in_p = Globals.injection;
		int out_p = Globals.ejection;

		if (ind > 0)
			in_p = this.routing.calcInputPort(topology, tileList.get(ind - 1)
					.getId(), tile);
		if (ind < tileList.size() - 1)
			out_p = this.routing.calcOutputPort(topology, tile,
					tileList.get(ind + 1).getId());

		double loss = this.lossMatrixNetworkLevel[tileList.get(0).getId()][tile]
				- this.router.getLossMatrix()[in_p][Globals.ejection]
				+ this.router.getLossMatrix()[in_p][out_p];
		return loss;
	}

	/**
	 * Sets up the crosstalk matrix
	 */
	private void setCrosstalkMatrixNetworkLevel() {
		this.crosstalkMatrixNetworkLevel = new double[this.topology
				.getNumTiles()][this.topology.getNumTiles()][this.topology
				.getNumTiles()][this.topology.getNumTiles()];
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] = 0;
						if (this.validCommunications[src0][dst0][src1][dst1]) {
							ArrayList<Tile> tileList0 = new ArrayList<Tile>();
							ArrayList<Tile> tileList1 = new ArrayList<Tile>();
							ArrayList<Waveguide> waveguideList0 = new ArrayList<Waveguide>();
							ArrayList<Waveguide> waveguideList1 = new ArrayList<Waveguide>();
							ArrayList<Crossing> crossingList0 = new ArrayList<Crossing>();
							ArrayList<Crossing> crossingList1 = new ArrayList<Crossing>();
							this.calcPath(src0, dst0, tileList0,
									waveguideList0, crossingList0);
							this.calcPath(src1, dst1, tileList1,
									waveguideList1, crossingList1);
							for (int tile_i = 0; tile_i < tileList0.size(); tile_i++) {
								if (tileList1.contains(tileList0.get(tile_i))) {
									int input_port0 = Globals.injection;
									int input_port1 = Globals.injection;
									int output_port0 = Globals.ejection;
									int output_port1 = Globals.ejection;
									if (tile_i > 0) {
										input_port0 = this.routing
												.calcInputPort(
														this.topology,
														tileList0.get(
																tile_i - 1)
																.getId(),
														tileList0.get(tile_i)
																.getId());
									}
									if (tile_i < tileList0.size() - 1) {
										output_port0 = this.routing
												.calcOutputPort(
														this.topology,
														tileList0.get(tile_i)
																.getId(),
														tileList0.get(
																tile_i + 1)
																.getId());
									}
									int tile_j = tileList1.indexOf(tileList0
											.get(tile_i));
									if (tile_j > 0) {
										input_port1 = this.routing
												.calcInputPort(
														this.topology,
														tileList1.get(
																tile_j - 1)
																.getId(),
														tileList1.get(tile_j)
																.getId());
									}
									if (tile_j < tileList1.size() - 1) {
										output_port1 = this.routing
												.calcOutputPort(
														this.topology,
														tileList1.get(tile_j)
																.getId(),
														tileList1.get(
																tile_j + 1)
																.getId());
									}

									double loss_tile_src = this.lossMatrixNetworkLevel[tileList1
											.get(0).getId()][tileList1.get(
											tile_j).getId()];
									double loss_tile_dst = this.lossMatrixNetworkLevel[tileList0
											.get(tile_i).getId()][tileList0
											.get(tileList0.size() - 1).getId()];
									double crosstalk = this.router
											.getCrosstalkMatrix()[input_port0][output_port0][input_port1][output_port1];

									if (loss_tile_src < 0)
										loss_tile_src -= this.router
												.getLossMatrix()[input_port1][Globals.ejection];
									if (loss_tile_dst < 0)
										loss_tile_dst -= this.router
												.getLossMatrix()[Globals.injection][output_port0];

									if (crosstalk != 0
											&& this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] == 0) {
										this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] = crosstalk
												+ loss_tile_src + loss_tile_dst;
									} else if (crosstalk != 0) {
										this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] = Utils
												.sumDB(this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1],
														crosstalk
																+ loss_tile_src
																+ loss_tile_dst);
									}
								}
							}
							for (int cross_i = 0; cross_i < crossingList0
									.size(); cross_i++) {
								if (crossingList1.contains(crossingList0
										.get(cross_i))) {
									int waveguide0_in = crossingList0.get(
											cross_i).getWaveguideInput0();
									int waveguide0_out = crossingList0.get(
											cross_i).getWaveguideOutput0();
									int waveguide1_in = crossingList0.get(
											cross_i).getWaveguideInput1();
									int waveguide1_out = crossingList0.get(
											cross_i).getWaveguideOutput1();
									int tile0_src = this.topology.getWaveguide(
											waveguide0_in).getInputPort();
									int tile0_dst = this.topology.getWaveguide(
											waveguide0_out).getOutputPort();
									int tile1_src = this.topology.getWaveguide(
											waveguide1_in).getInputPort();
									int tile1_dst = this.topology.getWaveguide(
											waveguide1_out).getOutputPort();

									int countCrosses0_src_wc = 0;
									double length0_src_wc = this.getTopology()
											.getWaveguide(waveguide0_in)
											.getLength();
									int countCrosses0_wc_dst = 0;
									double length0_wc_dst = this.getTopology()
											.getWaveguide(waveguide0_out)
											.getLength();
									int countCrosses1_src_wc = 0;
									double length1_src_wc = this.getTopology()
											.getWaveguide(waveguide1_in)
											.getLength();
									int countCrosses1_wc_dst = 0;
									double length1_wc_dst = this.getTopology()
											.getWaveguide(waveguide1_out)
											.getLength();

									while (tile0_src == -1) {
										int nextCross = this.topology
												.getWaveguide(waveguide0_in)
												.getInputCrossingID();
										waveguide0_in = this.topology
												.getCrossing(nextCross)
												.getWaveguideInput0();
										length0_src_wc += this.topology
												.getWaveguide(waveguide0_in)
												.getLength();
										tile0_src = this.topology.getWaveguide(
												waveguide0_in).getInputPort();
										countCrosses0_src_wc++;
									}
									while (tile0_dst == -1) {
										int nextCross = this.topology
												.getWaveguide(waveguide0_out)
												.getOutputCrossingID();
										waveguide0_out = this.topology
												.getCrossing(nextCross)
												.getWaveguideOutput0();
										length0_wc_dst += this.topology
												.getWaveguide(waveguide0_out)
												.getLength();
										tile0_dst = this.topology.getWaveguide(
												waveguide0_out).getOutputPort();
										countCrosses0_wc_dst++;
									}
									while (tile1_src == -1) {
										int nextCross = this.topology
												.getWaveguide(waveguide1_in)
												.getInputCrossingID();
										waveguide1_in = this.topology
												.getCrossing(nextCross)
												.getWaveguideInput1();
										length1_src_wc += this.topology
												.getWaveguide(waveguide1_in)
												.getLength();
										tile1_src = this.topology.getWaveguide(
												waveguide1_in).getInputPort();
										countCrosses1_src_wc++;
									}
									while (tile1_dst == -1) {
										int nextCross = this.topology
												.getWaveguide(waveguide1_out)
												.getOutputCrossingID();
										waveguide1_out = this.topology
												.getCrossing(nextCross)
												.getWaveguideOutput1();
										length1_wc_dst += this.topology
												.getWaveguide(waveguide1_out)
												.getLength();
										tile1_dst = this.topology.getWaveguide(
												waveguide1_out).getOutputPort();
										countCrosses1_wc_dst++;
									}
									int i_tile0_src = tileList1
											.indexOf(this.topology
													.getTile(tile0_src));
									int i_tile1_src = tileList1
											.indexOf(this.topology
													.getTile(tile1_src));

									double loss = 0;
									if (tileList1.contains(this.topology
											.getTile(tile0_src))
											&& tileList1.size() > (i_tile0_src + 1)
											&& tile0_dst == tileList1.get(
													i_tile0_src + 1).getId()) {
										loss = removeLoss(tileList1, tile0_src,
												i_tile0_src);
										int out_p = Globals.ejection;
										int i_tile1_dst = tileList0
												.indexOf(this.topology
														.getTile(tile1_dst));
										if (i_tile1_dst < tileList0.size() - 1) {
											loss += this.lossMatrixNetworkLevel[tile1_dst][tileList0
													.get(tileList0.size() - 1)
													.getId()];
											out_p = this.routing
													.calcOutputPort(
															topology,
															tile1_dst,
															tileList0
																	.get(i_tile1_dst + 1)
																	.getId());
											loss -= this.router.getLossMatrix()[Globals.injection][out_p];
										}

										int in_p = this.routing.calcInputPort(
												this.topology,
												tileList0.get(i_tile1_dst - 1)
														.getId(), tile1_dst);
										loss += this.router.getLossMatrix()[in_p][out_p];
										loss += Configuration.getL_c()
												* (countCrosses0_src_wc + countCrosses1_wc_dst);
										loss += Configuration.getL_p()
												* (length0_src_wc + length1_wc_dst);
									} else if (tileList1.contains(this.topology
											.getTile(tile1_src))
											&& tileList1.size() > (i_tile1_src + 1)
											&& tile1_dst == tileList1.get(
													i_tile1_src + 1).getId()) {
										loss = removeLoss(tileList1, tile1_src,
												i_tile1_src);
										int out_p = Globals.ejection;
										int i_tile0_dst = tileList0
												.indexOf(this.topology
														.getTile(tile0_dst));
										if (i_tile0_dst < tileList0.size() - 1) {
											loss += this.lossMatrixNetworkLevel[tile0_dst][tileList0
													.get(tileList0.size() - 1)
													.getId()];
											out_p = this.routing
													.calcOutputPort(
															topology,
															tile0_dst,
															tileList0
																	.get(i_tile0_dst + 1)
																	.getId());
											loss -= this.router.getLossMatrix()[Globals.injection][out_p];
										}

										int in_p = this.routing.calcInputPort(
												this.topology,
												tileList0.get(i_tile0_dst - 1)
														.getId(), tile0_dst);
										loss += this.router.getLossMatrix()[in_p][out_p];
										loss += Configuration.getL_c()
												* (countCrosses1_src_wc + countCrosses0_wc_dst);
										loss += Configuration.getL_p()
												* (length1_src_wc + length0_wc_dst);
									} else {
										try {
											throw new Exception(
													"@setCrosstalkMatrixNetworkLevel -> Error.");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									double crosstalk = Configuration.getK_c()
											+ loss;
									if (crosstalk != 0
											&& this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] == 0) {
										this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] = crosstalk;
									} else if (crosstalk != 0) {
										this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1] = Utils
												.sumDB(this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1],
														crosstalk);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sets up the power loss matrix
	 */
	private void setLossMatrixNetworkLevel() {
		this.lossMatrixNetworkLevel = new double[this.topology.getNumTiles()][this.topology
				.getNumTiles()];
		for (int src_tile = 0; src_tile < this.topology.getNumTiles(); src_tile++) {
			for (int dst_tile = 0; dst_tile < this.topology.getNumTiles(); dst_tile++) {
				double loss = 0;
				if (src_tile != dst_tile) {
					ArrayList<Tile> tileList = new ArrayList<Tile>();
					ArrayList<Waveguide> waveguideList = new ArrayList<Waveguide>();
					ArrayList<Crossing> crossingList = new ArrayList<Crossing>();
					this.calcPath(src_tile, dst_tile, tileList, waveguideList,
							crossingList);
					for (int tile_i = 0; tile_i < tileList.size(); tile_i++) {
						int input_port = Globals.injection;
						int output_port = Globals.ejection;
						if (tile_i > 0) {
							input_port = this.routing.calcInputPort(
									this.topology, tileList.get(tile_i - 1)
											.getId(), tileList.get(tile_i)
											.getId());
						}
						if (tile_i < tileList.size() - 1) {
							output_port = this.routing.calcOutputPort(
									this.topology,
									tileList.get(tile_i).getId(),
									tileList.get(tile_i + 1).getId());
						}
						loss += this.router.getLossMatrix()[input_port][output_port];
					}
					double length = 0;
					for (Waveguide wg : waveguideList){
						length += wg.getLength();
					}
					loss += length * Configuration.getL_p();
					for (int cross_i = 0; cross_i < crossingList.size(); cross_i++) {
						loss += Configuration.getL_c();
					}
				}
				this.lossMatrixNetworkLevel[src_tile][dst_tile] = loss;
			}
		}
	}

	/**
	 * Sets up the valid communications data structure.
	 */
	public void setValidCommunications() {
		int numTiles = this.topology.getNumTiles();
		this.validCommunications = new boolean[numTiles][numTiles][numTiles][numTiles];
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						this.validCommunications[src0][dst0][src1][dst1] = false;
						if (src0 != dst0
								&& src1 != dst1
								&& (!(src0 == src1 && dst0 == dst1))
								&& isValidSimultaneousCommunication(src0, dst0,
										src1, dst1)) {
							this.validCommunications[src0][dst0][src1][dst1] = true;
						}
					}
				}
			}
		}
	}


	/**
	 * Calculates if two communication could perform simultaneously. If there is
	 * a conflict in the required resources it returns false.
	 * 
	 * @param src0
	 *            The first source tile
	 * @param dst0
	 *            The first destination tile
	 * @param src1
	 *            The second source tile
	 * @param dst1
	 *            The second destination tile
	 * @return <code>true</code> if the two communication could perform
	 *         simultaneously <code>false</code> otherwise
	 */
	private boolean isValidSimultaneousCommunication(int src0, int dst0,
			int src1, int dst1) {
		ArrayList<Tile> tileList0 = new ArrayList<Tile>();
		ArrayList<Tile> tileList1 = new ArrayList<Tile>();
		ArrayList<Waveguide> waveguideList0 = new ArrayList<Waveguide>();
		ArrayList<Waveguide> waveguideList1 = new ArrayList<Waveguide>();
		ArrayList<Crossing> crossingList0 = new ArrayList<Crossing>();
		ArrayList<Crossing> crossingList1 = new ArrayList<Crossing>();
		this.calcPath(src0, dst0, tileList0, waveguideList0, crossingList0);
		this.calcPath(src1, dst1, tileList1, waveguideList1, crossingList1);
		for (int tile_i = 0; tile_i < tileList0.size(); tile_i++) {
			if (tileList1.contains(tileList0.get(tile_i))) {
				int input_port0 = Globals.injection;
				int input_port1 = Globals.injection;
				int output_port0 = Globals.ejection;
				int output_port1 = Globals.ejection;
				if (tile_i > 0) {
					input_port0 = this.routing.calcInputPort(this.topology,
							tileList0.get(tile_i - 1).getId(),
							tileList0.get(tile_i).getId());
				}
				if (tile_i < tileList0.size() - 1) {
					output_port0 = this.routing.calcOutputPort(this.topology,
							tileList0.get(tile_i).getId(),
							tileList0.get(tile_i + 1).getId());
				}
				int tile_j = tileList1.indexOf(tileList0.get(tile_i));
				if (tile_j > 0) {
					input_port1 = this.routing.calcInputPort(this.topology,
							tileList1.get(tile_j - 1).getId(),
							tileList1.get(tile_j).getId());
				}
				if (tile_j < tileList1.size() - 1) {
					output_port1 = this.routing.calcOutputPort(this.topology,
							tileList1.get(tile_j).getId(),
							tileList1.get(tile_j + 1).getId());
				}
				if (!this.router.isValidSimultaneousCommunication(input_port0,
						output_port0, input_port1, output_port1)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Evaluates the path that an optical signal should take from a source tile
	 * to a destination tile. Three data structures are filled:
	 * <ul>
	 * <li>1. the tile list which contains the IDs of all the tiles that are
	 * crossed
	 * <li>2. the waveguide list which contains the IDs of all the waveguides
	 * (placed outside the optical router) that are crossed
	 * <li>3. the waveguide crossing list which contains the IDs of all the
	 * waveguides crossing (placed outside the optical router) that are crossed
	 * </ul>
	 * 
	 * @param srcTile
	 *            The source tile
	 * @param dstTile
	 *            The destination tile
	 * @param tileList
	 *            The list of all the tiles that the optical signal must cross
	 * @param waveguideList
	 *            The list of all the waveguide (outside) that the optical
	 *            signal must cross
	 * @param crossingList
	 *            The list of all the waveguide crossing that the optical signal
	 *            must cross
	 */
	private void calcPath(int srcTile, int dstTile, ArrayList<Tile> tileList,
			ArrayList<Waveguide> waveguideList, ArrayList<Crossing> crossingList) {
		int nextTile = srcTile;
		while (nextTile != dstTile) {
			tileList.add(this.topology.getTile(nextTile));
			int port = this.routing.calcOutputPort(this.topology, nextTile,
					dstTile);
			Waveguide nextWaveguide = this.topology.getTile(nextTile)
					.getOutWaveguide(port);
			Crossing nextCrossing = null;
			waveguideList.add(nextWaveguide);
			while (nextWaveguide.getOutputPort() == -1) {
				nextCrossing = this.topology.getCrossing(nextWaveguide
						.getOutputCrossingID());
				crossingList.add(nextCrossing);
				if (nextCrossing.getWaveguideInput0() == nextWaveguide.getID()) {
					nextWaveguide = this.topology.getWaveguide(nextCrossing
							.getWaveguideOutput0());
				} else if (nextCrossing.getWaveguideInput1() == nextWaveguide
						.getID()) {
					nextWaveguide = this.topology.getWaveguide(nextCrossing
							.getWaveguideOutput1());
				}
				waveguideList.add(nextWaveguide);
			}
			nextTile = nextWaveguide.getOutputPort();
		}
		tileList.add(this.topology.getTile(nextTile));
	}

	/**
	 * Checks the size of the NoC in terms of number of tiles and compares it
	 * with the number of cores of the application
	 * 
	 * @param app The application
	 * @return <code>true</code> if the number of cores is greater than the number of tiles;
	 *         <code>false</code> otherwise
	 */
	public boolean checkNoCsize(Application app) {
		final int numCores = app.getNum_cores();
		final int numTiles = this.topology.getNumTiles();
		if (numCores > numTiles) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return a String object containing a formatted representations of the NoC
	 *         architecture.
	 */
	public String getReadableRepresentation() {
		String values = "***** NoC architecture *****\n";
		values += this.topology.getReadableRepresentation();
		values += this.router.getReadableRepresentation();
		values += "***** Network level *****\n";
		// values+=this.isValidToString();
		// values+=this.lossToString();
		// values+=this.crosstalkToString();
		return values;
	}

	/**
	 * Prints formatted representations of the NoC architecture and its main
	 * features to a text-output stream.
	 */
	public void print() {
		System.out.println("");
		System.out.println("***** NoC architecture *****");
		this.topology.print();
		this.router.printLoss();
		this.router.printCrosstalk();

		this.printIsValid();
		this.printLoss();
		this.printCrosstalk();
	}

	/**
	 * @return a String object containing a formatted representations of the
	 *         valid communication matrix.
	 */
	public String isValidToString() {
		String values = "Valid simultaneous communications at network level:\n";
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				values += ("signal " + src0 + " -> " + dst0 + "\n");
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						values += (this.validCommunications[src0][dst0][src1][dst1] + " ");
					}
					values += "\n";
				}
				values += "\n";
			}
		}
		return values;
	}

	/**
	 * @return a String object containing a formatted representations of the
	 *         power loss matrix.
	 * 
	 */
	public String lossToString() {
		String values = "Loss matrix at network level:\n";
		for (int src_tile = 0; src_tile < this.topology.getNumTiles(); src_tile++) {
			for (int dst_tile = 0; dst_tile < this.topology.getNumTiles(); dst_tile++) {
				values += (new DecimalFormat("##.###")
						.format(this.lossMatrixNetworkLevel[src_tile][dst_tile]) + "\t");
			}
			values += "\n";
		}
		values += "\n";
		return values;
	}

	/**
	 * @return a String object containing a formatted representations of the
	 *         crosstalk matrix.
	 */
	public String crosstalkToString() {
		String values = "Crosstalk matrices at network level:\n";
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				values += ("signal " + src0 + " -> " + dst0 + "\n");
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						values += (new DecimalFormat("##.###")
								.format(this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1]) + "\t");
					}
					values += "\n";
				}
				values += "\n";
			}
		}
		return values;
	}

	/**
	 * Prints formatted representations of the valid communication matrix matrix
	 * to a text-output stream.
	 */
	public void printIsValid() {
		System.out.println("Valid simultaneous communication matrices:");
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				System.out.println("signal " + src0 + " -> " + dst0);
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						System.out
								.print(this.validCommunications[src0][dst0][src1][dst1]
										+ " ");
					}
					System.out.println("");
				}
				System.out.println("");
			}
		}
	}

	/**
	 * Prints formatted representations of the power loss matrix to a
	 * text-output stream.
	 */
	public void printLoss() {
		System.out.println("Loss matrix at network level:");
		for (int src_tile = 0; src_tile < this.topology.getNumTiles(); src_tile++) {
			for (int dst_tile = 0; dst_tile < this.topology.getNumTiles(); dst_tile++) {
				System.out
						.print(new DecimalFormat("##.###")
								.format(this.lossMatrixNetworkLevel[src_tile][dst_tile])
								+ "\t");
			}
			System.out.println("");
		}
	}

	/**
	 * Prints formatted representations of the crosstalk matrix to a text-output
	 * stream.
	 */
	public void printCrosstalk() {
		System.out.println("Crosstalk matrix at network level:");
		for (int src0 = 0; src0 < this.topology.getNumTiles(); src0++) {
			for (int dst0 = 0; dst0 < this.topology.getNumTiles(); dst0++) {
				System.out.println("signal " + src0 + " -> " + dst0);
				for (int src1 = 0; src1 < this.topology.getNumTiles(); src1++) {
					for (int dst1 = 0; dst1 < this.topology.getNumTiles(); dst1++) {
						System.out
								.print(new DecimalFormat("##.###")
										.format(this.crosstalkMatrixNetworkLevel[src0][dst0][src1][dst1])
										+ "\t");
					}
					System.out.println("");
				}
				System.out.println("");
			}
		}
	}

	/**
	 * @return The NoC topology
	 */
	public Topology getTopology() {
		return topology;
	}

	/**
	 * @return The routing strategy
	 */
	public Routing getRouting() {
		return routing;
	}

	/**
	 * @return The optical router
	 */
	public Router getRouter() {
		return router;
	}

	/**
	 * @return The valid communication matrix
	 */
	public boolean[][][][] getValidCommunications() { 
		return validCommunications; 
	}
	
	/**
	 * @param src
	 *            The source tile
	 * @param dst
	 *            The destination tile
	 * @return The power loss that affects the communication between src and dst
	 */
	public double getPowerLoss(int src, int dst) {
		return lossMatrixNetworkLevel[src][dst];
	}

	/**
	 * @param src
	 *            The source tile
	 * @param dst
	 *            The destination tile
	 * @param src_noise
	 *            The source of the signal generating crosstalk
	 * @param dst_noise
	 *            The destination of the signal generating crosstalk
	 * @return The crosstalk that affects the communication between src and dst
	 *         tiles due to the communication between src_noise and dst_noise
	 *         tiles.
	 */
	public double getCrosstalk(int src, int dst, int src_noise, int dst_noise) {
		return crosstalkMatrixNetworkLevel[src][dst][src_noise][dst_noise];
	}

	/**
	 * The NoC topology
	 */
	private Topology topology;
	/**
	 * The routing algorithm
	 */
	private Routing routing;
	/**
	 * The optical router
	 */
	private Router router;
	/**
	 * The valid communication matrix. Each entry is identified by a tile ID.
	 * The first two indexes as well as the last two indexes represent both a
	 * communication between a source tile and a destination tile. If true the
	 * two communications could happen simultaneously. As an example
	 * validCommunications[scr0][dst0][src1][dst1]=true means that the two
	 * communications between tiles src0->dst0 and src1->dst1 can be performed
	 * at the same time.
	 */
	private boolean[][][][] validCommunications;
	/**
	 * The loss matrix gives for each couple source-destination the power loss
	 * that the optical signal is affected by.
	 */
	private double[][] lossMatrixNetworkLevel;
	/**
	 * The crosstalk matrix gives for each signal traveling between a couple
	 * source-destination, the crosstalk noise impact due to another signal
	 * traveling between a different couple of tiles. As an example
	 * crosstalkMatrixNetworkLevel[scr0][dst0][src1][dst1]=a means that the
	 * communication between tiles src0->dst0 is affected by a crosstalk noise
	 * equal to 'a' due to the optical signal traveling between src1->dst1.
	 */
	private double[][][][] crosstalkMatrixNetworkLevel;
}
