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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Provides a mapping solution object
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class MappingSolution {
	
	/**
	 * Constructs an empty mapping solution object.
	 */
	public MappingSolution(){
		this.mappingMatrix = new int[Configuration.getM()][Configuration.getN()];
	}
	
	/**
	 * Constructs an mapping solution object given a new mapping matrix.
	 * 
	 * @param mapping_matrix The mapping matrix.
	 */
	public MappingSolution(int[][] mapping_matrix){
		this.mappingMatrix = Utils.cloneMatrix(mapping_matrix);
	}
	
	/**
	 * Constructs a random mapping solution if random is <code>true</code> 
	 * otherwise an empty mapping solution object. 
	 * 
	 * @param random <code>true</code> for creating a random mapping solution; 
	 * 			<code>false</code> for an empty mapping solution object. 
	 */
	public MappingSolution(boolean random){
		this();
		if (random){
			int m = Configuration.getM();
			int n = Configuration.getN();
			this.mappingMatrix = new int[m][n];
			for(int i=0; i<m; i++){
				for(int j=0; j<n; j++){
					this.mappingMatrix[i][j] = -1;
				}
			}
			int d = 0;
			int core_start = (int)(Math.random()*Configuration.getNum_cores());
			while (d < Configuration.getNum_cores()){
				int tile_row = (int)(Math.random()*m);
				int tile_column = (int)(Math.random()*n);
				if (this.mappingMatrix[tile_row][tile_column] == -1){
					this.mappingMatrix[tile_row][tile_column] = ((core_start+d)%Configuration.getNum_cores());
					d++;
				}
			}
		}
	}
	
	/**
	 * Returns the tile where a certain core is mapped on
	 * 
	 * @param coreID The core ID
	 * @param arch The NoC architecture
	 * @return The tile ID
	 */
	public Tile getTileFromCore(int coreID, NoCarchitecture arch){
		for(int i=0; i<this.mappingMatrix.length; i++){
			for(int j=0; j<this.mappingMatrix[0].length; j++){
				if(this.mappingMatrix[i][j] == coreID){
					for (Entry<Integer, Tile> tile_i : arch.getTopology().getTiles().entrySet()){
						if(tile_i.getValue().getRow() == i && tile_i.getValue().getColumn() == j){
							return tile_i.getValue();
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Displays in a multi-line area of the GUI the mapping 
	 * solution including information calculated after the mapping evaluation.
	 * 
	 * @param textArea the multi-line area used in the GUI to displays 
	 * the information about the application configuration loading.
	 */
	public void printOnConsole(final JTextArea textArea){
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	textArea.append("***** Mapping  *****\n");
        		textArea.append("***** Mapping Matrix *****\n");
        		for(int i=0; i<mappingMatrix.length; i++){
        			for(int j=0; j<mappingMatrix[0].length; j++){
        				if(mappingMatrix[i][j] == -1){
        					textArea.append("n/a\t");
        				}
        				else{
        					textArea.append(mappingMatrix[i][j]+"\t");
        				}
        			}
        			textArea.append("\n");
        		}
        		textArea.append("Worst case path src core: "+sourceCoreWC+" dst core: "+destinationCoreWC+"\n");
        		textArea.append("Communications adding noise: \n");
        		for(CommunicationTask task : taskListWC){
        			textArea.append(task.getSrcCore()+" -> "+task.getDstCore()+"\n");
        		}
        		textArea.append("\n");
        		textArea.append("signal attenuation: "+new DecimalFormat("##.###").format(signalAttenuationWC)+" dB"+
        				"\nnoise attenuation: "+new DecimalFormat("##.###").format(noiseAttenuationWC)+" dB"+
        				"\nSNR WC "+new DecimalFormat("##.###").format(snrWC)+
        				"\nLaser Power Consumption "+new DecimalFormat("##.###").format(laserPowerConsumption)+" mW \n");
            }
        });
	}
	
	/**
	 * @return a String object containing all the 
	 * main information about the mapping
	 * solution. This information is written
	 * in the output file.
	 */
	public String getReadableRepresentation(){
		String values="***** Mapping Solution *****\n";
		values+="mapping matrix:\n";
		for(int i=0; i<this.mappingMatrix.length; i++){
			for(int j=0; j<this.mappingMatrix[0].length; j++){
				if(this.mappingMatrix[i][j] != -1){
					values+=(this.mappingMatrix[i][j]  + "\t");
				}
				else{
					values+=("n/a\t");
				}
			}
			values+="\n";
		}
		values+="Worst case path src core: "+sourceCoreWC+" dst core: "+destinationCoreWC+"\n";
		values+="Communications adding noise: \n";
		for(CommunicationTask task : taskListWC){
			values+=task.getSrcCore()+" -> "+task.getDstCore()+"\n";
		}
		values+="\n";
		values+="signal attenuation: "+new DecimalFormat("##.###").format(signalAttenuationWC)+" dB"+
				"\nnoise attenuation: "+new DecimalFormat("##.###").format(noiseAttenuationWC)+" dB"+
				"\nSNR WC "+new DecimalFormat("##.###").format(snrWC)+
				"\nLaser Power Consumption "+new DecimalFormat("##.###").format(laserPowerConsumption)+" mW \n";
		return values;
	}
	
	/**
	 * Prints formatted representations of the mapping 
	 * matrix to a text-output stream.
	 */
	public void printMappingMatrix(){
		System.out.println("***** Mapping Matrix *****");
		for(int i=0; i<this.mappingMatrix.length; i++){
			for(int j=0; j<this.mappingMatrix[0].length; j++){
				if(this.mappingMatrix[i][j] == -1){
					System.out.print("n/a\t");
				}
				else{
					System.out.print(this.mappingMatrix[i][j]+"\t");
				}
			}
			System.out.println("");
		}
	}

	/**
	 * @return The core that is source of the worst case path
	 */
	public int getSourceCoreWC() {
		return sourceCoreWC;
	}
	/**
	 * @param sourceCoreWC The core that is source of the worst case path
	 */
	public void setSourceCoreWC(int sourceCoreWC) {
		this.sourceCoreWC = sourceCoreWC;
	}
	/**
	 * @return The core that is destination of the worst case path
	 */
	public int getDestinationCoreWC() {
		return destinationCoreWC;
	}
	/**
	 * @param destinationCoreWC The core that is destination of the worst case path
	 */
	public void setDestinationCoreWC(int destinationCoreWC) {
		this.destinationCoreWC = destinationCoreWC;
	}
	/**
	 * @return The list of all the task that impact on the worst case SNR
	 */
	public ArrayList<CommunicationTask> getTaskListWC() {
		return taskListWC;
	}
	/**
	 * @param taskListWC The list of all the task that impact on the worst case SNR
	 */
	public void setTaskListWC(ArrayList<CommunicationTask> taskListWC) {
		this.taskListWC = taskListWC;
	}
	/**
	 * @return The signal attenuation in the worst case SNR path
	 */
	public double getSignalAttenuationWC() {
		return signalAttenuationWC;
	}
	/**
	 * @param signalAttenuationWC The signal attenuation in the worst case SNR path
	 */
	public void setSignalAttenuationWC(double signalAttenuationWC) {
		this.signalAttenuationWC = signalAttenuationWC;
	}
	/**
	 * @return The noise attenuation in the worst case SNR path
	 */
	public double getNoiseAttenuationWC() {
		return noiseAttenuationWC;
	}
	/**
	 * @param noiseAttenuationWC The noise attenuation in the worst case SNR path
	 */
	public void setNoiseAttenuationWC(double noiseAttenuationWC) {
		this.noiseAttenuationWC = noiseAttenuationWC;
	}
	/**
	 * @return The worst case SNR
	 */
	public double getSnrWC() {
		return snrWC;
	}
	/**
	 * @param snrWC The worst case SNR
	 */
	public void setSnrWC(double snrWC) {
		this.snrWC = snrWC;
	}
	/**
	 * @return The total laser power consumption
	 */
	public double getLaserPowerConsumption() {
		return laserPowerConsumption;
	}
	/**
	 * @param laserPowerConsumption The total laser power consumption
	 */
	public void setLaserPowerConsumption(double laserPowerConsumption) {
		this.laserPowerConsumption = laserPowerConsumption;
	}
	/**
	 * @return The mapping matrix.
	 */
	public int[][] getMapping_matrix() {
		return mappingMatrix;
	}
	/**
	 * Sets the mapping matrix.
	 * 
	 * @param mapping_matrix The mapping matrix.
	 * @param app The application
	 * @param arch The architecture
	 */
	public void setMappingMatrix(int[][] mapping_matrix, Application app, NoCarchitecture arch){
		this.mappingMatrix = Utils.cloneMatrix(mapping_matrix);
	}

	/**
	 * The core that is source of the worst case path
	 */
	private int sourceCoreWC;
	/**
	 * The core that is destination of the worst case path
	 */
	private int destinationCoreWC;
	/**
	 * The list of all the task that impact on the worst case SNR
	 */
	private ArrayList<CommunicationTask> taskListWC = new ArrayList<CommunicationTask>();
	/**
	 * The signal attenuation in the worst case SNR path
	 */
	private double signalAttenuationWC;
	/**
	 * The noise attenuation in the worst case SNR path
	 */
	private double noiseAttenuationWC;
	/**
	 *  The worst case SNR
	 */
	private double snrWC;
	/**
	 * The total laser power consumption
	 */
	private double laserPowerConsumption;
	/**
	 *  The mapping matrix. The matrix size is MxN where m and n 
	 *  are the topology sizes and each entry represents a tile of 
	 *  the NoC. Each entry stores the identifier of the core mapped 
	 *  on that tile.
	 */
	private int[][] mappingMatrix;
}
