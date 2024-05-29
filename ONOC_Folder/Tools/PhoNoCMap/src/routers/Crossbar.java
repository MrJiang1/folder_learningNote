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
 * Provides the Crossbar router architecture as defined in the following paper:
 * <p>
 * A. W. Poon, X. Luo, F. Xu, and H. Chen, “Cascaded microresonatorbased matrix 
 * switch for silicon on-chip optical interconnection,” Proceedings of the IEEE, 
 * vol. 97, no. 7, pp. 1216–1238, 2009.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 */
public class Crossbar extends Router {

	/**
	 * Construct the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 */
	public Crossbar() {
		super(20, 26, 52, null);
	}
	
	/**
	 * Construct the router object with several Waveguide, Crossing and
	 * MicroringResonator instances. It will calls the initialize() method.
	 * 
	 * @param textArea
	 *            The GUI element used to print messages
	 */
	public Crossbar(final JTextArea textArea) {
		super(20, 26, 52, textArea);
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
		this.rings.put(1, new MicroringResonator(1, Globals.cse_type, 28, 2, Globals.north));
		this.rings.put(2, new MicroringResonator(2, Globals.cse_type, 29, 6, Globals.west));
		this.rings.put(3, new MicroringResonator(3, Globals.cse_type, 30, 7, Globals.south));
		this.rings.put(4, new MicroringResonator(4, Globals.cse_type, 31, 8, Globals.east));
		this.rings.put(5, new MicroringResonator(5, Globals.cse_type, 32, 4, Globals.ejection));
		this.rings.put(6, new MicroringResonator(6, Globals.cse_type, 33, 5, Globals.north));
		this.rings.put(7, new MicroringResonator(7, Globals.cse_type, 35, 12, Globals.south));
		this.rings.put(8, new MicroringResonator(8, Globals.cse_type, 36, 13, Globals.east));
		this.rings.put(9, new MicroringResonator(9, Globals.cse_type, 37, 9, Globals.ejection));
		this.rings.put(10, new MicroringResonator(10, Globals.cse_type, 39, 16, Globals.west));
		this.rings.put(11, new MicroringResonator(11, Globals.cse_type, 40, 17, Globals.south));
		this.rings.put(12, new MicroringResonator(12, Globals.cse_type, 3, 18, Globals.east));
		this.rings.put(13, new MicroringResonator(13, Globals.cse_type, 41, 14, Globals.ejection));
		this.rings.put(14, new MicroringResonator(14, Globals.cse_type, 42, 15, Globals.north));
		this.rings.put(15, new MicroringResonator(15, Globals.cse_type, 43, 21, Globals.west));
		this.rings.put(16, new MicroringResonator(16, Globals.cse_type, 44, 22, Globals.south));
		this.rings.put(17, new MicroringResonator(17, Globals.cse_type, 46, 19, Globals.ejection));
		this.rings.put(18, new MicroringResonator(18, Globals.cse_type, 47, 20, Globals.north));
		this.rings.put(19, new MicroringResonator(19, Globals.cse_type, 48, 24, Globals.west));
		this.rings.put(20, new MicroringResonator(20, Globals.cse_type, 26, 52, Globals.east));

		this.crossings.put(1, new Crossing(1, 27, 4, 28, 1));
		this.crossings.put(2, new Crossing(2, 28, 5, 29, 2));
		this.crossings.put(3, new Crossing(3, 29, -1, 30, 6));
		this.crossings.put(4, new Crossing(4, 30, -1, 31, 7));
		this.crossings.put(5, new Crossing(5, 31, -1, -1, 8));
		this.crossings.put(6, new Crossing(6, 32, 9, 33, 4));
		this.crossings.put(7, new Crossing(7, 33, 10, 34, 5));
		this.crossings.put(8, new Crossing(8, 34, 6, 35, 11));
		this.crossings.put(9, new Crossing(9, 35, 7, 36, 12));
		this.crossings.put(10, new Crossing(10, 36, 8, -1, 13));
		this.crossings.put(11, new Crossing(11, 37, 14, -1, 9));
		this.crossings.put(12, new Crossing(12, 38, 15, 37, 10));
		this.crossings.put(13, new Crossing(13, 39, 11, 38, 16));
		this.crossings.put(14, new Crossing(14, 40, 12, 39, 17));
		this.crossings.put(15, new Crossing(15, 3, 13, 40, 18));
		this.crossings.put(16, new Crossing(16, 41, 19, -1, 14));
		this.crossings.put(17, new Crossing(17, 42, 20, 41, 15));
		this.crossings.put(18, new Crossing(18, 43, 16, 42, 21));
		this.crossings.put(19, new Crossing(19, 44, 17, 43, 22));
		this.crossings.put(20, new Crossing(20, 45, 18, 44, 23));
		this.crossings.put(21, new Crossing(21, 46, -1, -1, 19));
		this.crossings.put(22, new Crossing(22, 47, -1, 46, 20));
		this.crossings.put(23, new Crossing(23, 48, 21, 47, 24));
		this.crossings.put(24, new Crossing(24, 49, 22, 48, 25));
		this.crossings.put(25, new Crossing(25, 26, 23, 49, 52));
		this.crossings.put(26, new Crossing(26, 52, 51, 50, 26));

		this.waveguides.put(1, new Waveguide(1, 1, -1));
		this.waveguides.put(2, new Waveguide(2, 2, -1));
		this.waveguides.put(3, new Waveguide(3, -1, 15));
		this.waveguides.put(4, new Waveguide(4, 6, 1));
		this.waveguides.put(5, new Waveguide(5, 7, 2));
		this.waveguides.put(6, new Waveguide(6, 3, 8));
		this.waveguides.put(7, new Waveguide(7, 4, 9));
		this.waveguides.put(8, new Waveguide(8, 5, 10));
		this.waveguides.put(9, new Waveguide(9, 11, 6));
		this.waveguides.put(10, new Waveguide(10, 12, 7));
		this.waveguides.put(11, new Waveguide(11, 8, 13));
		this.waveguides.put(12, new Waveguide(12, 9, 14));
		this.waveguides.put(13, new Waveguide(13, 10, 15));
		this.waveguides.put(14, new Waveguide(14, 16, 11));
		this.waveguides.put(15, new Waveguide(15, 17, 12));
		this.waveguides.put(16, new Waveguide(16, 13, 18));
		this.waveguides.put(17, new Waveguide(17, 14, 19));
		this.waveguides.put(18, new Waveguide(18, 15, 20));
		this.waveguides.put(19, new Waveguide(19, 21, 16));
		this.waveguides.put(20, new Waveguide(20, 22, 17));
		this.waveguides.put(21, new Waveguide(21, 18, 23));
		this.waveguides.put(22, new Waveguide(22, 19, 24));
		this.waveguides.put(23, new Waveguide(23, 20, 25));
		this.waveguides.put(24, new Waveguide(24, 7, 6));
		this.waveguides.put(25, new Waveguide(25, 24, -1));
		this.waveguides.put(26, new Waveguide(26, 26, 25));
		this.waveguides.put(27, new Waveguide(27, -1, 1));
		this.waveguides.put(28, new Waveguide(28, 1, 2));
		this.waveguides.put(29, new Waveguide(29, 2, 3));
		this.waveguides.put(30, new Waveguide(30, 3, 4));
		this.waveguides.put(31, new Waveguide(31, 4, 5));
		this.waveguides.put(32, new Waveguide(32, -1, 6));
		this.waveguides.put(33, new Waveguide(33, 6, 7));
		this.waveguides.put(34, new Waveguide(34, 7, 8));
		this.waveguides.put(35, new Waveguide(35, 8, 9));
		this.waveguides.put(36, new Waveguide(36, 9, 10));
		this.waveguides.put(37, new Waveguide(37, 12, 11));
		this.waveguides.put(38, new Waveguide(38, 13, 12));
		this.waveguides.put(39, new Waveguide(39, 14, 13));
		this.waveguides.put(40, new Waveguide(40, 15, 14));
		this.waveguides.put(41, new Waveguide(41, 17, 16));
		this.waveguides.put(42, new Waveguide(42, 18, 17));
		this.waveguides.put(43, new Waveguide(43, 19, 18));
		this.waveguides.put(44, new Waveguide(44, 20, 19));
		this.waveguides.put(45, new Waveguide(45, -1, 20));
		this.waveguides.put(46, new Waveguide(46, 22, 21));
		this.waveguides.put(47, new Waveguide(47, 23, 22));
		this.waveguides.put(48, new Waveguide(48, 24, 23));
		this.waveguides.put(49, new Waveguide(49, 25, 24));
		this.waveguides.put(50, new Waveguide(50, 26, -1));
		this.waveguides.put(51, new Waveguide(51, -1, 26));
		this.waveguides.put(52, new Waveguide(52, 25, 26));

		this.crossings.get(2).setMicroringIn0Out1(1);
		this.crossings.get(3).setMicroringIn0Out1(2);
		this.crossings.get(4).setMicroringIn0Out1(3);
		this.crossings.get(5).setMicroringIn0Out1(4);
		this.crossings.get(6).setMicroringIn0Out1(5);
		this.crossings.get(7).setMicroringIn0Out1(6);
		this.crossings.get(9).setMicroringIn0Out1(7);
		this.crossings.get(10).setMicroringIn0Out1(8);
		this.crossings.get(11).setMicroringIn0Out1(9);
		this.crossings.get(13).setMicroringIn0Out1(10);
		this.crossings.get(14).setMicroringIn0Out1(11);
		this.crossings.get(15).setMicroringIn0Out1(12);
		this.crossings.get(16).setMicroringIn0Out1(13);
		this.crossings.get(17).setMicroringIn0Out1(14);
		this.crossings.get(18).setMicroringIn0Out1(15);
		this.crossings.get(19).setMicroringIn0Out1(16);
		this.crossings.get(21).setMicroringIn0Out1(17);
		this.crossings.get(22).setMicroringIn0Out1(18);
		this.crossings.get(23).setMicroringIn0Out1(19);
		this.crossings.get(25).setMicroringIn0Out1(20);

		this.waveguides.get(1).setOutputPort(Globals.ejection);
		this.waveguides.get(27).setInputPort(Globals.injection);
		this.waveguides.get(2).setOutputPort(Globals.north);
		this.waveguides.get(3).setInputPort(Globals.north);
		this.waveguides.get(45).setInputPort(Globals.east);
		this.waveguides.get(50).setOutputPort(Globals.east);
		this.waveguides.get(25).setOutputPort(Globals.south);
		this.waveguides.get(51).setInputPort(Globals.south);
		this.waveguides.get(24).setOutputPort(Globals.west);
		this.waveguides.get(32).setInputPort(Globals.west);
	}

}
