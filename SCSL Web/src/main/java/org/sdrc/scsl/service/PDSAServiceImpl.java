package org.sdrc.scsl.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.ArchivePDSA;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.ChangeIdea;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.PDSA;
import org.sdrc.scsl.domain.SYSConfiguration;
import org.sdrc.scsl.domain.TXNPDSA;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.model.mobile.AreaModel;
import org.sdrc.scsl.model.web.LineChartDataModel;
import org.sdrc.scsl.model.web.PDSADataModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.TXNPDSAModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueModel;
import org.sdrc.scsl.repository.ArchivePDSARepository;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.ChangeIdeaRepository;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.sdrc.scsl.repository.PDSARepository;
import org.sdrc.scsl.repository.SYSConfigurationRepository;
import org.sdrc.scsl.repository.TXNPDSARepository;
import org.sdrc.scsl.repository.TypeDetailRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mandakini Biswal
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * This class will hold all the txn related method of pdsa data entry
 */

@Service
public class PDSAServiceImpl implements PDSAService {

	@Autowired
	private PDSARepository pdsaRepository;

	@Autowired
	private IndicatorRepository indicatorRepository;

	@Autowired
	private TXNPDSARepository txnPDSARepository;

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private ArchivePDSARepository archivePDSARepository;

	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;

	@Autowired
	private TypeDetailRepository typeDeatilRepository;

	@Autowired
	private SYSConfigurationRepository sysConfigurationRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ChangeIdeaRepository changeIdeaRepository;

	@Autowired
	private StateManager stateManager;
	
