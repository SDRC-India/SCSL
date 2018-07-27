package org.sdrc.scsl.job;

import java.time.LocalDateTime;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sdrc.scsl.service.EngagementScoreService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * Runs the job every month 21
 * To send mail notification to AHI associate and leads for pending engangement score
 *
 */
public class EngagementScorePendingMailMonthlyJob extends QuartzJobBean {
	
	private EngagementScoreService engagementScoreService;
	
	public void setEngagementScoreService(
			EngagementScoreService engagementScoreService) {
		this.engagementScoreService = engagementScoreService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			LocalDateTime currentDate = LocalDateTime.now();

			if (currentDate.getDayOfMonth() == 21) {
				engagementScoreService.pendingEngagementScoreMailService();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
