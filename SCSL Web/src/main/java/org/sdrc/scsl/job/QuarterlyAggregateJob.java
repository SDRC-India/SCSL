package org.sdrc.scsl.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sdrc.scsl.service.AggregationService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * This job will get called on 26th of every 3 month at 01:30 AM
 * to aggregate all the mne approved sncu data for the last quarter
 *
 */
public class QuarterlyAggregateJob extends QuartzJobBean{

	private AggregationService aggregationService;
	
	public void setAggregationService(AggregationService aggregationService) {
		this.aggregationService = aggregationService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		aggregationService.aggregateSNCUData("3", null);
		
	}
}
