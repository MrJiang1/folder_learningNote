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
 * Provides the OXY router architecture as defined in the following paper:
 * <p>
 * H. Gu, J. Xu, and Z. Wang, “A novel optical mesh network-on-chip for 
 * gigascale systems-on-chip,” in Circuits and Systems, 2008. APCCAS 
 * 2008. IEEE Asia Pacific Conference on. IEEE, 2008, pp. 1728–1731.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class OXY extends Router {

	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public OXY() {
		super(12, 9, 24, null);
	}
	
	/**
	 * Constructs the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public OXY(final JTextArea textArea) {
		super(12, 9, 24, textArea);
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
		// instantiate the 12 microrings of the OXY router
		this.rings.put(1, new MicroringResonator(1, Globals.pse_type, 2, 1, Globals.ejection));
		this.rings.put(2, new MicroringResonator(2, Globals.cse_type, 16, 3, Globals.north));
		this.rings.put(3, new MicroringResonator(3, Globals.pse_type, 14, 18, Globals.west));
		this.rings.put(4, new MicroringResonator(4, Globals.cse_type, 15, 5, Globals.south));
		this.rings.put(5, new MicroringResonator(5, Globals.cse_type, 19, 4, Globals.ejection));
		this.rings.put(6, new MicroringResonator(6, Globals.cse_type, 21, 6, Globals.north));
		this.rings.put(7, new MicroringResonator(7, Globals.cse_type, 20, 8, Globals.south));
		this.rings.put(8, new MicroringResonator(8, Globals.cse_type, 24, 7, Globals.ejection));
		this.rings.put(9, new MicroringResonator(9, Globals.cse_type, 26, 9, Globals.north));
		this.rings.put(10, new MicroringResonator(10, Globals.pse_type, 23, 27, Globals.east));
		this.rings.put(11, new MicroringResonator(11, Globals.cse_type, 25, 11, Globals.south));
		this.rings.put(12, new MicroringResonator(12, Globals.pse_type, 12, 28, Globals.ejection));

		// instantiate the 11 crossings of the OXY router
		this.crossings.put(1, new Crossing(1, 14, 4, 15, 1));
		this.crossings.put(2, new Crossing(2, 15, 2, 16, 5));
		this.crossings.put(3, new Crossing(3, 16, 6, 17, 3));
		this.crossings.put(4, new Crossing(4, 19, 7, 18, 4));
		this.crossings.put(5, new Crossing(5, 20, 5, 19, 8));
		this.crossings.put(6, new Crossing(6, 21, 9, 20, 6));
		this.crossings.put(7, new Crossing(7, 22, 17, 21, 23));
		this.crossings.put(8, new Crossing(8, 24, 10, 25, 7));
		this.crossings.put(9, new Crossing(9, 25, 8, 26, 11));
		this.crossings.put(10, new Crossing(10, 26, 12, 27, 9));
		this.crossings.put(11, new Crossing(11, 28, 11, 10, 13));

		// instantiate the waveguides
		this.waveguides.put(1, new Waveguide(1, 1, -1));
		this.waveguides.put(2, new Waveguide(2, -1, 2));
		this.waveguides.put(3, new Waveguide(3, 3, -1));
		this.waveguides.put(4, new Waveguide(4, 4, 1));
		this.waveguides.put(5, new Waveguide(5, 2, 5));
		this.waveguides.put(6, new Waveguide(6, 6, 3));
		this.waveguides.put(7, new Waveguide(7, 8, 4));
		this.waveguides.put(8, new Waveguide(8, 5, 9));
		this.waveguides.put(9, new Waveguide(9, 10, 6));
		this.waveguides.put(10, new Waveguide(10, 11, 8));
		this.waveguides.put(11, new Waveguide(11, 9, 11));
		this.waveguides.put(12, new Waveguide(12, -1, 10));
		this.waveguides.put(13, new Waveguide(13, 11, -1));
		this.waveguides.put(14, new Waveguide(14, -1, 1));
		this.waveguides.put(15, new Waveguide(15, 1, 2));
		this.waveguides.put(16, new Waveguide(16, 2, 3));
		this.waveguides.put(17, new Waveguide(17, 3, 7));
		this.waveguides.put(18, new Waveguide(18, 4, -1));
		this.waveguides.put(19, new Waveguide(19, 5, 4));
		this.waveguides.put(20, new Waveguide(20, 6, 5));
		this.waveguides.put(21, new Waveguide(21, 7, 6));
		this.waveguides.put(22, new Waveguide(22, -1, 7));
		this.waveguides.put(23, new Waveguide(23, 7, -1));
		this.waveguides.put(24, new Waveguide(24, -1, 8));
		this.waveguides.put(25, new Waveguide(25, 8, 9));
		this.waveguides.put(26, new Waveguide(26, 9, 10));
		this.waveguides.put(27, new Waveguide(27, 10, -1));
		this.waveguides.put(28, new Waveguide(28, -1, 11));

		// add the microrings for parallel PSE to the waveguides
		this.waveguides.get(2).setMicroringPPSE(1);
		this.waveguides.get(12).setMicroringPPSE(12);
		this.waveguides.get(14).setMicroringPPSE(3);
		this.waveguides.get(23).setMicroringPPSE(10);

		this.waveguides.get(1).setMicroringPPSE(1);
		this.waveguides.get(28).setMicroringPPSE(12);
		this.waveguides.get(18).setMicroringPPSE(3);
		this.waveguides.get(27).setMicroringPPSE(10);

		// add the microrings for crossing PSE to the crossings
		this.crossings.get(2).setMicroringIn0Out1(4);
		this.crossings.get(3).setMicroringIn0Out1(2);
		this.crossings.get(4).setMicroringIn0Out1(5);
		this.crossings.get(5).setMicroringIn0Out1(7);
		this.crossings.get(6).setMicroringIn0Out1(6);
		this.crossings.get(8).setMicroringIn0Out1(8);
		this.crossings.get(9).setMicroringIn0Out1(11);
		this.crossings.get(10).setMicroringIn0Out1(9);

		// add ports to the waveguides
		this.waveguides.get(14).setInputPort(Globals.injection);
		this.waveguides.get(2).setInputPort(Globals.north);
		this.waveguides.get(22).setInputPort(Globals.east);
		this.waveguides.get(12).setInputPort(Globals.south);
		this.waveguides.get(24).setInputPort(Globals.west);
		this.waveguides.get(1).setOutputPort(Globals.ejection);
		this.waveguides.get(3).setOutputPort(Globals.north);
		this.waveguides.get(27).setOutputPort(Globals.east);
		this.waveguides.get(13).setOutputPort(Globals.south);
		this.waveguides.get(18).setOutputPort(Globals.west);
	}
}
