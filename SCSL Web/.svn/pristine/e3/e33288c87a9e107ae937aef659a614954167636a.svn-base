package org.sdrc.scsl.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.model.web.PDSADataModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.TXNPDSAModel;
import org.sdrc.scsl.model.web.ValueModel;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mandakini Biswal
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public interface PDSAService {

	/**
	 * @param pdsaModel
	 * @return
	 * this method gets called when a new pdsa is created
	 */
	boolean savePDSADetails(PDSAModel pdsaModel);

	/**
	 * This method will be responsible for updating the TXNPDSA table For every
	 * PDSA for given facility a new record will be created in TXNPDSA for every
	 * due date whenever the user will login
	 * 
	 * @param facilityId
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	boolean updateTXNPdsa(int facilityId);

	/**
	 * This method will return the PDSA Detail and the TXNPDSA for a selected
	 * PDSA
	 * 
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param pdsaId
	 * @return
	 */
	PDSADataModel getTXNPdsaData(int pdsaId);

	/**
	 * 
	 * @param txnPDSAModel
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 * save transaction pdsa data
	 */
	boolean saveTXNPDSA(List<TXNPDSAModel> txnPDSAModel);

	/**
	 * This method will return the all the status for closing the PDSA
	 * 
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<ValueModel> getClosingStatus();

	/**
	 * this method will be responsible for closing the PDSA
	 * 
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param pdsaNumber
	 * @param description
	 * @param documents
	 * @return
	 */
	boolean closingPDSA(int pdsaId, String description,
			MultipartFile[] documents, int statusId);

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in) This method will return a
	 *         unique pdsa number for each request of adding pdsa and server
	 *         Time
	 * @return
	 */
	Map<String, String> generatePDSANumberAndDate();

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in) This method will return the
	 *         FacilityList with number of PDSA of for each status categorized by
	 *         the Facility
	 * @return
	 */
	Map<String, Object> getAllFacilityPDSADetails();

	/**This method will return the filter option for the pdsa summary 
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	Map<String, Object>  getAllFilterOptionForPDSASummaryReport();

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param facilityId
	 * @param changeIdea
	 * @return
	 * this method will save a new change idea
	 */
	public ValueModel saveChangeIdea(int indicatorId,String changeIdea);

}
