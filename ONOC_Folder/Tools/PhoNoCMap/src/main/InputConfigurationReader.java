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

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides the configuration file reader. An object of this class is 
 * required to read the configuration xml file.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class InputConfigurationReader {

	/**
	 * @param fileName The configuration file name
	 */
	public InputConfigurationReader(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Parses the configuration file name and store the configuration
	 * information in appropriate data structures.
	 */
	public void acquireData() {
		try {
			File sourceFile = new File(this.fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(sourceFile);
			doc.getDocumentElement().normalize();

			this.mapping_strategies = new ArrayList<String>();
			this.topology_types = new ArrayList<String>();
			this.router_types = new ArrayList<String>();
			this.routing_types = new ArrayList<String>();
			
			this.acquireOpticalCoefficients(doc);
			this.acquireNoCArchitecture(doc);
			this.acquireApplicationAndMapping(doc);

		} catch (Exception e) {
			System.out.println("Excpetion = " + e);
		}
	}

	/**
	 * Acquires the optical loss and crosstalk coefficients
	 * 
	 * @param doc The DOM document object resulting
	 * from the xml file parsing
	 */
	private void acquireOpticalCoefficients(Document doc) {
		Element power_loss = (Element) doc.getElementsByTagName("power_loss").item(0);
		this.L_p = Double.parseDouble(power_loss.getAttribute("propagation").toString());
		this.L_c = Double.parseDouble(power_loss.getAttribute("crossing").toString());
		this.L_poff = Double.parseDouble(power_loss.getAttribute("ppse_off").toString());
		this.L_pon = Double.parseDouble(power_loss.getAttribute("ppse_on").toString());
		this.L_coff = Double.parseDouble(power_loss.getAttribute("cpse_off").toString());
		this.L_con = Double.parseDouble(power_loss.getAttribute("cpse_on").toString());
		Element crosstalk = (Element) doc.getElementsByTagName("crosstalk").item(0);
		this.K_c = Double.parseDouble(crosstalk.getAttribute("crossing").toString());
		this.K_poff = Double.parseDouble(crosstalk.getAttribute("ppse_off").toString());
		this.K_pon = Double.parseDouble(crosstalk.getAttribute("ppse_on").toString());
		Element miscellaneous = (Element) doc.getElementsByTagName("miscellaneous").item(0);
		this.sensitivity = Double.parseDouble(miscellaneous.getAttribute("photodetector_sensitivity").toString());
		this.efficiency = Integer.parseInt(miscellaneous.getAttribute("laser_efficiency").toString());
		this.modulation = Integer.parseInt(miscellaneous.getAttribute("modulation_rate").toString());
	}

	/**
	 * Acquires the architecture features
	 * 
	 * @param doc The DOM document object resulting
	 * from the xml file parsing
	 */
	private void acquireNoCArchitecture(Document doc) {
		NodeList nodeListTopology = doc.getElementsByTagName("topology");
		Element topology = (Element) nodeListTopology.item(0);
		this.m = Integer.parseInt(topology.getAttribute("rows").toString());
		this.n = Integer.parseInt(topology.getAttribute("columns").toString());
		NodeList nodeListTopologyTypes = nodeListTopology.item(0).getChildNodes();
		for (int i = 0; i < nodeListTopologyTypes.getLength(); i++) {
			Node nodeType = nodeListTopologyTypes.item(i);
			if (nodeType.getNodeType() == Node.ELEMENT_NODE) {
				Element elementType = (Element) nodeType;
				this.topology_types.add(elementType.getAttribute("class_name").toString());
			}
		}
		
		NodeList nodeListRoutingTypes = doc.getElementsByTagName("routing").item(0).getChildNodes();
		for (int i = 0; i < nodeListRoutingTypes.getLength(); i++) {
			Node nodeType = nodeListRoutingTypes.item(i);
			if (nodeType.getNodeType() == Node.ELEMENT_NODE) {
				Element elementType = (Element) nodeType;
				this.routing_types.add(elementType.getAttribute("class_name").toString());
			}
		}
		
		NodeList nodeListRouterTypes = doc.getElementsByTagName("router").item(0).getChildNodes();
		for (int i = 0; i < nodeListRouterTypes.getLength(); i++) {
			Node nodeType = nodeListRouterTypes.item(i);
			if (nodeType.getNodeType() == Node.ELEMENT_NODE) {
				Element elementType = (Element) nodeType;
				this.router_types.add(elementType.getAttribute("class_name").toString());
			}
		}
		Element chip = (Element) doc.getElementsByTagName("chip").item(0);
		this.chipSize = Integer.parseInt(chip.getAttribute("size").toString());
	}

	/**
	 * Acquires the application features
	 * 
	 * @param doc The DOM document object resulting
	 * from the xml file parsing
	 */
	private void acquireApplicationAndMapping(Document doc) {
		NodeList nodeListMapping = doc.getElementsByTagName("Mapping");
		Element mapping = (Element) nodeListMapping.item(0);
		this.iterations = Integer.parseInt(mapping.getAttribute("iterations").toString());
		NodeList nodeListMappingTypes = nodeListMapping.item(0).getChildNodes();
		for (int i = 0; i < nodeListMappingTypes.getLength(); i++) {
			Node nodeType = nodeListMappingTypes.item(i);
			if (nodeType.getNodeType() == Node.ELEMENT_NODE) {
				Element elementType = (Element) nodeType;
				this.mapping_strategies.add(elementType.getAttribute("class_name").toString());
				if (elementType.getAttribute("class_name").toString().compareTo("GeneticMapping") == 0){
					this.population = Integer.parseInt(elementType.getAttribute("population").toString());
					this.offspring = Integer.parseInt(elementType.getAttribute("offspring").toString());
				}
			}
		}
	}
	
	
	/**
	 * @return The propagation loss
	 */
	public double getL_p() {
		return L_p;
	}
	/**
	 * @return The crossing loss
	 */
	public double getL_c() {
		return L_c;
	}
	/**
	 * @return The power loss per parallel switching element in OFF resonance state
	 */
	public double getL_poff() {
		return L_poff;
	}
	/**
	 * @return The power loss per parallel switching element in ON resonance state
	 */
	public double getL_pon() {
		return L_pon;
	}
	/**
	 * @return The power loss per crossing switching element in OFF resonance state.
	 */
	public double getL_coff() {
		return L_coff;
	}
	/**
	 * @return The power loss per crossing switching element in ON resonance state.
	 */
	public double getL_con() {
		return L_con;
	}
	/**
	 * @return The crossing’s crosstalk coefficient.
	 */
	public double getK_c() {
		return K_c;
	}
	/**
	 * @return The crosstalk coefficient per PSE in OFF state.
	 */
	public double getK_poff() {
		return K_poff;
	}
	/**
	 * @return The crosstalk coefficient per PSE in ON state.
	 */
	public double getK_pon() {
		return K_pon;
	}
	/**
	 * @return The photodetector sensitivity.
	 */
	public double getPhotodetectorSensitivity() {
		return sensitivity;
	}
	/**
	 * @return The laser efficiency.
	 */
	public int getLaserEfficiency() {
		return efficiency;
	}
	/**
	 * @return The modulation rate.
	 */
	public int getModulationRate() {
		return modulation;
	}
	/**
	 * @return The number of router in the first dimension.
	 */
	public int getM() {
		return m;
	}
	/**
	 * @return The number of router in the second dimension.
	 */
	public int getN() {
		return n;
	}
	/**
	 * @return The chip size
	 */
	public int getChipSize() {
		return chipSize;
	}
	/**
	 * @return The mapping optimization algorithm name. 
	 */
	public ArrayList<String> getMapping_strategies() {
		return mapping_strategies;
	}
	/**
	 * @return The router name. 
	 */
	public ArrayList<String> getRouter_types() {
		return router_types;
	}
	/**
	 * @return The routing strategy name. 
	 */
	public ArrayList<String> getRouting_types() {
		return routing_types;
	}
	/**
	 * @return The topology name. 
	 */
	public ArrayList<String> getTopology_types() {
		return topology_types;
	}
	/**
	 * @return The number of iterations for the mapping optimization.
	 */
	public int getIterations() {
		return iterations;
	}
	/**
	 * @return The population size in case of genetic algorithm.
	 */
	public int getPopulation() {
		return population;
	}
	/**
	 * @return The offspring size in case of genetic algorithm.
	 */
	public int getOffspring() {
		return offspring;
	}

	/**
	 * Propagation loss (dB/cm)
	 */
	private double L_p;
	/**
	 * Crossing loss (dB)
	 */
	private double L_c;
	/**
	 * Power loss per parallel switching element in
	 * OFF resonance state (dB)
	 */
	private double L_poff;
	/**
	 * Power loss per parallel switching element in
	 * ON resonance state (dB)
	 */
	private double L_pon;
	/**
	 * Power loss per crossing switching element in
	 * OFF resonance state. The contribution is due to
	 * the ring passby and the waveguide crossing. (dB) 
	 */
	private double L_coff;
	/**
	 * Power loss per crossing switching element in
	 * ON resonance state. (dB)
	 */
	private double L_con;
	/**
	 * Crossing’s crosstalk coefficient (dB)
	 */
	private double K_c;
	/**
	 * Crosstalk coefficient per PSE in OFF state (dB)
	 */
	private double K_poff;
	/**
	 * Crosstalk coefficient per PSE in ON state (dB)
	 */
	private double K_pon;
	/**
	 * Photodetector sensitivity (dBm)
	 */
	private double sensitivity;
	/**
	 * Laser efficiency (%)
	 */
	private int efficiency;
	/**
	 * Modulation rate (Gb/s)
	 */
	private int modulation;
	/**
	 * The number of router in the first dimension
	 */
	private int m;
	/**
	 * The number of router in the second dimension
	 */
	private int n;
	/**
	 * The chip size in mm^2
	 */
	private int chipSize;
	/**
	 * The mapping optimization algorithm name. 
	 */
	private ArrayList<String> mapping_strategies;
	/**
	 * The router name. 
	 */
	private ArrayList<String> router_types;
	/**
	 * The routing strategy name. 
	 */
	private ArrayList<String> routing_types;
	/**
	 * The topology name. 
	 */
	private ArrayList<String> topology_types;
	/**
	 * The number of iterations for the mapping 
	 * optimization
	 */
	private int iterations;
	/**
	 * The population size in case of 
	 * genetic algorithm
	 */
	private int population;
	/**
	 * The offspring size in case of 
	 * genetic algorithm
	 */
	private int offspring;
	/**
	 * The configuration file name
	 */
	private String fileName;
}
