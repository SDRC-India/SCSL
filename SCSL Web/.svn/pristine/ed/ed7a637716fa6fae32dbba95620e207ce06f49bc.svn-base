package org.sdrc.scsl.service;

import java.util.List;

import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SubmittedDataModel;
import org.sdrc.scsl.model.web.SubmittedFacilityDetailModel;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
* @author Harsh Pratyush (harsh@sdrc.co.in)
*This service will hold all the  methods for submission management view page
 *
*/

public interface SubmissionManagementService {

	/**
	 * @return submission list for superintendent
	 * 
	 */
	List<SubmittedDataModel> getAllSubmissionsForSuperintendent();

	/**
	 * @return list of submission for M&E
	 */
	List<SubmittedDataModel> getAllSubmissionsForSuperintendentMnE();

	/**
	 * @param txnSubmissionId
	 * @param refSubmissionId
	 * @param facilityId
	 * @param timePeriodId
	 * @return each submission's data
	 */
	List<SubmittedFacilityDetailModel> fetchSubmittedValues(Integer txnSubmissionId, Integer refSubmissionId,
			Integer facilityId, Integer timePeriodId);

	/**
	 * @param txnSubmissionId
	 * @param remarks
	 * @param isApprove
	 * @return approves/reject data
	 */
	ReturnModel approveOrRejectBySuperintendentAndMnE(Integer txnSubmissionId, String remarks, boolean isApprove);

	/**
	 * This method will auto approve the last month submission which hasn't been approved by the Superitendent 
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @return
	 */
	boolean autoApproveForSuperintendent();

	/**
	 * @param toEmailIds
	 * @param ccEmailIds
	 * @param subject
	 * @param message
	 * @param toUserName
	 * @throws Exception
	 * send email
	 */
	void sendMail(List<String> toEmailIds, List<String> ccEmailIds, String subject, String message, String toUserName)
			throws Exception;
	
}
