package org.sdrc.scsl.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sdrc.scsl.service.SNCUService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Sarita
 *This job will first carry frward all the mappings of last tp and then it will create the latest timeperiod
 */
public class MonthlyTimeperiodJob extends QuartzJobBean {

	private SNCUService sncuService;

	public void setSncuService(SNCUService sncuService) {
		this.sncuService = sncuService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			sncuService.updateFacilityIndicatorMapping();
			sncuService.createTimeperiod();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
