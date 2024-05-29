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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import building_blocks.Crossing;
import building_blocks.MicroringResonator;
import building_blocks.Waveguide;

/**
 * Is the abstract base class for all the custom routers defined in the routers
 * package. A Router object encapsulates the specific features of a type of
 * router that are needed when instantiating a NoC architecture. This
 * information includes:
 * <ul>
 * <li>The number of ports
 * <li>The number of waveguides
 * <li>The number of crossings
 * <li>The number of microring resonators
 * <li>The inner architecture of the router including the position of each
 * building blocks (waveguides, crossings, microring resonators)
 * <li>The power loss and crosstalk characteristics of this router
 * </ul>
 * <p>
 * A custom router that extends this abstract class must implement the
 * initialize method in order to proper set the different router architectures
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public abstract class Router {

	/**
	 * Constructs a five port Router with a certain number of microring
	 * resonators, crossings and waveguides.
	 * 
	 * @param numRings
	 *            the number of microring resonators
	 * @param numCrossings
	 *            the number of crossings
	 * @param numWaveguides
	 *            the number of waveguides
	 * @param textArea
	 *            the GUI element used to print messages
	 */
	public Router(int numRings, int numCrossings, int numWaveguides,
			final JTextArea textArea) {
		this(5, numRings, numCrossings, numWaveguides, textArea);
	}

	/**
	 * Constructs Router with a certain number of ports, microring resonators,
	 * crossings and waveguides.
	 * 
	 * @param numPorts
	 *            the number of ports
	 * @param numRings
	 *            the number of microring resonators
	 * @param numCrossings
	 *            the number of crossings
	 * @param numWaveguides
	 *            the number of waveguides
	 * @param textArea
	 *            the GUI element used to print messages
	 */
	public Router(int numPorts, int numRings, int numCrossings,
			int numWaveguides, final JTextArea textArea) {
		this.numPorts = numPorts;
		this.numRings = numRings;
		this.numCrossings = numCrossings;
		this.numWaveguides = numWaveguides;
		this.textArea = textArea;

		this.rings = new HashMap<Integer, MicroringResonator>();
		this.crossings = new HashMap<Integer, Crossing>();
		this.waveguides = new HashMap<Integer, Waveguide>();

		this.lossMatrix = new double[numPorts][numPorts];
		this.crosstalkMatrix = new double[numPorts][numPorts][numPorts][numPorts];
		this.connectivityMatrix = new boolean[this.getNumPorts()][this.getNumPorts()];

		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				this.lossMatrix[p_in][p_out] = 0;
				for (int p = 0; p < this.numPorts; p++) {
					for (int q = 0; q < this.numPorts; q++) {
						this.crosstalkMatrix[p_in][p_out][p][q] = 0;
					}
				}
			}
		}
		this.initialize();
		this.checkRouterArchitecture();
		this.calcLossMatrix();
		this.calcCrosstalkMatrix();
	}

	/**
	 * Initializes the router object. This method must be implemented in every
	 * custom router class. The initialize method must set three class variables
	 * that every router should customize according to its inner architecture:
	 * <ul>
	 * <li>rings, the map containing the ring objects
	 * <li>crossings, the map containing the crossing objects
	 * <li>waveguides, the map containing the waveguide objects
	 * </ul>
	 */
	protected abstract void initialize();

	/**
	 * Checks if every output port is reachable from every other input port.
	 * This method is designed taking into account the inner architecture of a 5
	 * ports router with a XY routing strategy
	 */
	private void checkRouterArchitecture() {
		boolean cond = false;
		for (int p_in = 0; p_in < this.getNumPorts(); p_in++) {
			for (int p_out = 0; p_out < this.getNumPorts(); p_out++) {
				this.connectivityMatrix[p_in][p_out] = false;
				if (p_in != p_out) {
					for (Entry<Integer, Waveguide> first_waveguide : this
							.getWaveguides().entrySet()) {
						// find the waveguide connected with the input port p_in
						if (first_waveguide.getValue().getInputPort() == p_in) {
							int waveguide;
							int next_waveguide = first_waveguide.getKey();
							// cycle for each waveguide along the path
							do {
								waveguide = next_waveguide;
								// check if it is necessary to drop into a microring of a PPSE
								int ringPPSE = -1;
								for (int i = 0; i < this.getWaveguides()
										.get(waveguide).getNumMicroringPPSE(); i++) {
									int id = this.getWaveguides()
											.get(waveguide).getMicroringPPSE(i);
									if (this.getRings().get(id)
											.getDirectionON() == p_out
											&& this.getRings().get(id)
													.getOutputWaveguideID() != waveguide) {
										ringPPSE = this.getWaveguides()
												.get(waveguide)
												.getMicroringPPSE(i);
									}
								}

								if (ringPPSE != -1) {
									next_waveguide = this.getRings()
											.get(ringPPSE)
											.getOutputWaveguideID();
								}
								// go straight
								else {
									int crossing = this.getWaveguides()
											.get(waveguide)
											.getOutputCrossingID();
									if (crossing != -1) {
										int ringCPSEin0out1 = this
												.getCrossings().get(crossing)
												.getMicroringIn0Out1();
										int ringCPSEin1out0 = this
												.getCrossings().get(crossing)
												.getMicroringIn1Out0();
										if (this.getCrossings().get(crossing)
												.getWaveguideInput0() == waveguide) {
											if (ringCPSEin0out1 != -1
													&& this.getRings()
															.get(ringCPSEin0out1)
															.getDirectionON() == p_out) {
												next_waveguide = this
														.getRings()
														.get(ringCPSEin0out1)
														.getOutputWaveguideID();
											} else {
												next_waveguide = this
														.getCrossings()
														.get(crossing)
														.getWaveguideOutput0();
											}
										} else if (this.getCrossings()
												.get(crossing)
												.getWaveguideInput1() == waveguide) {
											if (ringCPSEin1out0 != -1
													&& this.getRings()
															.get(ringCPSEin1out0)
															.getDirectionON() == p_out) {
												next_waveguide = this
														.getRings()
														.get(ringCPSEin1out0)
														.getOutputWaveguideID();
											} else {
												next_waveguide = this
														.getCrossings()
														.get(crossing)
														.getWaveguideOutput1();
											}
										}
									}
								}

							} while (this.getWaveguides().get(waveguide)
									.getOutputPort() == -1);
							// check if the path arrives at the right output port
							if (this.getWaveguides().get(next_waveguide)
									.getOutputPort() != p_out) {
								
								final String str;

								if (!cond)
									str = "\nOptical Router Warning\nThere is no path between input port "
											+ p_in
											+ " and output port "
											+ p_out;
								else
									str = "\nThere is no path between input port "
											+ p_in
											+ " and output port "
											+ p_out;

								if (this.textArea != null) {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											textArea.append(str);
										}
									});
								} else
									System.out.println(str);
								cond = true;
							} else {
								this.connectivityMatrix[p_in][p_out] = true;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Calculates the power loss matrix where each element L_{i,j} gives the
	 * power loss that affect a communication traveling from port i to port j.
	 */
	private void calcLossMatrix() {
		for (int p_in = 0; p_in < this.getNumPorts(); p_in++) {
			for (int p_out = 0; p_out < this.getNumPorts(); p_out++) {
				if (this.connectivityMatrix[p_in][p_out]) {
					List<BuildingBlock> path = this.calcPath(p_in, p_out);
					this.lossMatrix[p_in][p_out] = this.calcLoss(path);
				}
			}
		}
	}

	/**
	 * Calculates the crosstalk matrix where each element K_{i,j,m,n} gives the
	 * crosstalk noise power that affect a communication traveling from port i
	 * to port j due to another communication traveling from port m to port n.
	 */
	private void calcCrosstalkMatrix() {
		for (int p_in = 0; p_in < this.getNumPorts(); p_in++) {
			for (int p_out = 0; p_out < this.getNumPorts(); p_out++) {
				// for each feasible signal communication between ports p_in and p_out
				if (this.connectivityMatrix[p_in][p_out]) {
					List<BuildingBlock> pathPinPout = this
							.calcPath(p_in, p_out);
					for (int p = 0; p < this.getNumPorts(); p++) {
						for (int q = 0; q < this.getNumPorts(); q++) {
							// for each feasible signal communication between ports p and q
							if (this.connectivityMatrix[p][q] && (p != p_in)
									&& (q != p_out)) {
								List<BuildingBlock> pathPQ = this
										.calcPath(p, q);
								if (!this.areSharedWaveguides(pathPinPout,
										pathPQ)) {
									this.crosstalkMatrix[p_in][p_out][p][q] = this
											.calcCrosstalk(pathPinPout, pathPQ);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Calculates the path taken by a message traveling from port p_in to port
	 * p_out. The path is made up of all the basic building blocks crossed,
	 * including waveguides, crossings and microring resonators.
	 * 
	 * @param p_in
	 *            the input port
	 * @param p_out
	 *            the output port
	 * @return the list containing all the crossed building blocks.
	 */
	private List<BuildingBlock> calcPath(int p_in, int p_out) {
		// array containing all the building blocks along the path between ports p_in and p_out
		ArrayList<BuildingBlock> path = new ArrayList<BuildingBlock>();
		for (Entry<Integer, Waveguide> first_waveguide : this.getWaveguides()
				.entrySet()) {
			// find the waveguide connected with the input port p_in
			if (first_waveguide.getValue().getInputPort() == p_in) {
				int waveguide;
				int next_waveguide = first_waveguide.getKey();
				// cycle for each waveguide along the path
				do {
					waveguide = next_waveguide;
					path.add(this.getWaveguides().get(waveguide));
					// check if it is necessary to drop into a microring of a PPSE
					int ringPPSE = -1;
					boolean PPSE = false;
					for (int i = 0; i < this.getWaveguides().get(waveguide)
							.getNumMicroringPPSE(); i++) {
						int id = this.getWaveguides().get(waveguide)
								.getMicroringPPSE(i);
						if (this.getRings().get(id).getDirectionON() == p_out
								&& this.getRings().get(id)
										.getOutputWaveguideID() != waveguide) {
							ringPPSE = this.getWaveguides().get(waveguide)
									.getMicroringPPSE(i);
							next_waveguide = this.getRings().get(ringPPSE)
									.getOutputWaveguideID();
							path.add(this.getRings().get(ringPPSE));
							PPSE = true;
						}
					}
					// go straight
					if (!PPSE) {
						int crossing = this.getWaveguides().get(waveguide)
								.getOutputCrossingID();
						if (crossing != -1) {
							int ringCPSEin0out1 = this.getCrossings()
									.get(crossing).getMicroringIn0Out1();
							int ringCPSEin1out0 = this.getCrossings()
									.get(crossing).getMicroringIn1Out0();
							if (this.getCrossings().get(crossing)
									.getWaveguideInput0() == waveguide) {
								if (ringCPSEin0out1 != -1
										&& this.getRings().get(ringCPSEin0out1)
												.getDirectionON() == p_out) {
									next_waveguide = this.getRings()
											.get(ringCPSEin0out1)
											.getOutputWaveguideID();
									path.add(this.getRings().get(
											ringCPSEin0out1));
								} else {
									next_waveguide = this.getCrossings()
											.get(crossing)
											.getWaveguideOutput0();
									path.add(this.getCrossings().get(crossing));
								}
							} else if (this.getCrossings().get(crossing)
									.getWaveguideInput1() == waveguide) {
								if (ringCPSEin1out0 != -1
										&& this.getRings().get(ringCPSEin1out0)
												.getDirectionON() == p_out) {
									next_waveguide = this.getRings()
											.get(ringCPSEin1out0)
											.getOutputWaveguideID();
									path.add(this.getRings().get(
											ringCPSEin1out0));
								} else {
									next_waveguide = this.getCrossings()
											.get(crossing)
											.getWaveguideOutput1();
									path.add(this.getCrossings().get(crossing));
								}
							}
						}
					}

				} while (this.getWaveguides().get(waveguide).getOutputPort() == -1);
				// check if the path arrives at the right output port
				if (this.getWaveguides().get(next_waveguide).getOutputPort() != p_out) {
					try {
						throw new Exception(
								"@calcLossMatrix -> the path arrive at the wrong output port");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return path;
	}

	/**
	 * Calculates the power loss that a signal traveling along a path is
	 * affected by.
	 * 
	 * @param path
	 *            the list containing all the crossed building blocks.
	 * @return the power loss (dB)
	 */
	private double calcLoss(List<BuildingBlock> path) {
		double loss = 0;
		for (int bb = 0; bb < path.size(); bb++) {
			int id = path.get(bb).getID();
			switch (path.get(bb).getClass().getSimpleName()) {
			case "Waveguide":
				/*
				 * check if there is a passby a ring for a parallel PSE where
				 * this waveguide is the input first condition: if there is a
				 * microring for a PPSE on this waveguide second condition: if
				 * this microring is not along the path
				 */
				int numMicroringPSE = 0;
				int first_index_mrPSE = 0;
				int last_index_mrPSE = this.getWaveguides().get(id)
						.getNumMicroringPPSE() - 1;
				if (bb > 0
						&& path.get(bb - 1).getClass().getSimpleName()
								.compareTo("MicroringResonator") == 0
						&& ((MicroringResonator) path.get(bb - 1)).getType() == Globals.pse_type) {
					numMicroringPSE--;
					for (int i = 0; i < this.getWaveguides().get(id)
							.getNumMicroringPPSE(); i++) {
						if (this.getWaveguides().get(id).getMicroringPPSE(i) == path
								.get(bb - 1).getID()) {
							first_index_mrPSE = i;
							break;
						}
					}
				}
				if (bb < path.size() - 1
						&& path.get(bb + 1).getClass().getSimpleName()
								.compareTo("MicroringResonator") == 0
						&& ((MicroringResonator) path.get(bb + 1)).getType() == Globals.pse_type) {
					numMicroringPSE--;
					for (int i = 0; i < this.getWaveguides().get(id)
							.getNumMicroringPPSE(); i++) {
						if (this.getWaveguides().get(id).getMicroringPPSE(i) == path
								.get(bb + 1).getID()) {
							last_index_mrPSE = i;
							break;
						}
					}
				}
				numMicroringPSE = numMicroringPSE + last_index_mrPSE
						- first_index_mrPSE + 1;

				if (this.getWaveguides().get(id).getNumMicroringPPSE() > 0) {
					loss += Configuration.getL_poff() * numMicroringPSE;
				}
				break;
			case "Crossing":

				/*
				 * first condition: if the crossing is not the first element add
				 * the loss due to the rings that anticipates the crossing.
				 * second condition: if the crossing is not the last element add
				 * the loss due to the rings that follows the crossing.
				 */
				if (bb > 0) {
					int waveguide_in = path.get(bb - 1).getID();
					if (this.crossings.get(id).getWaveguideInput0() == waveguide_in
							&& this.crossings.get(id).getMicroringIn0Out1() != -1) {
						loss += Configuration.getL_poff();
					} else if (this.crossings.get(id).getWaveguideInput1() == waveguide_in
							&& this.crossings.get(id).getMicroringIn1Out0() != -1) {
						loss += Configuration.getL_poff();
					}
				}
				loss += Configuration.getL_c();
				if (bb < path.size() - 1) {
					int waveguide_out = path.get(bb + 1).getID();
					if (this.crossings.get(id).getWaveguideOutput0() == waveguide_out
							&& this.crossings.get(id).getMicroringIn1Out0() != -1) {
						loss += Configuration.getL_poff();
					} else if (this.crossings.get(id).getWaveguideOutput1() == waveguide_out
							&& this.crossings.get(id).getMicroringIn0Out1() != -1) {
						loss += Configuration.getL_poff();
					}
				}

				break;
			case "MicroringResonator":
				if (this.rings.get(id).getType() == Globals.pse_type) {
					loss += Configuration.getL_pon();
				} else if (this.rings.get(id).getType() == Globals.cse_type) {
					loss += Configuration.getL_con();
				}
				break;
			default:
				try {
					throw new Exception(
							"@calcLossMatrix -> unknown building block");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return loss;
	}

	/**
	 * Calculates the crosstalk noise power that a signal traveling along a path
	 * is affected by due to another signal traveling along another path. This
	 * method do not consider the crosstalk noise in case of a double PSE where
	 * two microrings are added to a waveguide crossings. In such a case, if the
	 * two microrings are used simultaneously, some crosstalk noise is
	 * generated. This crosstalk noise is neglected.
	 * 
	 * @param pathPinPout
	 *            the list containing all the building blocks that are crossed
	 *            by the signal
	 * @param pathPQ
	 *            the list containing all the building blocks that are crossed
	 *            by the signal generating the noise.
	 * @return the crosstalk noise power (dB)
	 */
	private double calcCrosstalk(List<BuildingBlock> pathPinPout,
			List<BuildingBlock> pathPQ) {
		double crosstalk = 0;
		for (int bb_i = 0; bb_i < pathPinPout.size(); bb_i++) {
			int id_i = pathPinPout.get(bb_i).getID();
			for (int bb_j = 0; bb_j < pathPQ.size(); bb_j++) {
				if (pathPinPout.get(bb_i).getClass().getSimpleName()
						.compareTo(pathPQ.get(bb_j).getClass().getSimpleName()) == 0) {
					int id_j = pathPQ.get(bb_j).getID();
					List<BuildingBlock> path_tmp = null;
					double loss_signal_j = 0;
					double loss_noise_to_port = 0;
					switch (pathPinPout.get(bb_i).getClass().getSimpleName()) {
					case "Waveguide":
						if (id_i == id_j) {
							try {
								throw new Exception(
										"@calcCrosstalk -> two different paths cannot share a waveguide");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							for (int i = 0; i < this.waveguides.get(id_i)
									.getNumMicroringPPSE(); i++) {
								int microringPPSE_i = this.waveguides.get(id_i)
										.getMicroringPPSE(i);
								for (int j = 0; j < this.waveguides.get(id_j)
										.getNumMicroringPPSE(); j++) {
									int microringPPSE_j = this.waveguides.get(
											id_j).getMicroringPPSE(j);
									if (microringPPSE_i == microringPPSE_j) {
										int[] indPinPout = new int[2];
										int[] indPQ = new int[2];
										indPinPout[0] = 0;
										indPinPout[1] = this.waveguides.get(
												id_i).getNumMicroringPPSE() - 1;
										indPQ[0] = 0;
										indPQ[1] = this.waveguides.get(id_j)
												.getNumMicroringPPSE() - 1;

										if (bb_i > 0
												&& pathPinPout
														.get(bb_i - 1)
														.getClass()
														.getSimpleName()
														.compareTo(
																"MicroringResonator") == 0) {
											for (int k = 0; k < this.waveguides
													.get(id_i)
													.getNumMicroringPPSE(); k++) {
												if (this.waveguides.get(id_i)
														.getMicroringPPSE(k) == pathPinPout
														.get(bb_i - 1).getID()) {
													indPinPout[0] = k;
													break;
												}
											}
										}
										if (bb_i < pathPinPout.size() - 1
												&& pathPinPout
														.get(bb_i + 1)
														.getClass()
														.getSimpleName()
														.compareTo(
																"MicroringResonator") == 0) {
											for (int k = 0; k < this.waveguides
													.get(id_i)
													.getNumMicroringPPSE(); k++) {
												if (this.waveguides.get(id_i)
														.getMicroringPPSE(k) == pathPinPout
														.get(bb_i + 1).getID()) {
													indPinPout[1] = k;
													break;
												}
											}
										}
										if (bb_j > 0
												&& pathPQ
														.get(bb_j - 1)
														.getClass()
														.getSimpleName()
														.compareTo(
																"MicroringResonator") == 0) {
											for (int k = 0; k < this.waveguides
													.get(id_j)
													.getNumMicroringPPSE(); k++) {
												if (this.waveguides.get(id_j)
														.getMicroringPPSE(k) == pathPQ
														.get(bb_j - 1).getID()) {
													indPQ[0] = k;
													break;
												}
											}
										}
										if (bb_j < pathPQ.size() - 1
												&& pathPQ
														.get(bb_j + 1)
														.getClass()
														.getSimpleName()
														.compareTo(
																"MicroringResonator") == 0) {
											for (int k = 0; k < this.waveguides
													.get(id_j)
													.getNumMicroringPPSE(); k++) {
												if (this.waveguides.get(id_j)
														.getMicroringPPSE(k) == pathPQ
														.get(bb_j + 1).getID()) {
													indPQ[1] = k;
													break;
												}
											}
										}
										boolean cond = false;
										for (int k = indPinPout[0]; k < indPinPout[1] + 1; k++) {
											for (int z = indPQ[0]; z < indPQ[1] + 1; z++) {
												if (microringPPSE_i == this.waveguides
														.get(id_i)
														.getMicroringPPSE(k)
														&& microringPPSE_i == this.waveguides
																.get(id_j)
																.getMicroringPPSE(
																		z)) {
													cond = true;
													break;
												}
											}
											if (cond)
												break;
										}
										if (cond) {
											path_tmp = Utils
													.cloneArrayList(pathPQ
															.subList(
																	0,
																	bb_j + 1));
											path_tmp.add(this.rings
													.get(microringPPSE_i));
											loss_signal_j = this
													.calcLoss(path_tmp)
													- Configuration
															.getL_pon();
											
											path_tmp = Utils
													.cloneArrayList(pathPinPout
															.subList(
																	bb_i,
																	pathPinPout
																			.size()));
											path_tmp.add(0, this.rings
													.get(microringPPSE_i));
											loss_noise_to_port = this
													.calcLoss(path_tmp)
													- Configuration
															.getL_pon();
											
											double crosstalk_component = loss_signal_j
													+ Configuration.getK_poff()
													+ loss_noise_to_port;
											if (crosstalk == 0) {
												crosstalk = crosstalk_component;
											} else {
												crosstalk = Utils.sumDB(
														crosstalk,
														crosstalk_component);
											}
										}
									}
								}
							}
						}
						break;
					case "Crossing":
						int waveguide_in_j = pathPQ.get(bb_j - 1).getID();
						if (id_i == id_j) {
							if (bb_j > 0) {
								path_tmp = pathPQ.subList(0, bb_j);
								loss_signal_j = this.calcLoss(path_tmp);
							} else {
								loss_signal_j = 0;
							}
							if (bb_i < pathPinPout.size() - 1) {
								path_tmp = pathPinPout.subList(bb_i + 1,
										pathPinPout.size());
								loss_noise_to_port = this.calcLoss(path_tmp);
							} else {
								loss_noise_to_port = 0;
							}
							double crosstalk_component = loss_signal_j
									+ loss_noise_to_port
									+ Configuration.getK_c();
							if ((this.crossings.get(id_j).getWaveguideInput0() == waveguide_in_j && this.crossings
									.get(id_j).getMicroringIn0Out1() != -1)
									|| (this.crossings.get(id_j)
											.getWaveguideInput1() == waveguide_in_j && this.crossings
											.get(id_j).getMicroringIn1Out0() != -1)) {
								crosstalk_component = Utils.sumDB(
										crosstalk_component, loss_signal_j
												+ loss_noise_to_port
												+ Configuration.getK_poff());
							}
							if (crosstalk == 0) {
								crosstalk = crosstalk_component;
							} else {
								crosstalk = Utils.sumDB(crosstalk,
										crosstalk_component);
							}
						}
						break;
					default:
						if (id_i == id_j) {
							try {
								throw new Exception(
										"@calcCrosstalk -> two different paths cannot share a microring");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return crosstalk;
	}

	/**
	 * Evaluates if two concurrent communications are feasible
	 * 
	 * @param p_in0
	 *            the input port of communication 0
	 * @param p_out0
	 *            the output port of communication 0
	 * @param p_in1
	 *            the input port of communication 1
	 * @param p_out1
	 *            the output port of communication 1
	 * @return <code>true</code> if the two communication don't share any
	 *         waveguide; <code>false</code> otherwise
	 */
	public boolean isValidSimultaneousCommunication(int p_in0, int p_out0,
			int p_in1, int p_out1) {
		if (p_in0 == p_in1 || p_out0 == p_out1) {
			return false;
		}
		List<BuildingBlock> path0 = this.calcPath(p_in0, p_out0);
		List<BuildingBlock> path1 = this.calcPath(p_in1, p_out1);
		if (this.areSharedWaveguides(path0, path1)) {
			return false;
		}
		return true;
	}

	/**
	 * Evaluates if two paths contain the same waveguide.
	 * 
	 * @param path_i
	 *            the list containing all the building blocks that are crossed
	 *            by the signal i
	 * @param path_j
	 *            the list containing all the building blocks that are crossed
	 *            by the signal j
	 * @return <code>true</code> if the two paths share at least a single
	 *         waveguide; <code>false</code> otherwise
	 */
	public boolean areSharedWaveguides(List<BuildingBlock> path_i,
			List<BuildingBlock> path_j) {
		for (int bb_i = 0; bb_i < path_i.size(); bb_i++) {
			int id_i = path_i.get(bb_i).getID();
			for (int bb_j = 0; bb_j < path_j.size(); bb_j++) {
				int id_j = path_j.get(bb_j).getID();
				if (path_i.get(bb_i).getClass().getSimpleName()
						.compareTo("Waveguide") == 0
						&& path_j.get(bb_j).getClass().getSimpleName()
								.compareTo("Waveguide") == 0 && id_i == id_j) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return the connectivity matrix in a readable format
	 */
	public String connectivityMatrixToString() {
		String values = "connectivity matrix at Router level:\n";
		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				values += (this.connectivityMatrix[p_in][p_out] + "\t");
			}
			values += "\n";
		}
		values += "\n";
		return values;
	}

	/**
	 * @return the loss matrix in a readable format
	 */
	public String lossMatrixToString() {
		String values = "loss matrix at Router level:\n";
		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				values += (new DecimalFormat("##.###")
						.format(this.lossMatrix[p_in][p_out]) + "\t");
			}
			values += "\n";
		}
		values += "\n";
		return values;
	}

	/**
	 * @return the loss matrix in a readable format
	 */
	public String crosstalkMatrixToString() {
		String values = "crosstalk matrices at Router level:\n";
		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				values += ("signal " + p_in + " -> " + p_out + "\n");
				for (int p = 0; p < this.numPorts; p++) {
					for (int q = 0; q < this.numPorts; q++) {
						values += (new DecimalFormat("##.###")
								.format(this.crosstalkMatrix[p_in][p_out][p][q]) + "\t");
					}
					values += "\n";
				}
				values += "\n";
			}
		}
		return values;
	}

	/**
	 * @return the router configuration in a readable format
	 */
	public String getReadableRepresentation() {
		String values = "***** Router *****\n";
		values += this.connectivityMatrixToString();
		values += this.lossMatrixToString();
		values += this.crosstalkMatrixToString();
		return values;
	}

	/**
	 * Prints the loss matrix
	 */
	public void printLoss() {
		System.out.println("loss matrix at Router level:");
		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				System.out.print(new DecimalFormat("##.###")
						.format(this.lossMatrix[p_in][p_out]) + "\t");
			}
			System.out.println("");
		}
	}

	/**
	 * Prints the crosstalk matrix
	 */
	public void printCrosstalk() {
		System.out.println("crosstalk matrices at Router level:");
		for (int p_in = 0; p_in < this.numPorts; p_in++) {
			for (int p_out = 0; p_out < this.numPorts; p_out++) {
				System.out.println("signal " + p_in + " -> " + p_out);
				for (int p = 0; p < this.numPorts; p++) {
					for (int q = 0; q < this.numPorts; q++) {
						System.out
								.print(new DecimalFormat("##.###")
										.format(this.crosstalkMatrix[p_in][p_out][p][q])
										+ "\t");
					}
					System.out.println("");
				}
				System.out.println("");
			}
		}
	}

	/**
	 * 
	 * @return the number of ports
	 */
	public int getNumPorts() {
		return numPorts;
	}

	/**
	 * 
	 * @return the number of waveguides
	 */
	public int getNumWaveguides() {
		return numWaveguides;
	}

	/**
	 * 
	 * @return the number of crossings
	 */
	public int getNumCrossings() {
		return numCrossings;
	}

	/**
	 * 
	 * @return the number of microring resonators
	 */
	public int getNumRings() {
		return numRings;
	}

	/**
	 * 
	 * @return the rings map
	 */
	public HashMap<Integer, MicroringResonator> getRings() {
		return rings;
	}

	/**
	 * 
	 * @return the crossings map
	 */
	public HashMap<Integer, Crossing> getCrossings() {
		return crossings;
	}

	/**
	 * 
	 * @return the waveguide maps
	 */
	public HashMap<Integer, Waveguide> getWaveguides() {
		return waveguides;
	}

	/**
	 * 
	 * @return the loss matrix
	 */
	public double[][] getLossMatrix() {
		return lossMatrix;
	}

	/**
	 * 
	 * @return the crosstalk matrix
	 */
	public double[][][][] getCrosstalkMatrix() {
		return crosstalkMatrix;
	}

	/**
	 * The number of ports of the router.
	 */
	private int numPorts;
	/**
	 * The number of waveguides of the router.
	 */
	private int numWaveguides;
	/**
	 * The number of crossings of the router.
	 */
	private int numCrossings;
	/**
	 * The number of microring resonators of the router.
	 */
	private int numRings;
	/**
	 * The map containing the ring objects
	 */
	protected HashMap<Integer, MicroringResonator> rings;
	/**
	 * The map containing the crossing objects
	 */
	protected HashMap<Integer, Crossing> crossings;
	/**
	 * The map containing the waveguide objects
	 */
	protected HashMap<Integer, Waveguide> waveguides;
	/**
	 * The port matrix. An element P_{i,j} is true when a communication between
	 * an input and an output ports is feasible.
	 */
	private boolean[][] connectivityMatrix;
	/**
	 * The loss matrix. An element L_{i,j} gives the power loss (dB) of going
	 * from port i to port j
	 */
	private double[][] lossMatrix;
	/**
	 * The crosstalk matrix. An element K_{i,j,m,n} gives the crosstalk noise
	 * (dB) generated from a message going from port m to port n that impact on
	 * a message going from port i to port j.
	 */
	private double[][][][] crosstalkMatrix;
	/**
	 * The GUI element used to print messages.
	 */
	private JTextArea textArea;
}
