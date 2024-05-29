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

/**
 * Is the class containing the setup configuration including the application and architecture 
 * features and the loss/crosstalk coefficients. 
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class Configuration {
	
	/**
	 * Constructs a Configuration object containing all the input setup 
	 * 
	 * @param chip_size The chip size
	 * @param L_p The propagation loss
	 * @param L_c The crossing loss
	 * @param L_poff The power loss per parallel switching element in OFF resonance state
	 * @param L_pon The power loss per parallel switching element in ON resonance state
	 * @param L_coff The power loss per crossing switching element in OFF resonance state
	 * @param L_con The power loss per crossing switching element in ON resonance state
	 * @param K_c The crossing’s crosstalk coefficient
	 * @param K_poff The crosstalk coefficient per PSE in OFF state
	 * @param K_pon The crosstalk coefficient per PSE in ON state
	 * @param sensitivity The photodetector sensitivity
	 * @param efficiency The laser efficiency
	 * @param modulation The modulation rate
	 * @param m The number of router in the first dimension
	 * @param n The number of router in the second dimension
	 * @param application_file_name The application file name
	 * @param mapping_strategy The mapping optimization algorithm name. 
	 * @param router_type The router name. 
	 * @param routing_type The routing strategy name. 
	 * @param topology_type The topology name. 
	 * @param iterations The number of iterations for the mapping optimization.
	 * @param populationSize The population size in case of genetic algorithm.
	 * @param offspringSize The offspring size in case of genetic algorithm.
	 * @param mappingObjective The optimization objective.
	 * @param writeOutput A boolean value. If true the results are stored in an output file.
	 * @param stopCondition A boolean value. If true mapping optimization uses the stop condition 1. 
	 * Otherwise, it uses the stop condition 0.
	 */
	public Configuration(double chip_size, double L_p, double L_c, double L_poff, double L_pon, double L_coff, double L_con, double K_c, double K_poff, double K_pon,
			double sensitivity, int efficiency, int modulation, int m, int n, String application_file_name, String mapping_strategy, String router_type, String routing_type, 
			String topology_type, int iterations, int populationSize, int offspringSize, String mappingObjective, boolean writeOutput, boolean stopCondition){
		Configuration.chip_size = chip_size;
		Configuration.L_p = L_p;
		Configuration.L_c = L_c;
		Configuration.L_poff = L_poff;
		Configuration.L_pon = L_pon;
		Configuration.L_coff = L_coff;
		Configuration.L_con = L_con;
		Configuration.K_c = K_c;
		Configuration.K_poff = K_poff;
		Configuration.K_pon = K_pon;
		Configuration.sensitivity = sensitivity;
		Configuration.efficiency = efficiency;
		Configuration.modulation = modulation;
		Configuration.m = m;
		Configuration.n = n;
		Configuration.application_file_name = application_file_name;
		Configuration.num_tiles = m*n;
		Configuration.mapping_strategy = mapping_strategy;
		Configuration.router_type = router_type;
		Configuration.routing_type = routing_type;
		Configuration.topology_type = topology_type;
		Configuration.iterations = iterations;
		Configuration.populationSize = populationSize;
		Configuration.offspringSize = offspringSize;
		Configuration.mappingObjective = mappingObjective;
		Configuration.writeOutput = writeOutput;
		Configuration.stopCondition = stopCondition;
		
	}
	
	/**
	 * @return a String object containing all the 
	 * main information about the Configuration
	 * characteristics. This information is written
	 * in the output file.
	 */
	public static String getReadableRepresentation(){
		String values = "***** Configuration *****\n";
		values += ("optical values: \n");
		values += ("L_p: " + Configuration.L_p+"\n");
		values += ("L_c: " + Configuration.L_c+"\n");
		values += ("L_poff: " + Configuration.L_poff+"\n");
		values += ("L_pon: " + Configuration.L_pon+"\n");
		values += ("L_coff: " + Configuration.L_coff+"\n");
		values += ("L_con: " + Configuration.L_con+"\n");
		values += ("K_c: " + Configuration.K_c+"\n");
		values += ("K_poff: " + Configuration.K_poff+"\n");
		values += ("K_pon: " + Configuration.K_pon+"\n");
		values += ("Photodetector sensitivity: " + Configuration.sensitivity+"\n");
		values += ("Laser efficiency: " + Configuration.efficiency+"\n");
		values += ("Modulation rate: " + Configuration.modulation+"\n");
		values += ("chip size: " + Configuration.chip_size+"\n");
		values += ("topology type: " + Configuration.topology_type+"\n");
		values += ("router type: " + Configuration.router_type+"\n");
		values += ("routing type: " + Configuration.routing_type+"\n");
		values += ("mapping strategy: " + Configuration.mapping_strategy+"\n");
		values += ("stop condition: " + (Configuration.stopCondition ? "1" : "0") +"\n");
		values += ("mapping objective: " + Configuration.mappingObjective+"\n");
		values += ("iterations: " + Configuration.iterations+"\n");
		if(Configuration.mapping_strategy.compareTo(Globals.geneticMapping) == 0){
			values += ("populationSize: " + Configuration.populationSize+"\n");
			values += ("offspringSize: " + Configuration.offspringSize+"\n");
		}
		values += ("application: " + Configuration.application_file_name+"\n");
		return values;
	}
	
	/**
	 * @return The chip size
	 */
	public static double getChip_size() {
		return chip_size;
	}
	/**
	 * @return The propagation loss
	 */
	public static double getL_p() {
		return L_p;
	}
	/**
	 * @return The crossing loss
	 */
	public static double getL_c() {
		return L_c;
	}
	/**
	 * @return The power loss per parallel switching element in OFF resonance state
	 */
	public static double getL_poff() {
		return L_poff;
	}
	/**
	 * @return The power loss per parallel switching element in ON resonance state
	 */
	public static double getL_pon() {
		return L_pon;
	}
	/**
	 * @return The power loss per crossing switching element in OFF resonance state.
	 */
	public static double getL_coff() {
		return L_coff;
	}
	/**
	 * @return The power loss per crossing switching element in ON resonance state.
	 */
	public static double getL_con() {
		return L_con;
	}
	/**
	 * @return The crossing’s crosstalk coefficient.
	 */
	public static double getK_c() {
		return K_c;
	}
	/**
	 * @return The crosstalk coefficient per PSE in OFF state.
	 */
	public static double getK_poff() {
		return K_poff;
	}
	/**
	 * @return The crosstalk coefficient per PSE in ON state.
	 */
	public static double getK_pon() {
		return K_pon;
	}
	/**
	 * @return The photodetector sensitivity.
	 */
	public static double getPhotodetectorSensitivity() {
		return sensitivity;
	}
	/**
	 * @return The laser efficiency.
	 */
	public static double getLaserEfficiency() {
		return efficiency;
	}
	/**
	 * @return The modulation rate.
	 */
	public static double getModulationRate() {
		return modulation;
	}
	/**
	 * @return The number of router in the first dimension.
	 */
	public static int getM() {
		return m;
	}
	/**
	 * @return The number of router in the second dimension.
	 */
	public static int getN() {
		return n;
	}
	/**
	 * @return The communication graph (CG)
	 */
	public static double[][] getCG() {
		return cg;
	}
	/**
	 * @return The number of tiles
	 */
	public static int getNum_tiles() {
		return num_tiles;
	}
	/**
	 * @return The number of cores
	 */
	public static int getNum_cores() {
		return num_cores;
	}
	/**
	 * @return The application file name
	 */
	public static String getApplication_file_name() {
		return application_file_name;
	}
	/**
	 * @return The mapping optimization algorithm name. 
	 */
	public static String getMapping_strategy() {
		return mapping_strategy;
	}
	/**
	 * @return The router name. 
	 */
	public static String getRouter_type() {
		return router_type;
	}
	/**
	 * @return The routing strategy name. 
	 */
	public static String getRouting_type() {
		return routing_type;
	}
	/**
	 * @return The topology name. 
	 */
	public static String getTopology_type() {
		return topology_type;
	}
	/**
	 * @return The number of iterations for the mapping 
	 * optimization
	 */
	public static int getIterations() {
		return iterations;
	}
	/**
	 * @return The population size in case of genetic algorithm.
	 */
	public static int getPopulationSize() {
		return populationSize;
	}
	/**
	 * @return The offspring size in case of genetic algorithm.
	 */
	public static int getOffspringSize() {
		return offspringSize;
	}
	/**
	 * @return The optimization objective.
	 */
	public static String getMappingObjective() {
		return mappingObjective;
	}
	/**
	 * @return <code>true</code> if the output should be written on a file; 
	 * 			<code>false</code> otherwise
	 */
	public static boolean isWriteOutput() {
		return writeOutput;
	}
	/**
	 * @param cg The communication graph (CG)
	 */
	public static void setCG(double[][] cg) {
		Configuration.cg = cg;
	}
	/**
	 * @param num_cores The number of cores
	 */
	public static void setNum_cores(int num_cores) {
		Configuration.num_cores = num_cores;
	}
	public static boolean getStopCondition() {
		return stopCondition;
	}
	
	/**
	 * The chip size in mm^2
	 */
	private static double chip_size;
	/**
	 * Propagation loss (dB/cm)
	 */
	private static double L_p;
	/**
	 * Crossing loss (dB)
	 */
	private static double L_c;
	/**
	 * Power loss per parallel switching element in
	 * OFF resonance state (dB)
	 */
	private static double L_poff;
	/**
	 * Power loss per parallel switching element in
	 * ON resonance state (dB)
	 */
	private static double L_pon;
	/**
	 * Power loss per crossing switching element in
	 * OFF resonance state. The contribution is due to
	 * the ring passby and the waveguide crossing. (dB)
	 */
	private static double L_coff; 
	/**
	 * Power loss per crossing switching element in
	 * ON resonance state. (dB)
	 */
	private static double L_con;
	/**
	 * Crossing’s crosstalk coefficient (dB)
	 */
	private static double K_c;
	/**
	 * Crosstalk coefficient per PSE in OFF state (dB)
	 */
	private static double K_poff;
	/**
	 * Crosstalk coefficient per PSE in ON state (dB)
	 */
	private static double K_pon;
	/**
	 * Photodetector sensitivity (dBm)
	 */
	private static double sensitivity;
	/**
	 * Laser efficiency (%)
	 */
	private static int efficiency;
	/**
	 * Modulation rate (Gb/s)
	 */
	private static int modulation;
	/**
	 * The number of router in the first dimension
	 */
	private static int m;
	/**
	 * The number of router in the second dimension
	 */
	private static int n;
	/**
	 * The communication graph (CG)
	 */
	private static double[][] cg;	
	/**
	 * The number of tiles
	 */
	private static int num_tiles;
	/**
	 * The number of cores
	 */
	private static int num_cores;
	/**
	 * The application file name
	 */
	private static String application_file_name;
	/**
	 * The mapping optimization algorithm name. 
	 */
	private static String mapping_strategy;
	/**
	 * The router name. 
	 */
	private static String router_type;
	/**
	 * The routing strategy name. 
	 */
	private static String routing_type;
	/**
	 * The topology name. 
	 */
	private static String topology_type;
	/**
	 * The number of iterations for the mapping 
	 * optimization
	 */
	private static int iterations;
	/**
	 * The population size in case of 
	 * genetic algorithm
	 */
	private static int populationSize;
	/**
	 * The offspring size in case of 
	 * genetic algorithm
	 */
	private static int offspringSize;
	/**
	 * The optimization objective: "Crosstalk" or "Power loss"
	 */
	private static String mappingObjective;
	/**
	 * A boolean value. If true the results are stored 
	 * in an output file.
	 */
	private static boolean writeOutput;
	/**
	 * A boolean value. If true the stop condition is condition 1. 
	 * Otherwise, the stop condition is condition 2. 
	 */
	private static boolean stopCondition;
}
