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

package routing;

import java.util.EmptyStackException;

import main.Globals;
import main.Routing;
import main.Topology;

/**
 * Provides the XY dimension order routing for folded torus topologies.
 * 
 * @author Giuseppe Ranieri Musella, University of Naples Federico II
 *
 */
public class XYTorusFolded extends Routing {

	/*
	 * (non-Javadoc)
	 * @see main.Routing#calcOutputPort(main.Topology, int, int)
	 */
	@Override
	public int calcOutputPort(Topology top, int src, int dst) {
		int port = -1;
		int src_x = top.getTile(src).getRow();
		int src_y = top.getTile(src).getColumn();
		int dst_x = top.getTile(dst).getRow();
		int dst_y = top.getTile(dst).getColumn();
		int last_odd;
		int last_even;
		int dist_even = -1;
		int dist_odd = -1;
		int dist = -1;

		if (src_x == dst_x && src_y == dst_y) {
			port = Globals.ejection;
		} else if (src_y != dst_y) {
			last_even = top.getN() / 2 - ((top.getN() + 1) % 2);

			if ((src_y <= last_even && dst_y <= last_even) || (src_y > last_even && dst_y > last_even)) {
				if (src_y <= last_even && dst_y <= last_even) {
					if (src_y < dst_y)
						port = Globals.east;
					else
						port = Globals.west;
				} else {
					if (src_y < dst_y)
						port = Globals.west;
					else
						port = Globals.east;
				}
			} else {
				dist = Math.abs(src_y - dst_y);
				if (dist <= ((float) top.getN() / 2.0))
					port = Globals.east;
				else
					port = Globals.west;
			}
		} else if (src_x != dst_x) {
			last_odd = (top.getM() % 2 == 0) ? top.getM() - 1 : top.getM() - 2;
			last_even = (top.getM() % 2 == 0) ? top.getM() - 2 : top.getM() - 1;
			if ((src_x % 2 == 0 && dst_x % 2 == 0) || ((src_x % 2 == 1 && dst_x % 2 == 1))) {
				if (src_x <= dst_x)
					port = Globals.south;
				else
					port = Globals.north;
			} else {
				if (src_x % 2 == 0) {
					dist_odd = Math.abs(dst_x - last_odd);
					dist_even = Math.abs(src_x - last_even);
				} else {
					dist_odd = Math.abs(src_x - last_odd);
					dist_even = Math.abs(dst_x - last_even);
				}
				dist = (dist_odd + dist_even) / 2 + 1;

				if (dist <= ((float) top.getM() / 2.0))
					port = Globals.south;
				else
					port = Globals.north;
			}
		}

		return port;
	}

	/*
	 * (non-Javadoc)
	 * @see main.Routing#calcInputPort(main.Topology, int, int)
	 */
	public int calcInputPort(Topology top, int src, int dst) {
		int p_out = this.calcOutputPort(top, src, dst);
		int src_x = top.getTiles().get(src).getRow();
		int src_y = top.getTiles().get(src).getColumn();

		switch (p_out) {
		case Globals.north:
			if (src_x == 0 || src_x == 1)
				return Globals.north;
			else
				return Globals.south;
		case Globals.east:
			if ((src_y == top.getN() / 2 - ((top.getN() + 1) % 2)) || (src_y == top.getN() / 2 + (top.getN() % 2)))
				return Globals.east;
			else
				return Globals.west;
		case Globals.south:
			if ((src_x == top.getM() - 1) || (src_x == top.getM() - 2))
				return Globals.south;
			else
				return Globals.north;
		case Globals.west:
			if ((src_y == 0) || (src_y == top.getN() - 1))
				return Globals.west;
			else
				return Globals.east;
		default:
			System.out.println("Error: calcInputPortFromOutputPort -> not possible for port " + p_out);
			throw new EmptyStackException();
		}
	}
}
