package main;

/**
 * Is the class for all the communication tasks. 
 * A communication task involves a source and destination 
 * cores and is identified by an ID.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class CommunicationTask implements Comparable<CommunicationTask>{
	
	/**
	 * Constructs a communication task object. It requires an identifier 
	 * and the source and destination of the communication.
	 * 
	 * @param id The communication task ID
	 * @param srcCore The ID of the source core of the communication
	 * @param dstCore The ID of the destination core of the communication
	 */
	public CommunicationTask(int id, int srcCore, int dstCore){
		this.srcCore = srcCore;
		this.dstCore = dstCore;
		this.id = id;
	}
	/**
	 * Compares this task with another communication task 
	 * in terms of crosstalk impact. 
	 */
	@Override
	public int compareTo(CommunicationTask arg0) {
		return (int) ((arg0.getCrosstalk_impact()-this.crosstalk_impact)*1000);
	}
	/**
	 * @return the communication task ID.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the source core ID.
	 */
	public int getSrcCore() {
		return srcCore;
	}
	/**
	 * @return the destination core ID.
	 */
	public int getDstCore() {
		return dstCore;
	}
	/**
	 * @return the crosstalk impact.
	 */
	public double getCrosstalk_impact() {
		return crosstalk_impact;
	}
	/**
	 * @param crosstalk_impact the crosstalk impact.
	 */
	public void setCrosstalk_impact(double crosstalk_impact) {
		this.crosstalk_impact = crosstalk_impact;
	}
	
	/**
	 * The communication task identifier number.
	 */
	private int id;
	/**
	 * The source core identifier number.
	 */
	private int srcCore;
	/**
	 * The destination core identifier number.
	 */
	private int dstCore;
	/**
	 * The crosstalk impact of this task on another communication
	 * task on a certain mapping.
	 */
	private double crosstalk_impact;

}
