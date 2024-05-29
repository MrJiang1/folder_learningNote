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

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides the application file reader. An object of this class is 
 * required to read the application description xml file located 
 * in the app/ folder.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class InputApplicationReader {
	
	
	/**
	 * Constructs an InputApplicationReader object. It checks the file and
	 * instantiates all the required data structures. 
	 * 
	 * @param fileName The application file name.
	 */
	public InputApplicationReader(String fileName) {
		try {
			File sourceFile = new File("app/"+fileName+".xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(sourceFile);
			this.doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Constructs an InputApplicationReader object. It checks the file and
	 * instantiates all the required data structures. 
	 * 
	 * @param fileName The application file name
	 * @param textArea The multi-line area used in the GUI to displays 
	 * the information about the application configuration loading.
	 * @throws Exception If the application file doesn't exist.
	 */
	public InputApplicationReader(final String fileName, final JTextArea textArea) throws Exception {
		boolean print_error = false;
		try {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("File name -> ");
	            }
	        });
			File sourceFile = new File("app/"+fileName+".xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(sourceFile);
			this.doc.getDocumentElement().normalize();
		} catch (Exception e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("ERROR: application file "+ "app/"+fileName+".xml" + " not found\n");
	            }
	        });
			print_error = true;
			e.printStackTrace();
			throw e;
		}
		if(!print_error){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("OK\n");
	            }
	        });
		}
	}
	
	/**
	 * Parses the application file name and store the application
	 * traffic information in appropriate data structures.
	 */
	public void acquireData() {
		try {
			Element application = (Element) this.doc.getElementsByTagName("application").item(0);
			this.numCore = Integer.parseInt(application.getAttribute("numCores").toString());
			this.cg = new double[this.numCore][this.numCore];
			for(int i=0;i<this.numCore;i++){
				for(int j=0;j<this.numCore;j++){
					this.cg[i][j] = 0;
				}
			}
			NodeList nodeListCtg = this.doc.getElementsByTagName("cg").item(0).getChildNodes();
			for (int i = 0; i < nodeListCtg.getLength(); i++) {
				Node nodeEdge = nodeListCtg.item(i);
				if (nodeEdge.getNodeType() == Node.ELEMENT_NODE) {
					Element elementType = (Element) nodeEdge;
					int srcCoreID = Integer.parseInt(elementType.getAttribute("srcCoreID").toString());
					int dstCoreID = Integer.parseInt(elementType.getAttribute("dstCoreID").toString());
					this.cg[srcCoreID][dstCoreID] = Configuration.getModulationRate()*1000;
					if(elementType.hasAttribute("bandwidth")){
						this.cg[srcCoreID][dstCoreID] = Double.parseDouble(elementType.getAttribute("bandwidth").toString());
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Excpetion = " + e);
		}
	}
	
	/**
	 * @return The number of cores
	 */
	public int getNumCore() {
		return numCore;
	}
	/**
	 * @return The communication graph
	 */
	public double[][] getCg() {
		return cg;
	}

	/**
	 * The number of cores
	 */
	private int numCore;
	/**
	 * The communication graph
	 */
	private double [][] cg;
	/**
	 * The DOM document object resulting
	 * from the xml file parsing
	 */
	private Document doc;
}