	private static final Logger LOGGER=Logger.getLogger(PDSAServiceImpl.class);
	private DateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER));
	private SimpleDateFormat dateFormat = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER));
	private SimpleDateFormat format = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_FORMATTER_NO_HYPHEN));
	
	// collect the PDSAModel Data from Controller into savePDSADetails()as
	// parameter
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#savePDSADetails(org.sdrc.scsl.model.web.PDSAModel)
	 * this method gets called when a new pdsa is created
	 */
	@Override
	@Transactional
	public boolean savePDSADetails(PDSAModel pdsaModel) {
		UserModel userModel = (UserModel) stateManager
				.getValue(Constants.Web.USER_PRINCIPAL);
		// create domain class(PDSA) object
		try {
			PDSA pdsa = new PDSA();
			Date parsedDate;
			Date date;
			date = Calendar.getInstance().getTime();

			// Get the values from PDSAModel Class and set into PDSA domain
			// class.
			pdsa.setChangeIdea(new ChangeIdea(pdsaModel.getChangeIdeaId()));
			pdsa.setIndicator(new Indicator(pdsaModel.getIndicatorId()));
			pdsa.setPdsaId(pdsaModel.getPdsaId());
			pdsa.setPdsaNumber(pdsaModel.getPdsaNumber());
			pdsa.setName(pdsaModel.getPdsaName());
			pdsa.setCreatedDate(new Timestamp(date.getTime()));
			pdsa.setCreatedBy(userModel.getUsername());
			parsedDate = dateFormat.parse(pdsaModel.getStartDate());
			pdsa.setStartDate(new Timestamp(parsedDate.getTime()));
			parsedDate = dateFormat.parse(pdsaModel.getEndDate());
			pdsa.setEndDate(new Timestamp(parsedDate.getTime()));
			pdsa.setFrequency(pdsaModel.getFrequency());
			pdsa.setSummary(pdsaModel.getSummary());
			pdsa.setStatus(new TypeDetail(Integer.parseInt(messages.getMessage(
					Constants.Web.ONGOING_STATUS, null, null))));
			pdsa.setFacility(new Area(userModel.getFacilityId()));
			pdsa.setPdsaFrequency(pdsaModel.getPdsaFrequency());
			pdsaRepository.save(pdsa);
			// if current day is same as the date of planning then one blank txnpdsa record will be created
			if (DateUtils.isSameDay(pdsa.getCreatedDate(), pdsa.getStartDate())) {
				updateTXNPdsa(userModel.getFacilityId());
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("exception occured while saving pdsa ",e);
			return false;
		}

	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 *  This method will be responsible for updating the TXNPDSA table For every
	 * PDSA for given facility a new record will be created in TXNPDSA for every
	 * due date a blank record will be created in a daily job
	 */
	@Override
	@Transactional
	public boolean updateTXNPdsa(int facilityId) {
		
		try{
			// briging al the pdsa status code for not close i.e ongoing and pending
			List<String> statusList = Arrays.asList(messages.getMessage(
					Constants.Web.STATUS_CODE_PDSA_NOT_CLOSED, null, null).split(
					","));
			List<Integer> statusListsId = new ArrayList<>();
			
			for (String status : statusList) {
				statusListsId.add(Integer.parseInt(status));
			}

			// briging all the PDSA that are not closed
			List<PDSA> pdsaListNotClose = pdsaRepository
					.findByFacilityAreaIdAndStatusTypeDetailIdIsIn(facilityId,
							statusListsId);

			// checking if any pdsa is open or not
			if (!pdsaListNotClose.isEmpty()) {
				List<TXNPDSA> txnPDSAList = new ArrayList<>();

				for (PDSA pdsa : pdsaListNotClose) {
					Timestamp previuosDueDate = null;

					// if their is no txnpdsa for the pdsa then we will start creating txnpdsa from start date of pdsa
					if (pdsa.getTxnpdsas() == null || pdsa.getTxnpdsas().isEmpty()) {
						previuosDueDate = pdsa.getStartDate();
						// else we will start creating blank txnpdsa from last due date available in database
					} else if (pdsa.getTxnpdsas() != null
							&& !pdsa.getTxnpdsas().isEmpty()) {

						Collections.sort(pdsa.getTxnpdsas(), new Comparator<TXNPDSA>() {

							@Override
							public int compare(TXNPDSA arg0, TXNPDSA arg1) {
								return arg0.getTxnPDSAId().compareTo(arg1.getTxnPDSAId());
							}
						});
						previuosDueDate = pdsa.getTxnpdsas()
								.get(pdsa.getTxnpdsas().size() - 1).getDueDate();
					}

					Date date;
					Calendar calendar = Calendar.getInstance();
					date = Calendar.getInstance().getTime();

					Date nextSubmissionDate = previuosDueDate;

					// checking if the next submission date is within the last
					// date of entry and current date
					while ((pdsa.getEndDate().after(nextSubmissionDate)
							|| DateUtils.isSameDay(pdsa.getEndDate(), nextSubmissionDate))
							&& (date.after(nextSubmissionDate) || DateUtils.isSameDay(date, nextSubmissionDate)))

					{
						// if submission date is afetr previous date or their is
						// no txnpdsa for pdsa
						if (nextSubmissionDate.after(previuosDueDate) || pdsa.getTxnpdsas() == null
								|| pdsa.getTxnpdsas().isEmpty()) {

							TXNPDSA txnpdsa = new TXNPDSA();

							txnpdsa.setDueDate(new Timestamp(nextSubmissionDate.getTime()));
							txnpdsa.setPdsa(pdsa);

							txnPDSAList.add(txnpdsa);
						}

						// daily
						calendar.setTime(nextSubmissionDate);
						calendar.add(Calendar.DATE, pdsa.getFrequency());
						nextSubmissionDate = calendar.getTime();
					}

				}
				txnPDSARepository.save(txnPDSAList);

				return true;
			}
			return false;
		}catch (Exception e) {
			LOGGER.error("exception while saving pdsa txn data",e);
			return false;
		}
		
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#getTXNPdsaData(int)
	 *  This method will return the PDSA Detail and the TXNPDSA for a selected
	 * PDSA
	 */
	@Override
	@Transactional
	public PDSADataModel getTXNPdsaData(int pdsaId) {

		Date date = Calendar.getInstance().getTime();
		
		// getting pdsa for a given pdsaId
		PDSA pdsa = pdsaRepository.findByPdsaId(pdsaId);
		PDSAModel pdsaModel = new PDSAModel();
		PDSADataModel pdsaDataModel = new PDSADataModel();
		List<TXNPDSAModel> txnpdsaModels = new ArrayList<>();
		List<LineChartDataModel> lineChartDataModels = new ArrayList<>();
		if (pdsa != null) {
			pdsaModel.setCoreAreaName(pdsa.getIndicator().getCoreArea()
					.getTypeDetail());
			pdsaModel.setIndicatorType(pdsa.getIndicator().getIndicatorType()
					.getTypeDetail());
			pdsaModel.setIndicatorName(pdsa.getIndicator().getIndicatorName());
			pdsaModel.setChangeIdeaName(pdsa.getChangeIdea().getDescription());
			pdsaModel.setStartDate((pdsa.getStartDate().toString()));
			pdsaModel.setEndDate((pdsa.getEndDate().toString()));
			pdsaModel.setPdsaId(pdsa.getPdsaId());
			pdsaModel.setNumeratorName(pdsa.getIndicator().getNumerator());
			pdsaModel.setDenominatorName(pdsa.getIndicator().getDenominator());
			pdsaModel.setPercentageName(pdsa.getIndicator().getIndicatorName());
			if (pdsa.getFirstDocFilepath() != null) {
				pdsaModel.setFirstDocFilepath(pdsa.getFirstDocFilepath());
			}
			List<TXNPDSA> txnpdsas = txnPDSARepository
					.findByPdsaPdsaIdOrderByDueDateAsc(pdsaId);
			for (TXNPDSA txnpdsa : txnpdsas) {
				TXNPDSAModel txnpdsaModel = new TXNPDSAModel();

				txnpdsaModel.setDenominatorValue(txnpdsa.getDenominatorValue());
				txnpdsaModel.setNumeratorValue(txnpdsa.getNumeratorValue());
				txnpdsaModel.setDueDate((txnpdsa.getDueDate().toString()));
				txnpdsaModel.setPercentage(txnpdsa.getPercentage());
				txnpdsaModel.setTxnPDSAId(txnpdsa.getTxnPDSAId());
				if (txnpdsa.getCreatedDate() != null)
					txnpdsaModel.setCreatedBy(txnpdsa.getCreatedDate()
							.toString());
				// if data is not filled for a txnpdsa  and date has been crossed the css class will be red for pending
				if (!DateUtils.isSameDay(date, txnpdsa.getDueDate())
						&& txnpdsa.getPercentage() == null) {
					txnpdsaModel.setCssClass("red");
				} 
				// if data is filled for a txnpdsa or not filled and date of entry is same as todays date
				else {
					txnpdsaModel.setCssClass("yellow");
				}
				txnpdsaModel.setEnable(true);
				if (txnpdsa.getPercentage() != null)
					txnpdsaModel.setEnable(false);
				LineChartDataModel lineChartDataModel = new LineChartDataModel();
				lineChartDataModel.setAxis(format.format(txnpdsa.getDueDate()));
				lineChartDataModel.setValue(String.valueOf(txnpdsaModel.getPercentage()));
				lineChartDataModels.add(lineChartDataModel);

				txnpdsaModel.setPdsaId(pdsaId);
				txnpdsaModels.add(txnpdsaModel);
			}

			pdsaDataModel.setPdsaModel(pdsaModel);
			pdsaDataModel.setTxnPDSAModel(txnpdsaModels);
			
			// getting last 12 entry of txnpdsa
			if (lineChartDataModels.size() > 12) {
				pdsaDataModel.setChartDataModel(lineChartDataModels.subList(
						Math.max(lineChartDataModels.size() - 12, 0),
						lineChartDataModels.size()));
			} else {
				pdsaDataModel.setChartDataModel(lineChartDataModels);
			}
		}
		return pdsaDataModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#saveTXNPDSA(java.util.List)
	 * save transaction pdsa data
	 */
	@Override
	@Transactional
	public boolean saveTXNPDSA(List<TXNPDSAModel> txnPDSAModels) {
		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		PDSA pdsa = pdsaRepository.findByPdsaId(txnPDSAModels.get(0)
				.getPdsaId());
		List<TXNPDSA> txnpdsas = new ArrayList<>();
		TXNPDSA txnpdsa ;
		if (pdsa != null) {
			Date date;
			date = Calendar.getInstance().getTime();

			// if this is notfirst entry of pdsa then first document will be not be null
			if (pdsa.getFirstDocFilepath() != null) {
				pdsa.setUpdatedDate(new Timestamp(date.getTime()));
				/* pdsa.setUpdatedBy(); To be filled from stateManager */
				for (TXNPDSAModel txnpdsaModel : txnPDSAModels) {
					
					// if txnpdsaModel's percentage is not null that is user has given some denominator and numerator then only we will insert it in database
					if(txnpdsaModel.getPercentage()!=null)
					{
					txnpdsa = new TXNPDSA();

					txnpdsa.setCreatedBy(userModel.getName());
					if (txnpdsaModel.getCreatedDate() != null) {
						txnpdsa.setCreatedDate(Timestamp.valueOf(txnpdsaModel
								.getCreatedDate()));
					} else {
						txnpdsa.setCreatedDate(new Timestamp(date.getTime()));
					}
					txnpdsa.setDenominatorValue(txnpdsaModel
							.getDenominatorValue());
					txnpdsa.setNumeratorValue(txnpdsaModel.getNumeratorValue());
					txnpdsa.setPercentage(txnpdsaModel.getPercentage());
					txnpdsa.setPdsa(pdsa);
					txnpdsa.setTxnPDSAId(txnpdsaModel.getTxnPDSAId());
					txnpdsa.setDueDate(Timestamp.valueOf(txnpdsaModel
							.getDueDate()));

					txnpdsas.add(txnpdsa);
					}
				}

			}
// if this is first time entry of the pdsa then document will be null
// then we will put the data of that pdsa from pdsa table to archive pdsa
// and update the pdsa table with new values
			else {
				PDSA newPDSA;
				newPDSA = pdsa;
				newPDSA.setCreatedDate(new Timestamp(date.getTime()));
				newPDSA.setCreatedBy(userModel.getUsername());
				ArchivePDSA archivePdsa = new ArchivePDSA();

				archivePdsa.setArcChangeIdea(pdsa.getChangeIdea());
				archivePdsa.setArcPdsaNumber(pdsa.getPdsaNumber());
				archivePdsa.setArcClossingRemarks(pdsa.getClossingRemarks());
				archivePdsa.setArcCreatedBy(pdsa.getCreatedBy());
				archivePdsa.setArcCreatedDate(pdsa.getCreatedDate());
				archivePdsa.setArcEndDate(pdsa.getEndDate());
				archivePdsa.setArcFacility(pdsa.getFacility());
				if (pdsa.getFirstDocFilepath() != null) {
					archivePdsa.setArcFirstDocFilepath(pdsa
							.getFirstDocFilepath());
				}
				archivePdsa.setArcFrequency(pdsa.getFrequency());
				archivePdsa.setArcIndicator(pdsa.getIndicator());
				if (pdsa.getLastDocFilepath() != null)
					archivePdsa
							.setArcLastDocFilepath(pdsa.getLastDocFilepath());

				archivePdsa.setArcName(pdsa.getName());
				if (pdsa.getOtherDocFilepath() != null)
					archivePdsa.setArcOtherDocFilepath(pdsa
							.getOtherDocFilepath());

				archivePdsa.setArcStartDate(pdsa.getStartDate());

				archivePdsa.setArcStatus(pdsa.getStatus());

				archivePdsa.setArcSummary(pdsa.getSummary());

				archivePdsa.setArcUpdatedDate(new Timestamp(date.getTime()));

				/* archivePdsa.setArcUpdatedBy(); To be filled from stateManager */

				// saving first doc

				String path = applicationMessageSource.getMessage(
						Constants.Web.FILE_PATH_PDSA, null, null);
				File filePath = new File(path);
				try {
					if (!filePath.exists())
						filePath.mkdir();
					// creating directory within scsl folder with name pdsa
					String directoryPath=path + applicationMessageSource.getMessage(
							Constants.Web.PDSA_FOLDER, null, null);
					File directory = new File(path + applicationMessageSource.getMessage(
						Constants.Web.PDSA_FOLDER, null, null));
					if (!directory.exists())
						directory.mkdirs();
					String folderPath=directoryPath+pdsa.getPdsaNumber();
					File folder=new File(folderPath);
					if (!folder.exists())
						folder.mkdirs();
					// creating directory with the name of pdsa number under folder pdsa
					File file = new File(folder.getAbsolutePath()
							+ File.separator
							+"first_" +txnPDSAModels.get(0).getDocument()
									.getOriginalFilename());
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(file));

					buffStream.write(txnPDSAModels.get(0).getDocument()
							.getBytes());
					buffStream.close();
					newPDSA.setFirstDocFilepath(folder.getAbsolutePath()
							+ File.separator
							+ "first_" +txnPDSAModels.get(0).getDocument()
									.getOriginalFilename());
				} catch (Exception e) {
					LOGGER.error("exception while keeping doc in pdsa",e);
					return false;
				}
				/*----------------------------------------------------------------------------------------*/

				archivePDSARepository.save(archivePdsa);
				pdsaRepository.save(newPDSA);
				
				// Setting the txnpdsa within the txnpdsa domain list to save
				for (TXNPDSAModel txnpdsaModel : txnPDSAModels) {
					// if percentage is not null i.e. user has given some denominator and numerator both
					if(txnpdsaModel.getPercentage()!=null)
					{
					txnpdsa = new TXNPDSA();

					txnpdsa.setCreatedBy(txnpdsaModel.getCreatedBy());
					if(txnpdsaModel.getCreatedDate()!=null)
					{
					txnpdsa.setCreatedDate(Timestamp.valueOf(txnpdsaModel
							.getCreatedDate()));
					}
					else
					{
						txnpdsa.setCreatedDate(new Timestamp(date.getTime()));
					}
					txnpdsa.setDenominatorValue(txnpdsaModel
							.getDenominatorValue());
					txnpdsa.setPdsa(pdsa);
					txnpdsa.setNumeratorValue(txnpdsaModel.getNumeratorValue());
					txnpdsa.setPercentage(txnpdsaModel.getPercentage());
					txnpdsa.setTxnPDSAId(txnpdsaModel.getTxnPDSAId());
					txnpdsa.setDueDate(Timestamp.valueOf(txnpdsaModel
							.getDueDate()));

					txnpdsas.add(txnpdsa);
					}
				}

			}

			txnPDSARepository.save(txnpdsas);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#getClosingStatus()
	 * This method will return the all the status for closing the PDSA
	 */
	@Override
	public List<ValueModel> getClosingStatus() {
		// getting ids of closing pdsa froom property file
		List<String> statusCodes = Arrays.asList(messages.getMessage(
				Constants.Web.STATUS_CODE_PDSA_CLOSED, null, null).split(","));
		List<Integer> statusCodeIds = new ArrayList<>();
		// 
		for (String satusCode : statusCodes) {
			statusCodeIds.add(Integer.parseInt(satusCode));
		}
		// getting all status for closing pdsa
		List<TypeDetail> typeDetails = typeDeatilRepository
				.findByTypeDetailIdIsIn(statusCodeIds);
		List<ValueModel> valueModels = new ArrayList<>();

		for (TypeDetail typeDetail : typeDetails) {
			ValueModel valueModel = new ValueModel();

			valueModel.setKey(typeDetail.getTypeDetailId());
			valueModel.setValue(typeDetail.getTypeDetail());

			valueModels.add(valueModel);
		}
		return valueModels;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#closingPDSA(int, java.lang.String, org.springframework.web.multipart.MultipartFile[], int)
	 * his method will be responsible for closing the PDSA
	 */
	@Override
	@Transactional
	public boolean closingPDSA(int pdsaId, String description,
			MultipartFile[] documents, int statusId) {
		if (documents.length > 0) {
			PDSA pdsa = pdsaRepository.findByPdsaId(pdsaId);
			if (pdsa != null) {
				// putting the data of pdsa in archive pdsa
				ArchivePDSA archivePdsa = new ArchivePDSA();

				Date date = Calendar.getInstance().getTime();

				archivePdsa.setArcChangeIdea(pdsa.getChangeIdea());
				archivePdsa.setArcClossingRemarks(pdsa.getClossingRemarks());
				archivePdsa.setArcCreatedBy(pdsa.getCreatedBy());
				archivePdsa.setArcCreatedDate(pdsa.getCreatedDate());
				archivePdsa.setArcEndDate(pdsa.getEndDate());
				archivePdsa.setArcFacility(pdsa.getFacility());
				archivePdsa.setArcPdsaNumber(pdsa.getPdsaNumber());
				if (pdsa.getFirstDocFilepath() != null) {
					archivePdsa.setArcFirstDocFilepath(pdsa
							.getFirstDocFilepath());
				}
				archivePdsa.setArcFrequency(pdsa.getFrequency());
				archivePdsa.setArcIndicator(pdsa.getIndicator());
				if (pdsa.getLastDocFilepath() != null)
					archivePdsa
							.setArcLastDocFilepath(pdsa.getLastDocFilepath());

				archivePdsa.setArcName(pdsa.getName());
				if (pdsa.getOtherDocFilepath() != null)
					archivePdsa.setArcOtherDocFilepath(pdsa
							.getOtherDocFilepath());

				archivePdsa.setArcStartDate(pdsa.getStartDate());

				archivePdsa.setArcStatus(pdsa.getStatus());

				archivePdsa.setArcSummary(pdsa.getSummary());

				archivePdsa.setArcUpdatedDate(new Timestamp(date.getTime()));

				archivePDSARepository.save(archivePdsa);

				// updating pdsa table
				pdsa.setCreatedDate(new Timestamp(date.getTime()));
				pdsa.setStatus(new TypeDetail(statusId));
				/* pdsa.setCreatedBy(""); to be filled from stateManager */

				String path = applicationMessageSource.getMessage(
						Constants.Web.FILE_PATH_PDSA, null, null);
				File filePath = new File(path);
				try {
					if (!filePath.exists())
						filePath.mkdir();
					String directoryPath=path + applicationMessageSource.getMessage(
							Constants.Web.PDSA_FOLDER, null, null);
					File directory = new File(path + applicationMessageSource.getMessage(
						Constants.Web.PDSA_FOLDER, null, null));
					if (!directory.exists())
						directory.mkdirs();
					String folderPath=directoryPath+pdsa.getPdsaNumber();
					File folder=new File(folderPath);
					if (!folder.exists())
						folder.mkdirs();
					for (int i = 0; i < documents.length; i++) {
						// for first file it will be last document so we will apppend last before the file name
						String docName=null;
						if(i==0)
						{
							docName="last_";
						}
						// other document will be at 1st index so we wil append the other before fileName
						else
						{
							docName="other_";
						}
						File file = new File(folder.getAbsolutePath()
								+ File.separator
								+docName+documents[i].getOriginalFilename());
						BufferedOutputStream buffStream = new BufferedOutputStream(
								new FileOutputStream(file));

						buffStream.write(documents[i].getBytes());
						buffStream.close();
						if (i == 0) {
							pdsa.setLastDocFilepath(folder.getAbsolutePath()
									+ File.separator
									+ docName+documents[i].getOriginalFilename());
						} else {
							pdsa.setOtherDocFilepath(folder.getAbsolutePath()
									+ File.separator
									+ docName+documents[i].getOriginalFilename());
						}
					}
					pdsa.setClossingRemarks(description);
					pdsaRepository.save(pdsa);
					// deleting blank txnPDSa
					txnPDSARepository
							.deleteByPdsaPdsaIdAndPercentageIsNull(pdsaId);
					return true;
				}

				catch (Exception e) {
					LOGGER.error("exception occured while closing pdsa",e);
					return false;
				}
			}

		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#generatePDSANumberAndDate()
	 * This method will return a unique pdsa number for each request of adding pdsa and server
	 */
	@Override
	@Transactional
	public Map<String, String> generatePDSANumberAndDate() {

		Map<String, String> pdsaNumberDateMap = new HashMap<>();
		// getting current date of server
		Date date = Calendar.getInstance().getTime();
		String pdsaNumber = null;
		String newPdsa = null;
		int pdsaNum = 1;
		// creating new instance of sysconfig
		SYSConfiguration sysConfig = new SYSConfiguration();
		List<SYSConfiguration> sysConfigurations = sysConfigurationRepository
				.findAll();

		// if sysconfig has some element within it then we willget the first element
		if (!sysConfigurations.isEmpty()) {
			sysConfig = sysConfigurations.get(0);
		}
		// if sysconfig has no element then we will create a no PDSA_001
		if (sysConfig == null || sysConfig.getMaxPDSANumber() == null
				|| sysConfigurations.isEmpty()) {

			pdsaNumber = "PDSA_" + String.format("%03d", pdsaNum);
			pdsaNum++;
			newPdsa = "PDSA_" + String.format("%03d", pdsaNum);
		}

		//other wise will increase it by one
		else {
			pdsaNumber = sysConfig.getMaxPDSANumber();
			pdsaNum = Integer.parseInt(pdsaNumber.split("_")[1]);
			pdsaNum++;
			// if number is less than 100 then it will be in format 0f 001 to 099
			if (pdsaNum < 100) {
				newPdsa = "PDSA_" + String.format("%03d", pdsaNum);

			} else {
				newPdsa = "PDSA_" + pdsaNum;

			}

		}
		sysConfig.setMaxPDSANumber(newPdsa);
		sysConfig.setLastUpdateDate(new Timestamp(date.getTime()));

		sysConfigurationRepository.save(sysConfig);
		// setting max date
		pdsaNumberDateMap.put("time", sdf.format(date));
		// setting auto generated pdsa number
		pdsaNumberDateMap.put("pdsaNumber", pdsaNumber);
		return pdsaNumberDateMap;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#getAllFacilityPDSADetails()
	 *  This method will return the
	 *         FacilityList with number of PDSA of for each status categorized by
	 *         the Facility
	 */
	@Override
	@Transactional
	public Map<String, Object> getAllFacilityPDSADetails() {
		// getting all area in a order of area level
		List<Area> areaList = areaRepository
				.findAllByOrderByLevelAscAreaNameAsc();
		// getting all the status of the pdsa
		List<TypeDetail> typeDetails = typeDeatilRepository
				.findByTypeTypeId(Integer.parseInt(messages.getMessage(
						Constants.Web.TYPE_ID_STATUS, null, null)));
		
		Map<Integer, Area> areaMap = new HashMap<>();
		
		List<Map<String, String>> facilityDataList = new ArrayList<>();
		Map<String, String> facilityDetailMap;
		Date date = Calendar.getInstance().getTime();

		Map<Integer, TypeDetail> typeDetailMaps = new HashMap<>();
		for (TypeDetail typeDetail : typeDetails) {
			typeDetailMaps.put(typeDetail.getTypeDetailId(), typeDetail);
		}

		List<AreaModel> areaModeList = new ArrayList<>();
		for (Area area : areaList) {
			// if area level is not 4 means either it is district,state,or country then we will simply put it in areaModel
			if (area.getLevel() != 4) {
				areaMap.put(area.getAreaId(), area);
				AreaModel areaModel = new AreaModel();
				areaModel.setAreaId(area.getAreaId());
				areaModel.setAreaName(area.getAreaName());
				areaModel.setLevel(area.getLevel());
				areaModel.setParentAreaId(area.getParentAreaId());
				areaModeList.add(areaModel);
			}

			// if area level is 4 i.e. facility level 
			else {
				// setting it in area model
				AreaModel areaModel = new AreaModel();
				areaModel.setAreaId(area.getAreaId());
				areaModel.setAreaName(area.getAreaName());
				areaModel.setLevel(area.getLevel());
				areaModel.setParentAreaId(area.getParentAreaId());
				areaModeList.add(areaModel);
				facilityDetailMap = new LinkedHashMap<>();
				// putting facilityName
				facilityDetailMap.put("Facility", area.getAreaName());
				// putting facilityId
				facilityDetailMap.put("FacilityId",
						String.valueOf(area.getAreaId()));
				// putting districtID
				facilityDetailMap.put("District",
						String.valueOf(area.getParentAreaId()));
				// putting state Id
				facilityDetailMap.put("State", String.valueOf(areaMap.get(
						area.getParentAreaId()).getParentAreaId()));
				// By default 
				for (TypeDetail typeDetail : typeDetails) {
					facilityDetailMap.put(typeDetail.getTypeDetail(), "0");
				}
// getting PDSA for facility
				for (PDSA pdsa : area.getPdsas()) {
					// checking whether PDSA is closed or not// if closed then we will increase respective close status key int facilityDetailMap by one
					if (Arrays.asList(
							messages.getMessage(
									Constants.Web.STATUS_CODE_PDSA_CLOSED,
									null, null).split(",")).contains(
							String.valueOf(pdsa.getStatus().getTypeDetailId()))) {
						int num = Integer.parseInt(facilityDetailMap.get(pdsa
								.getStatus().getTypeDetail())) + 1;
						facilityDetailMap.put(pdsa.getStatus().getTypeDetail(),
								String.valueOf(num));
					} 
					// if  not closed
					else {
						
						// if PDSA started i.e. current date is greater than the start date of pdsa
						if (!pdsa.getTxnpdsas().isEmpty()) {
							int i = 1;
							for (TXNPDSA txnPDSA : pdsa.getTxnpdsas()) {
								// if any of the TXNPDSA of the PDSA will be blank and its date is before current date then the PDSA will be counted as PENDING
								if (txnPDSA.getDenominatorValue() == null
										&& !DateUtils.isSameDay(date,
												txnPDSA.getDueDate())) {
									int num = Integer
											.parseInt(facilityDetailMap.get(typeDetailMaps
													.get(Integer.parseInt(messages
															.getMessage(
																	Constants.Web.PENDING_STATUS,
																	null, null)))
													.getTypeDetail())) + 1;
									facilityDetailMap
											.put(typeDetailMaps
													.get(Integer.parseInt(messages
															.getMessage(
																	Constants.Web.PENDING_STATUS,
																	null, null)))
													.getTypeDetail(), String
													.valueOf(num));
									break;
								}
								// checking if ongoing
								if (i == pdsa.getTxnpdsas().size()) {
									int num = Integer
											.parseInt(facilityDetailMap.get(typeDetailMaps
													.get(Integer.parseInt(messages
															.getMessage(
																	Constants.Web.ONGOING_STATUS,
																	null, null)))
													.getTypeDetail())) + 1;
									facilityDetailMap
											.put(typeDetailMaps
													.get(Integer.parseInt(messages
															.getMessage(
																	Constants.Web.ONGOING_STATUS,
																	null, null)))
													.getTypeDetail(), String
													.valueOf(num));
								}
								i++;
							}
						} 
						//// if PDSA not started i.e. current date is smaller  than the start date of pdsa then it will be counted as ongoing
						else {
							int num = Integer
									.parseInt(facilityDetailMap.get(typeDetailMaps
											.get(Integer.parseInt(messages
													.getMessage(
															Constants.Web.ONGOING_STATUS,
															null, null)))
											.getTypeDetail())) + 1;
							facilityDetailMap
									.put(typeDetailMaps
											.get(Integer.parseInt(messages
													.getMessage(
															Constants.Web.ONGOING_STATUS,
															null, null)))
											.getTypeDetail(), String
											.valueOf(num));
						}
					}
				}

				facilityDataList.add(facilityDetailMap);
			}
		}
		Map<String, Object> pdsaDataAndArea = new HashMap<>();
		pdsaDataAndArea.put("facilityPdsa", facilityDataList);
		pdsaDataAndArea.put("areaList", areaModeList);
		return pdsaDataAndArea;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PDSAService#getAllFilterOptionForPDSASummaryReport()
	 * This method will return the filter option for the pdsa summary 
	 */
	@Override
	@Transactional
	public Map<String, Object>  getAllFilterOptionForPDSASummaryReport() {
		// Getting typeId of PROCESS indicator
		int indicatorTypeId = Integer.parseInt(messages.getMessage(
				Constants.Web.INDICATOR_TYPE_PROCESS, null, null));
		
		// Getting all the indicator for PROCESS type 
		List<Indicator> indicatorDetails = indicatorRepository
				.findByCoreAreaTypeDetailIdIsNotNullAndIndicatorTypeTypeDetailIdOrderByCoreAreaTypeDetailIdAscIndicatorTypeTypeDetailIdAsc(indicatorTypeId);
		List<TypeDetail> frequencyTypeDetails = typeDeatilRepository
				.findByTypeTypeId(Integer.parseInt(messages.getMessage(
						Constants.Web.FREQUENCY_TYPE_ID, null, null)));
		Map<String, List<ValueModel>> indicatorDetailMap = new HashMap<>();
		List<ValueModel> listOfValueModel;
		//  we will put the filter in form of : coreAreaName_coreAreaId-[List<Indicator with list within it of change idea>]
		for (Indicator indicator : indicatorDetails) {

			// if we have already core area in map  we will add indicator to the list 
			if (indicatorDetailMap.containsKey(indicator.getCoreArea()
					.getTypeDetail()
					+ "_"
					+ indicator.getCoreArea().getTypeDetailId())) {

				ValueModel valueModel = new ValueModel();
				valueModel.setKey(indicator.getIndicatorId());
				valueModel.setValue(indicator.getIndicatorName());
				List<ValueModel> valueModels = new ArrayList<>();
				for (ChangeIdea changeIdea : indicator.getChangeIdeas()) {
					valueModels.add(new ValueModel(
							changeIdea.getChangeIdeaId(), changeIdea
									.getDescription()));
				}
				valueModel.setValueModelList(valueModels);
				indicatorDetailMap.get(
						indicator.getCoreArea().getTypeDetail() + "_"
								+ indicator.getCoreArea().getTypeDetailId())
						.add(valueModel);

			} 
			// else we will put the indicator in the list of that key 
			else {
				listOfValueModel = new ArrayList<>();
				ValueModel valueModel = new ValueModel();
				valueModel.setKey(indicator.getIndicatorId());
				valueModel.setValue(indicator.getIndicatorName());
				listOfValueModel.add(valueModel);
				List<ValueModel> valueModels = new ArrayList<>();
				for (ChangeIdea changeIdea : indicator.getChangeIdeas()) {
					valueModels.add(new ValueModel(
							changeIdea.getChangeIdeaId(), changeIdea
									.getDescription()));
				}
				valueModel.setValueModelList(valueModels);
				indicatorDetailMap.put(indicator.getCoreArea().getTypeDetail()
						+ "_" + indicator.getCoreArea().getTypeDetailId(),
						listOfValueModel);
			}

		}

		List<ValueModel> valueModels = new ArrayList<>();
		for (TypeDetail frequencyTypeDetail : frequencyTypeDetails) {
			ValueModel valueModel = new ValueModel();
			valueModel.setKey(frequencyTypeDetail.getTypeDetailId());
			valueModel.setValue(frequencyTypeDetail.getTypeDetail());
			valueModels.add(valueModel);
		}

		Map<String, Object> filterOptions = new HashMap<>();
		filterOptions.put("Indicator", indicatorDetailMap);
		filterOptions.put("frequencies", valueModels);
		return filterOptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.service.ChangeIdeaService#saveChangeIdea(int,
	 * java.lang.String)
	 * this method will save a new change idea
	 */

	@Override
	@Transactional
	public ValueModel saveChangeIdea(int indicatorId, String changeIdea) {
		changeIdea = changeIdea.trim();
		ValueModel valueModel = new ValueModel();
		// checking if change idea is already present in database or not
		ChangeIdea changeIdeaDomain = changeIdeaRepository
				.findByDescriptionIgnoreCaseAndIndicatorIndicatorId(changeIdea,
						indicatorId);

		if (changeIdeaDomain == null) {
			changeIdeaDomain = new ChangeIdea();
			Date date = Calendar.getInstance().getTime();
			// changeIdeaDomain.setCreatedBy(); to be filled from stateManager
			changeIdeaDomain.setCreatedDate(new Timestamp(date.getTime()));
			changeIdeaDomain.setDescription(changeIdea);
			changeIdeaDomain.setIndicator(new Indicator(indicatorId));

			changeIdeaDomain = changeIdeaRepository.save(changeIdeaDomain);

			valueModel = new ValueModel(changeIdeaDomain.getChangeIdeaId(),
					changeIdeaDomain.getDescription());
			return valueModel;
		}

		return valueModel;
	}
}
