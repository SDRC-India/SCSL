package org.sdrc.scsl.service;

import org.sdrc.scsl.model.web.PlanningModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.TxnPlanningModel;

/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *This class is responsible for planning module functionalities
 */
public interface PlanningService {

	
	/**
	 * This method will return the data on load of planning page 
	 * @return
	 */
	public PlanningModel getPlanningData();
	
	
	/**
	 * This method will be responsible for releasing a plan for a facility 
	 * @return
	 */
	public ReturnModel releasePlanning(int planningId)throws Exception ;
	
	/**
	 * This method will be used for the planning
	 * @param txnPlanningModel
	 * @return
	 */
	public ReturnModel planFacility(TxnPlanningModel txnPlanningModel);
	
	/**
	 * This method will be used for the visit of plan for the facility
	 * @param txnPlanningModel
	 * @return
	 */
	public ReturnModel uploadPlanningReport (TxnPlanningModel txnPlanningModel);

	
}
