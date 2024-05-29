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

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import mapping_strategies.RandomMapping;

/**
 * Is the main class require to perform a mapping 
 * optimization. In this class the application and 
 * architecture objects are instantiated and the
 * optimization phase is performed.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class OptimizationMain extends SwingWorker<Object, Object>  {
	
	/**
	 * Constructs an OptimizationMain object that 
	 * is required to execute the whole mapping 
	 * phase. 
	 * 
	 * @param textArea The multi-line area 
	 * @param progressBarMapping The progress bar
	 * @param tabbedPane the tab handler
	 * @param btnNewButton The Start Mapping Optimization botton
	 * @param rdbtnWriting The Writing output radio botton
	 */
	public OptimizationMain(JTextArea textArea, JProgressBar progressBarMapping, JTabbedPane tabbedPane, JButton btnNewButton, JRadioButton rdbtnWriting ){
		this.textArea = textArea;
		this.progressBarMapping = progressBarMapping;
		this.tabbedPane = tabbedPane;
		this.btnNewButton = btnNewButton;
		this.rdbtnWriting = rdbtnWriting;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Object doInBackground() throws Exception {
		NoCarchitecture arch = new NoCarchitecture(textArea);
		Application app = new Application(textArea);
		
		boolean print_error = false;
		if (!arch.checkNoCsize(app)){
			final int numTiles = arch.getTopology().getNumTiles();
			final int numCores = app.getNum_cores();
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("Error: number of tiles: "+Integer.toString(numTiles) + " < number of cores: "+ Integer.toString(numCores) + "\n");
	            }
	        });
			print_error = true;
			try {
				done();
				throw new Exception("@doInBackground -> number of tiles: "+Integer.toString(numTiles) + " < number of cores: "+ Integer.toString(numCores));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!print_error){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("Loading mapping configuration... \n");
	            	textArea.append("Mapping strategy -> ");
	            }
	        });
		}
		MappingEvaluator eval = new MappingEvaluator(arch, app);
		MappingStrategyInterface mapper = null;
		try {
			mapper = (MappingStrategyInterface) Class.forName("mapping_strategies."+Configuration.getMapping_strategy()).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			if(!print_error){
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	textArea.append("ERROR: class not found\n");
		            }
		        });
			}
			print_error = true;
			e.printStackTrace();
		}
		if(!print_error){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("OK\n");
	            }
	        });
		}
		
		if(!print_error){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("Starting mapping optimization... ");
	            }
	        });
		}
		mapper.initialize(eval, progressBarMapping);
		MappingSolution map = mapper.map(app, arch);
		if(!print_error){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("DONE\n");
	            }
	        });
		}
		
		
		if (!print_error && Configuration.isWriteOutput()){
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("Writing output on file... ");
	            }
	        });
			OutputFileWriter writer = new OutputFileWriter(Configuration.getApplication_file_name()
					+"_"+Configuration.getTopology_type()
					+"_"+Configuration.getM()+"x"+Configuration.getN()
					+"_"+Configuration.getRouting_type()
					+"_"+Configuration.getRouter_type()
					+"_"+Configuration.getMapping_strategy()
					+"_"+Configuration.getMappingObjective()
					+".txt");
			writer.write(Configuration.getReadableRepresentation()+arch.getReadableRepresentation()+app.getReadableRepresentation()+mapper.getReadableRepresentation()+map.getReadableRepresentation());
			if(Configuration.getMapping_strategy().compareTo(Globals.randomMapping) == 0){
				OutputFileWriter writerProbDist = new OutputFileWriter(Configuration.getApplication_file_name()
						+"_"+Configuration.getTopology_type()
						+"_"+Configuration.getM()+"x"+Configuration.getN()
						+"_"+Configuration.getRouting_type()
						+"_"+Configuration.getRouter_type()
						+"_"+Configuration.getMapping_strategy()
						+"_"+Configuration.getMappingObjective()
						+"_probDist.txt");
				writerProbDist.write(Configuration.getReadableRepresentation()+((RandomMapping) mapper).getScoreList());
			}
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	textArea.append("DONE\n");
	            }
	        });
		}
		map.printOnConsole(textArea);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done(){
		btnNewButton.setEnabled(true);
		tabbedPane.setEnabled(true);
		rdbtnWriting.setEnabled(true);
	}
	
	/**
	 * The multi-line area used in the GUI to displays 
	 * the information about the configuration loadings.
	 */
	private JTextArea textArea;
	/**
	 * The bar used in the GUI to displays the progress
	 * of the mapping optimization phase
	 */
	private JProgressBar progressBarMapping;
	/**
	 * The Start Mapping Optimization botton
	 */
	private JButton btnNewButton;
	/**
	 * The GUI component that lets the user switch between 
	 * a group of components by clicking on a tab 
	 */
	private JTabbedPane tabbedPane;
	/**
	 * The Writing output on file radio botton
	 */
	private JRadioButton rdbtnWriting;
}
