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

import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Is the class containing the application features and its traffic behavior. 
 * This information includes:
 * <ul>
 * <li>The number of cores
 * <li>The core list
 * <li>The Communcation Graph (CG)
 * </ul>
 * <p>
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Application {
	
	/**
	 * Constructs an Application object. It search 
	 * the application description file inside the app/ folder
	 * and read from file the application description.
	 * 
	 */
	public Application(){
		InputApplicationReader appReader = new InputApplicationReader(Configuration.getApplication_file_name());
		appReader.acquireData();
		this.num_cores = appReader.getNumCore();
		this.cg = appReader.getCg();
		Configuration.setCG(this.cg);
		Configuration.setNum_cores(this.num_cores);
		this.taskList = new HashMap<Integer, CommunicationTask>();
		for (int src=0; src<this.num_cores; src++){
			for (int dst=0; dst<this.num_cores; dst++){
				if(this.cg[src][dst]!=0){
					int id = Utils.calcID(src, dst, this.num_cores);
					this.taskList.put(id, new CommunicationTask(id, src, dst));
				}
			}
		}
	}
	
	/**
	 * Constructs an Application object. It search 
	 * the application description file inside the app/ folder
	 * and read from file the application description.
	 * 
	 * @param textArea The multi-line area used in the GUI to displays 
	 * the information about the application configuration loading.
	 * @throws Exception If the application file doesn't exist.
	 */
	public Application(final JTextArea textArea) throws Exception{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	textArea.append("Loading application configuration... \n");
            }
        });
		InputApplicationReader appReader = new InputApplicationReader(Configuration.getApplication_file_name(), textArea);
		appReader.acquireData();
		this.num_cores = appReader.getNumCore();
		this.cg = appReader.getCg();
		Configuration.setCG(this.cg);
		Configuration.setNum_cores(this.num_cores);
		this.taskList = new HashMap<Integer, CommunicationTask>();
		for (int src=0; src<this.num_cores; src++){
			for (int dst=0; dst<this.num_cores; dst++){
				if(this.cg[src][dst]!=0){
					int id = Utils.calcID(src, dst, this.num_cores);
					this.taskList.put(id, new CommunicationTask(id, src, dst));
				}
			}
		}
	}
	
	/**
	 * @return a String object containing all the 
	 * main information about the application
	 * characteristics. This information is written
	 * in the output file.
	 */
	public String getReadableRepresentation(){
		String values="***** Application *****\n";
		values+=("numCores: "+this.num_cores+"\n");
		values+=("CTG:"+"\n");
		for(int i=0; i<this.num_cores; i++){
			for(int j=0; j<this.num_cores; j++){
				values+=(this.cg[i][j]+" ");
			}
			values+="\n";
		}
		values+="\n";
		return values;
	}
	
	/**
	 * Prints formatted representations of the object 
	 * to a text-output stream. 
	 */
	public void print(){
		System.out.println("");
		System.out.println("***** Application *****");
		System.out.println("numCores: "+this.num_cores);
		System.out.println("CTG:");
		for(int i=0; i<this.num_cores; i++){
			for(int j=0; j<this.num_cores; j++){
				System.out.print(this.cg[i][j]+" ");
			}
			System.out.println("");
		}
	}
	/**
	 * 
	 * @return the number of cores.
	 */
	public int getNum_cores() {
		return num_cores;
	}
	/**
	 * 
	 * @return the task list.
	 */
	public HashMap<Integer, CommunicationTask> getTaskList(){
		return this.taskList;
	}
	/**
	 * 
	 * @param id The communication task identifier
	 * @return the task given a certain identifier.
	 */
	public CommunicationTask getTask(int id){
		return this.taskList.get(id);
	}
	/**
	 * 
	 * @return the communication graph (CG).
	 */
	public double[][] getCG() {
		return cg;
	}

	/**
	* The number of cores
	*/
	private int num_cores;
	/**
	* The task list
	*/
	private HashMap<Integer, CommunicationTask> taskList; 
	/**
	* The communication graph (CG)
	*/
	private double[][] cg;
}
