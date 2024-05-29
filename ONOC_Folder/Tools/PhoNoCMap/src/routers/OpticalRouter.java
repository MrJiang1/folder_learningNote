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
 * Provides the optical router architecture as defined in the following
 * paper:
 * <p>
 * R. Ji, L. Yang, L. Zhang, Y. Tian, J. Ding, H. Chen, Y. Lu, P. Zhou, and 
 * W. Zhu, “Five-port optical router for photonic networks-on-chip,” Optics 
 * express, vol. 19, no. 21, pp. 20 258–20 268, 2011.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class OpticalRouter extends Router {
	
	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public OpticalRouter() {
		super(16, 14, 33, null);
	}

	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public OpticalRouter(final JTextArea textArea) {
		super(16, 14, 33, textArea);
	}

	/**
	 * Initializes the router according to the guidelines of the basic Router
	 * class. This method must set the three maps containing the ring, crossing
	 * and waveguide objects.
	 * 
	 * @see main.Router
	 */
	@Override
	protected void initialize() {
		this.rings.put(1, new MicroringResonator(1, Globals.pse_type, 34, 33, Globals.south));
		this.rings.put(2, new MicroringResonator(2, Globals.cse_type, 22, 33, Globals.south));
		this.rings.put(3, new MicroringResonator(3, Globals.cse_type, 24, 1, Globals.ejection));
		this.rings.put(4, new MicroringResonator(4, Globals.pse_type, 6, 5, Globals.south));
		this.rings.put(5, new MicroringResonator(5, Globals.pse_type, 7, 21, Globals.west));
		this.rings.put(6, new MicroringResonator(6, Globals.cse_type, 8, 21, Globals.west));
		this.rings.put(7, new MicroringResonator(7, Globals.pse_type, 6, 21, Globals.west));
		this.rings.put(8, new MicroringResonator(8, Globals.cse_type, 9, 10, Globals.ejection));
		this.rings.put(9, new MicroringResonator(9, Globals.pse_type, 7, 12, Globals.ejection));
		this.rings.put(10, new MicroringResonator(10, Globals.cse_type, 13, 25, Globals.ejection));
		this.rings.put(11, new MicroringResonator(11, Globals.cse_type, 26, 16, Globals.north));
		this.rings.put(12, new MicroringResonator(12, Globals.cse_type, 14, 27, Globals.east));
		this.rings.put(13, new MicroringResonator(13, Globals.cse_type, 11, 28, Globals.east));
		this.rings.put(14, new MicroringResonator(14, Globals.pse_type, 3, 28, Globals.east));
		this.rings.put(15, new MicroringResonator(15, Globals.pse_type, 32, 17, Globals.north));
		this.rings.put(16, new MicroringResonator(16, Globals.pse_type, 19, 17, Globals.north));

		this.crossings.put(1, new Crossing(1, 21, 33, 20, 5));
		this.crossings.put(2, new Crossing(2, 22, 8, 21, 33));
		this.crossings.put(3, new Crossing(3, 23, 34, 22, 9));
		this.crossings.put(4, new Crossing(4, 24, 10, 23, 1));
		this.crossings.put(5, new Crossing(5, 3, 2, 24, 11));
		this.crossings.put(6, new Crossing(6, 12, 13, 25, 8));
		this.crossings.put(7, new Crossing(7, 25, 9, 10, 14));
		this.crossings.put(8, new Crossing(8, 7, 15, 26, 13));
		this.crossings.put(9, new Crossing(9, 26, 14, 27, 16));
		this.crossings.put(10, new Crossing(10, 27, 11, 28, 32));
		this.crossings.put(11, new Crossing(11, 28, 17, 29, 4));
		this.crossings.put(12, new Crossing(12, 30, 18, 6, 7));
		this.crossings.put(13, new Crossing(13, 31, 19, 30, 15));
		this.crossings.put(14, new Crossing(14, 32, 16, 31, 17));

		this.waveguides.put(1, new Waveguide(1, 4, -1));
		this.waveguides.put(2, new Waveguide(2, -1, 5));
		this.waveguides.put(3, new Waveguide(3, -1, 5));
		this.waveguides.put(4, new Waveguide(4, 11, -1));
		this.waveguides.put(5, new Waveguide(5, 1, -1));
		this.waveguides.put(6, new Waveguide(6, 12, -1));
		this.waveguides.put(7, new Waveguide(7, 12, 8));
		this.waveguides.put(8, new Waveguide(8, 6, 2));
		this.waveguides.put(9, new Waveguide(9, 3, 7));
		this.waveguides.put(10, new Waveguide(10, 7, 4));
		this.waveguides.put(11, new Waveguide(11, 5, 10));
		this.waveguides.put(12, new Waveguide(12, -1, 6));
		this.waveguides.put(13, new Waveguide(13, 8, 6));
		this.waveguides.put(14, new Waveguide(14, 7, 9));
		this.waveguides.put(15, new Waveguide(15, 13, 8));
		this.waveguides.put(16, new Waveguide(16, 9, 14));
		this.waveguides.put(17, new Waveguide(17, 14, 11));
		this.waveguides.put(18, new Waveguide(18, -1, 12));
		this.waveguides.put(19, new Waveguide(19, -1, 13));
		this.waveguides.put(20, new Waveguide(20, 1, -1));
		this.waveguides.put(21, new Waveguide(21, 2, 1));
		this.waveguides.put(22, new Waveguide(22, 3, 2));
		this.waveguides.put(23, new Waveguide(23, 4, 3));
		this.waveguides.put(24, new Waveguide(24, 5, 4));
		this.waveguides.put(25, new Waveguide(25, 6, 7));
		this.waveguides.put(26, new Waveguide(26, 8, 9));
		this.waveguides.put(27, new Waveguide(27, 9, 10));
		this.waveguides.put(28, new Waveguide(28, 10, 11));
		this.waveguides.put(29, new Waveguide(29, 11, -1));
		this.waveguides.put(30, new Waveguide(30, 13, 12));
		this.waveguides.put(31, new Waveguide(31, 14, 13));
		this.waveguides.put(32, new Waveguide(32, 10, 14));
		this.waveguides.put(33, new Waveguide(33, 2, 1));
		this.waveguides.put(34, new Waveguide(34, -1, 3));

		this.waveguides.get(34).setMicroringPPSE(1);
		this.waveguides.get(6).setMicroringPPSE(4);
		this.waveguides.get(7).setMicroringPPSE(5);
		this.waveguides.get(6).setMicroringPPSE(7);
		this.waveguides.get(7).setMicroringPPSE(9);
		this.waveguides.get(3).setMicroringPPSE(14);
		this.waveguides.get(32).setMicroringPPSE(15);
		this.waveguides.get(19).setMicroringPPSE(16);

		this.waveguides.get(33).setMicroringPPSE(1);
		this.waveguides.get(5).setMicroringPPSE(4);
		this.waveguides.get(21).setMicroringPPSE(5);
		this.waveguides.get(21).setMicroringPPSE(7);
		this.waveguides.get(12).setMicroringPPSE(9);
		this.waveguides.get(28).setMicroringPPSE(14);
		this.waveguides.get(17).setMicroringPPSE(15);
		this.waveguides.get(17).setMicroringPPSE(16);

		this.crossings.get(2).setMicroringIn0Out1(2);
		this.crossings.get(4).setMicroringIn0Out1(3);
		this.crossings.get(2).setMicroringIn1Out0(6);
		this.crossings.get(7).setMicroringIn1Out0(8);
		this.crossings.get(6).setMicroringIn1Out0(10);
		this.crossings.get(9).setMicroringIn0Out1(11);
		this.crossings.get(9).setMicroringIn1Out0(12);
		this.crossings.get(10).setMicroringIn1Out0(13);

		this.waveguides.get(1).setOutputPort(Globals.ejection);
		this.waveguides.get(2).setInputPort(Globals.injection);
		this.waveguides.get(4).setOutputPort(Globals.north);
		this.waveguides.get(3).setInputPort(Globals.north);
		this.waveguides.get(19).setInputPort(Globals.east);
		this.waveguides.get(29).setOutputPort(Globals.east);
		this.waveguides.get(5).setOutputPort(Globals.south);
		this.waveguides.get(18).setInputPort(Globals.south);
		this.waveguides.get(20).setOutputPort(Globals.west);
		this.waveguides.get(34).setInputPort(Globals.west);

	}

}
