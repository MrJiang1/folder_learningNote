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
package routers;

import javax.swing.JTextArea;

import building_blocks.Crossing;
import building_blocks.MicroringResonator;
import building_blocks.Waveguide;
import main.Globals;
import main.Router;

/**
 * Provides the Cygnus router architecture as defined in the following paper:
 * <p>
 * H. Gu, K. H. Mo, J. Xu, and W. Zhang, “A low-power low-cost optical router 
 * for optical networks-on-chip in multiprocessor systems-on-chip,” in VLSI, 
 * 2009. ISVLSI’09. IEEE Computer Society Annual Symposium on. IEEE, 2009, 
 * pp. 19–24.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class Cygnus extends Router {
	
	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public Cygnus() {
		super(16, 13, 32, null);
	}

	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public Cygnus(final JTextArea textArea) {
		super(16, 13, 32, textArea);
	}

	/**
	 * Initializes the router according to the guidelines of the basic Router
	 * class. This method must set the three maps containing the ring, crossing
	 * and waveguide objects.
	 * 
	 * @see main.Router
	 */
	@Override
	public void initialize() {
		// instantiate the 16 microrings of the Cygnus router
		this.rings.put(1, new MicroringResonator(1, Globals.pse_type, 17, 21, Globals.west));
		this.rings.put(2, new MicroringResonator(2, Globals.pse_type, 24, 5, Globals.ejection));
		this.rings.put(3, new MicroringResonator(3, Globals.pse_type, 19, 3, Globals.north));
		this.rings.put(4, new MicroringResonator(4, Globals.pse_type, 17, 4, Globals.south));
		this.rings.put(5, new MicroringResonator(5, Globals.cse_type, 24, 7, Globals.south));
		this.rings.put(6, new MicroringResonator(6, Globals.pse_type, 6, 5, Globals.ejection));
		this.rings.put(7, new MicroringResonator(7, Globals.cse_type, 27, 3, Globals.north));
		this.rings.put(8, new MicroringResonator(8, Globals.pse_type, 10, 9, Globals.ejection));
		this.rings.put(9, new MicroringResonator(9, Globals.cse_type, 11, 23, Globals.west));
		this.rings.put(10, new MicroringResonator(10, Globals.pse_type, 10, 23, Globals.west));
		this.rings.put(11, new MicroringResonator(11, Globals.pse_type, 29, 9, Globals.ejection));
		this.rings.put(12, new MicroringResonator(12, Globals.cse_type, 10, 31, Globals.east));
		this.rings.put(13, new MicroringResonator(13, Globals.pse_type, 11, 31, Globals.east));
		this.rings.put(14, new MicroringResonator(14, Globals.pse_type, 12, 31, Globals.east));
		this.rings.put(15, new MicroringResonator(15, Globals.cse_type, 28, 14, Globals.south));
		this.rings.put(16, new MicroringResonator(16, Globals.cse_type, 30, 16, Globals.north));

		// instantiate the 13 crossings of the Cygnus router
		this.crossings.put(1, new Crossing(1, 17, 5, 18, 1));
		this.crossings.put(2, new Crossing(2, 18, 2, 19, 6));
		this.crossings.put(3, new Crossing(3, 20, 7, 5, 4));
		this.crossings.put(4, new Crossing(4, 22, 4, 21, 8));
		this.crossings.put(5, new Crossing(5, 23, 9, 22, 20));
		this.crossings.put(6, new Crossing(6, 24, 11, 23, 7));
		this.crossings.put(7, new Crossing(7, 25, 6, 24, 11));
		this.crossings.put(8, new Crossing(8, 26, 19, 25, 12));
		this.crossings.put(9, new Crossing(9, 27, 13, 26, 3));
		this.crossings.put(10, new Crossing(10, 28, 8, 29, 14));
		this.crossings.put(11, new Crossing(11, 29, 15, 30, 10));
		this.crossings.put(12, new Crossing(12, 30, 10, 31, 16));
		this.crossings.put(13, new Crossing(13, 31, 16, 32, 13));

		// instantiate the waveguides
		this.waveguides.put(1, new Waveguide(1, 1, -1));
		this.waveguides.put(2, new Waveguide(2, -1, 2));
		this.waveguides.put(3, new Waveguide(3, 9, -1));
		this.waveguides.put(4, new Waveguide(4, 3, 4));
		this.waveguides.put(5, new Waveguide(5, 3, 1));
		this.waveguides.put(6, new Waveguide(6, 2, 7));
		this.waveguides.put(7, new Waveguide(7, 6, 3));
		this.waveguides.put(8, new Waveguide(8, 4, 10));
		this.waveguides.put(9, new Waveguide(9, -1, 5));
		this.waveguides.put(10, new Waveguide(10, 11, 12));
		this.waveguides.put(11, new Waveguide(11, 7, 6));
		this.waveguides.put(12, new Waveguide(12, 8, -1));
		this.waveguides.put(13, new Waveguide(13, 13, 9));
		this.waveguides.put(14, new Waveguide(14, 10, -1));
		this.waveguides.put(15, new Waveguide(15, -1, 11));
		this.waveguides.put(16, new Waveguide(16, 12, 13));
		this.waveguides.put(17, new Waveguide(17, -1, 1));
		this.waveguides.put(18, new Waveguide(18, 1, 2));
		this.waveguides.put(19, new Waveguide(19, 2, 8));
		this.waveguides.put(20, new Waveguide(20, 5, 3));
		this.waveguides.put(21, new Waveguide(21, 4, -1));
		this.waveguides.put(22, new Waveguide(22, 5, 4));
		this.waveguides.put(23, new Waveguide(23, 6, 5));
		this.waveguides.put(24, new Waveguide(24, 7, 6));
		this.waveguides.put(25, new Waveguide(25, 8, 7));
		this.waveguides.put(26, new Waveguide(26, 9, 8));
		this.waveguides.put(27, new Waveguide(27, -1, 9));
		this.waveguides.put(28, new Waveguide(28, -1, 10));
		this.waveguides.put(29, new Waveguide(29, 10, 11));
		this.waveguides.put(30, new Waveguide(30, 11, 12));
		this.waveguides.put(31, new Waveguide(31, 12, 13));
		this.waveguides.put(32, new Waveguide(32, 13, -1));

		// add the microrings for parallel PSE to the waveguides
		this.waveguides.get(17).setMicroringPPSE(1);
		this.waveguides.get(17).setMicroringPPSE(4);
		this.waveguides.get(6).setMicroringPPSE(6);
		this.waveguides.get(19).setMicroringPPSE(3);
		this.waveguides.get(24).setMicroringPPSE(2);
		this.waveguides.get(10).setMicroringPPSE(8);
		this.waveguides.get(10).setMicroringPPSE(10);
		this.waveguides.get(29).setMicroringPPSE(11);
		this.waveguides.get(11).setMicroringPPSE(13);
		this.waveguides.get(12).setMicroringPPSE(14);

		this.waveguides.get(21).setMicroringPPSE(1);
		this.waveguides.get(5).setMicroringPPSE(2);
		this.waveguides.get(5).setMicroringPPSE(6);
		this.waveguides.get(4).setMicroringPPSE(4);
		this.waveguides.get(3).setMicroringPPSE(3);
		this.waveguides.get(9).setMicroringPPSE(8);
		this.waveguides.get(9).setMicroringPPSE(11);
		this.waveguides.get(23).setMicroringPPSE(10);
		this.waveguides.get(31).setMicroringPPSE(13);
		this.waveguides.get(31).setMicroringPPSE(14);

		// add the microrings for crossing PSE to the waveguide crossings
		this.crossings.get(6).setMicroringIn0Out1(5);
		this.crossings.get(6).setMicroringIn1Out0(9);
		this.crossings.get(9).setMicroringIn0Out1(7);
		this.crossings.get(12).setMicroringIn0Out1(16);
		this.crossings.get(12).setMicroringIn1Out0(12);
		this.crossings.get(10).setMicroringIn0Out1(15);

		// add ports to the waveguides
		this.waveguides.get(1).setOutputPort(Globals.ejection);
		this.waveguides.get(17).setInputPort(Globals.injection);
		this.waveguides.get(21).setOutputPort(Globals.west);
		this.waveguides.get(28).setInputPort(Globals.west);
		this.waveguides.get(15).setInputPort(Globals.south);
		this.waveguides.get(14).setOutputPort(Globals.south);
		this.waveguides.get(2).setInputPort(Globals.north);
		this.waveguides.get(3).setOutputPort(Globals.north);
		this.waveguides.get(27).setInputPort(Globals.east);
		this.waveguides.get(32).setOutputPort(Globals.east);

	}
}
