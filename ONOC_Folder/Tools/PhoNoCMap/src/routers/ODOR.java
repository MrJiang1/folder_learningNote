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
 * Provides the ODOR router architecture as defined in the following paper:
 * <p>
 * H. Gu, J. Xu, and Z. Wang, “Odor: a microresonator-based high-performance 
 * low-cost router for optical networks-on-chip,” in Proceedings of the 6th 
 * IEEE/ACM/IFIP international conference on Hardware/Software codesign and 
 * system synthesis. ACM, 2008, pp. 203–208.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class ODOR extends Router {

	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public ODOR() {
		super(12, 19, 47, null);
	}
	
	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public ODOR(final JTextArea textArea) {
		super(12, 19, 47, textArea);
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
		// instantiate the 12 microrings of the ODOR router
		this.rings.put(1, new MicroringResonator(1, Globals.cse_type, 5, 4, Globals.ejection));
		this.rings.put(2, new MicroringResonator(2, Globals.cse_type, 32, 9, Globals.north));
		this.rings.put(3, new MicroringResonator(3, Globals.pse_type, 31, 36, Globals.west));
		this.rings.put(4, new MicroringResonator(4, Globals.cse_type, 31, 12, Globals.south));
		this.rings.put(5, new MicroringResonator(5, Globals.cse_type, 38, 13, Globals.north));
		this.rings.put(6, new MicroringResonator(6, Globals.cse_type, 40, 28, Globals.ejection));
		this.rings.put(7, new MicroringResonator(7, Globals.cse_type, 37, 16, Globals.south));
		this.rings.put(8, new MicroringResonator(8, Globals.cse_type, 41, 14, Globals.ejection));
		this.rings.put(9, new MicroringResonator(9, Globals.cse_type, 43, 22, Globals.south));
		this.rings.put(10, new MicroringResonator(10, Globals.cse_type, 44, 17, Globals.north));
		this.rings.put(11, new MicroringResonator(11, Globals.pse_type, 18, 45, Globals.east));
		this.rings.put(12, new MicroringResonator(12, Globals.cse_type, 25, 46, Globals.ejection));

		// instantiate the 19 crossings of the ODOR router
		this.crossings.put(1, new Crossing(1, 26, 5, 4, 7));
		this.crossings.put(2, new Crossing(2, 27, 7, 3, 8));
		this.crossings.put(3, new Crossing(3, 28, 9, 27, 6));
		this.crossings.put(4, new Crossing(4, 29, 10, 30, 1));
		this.crossings.put(5, new Crossing(5, 30, 11, 31, 2));
		this.crossings.put(6, new Crossing(6, 31, 8, 32, 12));
		this.crossings.put(7, new Crossing(7, 32, 13, 33, 9));
		this.crossings.put(8, new Crossing(8, 35, 14, 34, 10));
		this.crossings.put(9, new Crossing(9, 36, 15, 35, 11));
		this.crossings.put(10, new Crossing(10, 37, 12, 36, 16));
		this.crossings.put(11, new Crossing(11, 38, 17, 37, 13));
		this.crossings.put(12, new Crossing(12, 39, 33, 38, 18));
		this.crossings.put(13, new Crossing(13, 40, 19, 39, 28));
		this.crossings.put(14, new Crossing(14, 41, 20, 42, 14));
		this.crossings.put(15, new Crossing(15, 42, 21, 43, 15));
		this.crossings.put(16, new Crossing(16, 43, 16, 44, 22));
		this.crossings.put(17, new Crossing(17, 44, 23, 45, 17));
		this.crossings.put(18, new Crossing(18, 46, 22, 21, 24));
		this.crossings.put(19, new Crossing(19, 47, 25, 46, 23));

		// instantiate the waveguides
		this.waveguides.put(1, new Waveguide(1, 4, -1));
		this.waveguides.put(2, new Waveguide(2, 5, -1));
		this.waveguides.put(3, new Waveguide(3, 2, -1));
		this.waveguides.put(4, new Waveguide(4, 1, -1));
		this.waveguides.put(5, new Waveguide(5, -1, 1));
		this.waveguides.put(6, new Waveguide(6, 3, -1));
		this.waveguides.put(7, new Waveguide(7, 1, 2));
		this.waveguides.put(8, new Waveguide(8, 2, 6));
		this.waveguides.put(9, new Waveguide(9, 7, 3));
		this.waveguides.put(10, new Waveguide(10, 8, 4));
		this.waveguides.put(11, new Waveguide(11, 9, 5));
		this.waveguides.put(12, new Waveguide(12, 6, 10));
		this.waveguides.put(13, new Waveguide(13, 11, 7));
		this.waveguides.put(14, new Waveguide(14, 14, 8));
		this.waveguides.put(15, new Waveguide(15, 15, 9));
		this.waveguides.put(16, new Waveguide(16, 10, 16));
		this.waveguides.put(17, new Waveguide(17, 17, 11));
		this.waveguides.put(18, new Waveguide(18, 12, -1));
		this.waveguides.put(19, new Waveguide(19, -1, 13));
		this.waveguides.put(20, new Waveguide(20, -1, 14));
		this.waveguides.put(21, new Waveguide(21, 18, 15));
		this.waveguides.put(22, new Waveguide(22, 16, 18));
		this.waveguides.put(23, new Waveguide(23, 19, 17));
		this.waveguides.put(24, new Waveguide(24, 18, -1));
		this.waveguides.put(25, new Waveguide(25, -1, 19));
		this.waveguides.put(26, new Waveguide(26, -1, 1));
		this.waveguides.put(27, new Waveguide(27, 3, 2));
		this.waveguides.put(28, new Waveguide(28, 13, 3));
		this.waveguides.put(29, new Waveguide(29, -1, 4));
		this.waveguides.put(30, new Waveguide(30, 4, 5));
		this.waveguides.put(31, new Waveguide(31, 5, 6));
		this.waveguides.put(32, new Waveguide(32, 6, 7));
		this.waveguides.put(33, new Waveguide(33, 7, 12));
		this.waveguides.put(34, new Waveguide(34, 8, -1));
		this.waveguides.put(35, new Waveguide(35, 9, 8));
		this.waveguides.put(36, new Waveguide(36, 10, 9));
		this.waveguides.put(37, new Waveguide(37, 11, 10));
		this.waveguides.put(38, new Waveguide(38, 12, 11));
		this.waveguides.put(39, new Waveguide(39, 13, 12));
		this.waveguides.put(40, new Waveguide(40, -1, 13));
		this.waveguides.put(41, new Waveguide(41, -1, 14));
		this.waveguides.put(42, new Waveguide(42, 14, 15));
		this.waveguides.put(43, new Waveguide(43, 15, 16));
		this.waveguides.put(44, new Waveguide(44, 16, 17));
		this.waveguides.put(45, new Waveguide(45, 17, -1));
		this.waveguides.put(46, new Waveguide(46, 19, 18));
		this.waveguides.put(47, new Waveguide(47, -1, 19));

		// add the microrings for parallel PSE to the waveguides
		this.waveguides.get(31).setMicroringPPSE(3);
		this.waveguides.get(18).setMicroringPPSE(11);

		this.waveguides.get(36).setMicroringPPSE(3);
		this.waveguides.get(45).setMicroringPPSE(11);

		// add the microrings for crossing PSE to the crossings
		this.crossings.get(1).setMicroringIn1Out0(1);
		this.crossings.get(6).setMicroringIn0Out1(4);
		this.crossings.get(7).setMicroringIn0Out1(2);
		this.crossings.get(10).setMicroringIn0Out1(7);
		this.crossings.get(11).setMicroringIn0Out1(5);
		this.crossings.get(13).setMicroringIn0Out1(6);
		this.crossings.get(14).setMicroringIn0Out1(8);
		this.crossings.get(16).setMicroringIn0Out1(9);
		this.crossings.get(17).setMicroringIn0Out1(10);
		this.crossings.get(19).setMicroringIn1Out0(12);

		// add ports to the waveguides
		this.waveguides.get(1).setOutputPort(Globals.ejection);
		this.waveguides.get(2).setOutputPort(Globals.ejection);
		this.waveguides.get(3).setOutputPort(Globals.ejection);
		this.waveguides.get(4).setOutputPort(Globals.ejection);
		this.waveguides.get(29).setInputPort(Globals.injection);
		this.waveguides.get(5).setInputPort(Globals.north);
		this.waveguides.get(6).setOutputPort(Globals.north);
		this.waveguides.get(40).setInputPort(Globals.east);
		this.waveguides.get(45).setOutputPort(Globals.east);
		this.waveguides.get(41).setInputPort(Globals.west);
		this.waveguides.get(34).setOutputPort(Globals.west);
		this.waveguides.get(25).setInputPort(Globals.south);
		this.waveguides.get(24).setOutputPort(Globals.south);
	}
}
