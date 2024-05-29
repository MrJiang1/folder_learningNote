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
 * Provides the Crux router architecture as defined in the following paper:
 * <p>
 * Y. Xie, M. Nikdast, J. Xu, W. Zhang, Q. Li, X. Wu, Y. Ye, X. Wang, and 
 * W. Liu, “Crosstalk noise and bit error rate analysis for optical 
 * network-on-chip,” in Proceedings of the 47th Design Automation Conference. 
 * ACM, 2010, pp. 657–660.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class Crux extends Router {

	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public Crux(final JTextArea textArea) {
		super(12, 9, 24, textArea);
	}
	
	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public Crux() {
		super(12, 9, 24, null);
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
		// instantiate the 12 microrings of the Crux router
		this.rings.put(1, new MicroringResonator(1, Globals.pse_type, 1, 2, Globals.ejection));
		this.rings.put(2, new MicroringResonator(2, Globals.cse_type, 15, 2, Globals.ejection));
		this.rings.put(3, new MicroringResonator(3, Globals.cse_type, 16, 3, Globals.north));
		this.rings.put(4, new MicroringResonator(4, Globals.pse_type, 17, 13, Globals.west));
		this.rings.put(5, new MicroringResonator(5, Globals.cse_type, 14, 4, Globals.south));
		this.rings.put(6, new MicroringResonator(6, Globals.cse_type, 19, 6, Globals.north));
		this.rings.put(7, new MicroringResonator(7, Globals.cse_type, 17, 7, Globals.south));
		this.rings.put(8, new MicroringResonator(8, Globals.cse_type, 22, 8, Globals.ejection));
		this.rings.put(9, new MicroringResonator(9, Globals.cse_type, 23, 9, Globals.north));
		this.rings.put(10, new MicroringResonator(10, Globals.pse_type, 20, 24, Globals.east));
		this.rings.put(11, new MicroringResonator(11, Globals.cse_type, 21, 10, Globals.south));
		this.rings.put(12, new MicroringResonator(12, Globals.pse_type, 12, 11, Globals.ejection));

		// instantiate the 9 crossings of the Crux router
		this.crossings.put(1, new Crossing(1, 14, 1, 13, 4));
		this.crossings.put(2, new Crossing(2, 15, 5, 14, 2));
		this.crossings.put(3, new Crossing(3, 16, 6, 15, 3));
		this.crossings.put(4, new Crossing(4, 17, 4, 18, 7));
		this.crossings.put(5, new Crossing(5, 18, 8, 19, 5));
		this.crossings.put(6, new Crossing(6, 19, 9, 20, 6));
		this.crossings.put(7, new Crossing(7, 21, 7, 22, 10));
		this.crossings.put(8, new Crossing(8, 22, 11, 23, 8));
		this.crossings.put(9, new Crossing(9, 23, 12, 24, 9));

		// instantiate the waveguides
		this.waveguides.put(1, new Waveguide(1, -1, 1));
		this.waveguides.put(2, new Waveguide(2, 2, -1));
		this.waveguides.put(3, new Waveguide(3, 3, -1));
		this.waveguides.put(4, new Waveguide(4, 1, 4));
		this.waveguides.put(5, new Waveguide(5, 5, 2));
		this.waveguides.put(6, new Waveguide(6, 6, 3));
		this.waveguides.put(7, new Waveguide(7, 4, 7));
		this.waveguides.put(8, new Waveguide(8, 8, 5));
		this.waveguides.put(9, new Waveguide(9, 9, 6));
		this.waveguides.put(10, new Waveguide(10, 7, -1));
		this.waveguides.put(11, new Waveguide(11, -1, 8));
		this.waveguides.put(12, new Waveguide(12, -1, 9));
		this.waveguides.put(13, new Waveguide(13, 1, -1));
		this.waveguides.put(14, new Waveguide(14, 2, 1));
		this.waveguides.put(15, new Waveguide(15, 3, 2));
		this.waveguides.put(16, new Waveguide(16, -1, 3));
		this.waveguides.put(17, new Waveguide(17, -1, 4));
		this.waveguides.put(18, new Waveguide(18, 4, 5));
		this.waveguides.put(19, new Waveguide(19, 5, 6));
		this.waveguides.put(20, new Waveguide(20, 6, -1));
		this.waveguides.put(21, new Waveguide(21, -1, 7));
		this.waveguides.put(22, new Waveguide(22, 7, 8));
		this.waveguides.put(23, new Waveguide(23, 8, 9));
		this.waveguides.put(24, new Waveguide(24, 9, -1));

		// add the microrings for parallel PSE to the waveguides
		this.waveguides.get(1).setMicroringPPSE(1);
		this.waveguides.get(17).setMicroringPPSE(4);
		this.waveguides.get(20).setMicroringPPSE(10);
		this.waveguides.get(12).setMicroringPPSE(12);

		this.waveguides.get(2).setMicroringPPSE(1);
		this.waveguides.get(13).setMicroringPPSE(4);
		this.waveguides.get(24).setMicroringPPSE(10);
		this.waveguides.get(11).setMicroringPPSE(12);

		// add the microrings for crossing PSE to the crossings
		this.crossings.get(1).setMicroringIn0Out1(5);
		this.crossings.get(2).setMicroringIn0Out1(2);
		this.crossings.get(3).setMicroringIn0Out1(3);
		this.crossings.get(4).setMicroringIn0Out1(7);
		this.crossings.get(6).setMicroringIn0Out1(6);
		this.crossings.get(7).setMicroringIn0Out1(11);
		this.crossings.get(8).setMicroringIn0Out1(8);
		this.crossings.get(9).setMicroringIn0Out1(9);

		// add ports to the waveguides
		this.waveguides.get(1).setInputPort(Globals.north);
		this.waveguides.get(16).setInputPort(Globals.east);
		this.waveguides.get(17).setInputPort(Globals.injection);
		this.waveguides.get(21).setInputPort(Globals.west);
		this.waveguides.get(12).setInputPort(Globals.south);
		this.waveguides.get(2).setOutputPort(Globals.ejection);
		this.waveguides.get(3).setOutputPort(Globals.north);
		this.waveguides.get(13).setOutputPort(Globals.west);
		this.waveguides.get(24).setOutputPort(Globals.east);
		this.waveguides.get(10).setOutputPort(Globals.south);
	}
}
