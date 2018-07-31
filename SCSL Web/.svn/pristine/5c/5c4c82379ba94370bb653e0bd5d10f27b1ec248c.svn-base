/**
 * 
 */
package org.sdrc.scsl.job;

import java.time.LocalDateTime;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sdrc.scsl.service.SubmissionManagementService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * This job will execute on 21st of every month and will aito approve all the superintendent pending submissions
 *
 */
public class SupritendentAutoApproveMonthlyJob extends QuartzJobBean {

	private SubmissionManagementService submissionManagementService;

	public void setSubmissionManagementService(
			SubmissionManagementService submissionManagementService) {
		this.submissionManagementService = submissionManagementService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			LocalDateTime currentDate = LocalDateTime.now();

			if (currentDate.getDayOfMonth() == 21) {
				submissionManagementService.autoApproveForSuperintendent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
