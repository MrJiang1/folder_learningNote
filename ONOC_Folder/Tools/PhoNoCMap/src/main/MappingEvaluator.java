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

import java.util.ArrayList;
import java.util.Collections;

/**
 * Provides an evaluator used to calculate the 
 * worst case power loss or signal-to-noise ratio given 
 * a certain mapping solution.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class MappingEvaluator {
	
	/**
	 * Constructs a MappingEvaluator object.
	 * 
	 * @param arch The NoC architecture
	 * @param app The application
	 */
	public MappingEvaluator(NoCarchitecture arch, Application app){
		this.arch = arch;
		this.app = app;
	}
	
	/**
	 * Evaluates the worst case power loss or SNR 
	 * according to a given mapping objective. 
	 * 
	 * @param map The mapping solution
	 * @return the worst case power loss or SNR
	 */
	public double evaluate(MappingSolution map){
		if (Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveCrosstalk) == 0){
			this.evaluateSNR(map);
			return map.getSnrWC();
		}
		else if(Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveLoss) == 0){
			this.evaluatePowerLoss(map);
			return map.getSignalAttenuationWC();
		}
		else if(Configuration.getMappingObjective().compareTo(Globals.mappingObjectiveLaserPower) == 0){
			this.evaluateLaserPower(map);
		}
		return Double.NaN;
	}
	
	/**
	 * Evaluates the worst case SNR
	 * 
	 * @param map The mapping solution
	 */
	public void evaluateSNR(MappingSolution map){
		double snrWC = Double.POSITIVE_INFINITY;
		for(int core_i=0; core_i<this.app.getNum_cores(); core_i++){
			for(int core_j=0; core_j<this.app.getNum_cores(); core_j++){
				if (this.app.getCG()[core_i][core_j] != 0){
					ArrayList<CommunicationTask> taskList = new ArrayList<CommunicationTask>();
					Tile src_tile = map.getTileFromCore(core_i, this.arch);
					Tile dst_tile = map.getTileFromCore(core_j, this.arch);
					double signalAttenuation = arch.getPowerLoss(src_tile.getId(), dst_tile.getId());
					for (int core_m=0; core_m<this.app.getNum_cores(); core_m++){
						for (int core_n=0; core_n<this.app.getNum_cores(); core_n++){
							if (core_i != core_m && core_j != core_n && 
								this.app.getCG()[core_m][core_n] != 0){
								Tile src_noise_tile = map.getTileFromCore(core_m, this.arch);
								Tile dst_noise_tile = map.getTileFromCore(core_n, this.arch);
								if (this.arch.getValidCommunications()[src_tile.getId()][dst_tile.getId()][src_noise_tile.getId()][dst_noise_tile.getId()]){
									double crosstalk = this.arch.getCrosstalk(src_tile.getId(), dst_tile.getId(), src_noise_tile.getId(), dst_noise_tile.getId());
									if (crosstalk == -1){
										try {
											throw new Exception("@calcCrosstalk -> Error in the NoC architecture. \nUndefined crosstalk for signal: "+Double.toString(core_i)+" -> "+Double.toString(core_j)+ "noise: "+Double.toString(core_m)+" -> "+Double.toString(core_n) +"\nCheck the setCrosstalkMatrixNetworkLevel method of the NoC architecture class.");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									if(crosstalk != 0){
										int id = Utils.calcID(core_m, core_n, this.app.getNum_cores());
										taskList.add(this.app.getTask(id));
										this.app.getTask(id).setCrosstalk_impact(crosstalk);
									}
								}
							}
						}
					}
					double crosstalk = 0;
					if(taskList.size()>0){
						Collections.sort(taskList);
						crosstalk = taskList.get(0).getCrosstalk_impact();
						for (int task_i=1; task_i<taskList.size(); task_i++){
							Tile src_tile_i = map.getTileFromCore(taskList.get(task_i).getSrcCore(), this.arch);
							Tile dst_tile_i = map.getTileFromCore(taskList.get(task_i).getDstCore(), this.arch);
							boolean remove = false;
							for (int task_j=0; task_j<task_i; task_j++){
								Tile src_tile_j = map.getTileFromCore(taskList.get(task_j).getSrcCore(), this.arch);
								Tile dst_tile_j = map.getTileFromCore(taskList.get(task_j).getDstCore(), this.arch);
								if(!this.arch.getValidCommunications()[src_tile_i.getId()][dst_tile_i.getId()][src_tile_j.getId()][dst_tile_j.getId()]){
									remove = true;
								}
							}
							if(remove){
								taskList.remove(task_i);
								task_i--;
							}
							else{
								crosstalk = Utils.sumDB(crosstalk, taskList.get(task_i).getCrosstalk_impact());
							}
						}
					}
					double snr = Utils.calcSNR(signalAttenuation, crosstalk);
					if (snrWC > snr){
						snrWC = snr;
						map.setSourceCoreWC(core_i);
						map.setDestinationCoreWC(core_j);
						map.setTaskListWC(taskList);
						map.setSignalAttenuationWC(signalAttenuation);
						map.setNoiseAttenuationWC(crosstalk);
						map.setSnrWC(snrWC);
					}
				}
			}
		}
	}

	/**
	 * Evaluates the worst case power loss
	 * 
	 * @param map The mapping solution
	 */
	public void evaluatePowerLoss(MappingSolution map){
		double loss_wc = 0;
		for(int core_i=0; core_i<this.app.getNum_cores(); core_i++){
			for(int core_j=0; core_j<this.app.getNum_cores(); core_j++){
				if (this.app.getCG()[core_i][core_j] != 0){
					Tile src_tile = map.getTileFromCore(core_i, this.arch);
					Tile dst_tile = map.getTileFromCore(core_j, this.arch);
					double loss = arch.getPowerLoss(src_tile.getId(), dst_tile.getId());
					if (loss < loss_wc){
						loss_wc = loss;
						map.setSourceCoreWC(core_i);
						map.setDestinationCoreWC(core_j);
						map.setSignalAttenuationWC(loss);
					}
				}
			}
		}
	}
	
	/**
	 * Evaluates the average laser power consumption
	 * 
	 * @param map The mapping solution
	 */
	public void evaluateLaserPower(MappingSolution map){
		double laser_power = 0;
		for(int core_i=0; core_i<this.app.getNum_cores(); core_i++){
			for(int core_j=0; core_j<this.app.getNum_cores(); core_j++){
				if (this.app.getCG()[core_i][core_j] != 0){
					Tile src_tile = map.getTileFromCore(core_i, this.arch);
					Tile dst_tile = map.getTileFromCore(core_j, this.arch);
					double loss = arch.getPowerLoss(src_tile.getId(), dst_tile.getId());
					double laser_power_tmp = Utils.dbToVal(Configuration.getPhotodetectorSensitivity()-loss);
					laser_power_tmp = laser_power_tmp*100/Configuration.getLaserEfficiency();
					laser_power_tmp = laser_power_tmp * this.app.getCG()[core_i][core_j]*8/1000 / Configuration.getModulationRate();
					laser_power+=laser_power_tmp;
				}
			}
		}
		map.setLaserPowerConsumption(laser_power);
	}
	
	/**
	 * The NoC architecture
	 */
	private NoCarchitecture arch;
	/**
	 * The application
	 */
	private Application app;
	
}
