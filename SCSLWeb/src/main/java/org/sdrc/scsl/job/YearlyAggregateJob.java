package org.sdrc.scsl.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sdrc.scsl.service.AggregationService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * This job will get called on 26th of every 12 month at 02:30 AM
 * to aggreagte all the mne approved sncu data for the last year
 *
 */
public class YearlyAggregateJob extends QuartzJobBean{

	private AggregationService aggregationService;
	
	public void setAggregationService(AggregationService aggregationService) {
		this.aggregationService = aggregationService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		aggregationService.aggregateSNCUData("12", null);
		
	}
}
