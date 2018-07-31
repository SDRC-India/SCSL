package org.sdrc.scsl.service;

import java.util.List;

import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.model.web.ReturnModel;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*
*/
public interface AggregationService {

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timeIds
	 * @param areaId
	 * @return
	 * Gets called while uploading historical data to aggregate
	 */
	ReturnModel aggregateAfterHistoricalDataUpload(List<Integer> timeIds, Integer areaId, List<TimePeriod> timePeriods);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param periodicity
	 * @return
	 * @throws Exception
	 * Returns the last timperiod id for which aggregation needs to be donw
	 */
	TimePeriod createTimePeriodByPeriodicity(String periodicity) throws Exception;

	/**
	 * @param aggregationFrequency
	 * @param tpId
	 * Aggregate monthly/quarterly/ yearly
	 */
	void aggregateSNCUData(String aggregationFrequency, Integer tpId);

	//for interanl reference. uncomment later
//	String createAggregateQuartelyAndYearlyTps();

	//for interanl reference. uncomment later
//	String aggregateMonthly();
	

}
