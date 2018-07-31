package org.sdrc.scsl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.hibernate.exception.ConstraintViolationException;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.HistoricalDataFileDetail;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.model.web.AreaWebModel;
import org.sdrc.scsl.model.web.ExceptionModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.HistoricalDataFileDetailRepository;
import org.sdrc.scsl.repository.IndicatorFacilityTimeperiodMappingRepository;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

/** 
 * @author Sarita Panigrahi
 * This service class gets called when user upload/download legacy/historical data into/from the system
 *
 */
@Service
public class UploadHistoricalDataServiceImpl implements UploadHistoricalDataService {

	@Autowired
	private IndicatorRepository indicatorRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private TXNSNCUDataRepository txnsncuDataRepository;

	@Autowired
	private IndicatorFacilityTimeperiodMappingRepository indicatorFacilityTimeperiodMappingRepository;

	@Autowired
	private ServletContext context;

	@Autowired
	private TXNSubmissionManagementRepository tXNSubmissionManagementRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ResourceBundleMessageSource messages;
	
	@Autowired
	private ResourceBundleMessageSource errorMessageSource;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private AggregationService aggregationService;
	
	private DecimalFormat df = new DecimalFormat("#.##");
	private SimpleDateFormat simpleDateformater = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));
	
	private static final Logger LOGGER=Logger.getLogger(UploadHistoricalDataServiceImpl.class);
	
	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;

	@Autowired
	private HistoricalDataFileDetailRepository historicalDataFileDetailRepository;
	
	@Autowired
	private SNCUService sncuService;
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#uploadDataExcel(org.springframework.web.multipart.MultipartFile)
	 * @author Sarita Panigrahi
	 * this method calls when user uploads excel file
	 */
	@Override
	@Transactional
	public List<ExceptionModel> uploadDataExcel(MultipartFile xlsFile) throws Exception {

		List<ExceptionModel> exceptionModelList = new ArrayList<>();

		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);

		if(null==userModel)
			throw new BadCredentialsException("Invalid User!");
		
		// get Workbook instance from multipart file holding reference to .xlsx
		// file

		HSSFWorkbook workbook = null;
		HistoricalDataFileDetail historicalDataFileDetail = new HistoricalDataFileDetail();
		
		//keep the file in filepath
		String[] originalName = xlsFile.getOriginalFilename().split("\\.(?=[^\\.]+$)");
		String filePath = messages.getMessage(Constants.Web.HISTORICAL_FILE_UPLOADED_PATH, null, null)+Constants.Web.HISTORICAL_FILE_REJECTED+"/"
				+ originalName[0] + "_" + new Date().getTime() +"."+ originalName[1];
		File destFile = new File(filePath);
		xlsFile.transferTo(destFile);
		
		try {
			historicalDataFileDetail.setCreatedDate(new Timestamp(new Date().getTime()));
			historicalDataFileDetail.setFilePath(filePath);
			historicalDataFileDetailRepository.save(historicalDataFileDetail);
			
			//POIFSFileSystem holds all the macro objects
			workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(destFile)), true);
		} catch (Exception e) {
			LOGGER.error("error while loading hssfworkbook", e);
			ExceptionModel exceptionModel = new ExceptionModel();
			exceptionModel.setExceptionMessage("Invalid file");
			exceptionModel.setExceptionType(Constants.Web.HISTORICAL_FILE_EXCEPTION_TYPE);
			exceptionModelList.add(exceptionModel);
			
			return exceptionModelList;
		}
		
		HSSFSheet sheet = null;
		HSSFSheet securitySheet = null;
		HSSFSheet startSheet = null;
		
		try {
			startSheet = workbook.getSheet("Start");
			securitySheet = workbook.getSheet("Secure");
			sheet = workbook.getSheet("Project Total");
			
			Row secureRow = null!=securitySheet ? securitySheet.getRow(0) : null;
			Cell prodVersionCell = null!=secureRow ? secureRow.getCell(1) : null;
			Row enableContentRow = securitySheet.getRow(19);
			Cell enableContentCell = enableContentRow.getCell(0);
			
			if (sheet == null || prodVersionCell == null
					|| !prodVersionCell.getStringCellValue()
							.equals(passwordEncoder.encodePassword(
									messages.getMessage(Constants.Web.HISTORICAL_TEMPLATE_VERSION, null, null),
									Constants.Web.HISTORICAL_TEMPLATE_SALT))
					|| null == startSheet || null==enableContentRow || null==enableContentCell){
				
				workbook.close();
				ExceptionModel exceptionModel = new ExceptionModel();
				exceptionModel.setExceptionMessage(messages.getMessage(Constants.Web.HISTORICAL_TEMPLATE_INVALID_ERROR_MESSAGE, null, null));
				exceptionModel.setExceptionType(Constants.Web.HISTORICAL_FILE_EXCEPTION_TYPE);
				exceptionModelList.add(exceptionModel);
				
				return exceptionModelList;
			}
			
			if(enableContentCell.getBooleanCellValue() == false){
				workbook.close();
				ExceptionModel exceptionModel = new ExceptionModel();
				exceptionModel.setExceptionMessage(messages.getMessage(Constants.Web.HISTORICAL_TEMPLATE_DISABLE_CONTENT_ERROR_MESSAGE, null, null));
				exceptionModel.setExceptionType(Constants.Web.HISTORICAL_FILE_EXCEPTION_TYPE);
				exceptionModelList.add(exceptionModel);
				
				return exceptionModelList;
			}
			
		} catch (Exception e) {
			LOGGER.error("error while reading sheet",e);
			workbook.close();
			ExceptionModel exceptionModel = new ExceptionModel();
			exceptionModel.setExceptionMessage("Invalid file");
			exceptionModel.setExceptionType(Constants.Web.HISTORICAL_FILE_EXCEPTION_TYPE);
			exceptionModelList.add(exceptionModel);
			
			return exceptionModelList;
		}
		
		
		// Get first/desired sheet from the workbook

		HSSFRow dateRow = sheet.getRow(1); // 2nd row

		Integer facilityId = Integer.parseInt(dateRow.getCell(2).getStringCellValue().split("_")[0]);
		Boolean hasLr = Boolean.valueOf(dateRow.getCell(2).getStringCellValue().split("_")[1]);
		
		List<Integer> timePeriodIds = new ArrayList<>();
		List<Integer> sheetTimeIds = new ArrayList<>();
		
		// max 60 time periods present in template
		for (int cellCount = 3; cellCount < 60; cellCount++) {

			if (null == dateRow.getCell(cellCount) || dateRow.getCell(cellCount).getNumericCellValue() == 0)
				break;
			timePeriodIds.add(((Double) dateRow.getCell(cellCount).getNumericCellValue()).intValue());

		}
		sheetTimeIds.addAll(timePeriodIds);
		
		TimePeriod latestTimeperiod = timePeriodRepository.lastMonthTimeperiod();
		Integer latestTimePeriodId = latestTimeperiod.getTimePeriodId();
		
		//prevent duplicate id if the latest time is already present in file
		if(!timePeriodIds.contains(latestTimePeriodId))
			timePeriodIds.add(latestTimePeriodId);
		
		List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappingList = indicatorFacilityTimeperiodMappingRepository
				.findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLiveTrue(facilityId, timePeriodIds);

		Map<String, Integer> dataMapForLastimePeriod = null;
		Map<String, Integer> dataMapForLatestTimePeriod = new HashMap<>();
		Map<String, IndicatorFacilityTimeperiodMapping> dataMapForAll = new HashMap<>(); // ind id  -
																																	// time
																																	// id,
		List<TimePeriod> lastTimePeriod = timePeriodRepository.findTopByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(
				timePeriodIds.get(0), messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null), new PageRequest(0, 1));
		
		
		//design the map for last available timeperiod id
		if(!lastTimePeriod.isEmpty()){
			
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappingListForLastTp = 
					indicatorFacilityTimeperiodMappingRepository.findByAreaIdAndTimePeriodIsLiveTrue(facilityId, lastTimePeriod.get(0).getTimePeriodId());

			dataMapForLastimePeriod = new HashMap<>();
			for(IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappingListForLastTp){
				dataMapForLastimePeriod.put(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
						+ lastTimePeriod.get(0).getTimePeriodId(), indicatorFacilityTimeperiodMapping.getIndFacilityTpId());
			}
		}
		
		
		Timestamp date = new Timestamp(new Date().getTime());
		Map<String, Map<String, String>> indicatorTimeperiodValueMap = new LinkedHashMap<>();
		
		//this map holds indicator id and occurrence of that indicator for any time period.
		//once it becomes true, that indicator will get persist in the db irrespective of availability of data
		//applicable for process ind only
		//because once a process indicator is added it will carry forward further for upcoming time periods 
		Map<Integer, Boolean> indicatorValueAvailableMap = new HashMap<>();
		for (Indicator ind : indicatorRepository.findByIndicatorTypeTypeDetailIdInOrderByIndicatorOrderAsc(Arrays
				.asList(Integer.parseInt(messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS, null, null))))) {
			indicatorValueAvailableMap.put(ind.getIndicatorId(), false);
		}
		
		List<TimePeriod> allTimePeriod = timePeriodRepository.findByTimePeriodIdInOrderByTimePeriodIdAsc(timePeriodIds);
		//here we will design two map, one for all available tp present in the sheet and another for latest tp.
		for (TimePeriod eachTimePeriod : allTimePeriod) {
			for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappingList) {
				// Putting the values of indicator value and lastest -
				// timeperiod value in a key value pair
				if (indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriodId() == latestTimePeriodId) {
					dataMapForLatestTimePeriod.put(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
							+ latestTimePeriodId, indicatorFacilityTimeperiodMapping.getIndFacilityTpId());
				}
				// Putting the values of indicator value and rest timeperiod
				// value in a key value pair
				if (indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriodId() == eachTimePeriod
						.getTimePeriodId()) {
					dataMapForAll.put(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
							+ eachTimePeriod.getTimePeriodId(), indicatorFacilityTimeperiodMapping);
				}
			}
		}
		//keep timeperiod id and entity
		Map<Integer, TimePeriod> timeIdMap = new HashMap<>();
		// Here we will Iterate all the timeperiod values
		int currentTimeIndex = 0;
		for (TimePeriod eachTimePeriod : allTimePeriod) {
			String timePeriod = eachTimePeriod.getTimePeriod();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			Row rowOne = sheet.getRow(1);
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (nextRow.getRowNum() > 1 && null != nextRow && null != nextRow.getCell(2)) // Row will iterate from 3rd row
				{
					try {
						int i = (int) nextRow.getCell(2).getNumericCellValue();
						String indTpKey = i + "_" + eachTimePeriod.getTimePeriodId();
						String ind = "";
						if(nextRow.getCell(0)!=null){
							if(nextRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
									nextRow.getCell(0).getStringCellValue().equalsIgnoreCase("N") ){
								//+2 row of numerator
								int indNum = (int) sheet.getRow(nextRow.getRowNum()+2).getCell(0).getNumericCellValue(); //get the ind id row val
								ind = "Indicator : "+indNum+" (N)";
							}else if(nextRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
									nextRow.getCell(0).getStringCellValue().equalsIgnoreCase("D") ){
								//next row of denominator
								int indNum = (int) sheet.getRow(nextRow.getRowNum()+1).getCell(0).getNumericCellValue(); //get the ind id row val
								ind = "Indicator : "+indNum+" (D)";
							}else{
								int indNum = (int) nextRow.getCell(0).getNumericCellValue();
								ind = "Indicator : "+indNum;
							}
						}
						// Checking the valid values in required column .. check
						// with latest mapping
						try {
							// check till last tp column
							// cellIndex = 3 is the starting column of data
							// input
							for (int cellIndex = 3; cellIndex < allTimePeriod.size() + 3; cellIndex++) {
								if (rowOne.getCell(cellIndex).getNumericCellValue() == eachTimePeriod
										.getTimePeriodId()) {
									Cell cellValues = nextRow.getCell(cellIndex);
									Cell numeratorDenominatorCell = nextRow.getCell(0); // N
																						// or
																						// D
																						// from
																						// 9th
																						// column

									//if there is no data in output indicator then check
									//starting from 101 row where there is no LR
									//107 if lr is present
									
									int startRow = hasLr ? 106: 98;
									int endRow =  hasLr ? 114: 106;
									
									if(nextRow.getRowNum() >= startRow && nextRow.getRowNum()<= endRow){
										if ((null == cellValues || Cell.CELL_TYPE_NUMERIC != Integer
												.valueOf(evaluator.evaluateInCell(cellValues).getCellType()))
												&& !cellValues.getStringCellValue().equalsIgnoreCase("NaN")){
											
											CellReference cellReference = new CellReference(cellValues);
											ExceptionModel exceptionModel = setExceptionModel(cellReference, cellValues,
													timePeriod, ind, "Outcome indicator value is missing", "510", evaluator);

											exceptionModelList.add(exceptionModel);
										}
									}
									
									// if there is data in cell then check for
									// mapping

									if (null != cellValues && null != numeratorDenominatorCell
											&& Cell.CELL_TYPE_NUMERIC == Integer
													.valueOf(evaluator.evaluateInCell(cellValues).getCellType())) {
										// check with latest timeperiod mapping
										if (dataMapForLatestTimePeriod.containsKey(i + "_" + latestTimePeriodId)) {
											// only if the mapping is present in
											// the latest mapping then only
											// check for current time period
											// mapping
											// if mapping is already exist,
											// simply take the value//
											// else we will save the new mapping
											// in the
											// IndicatorFacilityTimeperiodMapping
											// table
											
											if (!hasLr && (i == 23 || i == 24 || i == 25)) {
												// for invalid mapping-- where
												// the user has filled any intermediate indicator which has
												//lr dependency

												CellReference cellReference = new CellReference(cellValues);
												ExceptionModel exceptionModel = setExceptionModel(cellReference,
														cellValues, timePeriod, ind, "Invalid mapping entry", "510",
														evaluator);

												exceptionModelList.add(exceptionModel);
												continue;
											}

											if (indicatorTimeperiodValueMap.containsKey(indTpKey)) {

												// set numerator value
												if (numeratorDenominatorCell.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("n")) {
													indicatorTimeperiodValueMap.get(indTpKey).put("N",
															String.valueOf(cellValues.getNumericCellValue()));
													// set denominator value
												} else if (numeratorDenominatorCell
														.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("d")) {
													indicatorTimeperiodValueMap.get(indTpKey).put("D",
															String.valueOf(cellValues.getNumericCellValue()));
													// set percentage value & profile indicator value
												} else {
													indicatorTimeperiodValueMap.get(indTpKey).put("P",
															String.valueOf(cellValues.getNumericCellValue()));
												}

												// for a new indicator and
												// time period combination
											} else {
												Map<String, String> indTpKeyValueMap = new HashMap<>();
												// set numerator value
												if (numeratorDenominatorCell.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("n")) {
													indTpKeyValueMap.put("N", String.valueOf(cellValues.getNumericCellValue()));
													// set denominator value
												} else if (numeratorDenominatorCell
														.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("d")) {
													indTpKeyValueMap.put("D", String.valueOf(cellValues.getNumericCellValue()));
													// set percentage value
												} else {
													indTpKeyValueMap.put("P", String.valueOf(cellValues.getNumericCellValue()));
												}
												indicatorTimeperiodValueMap.put(indTpKey, indTpKeyValueMap);
												//put indicator and occurrence in indicatorValueAvailableMap map
												if(indicatorValueAvailableMap.containsKey(i) && !indicatorValueAvailableMap.get(i))
													indicatorValueAvailableMap.put(i, true);
											}
											
											
										} else { 
											CellReference cellReference = new CellReference(cellValues);
											//for auto populated data, skip these rows. do not show these as invalid mapping,
											//neither store them in db
											if ((!hasLr && (cellReference.getRow() == 54
													|| cellReference.getRow() == 57 || cellReference.getRow() == 63
													|| cellReference.getRow() == 72))
													|| (hasLr && (cellReference.getRow() == 20
															|| cellReference.getRow() == 23
															|| cellReference.getRow() == 35
															|| cellReference.getRow() == 53
															|| cellReference.getRow() == 62
															|| cellReference.getRow() == 65
															|| cellReference.getRow() == 71
															|| cellReference.getRow() == 80
															|| cellReference.getRow() == 89
															|| cellReference.getRow() == 92
															|| cellReference.getRow() == 95))){
																continue;
												
											}
											
											// for invalid mapping-- where
													// the mapping is not
													// present in the latest
													// mapping
											ExceptionModel exceptionModel = setExceptionModel(cellReference, cellValues,
													timePeriod, ind, "Invalid mapping entry", "510", evaluator);

											exceptionModelList.add(exceptionModel);
										}
									} 
									//if cell value is null 
									else if ((null == cellValues || Cell.CELL_TYPE_BLANK == Integer
											.valueOf(evaluator.evaluateInCell(cellValues).getCellType()))
											&& null != numeratorDenominatorCell){ 
										//if it is present in the first column, then check for the last available timeperiod mapping
										if ((currentTimeIndex == 0 && dataMapForLastimePeriod != null
												&& dataMapForLastimePeriod
														.containsKey(i + "_" + lastTimePeriod.get(0).getTimePeriodId()))
												|| (currentTimeIndex == 0 && dataMapForAll.containsKey(i + "_"
														+ allTimePeriod.get(currentTimeIndex).getTimePeriodId()))
												|| (currentTimeIndex != 0 && dataMapForAll.containsKey(i + "_"
														+ allTimePeriod.get(currentTimeIndex - 1).getTimePeriodId()))) {
											if (indicatorTimeperiodValueMap.containsKey(indTpKey)) {

												// set numerator value
												if (numeratorDenominatorCell.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("n")) {
													indicatorTimeperiodValueMap.get(indTpKey).put("N",
															"");
													// set denominator value
												} else if (numeratorDenominatorCell
														.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("d")) {
													indicatorTimeperiodValueMap.get(indTpKey).put("D",
															"");
													// set percentage value
												} else {
													indicatorTimeperiodValueMap.get(indTpKey).put("P",
															"");
												}

												// for a new indicator and
												// time period combination
											} else {
												Map<String, String> indTpKeyValueMap = new HashMap<>();
												// set numerator value
												if (numeratorDenominatorCell.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("n")) {
													indTpKeyValueMap.put("N", "");
													// set denominator value
												} else if (numeratorDenominatorCell
														.getCellType() == Cell.CELL_TYPE_STRING
														&& numeratorDenominatorCell.getStringCellValue()
																.equalsIgnoreCase("d")) {
													indTpKeyValueMap.put("D", "");
													// set percentage value
												} else {
													indTpKeyValueMap.put("P", "");
												}
												indicatorTimeperiodValueMap.put(indTpKey, indTpKeyValueMap);
											}
											
										}
										//if that indicator is available for an existing time period, then it will carry forward further
										if(dataMapForLatestTimePeriod.containsKey(i + "_" + latestTimePeriodId) &&
												indicatorValueAvailableMap.containsKey(i) && indicatorValueAvailableMap.get(i)){
											Map<String, String> indTpKeyValueMap = new HashMap<>();
											// set numerator value
											if (numeratorDenominatorCell.getCellType() == Cell.CELL_TYPE_STRING
													&& numeratorDenominatorCell.getStringCellValue()
															.equalsIgnoreCase("n")) {
												indTpKeyValueMap.put("N", "");
												// set denominator value
											} else if (numeratorDenominatorCell
													.getCellType() == Cell.CELL_TYPE_STRING
													&& numeratorDenominatorCell.getStringCellValue()
															.equalsIgnoreCase("d")) {
												indTpKeyValueMap.put("D", "");
												// set percentage value
											} else {
												indTpKeyValueMap.put("P", "");
											}
											indicatorTimeperiodValueMap.put(indTpKey, indTpKeyValueMap);
										}
									}
									else // if the cell type is other than
											// numeric or blank
									if (null != cellValues && null != numeratorDenominatorCell
											&& Cell.CELL_TYPE_BOOLEAN == Integer
													.valueOf(evaluator.evaluateInCell(cellValues).getCellType())
													&& (Cell.CELL_TYPE_STRING == Integer.valueOf(
															evaluator.evaluateInCell(cellValues).getCellType()) && 
													!cellValues.equals("") && !cellValues.equals("N/A") && !cellValues.equals("NaN"))) {

										CellReference cellReference = new CellReference(cellValues);

										ExceptionModel exceptionModel = setExceptionModel(cellReference, cellValues,
												timePeriod, ind, "Invalid Input", "510", evaluator);

										exceptionModelList.add(exceptionModel);
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error("error while iterating cells",e);
						}
					} catch (Exception e) {
						LOGGER.error("error while iterating rows",e);
					}
				}
			}
			currentTimeIndex++;
			timeIdMap.put(eachTimePeriod.getTimePeriodId(), eachTimePeriod);
		}
		
		//remove the latest timeperiod of system from the list if that is not present in the sheet
		if(!sheetTimeIds.contains(latestTimePeriodId))
			allTimePeriod.remove(allTimePeriod.size()-1);
		
		/*Map<Integer, TimePeriod> timeIdMap = new HashMap<>();
		for (TimePeriod succeedingTimePeriod : succeedingTimePeriods) {
			succeedingTimePeriodIds.add(succeedingTimePeriod.getTimePeriodId());
			timeIdMap.put(succeedingTimePeriod.getTimePeriodId(), succeedingTimePeriod);
		}*/
		
		// if there is no invalid mapping--proceed towards persisting
		if (exceptionModelList.isEmpty()) {
			Area area = areaRepository.findByAreaId(facilityId);
			Map<Integer, Indicator> indIdMap = new HashMap<>();
			
			List<Indicator> inds = indicatorRepository.findAll();
			for (Indicator indicator : inds) {
				indIdMap.put(indicator.getIndicatorId(), indicator);
			}
			
			for (TimePeriod allTimePeriodObject : allTimePeriod) {
				try {
					// for every time period if it is present then set islatest
					// false and create a new TXNSubmissionManagement
					TXNSubmissionManagement txnSubmissionManagement = tXNSubmissionManagementRepository
							.findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(facilityId,
									allTimePeriodObject.getTimePeriodId());
					if (null != txnSubmissionManagement) {
						txnSubmissionManagement.setIsLatest(false);
						tXNSubmissionManagementRepository.save(txnSubmissionManagement);
						// set false of sncu old data for ref submission or
						// direct submission
						txnsncuDataRepository.updateByTXNSubmissionManagementTxnSubmissionId(
								null == txnSubmissionManagement.getRefSubmissionId()
										? txnSubmissionManagement.getTxnSubmissionId()
										: txnSubmissionManagement.getRefSubmissionId());
					}
					TXNSubmissionManagement newTxnSubmissionManagement = new TXNSubmissionManagement();
					newTxnSubmissionManagement.setCreatedBy(userModel.getUsername());
					newTxnSubmissionManagement.setCreatedDate(date);
					newTxnSubmissionManagement.setFacility(area);
					newTxnSubmissionManagement.setIsLatest(true);
					newTxnSubmissionManagement.setIsWeb(true);
					newTxnSubmissionManagement.setStatusMne(new TypeDetail(
							Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)))); 
					newTxnSubmissionManagement.setStatusSuperintendent(new TypeDetail(
							Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)))); //set both status as legacy
					newTxnSubmissionManagement.setTimePeriod(allTimePeriodObject);
					newTxnSubmissionManagement = tXNSubmissionManagementRepository.save(newTxnSubmissionManagement); // new submission

					IndicatorFacilityTimeperiodMapping savedIftm = null;
					List<TXNSNCUData> latestTxnsncuDataList = new ArrayList<>();

					for (Entry<String, Map<String, String>> entryMap : indicatorTimeperiodValueMap.entrySet()) {
						String key = entryMap.getKey();
						String[] parts = key.split("_");
						String indicatorIdValue = parts[0];
						String timeIdValue = parts[1];
						
						if(allTimePeriodObject.getTimePeriodId() != Integer.parseInt(timeIdValue))
							continue; //save for the current iteration of time period
						
						if (!dataMapForAll.containsKey(key)) {
							IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
							indicatorFacilityTimeperiodMapping
									.setIndicator(indIdMap.get(Integer.parseInt(indicatorIdValue))); 
							indicatorFacilityTimeperiodMapping
									.setTimePeriod(timeIdMap.get(Integer.parseInt(timeIdValue)));
							indicatorFacilityTimeperiodMapping.setCreatedDate(date);
							indicatorFacilityTimeperiodMapping.setFacility(area);
							indicatorFacilityTimeperiodMapping.setIsLive(true);
							// values have been saved to
							// IndicatorFacilityTimePeriodMapping Table
							savedIftm = indicatorFacilityTimeperiodMappingRepository
									.save(indicatorFacilityTimeperiodMapping);
						}
						TXNSNCUData txnsncuData = new TXNSNCUData();
						// set all numerator denominator percentage for each
						// ind-time combination
						for (Entry<String, String> entry : entryMap.getValue().entrySet()) {
							
							//for profile indicator percent type
							//set numerator and denominator value
							if (indicatorIdValue.equals("37")) {// Percentage of inborn babies

								txnsncuData = setTxnsncuData(timeIdValue, "34", "36", "P", txnsncuData,
										indicatorTimeperiodValueMap, entry);

							} else if (indicatorIdValue.equals("41")) {		//Percentage of c-section deliveries
								
								txnsncuData = setTxnsncuData(timeIdValue, "38", "40", "P", txnsncuData,
										indicatorTimeperiodValueMap, entry);
								
							} else if (indicatorIdValue.equals("42")) {		//Percentage of normal deliveries
								
								txnsncuData = setTxnsncuData(timeIdValue, "39", "40", "P", txnsncuData,
										indicatorTimeperiodValueMap, entry);
								
							} else if (indicatorIdValue.equals("44")) {		// Percentage of outborn babies
								
								txnsncuData = setTxnsncuData(timeIdValue, "35", "36", "P", txnsncuData,
										indicatorTimeperiodValueMap, entry);
								
							}else{ //for rest indicators
								if (entry.getKey().equalsIgnoreCase("n"))
									txnsncuData.setNumeratorValue(!entry.getValue().isEmpty() && !entry.getValue().equals("") ?
											(int) Double.parseDouble(entry.getValue()) : null);
								else if (entry.getKey().equalsIgnoreCase("d"))
									txnsncuData.setDenominatorValue(!entry.getValue().isEmpty() && !entry.getValue().equals("") ?
											(int) Double.parseDouble(entry.getValue()) : null);
								else{
									
									//for these profile indicators set numerator values same as percent values
									if (indicatorIdValue.equals("34") || indicatorIdValue.equals("35")
											|| indicatorIdValue.equals("36") || indicatorIdValue.equals("38")
											|| indicatorIdValue.equals("39") || indicatorIdValue.equals("40")
											|| indicatorIdValue.equals("43"))

										txnsncuData.setNumeratorValue(
												!entry.getValue().isEmpty() && !entry.getValue().equals("")
														? (int) Double.parseDouble(entry.getValue()) : null);

									txnsncuData
											.setPercentage(
													!entry.getValue().isEmpty() && !entry.getValue().equals("")
															? Double.parseDouble(
																	df.format(Double.parseDouble(entry.getValue())))
															: null);
									
								}
							}
						}

						// if the iftm mapping is already exist then save
						// that,
						// else save the new one
						txnsncuData.setIndicatorFacilityTimeperiodMapping(
								!dataMapForAll.containsKey(key) ? savedIftm : dataMapForAll.get(key));
						txnsncuData.setCreatedDate(date);
						txnsncuData.setIsLive(true);
						txnsncuData.setTxnSubmissionManagement(newTxnSubmissionManagement);
						latestTxnsncuDataList.add(txnsncuData);
					}
					txnsncuDataRepository.save(latestTxnsncuDataList);
					
					workbook.close();
					//if uploaded then set txnsubmission FK
					String uploadFilePath = messages.getMessage(Constants.Web.HISTORICAL_FILE_UPLOADED_PATH, null, null)+Constants.Web.HISTORICAL_FILE_UPLOADED+"/"
							+ originalName[0] + "_" + new Date().getTime() +"."+ originalName[1];
					destFile.renameTo(new File(uploadFilePath));
					historicalDataFileDetail.setFilePath(uploadFilePath);
					historicalDataFileDetail.settXNSubmissionManagement(newTxnSubmissionManagement);
					historicalDataFileDetailRepository.save(historicalDataFileDetail);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("error occured while persisting in db",e);
					ExceptionModel exceptionModel = new ExceptionModel();
					exceptionModel.setExceptionMessage("Something went wrong!!");
					exceptionModel.setExceptionType("Data could not be proccessed at this moment.");

					exceptionModelList.add(exceptionModel);
					
					return exceptionModelList;
				}
			}
			
			
			//get timeids greater than latest present in sheet
			List<TimePeriod> succeedingTimePeriods = timePeriodRepository.findByTimePeriodIdGreaterThanAndPeriodicityAndTimePeriodIdLessThanOrderByTimePeriodIdDesc(sheetTimeIds.get(sheetTimeIds.size()-1),
					messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null), latestTimePeriodId);
			
			if (null != succeedingTimePeriods) {
				List<Integer> succeedingTimePeriodIds = new ArrayList<>() ;
				succeedingTimePeriods.forEach(succeedingTimePeriod -> succeedingTimePeriodIds.add(succeedingTimePeriod.getTimePeriodId()));

				//get all process indicator mapping of succeeding time periods and if the mapping is not available then create
				List<IndicatorFacilityTimeperiodMapping> succedingIndicatorFacilityTimeperiodMappingList = indicatorFacilityTimeperiodMappingRepository
						.findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLiveTrueAndIndicatorIndicatorTypeTypeDetailId(facilityId, succeedingTimePeriodIds, 
								Integer.parseInt(messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS, null, null)));
				
				Map<Integer, Map<Integer,Indicator>> timeIdIndicatorIdMap = new HashMap<>();
			
				if(succedingIndicatorFacilityTimeperiodMappingList!=null && !succedingIndicatorFacilityTimeperiodMappingList.isEmpty()){
					for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : succedingIndicatorFacilityTimeperiodMappingList) {
						Integer succeedingTimeId = indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriodId();
						Integer indId = indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId();
						if (timeIdIndicatorIdMap.containsKey(succeedingTimeId)) {
							timeIdIndicatorIdMap.get(succeedingTimeId).put(indId,
									indicatorFacilityTimeperiodMapping.getIndicator());
						} else {
							Map<Integer, Indicator> innerIdIdMap = new HashMap<>();
							innerIdIdMap.put(indId, indicatorFacilityTimeperiodMapping.getIndicator());
							timeIdIndicatorIdMap.put(succeedingTimeId, innerIdIdMap);
						}

					}
				}
				List<IndicatorFacilityTimeperiodMapping> listOfIndicatorFacilityTimperiod = new ArrayList<>();
				// carry forward all new mappings for which data has been
				// given
				for (TimePeriod succeedingTimePeriod : succeedingTimePeriods) {
					
					for (Entry<Integer, Boolean> entry : indicatorValueAvailableMap.entrySet()) {
						
						if (entry.getValue() && (timeIdIndicatorIdMap == null || timeIdIndicatorIdMap.isEmpty()
								|| !timeIdIndicatorIdMap.containsKey(succeedingTimePeriod.getTimePeriodId())
								|| !timeIdIndicatorIdMap.get(succeedingTimePeriod.getTimePeriodId())
										.containsKey(entry.getKey()))){
							
							IndicatorFacilityTimeperiodMapping indFacilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
							indFacilityTimeperiodMapping.setCreatedDate(new Timestamp(new Date().getTime()));
							indFacilityTimeperiodMapping.setFacility(area);
							indFacilityTimeperiodMapping
									.setIndicator(indIdMap.get(entry.getKey()));
							indFacilityTimeperiodMapping.setTimePeriod(succeedingTimePeriod);
							indFacilityTimeperiodMapping.setIsLive(true);

							listOfIndicatorFacilityTimperiod.add(indFacilityTimeperiodMapping);
						}
						
					}
				}
				try {
					indicatorFacilityTimeperiodMappingRepository.save(listOfIndicatorFacilityTimperiod);
				} catch (ConstraintViolationException constraintViolationException) {
					LOGGER.error("error occured while persisting duplicate unique combine key",
							constraintViolationException);
				}
			}
			// once all the SNCU TXN data saved call aggregation
			aggregationService.aggregateAfterHistoricalDataUpload(sheetTimeIds, facilityId, allTimePeriod);
		}

		return exceptionModelList;
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 07-Dec-2017
	 * @param timeIdValue
	 * @param numeratorIndicatorId
	 * @param denominatorIndicatorId
	 * @param key
	 * @param txnsncuData
	 * @param indicatorTimeperiodValueMap
	 * @param entry
	 * @return
	 * set in TXNSNCUData
	 */
	private TXNSNCUData setTxnsncuData(String timeIdValue, String numeratorIndicatorId, String denominatorIndicatorId, String key,
			TXNSNCUData txnsncuData, Map<String, Map<String, String>> indicatorTimeperiodValueMap, Entry<String, String> entry){
		txnsncuData.setNumeratorValue((int) Double
				.parseDouble(indicatorTimeperiodValueMap.get(numeratorIndicatorId +"_"+ timeIdValue).get(key)));// get numerator
																							// value from another indicator
		txnsncuData.setDenominatorValue((int) Double
				.parseDouble(indicatorTimeperiodValueMap.get(denominatorIndicatorId +"_"+ timeIdValue).get(key)));// get denominator
																							// value from another indicator
		
		txnsncuData.setPercentage(!entry.getValue().isEmpty() && !entry.getValue().equals("") ?
				Double.parseDouble(df.format( Double.parseDouble(entry.getValue()))) : null);
		
		return txnsncuData;
	}

	/** 
	 * @author Sarita Panigrahi, created on: 07-Dec-2017
	 * @param cellReference
	 * @param cellValues
	 * @param timePeriod
	 * @param indicator
	 * @param exceptionType
	 * @param statusCode
	 * @param evaluator
	 * @return
	 * set exception model
	 */
	private ExceptionModel setExceptionModel(CellReference cellReference, Cell cellValues, String timePeriod,
			String indicator, String exceptionType, String statusCode, FormulaEvaluator evaluator) {

		ExceptionModel exceptionModel = new ExceptionModel();

		exceptionModel.setExceptionType(exceptionType);
		try {
			exceptionModel.setExceptionMessage(cellValues != null
					&& ((Cell.CELL_TYPE_STRING == Integer.valueOf(evaluator.evaluateInCell(cellValues).getCellType())
					&& evaluator.evaluateInCell(cellValues).getStringCellValue().equals("") ) ||
					(Cell.CELL_TYPE_BLANK == Integer.valueOf(evaluator.evaluateInCell(cellValues).getCellType())))
							? ("Cell Reference : " + cellReference.toString().split("CellReference")[1]
									+ ", " + indicator + ", Time Period : "
									+ timePeriod)
							: "Cell Reference : " + cellReference.toString().split("CellReference")[1]
									+ ", Cell Value : " + cellValues + ", " + indicator + ", Time Period : " + timePeriod);
		} catch (Exception e) {
			LOGGER.error("error while setting exception model",e);
		}
		exceptionModel.setStatusCode(statusCode);

		return exceptionModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#readDataAndDownloadExcel(java.lang.Integer, 
	 * java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)
	 * @author Sarita Panigrahi
	 * this method gets called when user download the excel template,
	 * if data is already present for the selected parameters
	 * in the system it will have those data in the file else it will simply download blank template
	 */
	@Override
	public String readDataAndDownloadExcel(Integer startTp, Integer endTp, Integer facilityId, String facilityName,
			String startTimeperiodName, String endTimePeriodName, Boolean hasLr) throws Exception {

		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		
		if(null!=userModel){
			List<Integer> timePeriodIds = new ArrayList<>();

			if(null==endTp)
				endTp = startTp ; //if the user will select only one tp
			
			List<TimePeriod> timeperiodList = timePeriodRepository
					.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc(startTp, endTp,  
							messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null)); // 1
																											// is
																											// for
																											// periodicity

			timeperiodList.forEach(timeperiod -> timePeriodIds.add(timeperiod.getTimePeriodId())); // add
																									// all
																									// time
																									// ids
																									// here

			List<TXNSNCUData> txnSncuDataList = txnsncuDataRepository
					.findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdInAndIsLiveTrue(
							facilityId, timePeriodIds);

			LinkedList<Indicator> indicatorList = indicatorRepository.findByOrderByIndicatorOrderAsc();
			
			//profile indicators = isProfile is true
			// set Lr false if that area has Lr else null
			List<Indicator> profileIndicators = filterIndicator(indicatorList, isProfileDataEntryIndicator(true, hasLr ? false : null));
			
			
			//data entry indicators = isProfile is null
			// set Lr true if that area has Lr else null
			List<Indicator> dataEntryIndicators = filterIndicator(indicatorList, isProfileDataEntryIndicator(null, hasLr ? false : null));

			//choose template based on availability of labor room
			FileInputStream stream = new FileInputStream(
					new File(context.getRealPath(
							!hasLr ? messages.getMessage(Constants.Web.HISTORICAL_NOLR_FILE_READ_PATH, null, null)
									: messages.getMessage(Constants.Web.HISTORICAL_LR_FILE_READ_PATH, null, null))));
			
			//POIFSFileSystem preserveNodes holds all the macro objects
			HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(stream), true);

			// Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheet("Project Total");
			
			//create a sheet for security purpose
			HSSFSheet securitySheet = workbook.getSheet("Secure"); //sheet 3
			
			Row secureRow = securitySheet.createRow(0);
			Cell prodVersionCell = secureRow.createCell(1);
			
			//encode version in security cell. as deployment for prod or uat 
			prodVersionCell.setCellValue(
					passwordEncoder.encodePassword(messages.getMessage(Constants.Web.HISTORICAL_TEMPLATE_VERSION, null, null),
							Constants.Web.HISTORICAL_TEMPLATE_SALT));
			
			//enable content cell will be false while downoading the file and when user will enable the content in excel it will update to true
			Row enableContentRow = securitySheet.getRow(19);
			Cell enableContentCell = enableContentRow.getCell(0);
			
			enableContentCell.setCellValue(false);
			
			workbook.setSheetHidden(1, 2); //2 for sheet very hidden 
			sheet.setColumnHidden(2, true); // IndicatorId column get hidden
			

			Map<Integer, Integer> dateColIdMap = new HashMap<>();
			Map<Integer, Integer> rowIdMap = new HashMap<>();

			
			CellStyle lockStyle = workbook.createCellStyle();
			lockStyle.setLocked(true);
			
			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setBold(true);
			font.setFontHeightInPoints((short)12);

			
			HSSFCellStyle timeHeaderStyle = workbook.createCellStyle();
			timeHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
			timeHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			timeHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			timeHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			timeHeaderStyle.setWrapText(true);
			timeHeaderStyle.setLocked(true);
			timeHeaderStyle.setFont(font);
			Cell rowTimeCell = null;
			Cell dateTimeCell = null;
			
			Row rowhead = sheet.getRow(0);
			Row dateHead = sheet.getRow(1);
			dateHead.setZeroHeight(true); // TimeperiodId row get hidden
			// put facility id in indicator head row cell
			Cell cell = dateHead.createCell(2);
			cell.setCellValue(facilityId+"_"+hasLr);//facility id + has labor room
			int dateColumnIndex = 3; //starting time period id column
			
			try {
				for (TimePeriod time : timeperiodList) {
					
					rowTimeCell = rowhead.getCell(dateColumnIndex);
					if(null!=rowTimeCell)
						rowTimeCell.setCellValue(time.getTimePeriod());
					else{
						rowTimeCell = rowhead.createCell(dateColumnIndex);
						rowTimeCell.setCellValue(time.getTimePeriod());
					}
					rowTimeCell.setCellStyle(timeHeaderStyle);
					dateTimeCell = dateHead.getCell(dateColumnIndex);
					if(null!=dateTimeCell)
						dateTimeCell.setCellValue(time.getTimePeriodId());
					else
						dateHead.createCell(dateColumnIndex).setCellValue(time.getTimePeriodId());

					sheet.setColumnWidth(dateColumnIndex, 4000);
					dateColIdMap.put(time.getTimePeriodId(), dateColumnIndex);
					dateColumnIndex++;
				}
				
				//lock rest cells -- max timperiods
				for (int columnNo = dateColumnIndex; columnNo < 63; columnNo++) { //column
					
					Iterator<Row> rowIterator = sheet.iterator();
					
					while (rowIterator.hasNext()) {
						
						 Row row = rowIterator.next();
						 
						 int endRow =  hasLr ? 114: 106;
						 if(row.getRowNum() > 1 && row.getRowNum() < endRow){//row start of data entry is 2, max is 115 in case labor room else 109
							 Cell lockCell = row.getCell(columnNo) == null ? row.createCell(columnNo) : row.getCell(columnNo);
							 lockCell.setCellStyle(lockStyle); 
						 }
						
					}
					
				}
				
				int row = 3; //starting of profile entry indicator
				for (Indicator indicator : profileIndicators) {
					Row row1 = sheet.getRow(row);
					Cell cell0 = row1.getCell(2);
					if(null==cell0)
						cell0 = row1.createCell(2);
					cell0.setCellValue(indicator.getIndicatorId());
					rowIdMap.put(indicator.getIndicatorId(), row);

					row++;
				}
				
				row = row+2; //starting of data entry indicator
				for (Indicator indicator : dataEntryIndicators) {
					Row row1 = sheet.getRow(row);
					Cell cell0 = row1.getCell(2);
					
					if(null==cell0)
						cell0 = row1.createCell(2);
					
					cell0.setCellValue(indicator.getIndicatorId());
					rowIdMap.put(indicator.getIndicatorId(), row);

					row++;

					Row row2 = sheet.getRow(row);
					cell0 = row2.getCell(2);
					
					if(null==cell0)
						cell0 = row2.createCell(2);
					
					cell0.setCellValue(indicator.getIndicatorId());

					row++;

					Row row3 = sheet.getRow(row);
					cell0 = row3.getCell(2);
					
					if(null==cell0)
						cell0 = row3.createCell(2);
					
					cell0.setCellValue(indicator.getIndicatorId());

					row++;

				}

				for (TXNSNCUData txnsncuData : txnSncuDataList) {

					int rowID = rowIdMap
							.get(txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorId());
					int dateColID = dateColIdMap
							.get(txnsncuData.getIndicatorFacilityTimeperiodMapping().getTimePeriod().getTimePeriodId());
					
					//for profile indicators
					//put the value in each row cell directly, no numerator denominator
					if(null!=txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIsProfile()){
						Row row3 = sheet.getRow(rowID);
						Cell cell0 = row3.getCell(dateColID);
						
						if(null==cell0)
							cell0 = row3.createCell(dateColID);
						
						cell0.setCellValue(txnsncuData.getPercentage());
						cell0.setCellStyle(cell0.getCellStyle());
						
					}else{//data entry indicators
						Row row3 = sheet.getRow(rowID++); //post increment for denominator 
						Cell cell0 = row3.getCell(dateColID);
						
						if(null==cell0)
							cell0 = row3.createCell(dateColID);
						
						if (null != txnsncuData.getNumeratorValue()){
							cell0.setCellValue(txnsncuData.getNumeratorValue());
							cell0.setCellStyle(cell0.getCellStyle());
						}
						if (null != txnsncuData.getDenominatorValue()) {
							row3 = sheet.getRow(rowID);
							cell0 = row3.getCell(dateColID);
							
							if(null==cell0)
								cell0 = row3.createCell(dateColID);
							
							cell0.setCellValue(txnsncuData.getDenominatorValue());
							cell0.setCellStyle(cell0.getCellStyle());
						}

						if (null != txnsncuData.getPercentage()) {
							row3 = sheet.getRow(++rowID); //pre increment for percentage 
							cell0 = row3.getCell(dateColID);
							
							if(null==cell0)
								cell0 = row3.createCell(dateColID);
							
							cell0.setCellValue(txnsncuData.getPercentage());
							cell0.setCellStyle(cell0.getCellStyle());
						}
					}


				}
			} catch (Exception e) {
				LOGGER.error("error while iterating cells",e);
			}
			String outputFilename = endTimePeriodName == null
					? messages.getMessage(Constants.Web.HISTORICAL_FILE_PATH_EXCEL, null, null) + facilityName
							+ "_" + startTimeperiodName
							+ messages.getMessage(Constants.Web.HISTORICAL_FILE_EXTENSION, null, null)
					: messages.getMessage(Constants.Web.HISTORICAL_FILE_PATH_EXCEL, null, null) + facilityName
							+ "_" + startTimeperiodName + "_" + endTimePeriodName
							+ messages.getMessage(Constants.Web.HISTORICAL_FILE_EXTENSION, null, null);
			FileOutputStream fileOut = new FileOutputStream(new File(outputFilename));
//			HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook); //formula evaluate to ensure refresh all the formulas present in the workbook
//			workbook.setForceFormulaRecalculation(true);
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			workbook.close();
			return outputFilename;
		}else
			return null;
		 
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#getAllArea()
	 * @author Sarita Panigrahi
	 * get all area
	 */
	@Override
	public List<AreaWebModel> getAllArea() {
		List<Area> listOfArea = areaRepository.findAllByOrderByLevelAscAreaNameAsc();
		List<AreaWebModel> listOfAreaModel = new ArrayList<>();
		AreaWebModel areaModel = null;
		
		for (Area area : listOfArea) {
			areaModel = new AreaWebModel();
			areaModel.setAreaId(area.getAreaId());
			areaModel.setAreaName(area.getAreaName());
			areaModel.setFacilitySize(
					area.getFacilitySize() == null ? 0 : area.getFacilitySize().getTypeDetailId());
			areaModel.setFacilitySizeName(
					area.getFacilitySize() == null ? null : area.getFacilitySize().getTypeDetail());
			areaModel.setFacilityType(
					area.getFacilityType() == null ? 0 : area.getFacilityType().getTypeDetailId());
			areaModel.setFacilityTypeName(
					area.getFacilityType() == null ? null : area.getFacilityType().getTypeDetail());
			areaModel.setLevel(area.getLevel());
			areaModel.setParentAreaId(area.getParentAreaId());
			areaModel.setWave(area.getWave() == null ? 0 : area.getWave());
			areaModel.setHasLr(area.getHasLr());
			
			listOfAreaModel.add(areaModel);
		}
		return listOfAreaModel;
	}
	
	
	//filter profile and data entry indicators
	public static List<Indicator> filterIndicator(List<Indicator> details, Predicate<Indicator> predicate){
		return details.stream().filter(predicate).collect(Collectors.<Indicator>toList());
	}
	
	//predict for indicator type
	public static Predicate<Indicator> isProfileDataEntryIndicator(Boolean isProfile, Boolean isLr){
		
		//if isLr is null then
		if(null == isLr && isProfile!=null )
			return p -> p.getIsProfile()==isProfile && p.getIsLr() == isLr;
		else
			return p -> p.getIsProfile()==isProfile;
	}
	
	/*@Transactional
	@Override
	public String updateDiscrepantData(Integer facilityId, Integer toTpId){
		
		TXNSubmissionManagement txnSubmissionManagement = tXNSubmissionManagementRepository
				.findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(facilityId, toTpId);
//		txnSubmissionManagement.setTimePeriod(timePeriodRepository.findByTimePeriodId(toTpId));
		
		
		List<TXNSNCUData> txnsncuDatas = txnSubmissionManagement.getTxnsncuDatas();
		Map<Integer, TXNSNCUData> fromIndIdMappValueId = new HashMap<>();
		
		List<IndicatorFacilityTimeperiodMapping> toIndicatorFacilityTimeperiodMappings = 
				indicatorFacilityTimeperiodMappingRepository.findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLiveTrue(facilityId, Arrays.asList(toTpId));
		
		for (TXNSNCUData txnsncuData : txnsncuDatas) {
			fromIndIdMappValueId.put(txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorId(), txnsncuData);
		}
		
		for (IndicatorFacilityTimeperiodMapping toIndicatorFacilityTimeperiodMapping : toIndicatorFacilityTimeperiodMappings) {

			if (fromIndIdMappValueId
					.containsKey(toIndicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId())) {
				System.out.println(toIndicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId());
				fromIndIdMappValueId.get(toIndicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId())
						.setIndicatorFacilityTimeperiodMapping(toIndicatorFacilityTimeperiodMapping);
			}
		}
		
		
		
		return "done";
	}*/

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#getSNCUIndicators(java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 * this method gets called when admin selects a area and time period to view legacy web data entry 
	 */
	@Override
	public SNCUIndicatorDataModel getSNCUIndicators(Integer timePeriod, Integer facilityId, Boolean hasLr){
		
		Map<String, Map<String, List<IndicatorModel>>> mapOfIndicators;
		SNCUIndicatorDataModel sncuIndicatorDataModel=new SNCUIndicatorDataModel();
		
		Date date = new Date(); 
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    
		TXNSubmissionManagement txnManagement = tXNSubmissionManagementRepository.findByStatus(facilityId, timePeriod);
		
		//if txnManagement is null then only get all sncu data
		if(null!= txnManagement){
			//we will take the RefSubmissionId if it is present else TxnSubmissionId
			Integer txnSubmissionId =  null!=txnManagement.getRefSubmissionId()?
					txnManagement.getRefSubmissionId() : txnManagement.getTxnSubmissionId() ;
					
			List<Object[]> listOfIndicators = indicatorRepository.fetchIndicatorsName(facilityId, timePeriod, txnSubmissionId);
			mapOfIndicators = sncuService.getData(listOfIndicators, true, facilityId, timePeriod, hasLr);
			
		}else{
			mapOfIndicators = sncuService.getBlankForm(facilityId, timePeriod, true, hasLr); // showing  blank form and isEnable = false
		}
		sncuIndicatorDataModel.setIndTypeIndicatorModelMap(mapOfIndicators);
		sncuIndicatorDataModel.setEnabled(true);
		
		if(null!= txnManagement)
		{
			if(txnManagement.getStatusSuperintendent()!=null)
			{
				sncuIndicatorDataModel.setSupritendentStatus(txnManagement.getStatusSuperintendent().getTypeDetail());
			}
			if(txnManagement.getStatusMne()!=null)
			{
				sncuIndicatorDataModel.setMneStatus(txnManagement.getStatusMne().getTypeDetail());
			}
			
			if(txnManagement.getStatusMne()!=null)
			{
				if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_MnE,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark(txnManagement.getRemarkByMnE());
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
				}
				else if (txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null))
						&&txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_SUPRITENDENT,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
				}
				
				else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_BY_MnE,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark(txnManagement.getRemarkByMnE());
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_CSS, null,null));
				}
				
				else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null))
						&&txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_AUTO_APPROVED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.STATUS_AUTO_APPROVED_SUPERITENDENT,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark("");
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
					
					//legacy data upload
				}else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS,null,null))){
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.STATUS_LEGACY_MNE,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark("");
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
				}
			}
			else
			{
				if(txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_BY_SUPRITENDENT,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_CSS, null,null));
				}
				else if(txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_PENDING,null,null ) 
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_PENDING_CSS, null,null));
				}
				else if (txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
				{
					sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_SUPRITENDENT,null,null )
							+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
					sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
					sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
				}
			}
		}
		
		return sncuIndicatorDataModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#saveLegacySNCUIndicatorData(java.util.Map, java.lang.Integer, java.lang.Integer)
	 * saves legacy data from web data entry
	 */
	@Override
	@Transactional
	public ReturnModel saveLegacySNCUIndicatorData(Map<String, List<IndicatorModel>> sncuIndicatorModel,
			Integer timeperiodId, Integer facilityId) {
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		ReturnModel model = new ReturnModel();
		
		List<Integer> noOfDeliveryIndicators = Arrays.asList(2,3,7,25,39); //only denominator and profile entry
		List<Integer> noOfLiveBirthIndicators = Arrays.asList(23,24,43); //only denominator and profile entry
		
		TXNSubmissionManagement latestDeoTxnSubmission = tXNSubmissionManagementRepository
				.findByIsLatestTrueAndFacilityAreaIdAndStatusSuperintendentTypeDetailIdAndTimePeriodTimePeriodId(facilityId,
						Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null)), timeperiodId);
		
		//(this scenario may occur while entering simultaneous data in app and web)
		if(null!=latestDeoTxnSubmission){
			model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SNCU_SUBMISSION_OLD_MESSAGE, null, null));
			model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			return model;
		}
		
		TXNSubmissionManagement existingTxnSubmissionManagement = tXNSubmissionManagementRepository
				.findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(facilityId, timeperiodId);

		if (existingTxnSubmissionManagement == null) {
			return sncuService.saveSNCUIndicator(sncuIndicatorModel, timeperiodId, noOfDeliveryIndicators, noOfLiveBirthIndicators, true, facilityId); //true when legacy called
		} else {
			try {
				existingTxnSubmissionManagement.setIsLatest(false);
				tXNSubmissionManagementRepository.save(existingTxnSubmissionManagement);

				//Updating old txnSubmissionManagement txnsncu data 
				TXNSubmissionManagement txnSubmissionManagement = sncuService.setTXNSubmissionManagement(facilityId, timeperiodId, userModel.getUsername(), true);
				sncuService.setSNCUIndicatorModel(txnSubmissionManagement.getTxnSubmissionId(),
						existingTxnSubmissionManagement.getRefSubmissionId(), sncuIndicatorModel, noOfDeliveryIndicators, noOfLiveBirthIndicators);
				

				// once all the SNCU TXN data saved call aggregation
				aggregationService.aggregateAfterHistoricalDataUpload(Arrays.asList(timeperiodId), facilityId, Arrays.asList(timePeriodRepository.findByTimePeriodId(timeperiodId)));
				
				model.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
				model.setStatusMessage(errorMessageSource.getMessage(Constants.Web.SNCU_SUBMISSION_SUCCESS, null, null));
				return model;
			} catch (Exception e) {
				LOGGER.error("message",e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SUBMISSION_ERROR_MESSAGE, null, null));
				model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
				return model;
			}
		}
	}
	
	/** @author: Sarita
	 * @param sncuIndicatorModels
	 * @param timerperiodId
	 * @param facilityId
	 * 
	 * This method adds the selected indicator(s) mapping to the corresponding time period and facility.
	 * It also adds the same mapping in succeeding time periods of that facility. As once an indicator mapped to a facility it will carry forward to next timeperiods.
	 */
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.UploadHistoricalDataService#addIndicatorFacilityTimePeriodMapping(java.util.List, java.lang.Integer, java.lang.Integer)
	 * add indicator from historical web module
	 */
	@Override
	@Transactional
	public void addIndicatorFacilityTimePeriodMapping(List<ValueObject> sncuIndicatorModels, Integer timerperiodId, Integer facilityId) {
		
		try {
			List<IndicatorFacilityTimeperiodMapping> listOfIndicatorFacilityTimperiod = new ArrayList<>();
			
			Integer latestTimePeriodId = timePeriodRepository.lastMonthTimeperiod().getTimePeriodId();
			//get all succeeding timeperiods including the selected one
			List<TimePeriod> succeedingTimePeriods = timePeriodRepository.findByTimePeriodIdGreaterThanEqualAndPeriodicityAndTimePeriodIdLessThanOrderByTimePeriodIdDesc
					(timerperiodId, messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null), latestTimePeriodId);
			
			//add mapping for all succeeding timeperiods and selected one of that facility
			for (TimePeriod succeedingTimePeriod : succeedingTimePeriods) {
				
				//for each timeperiod save all selected mappings
				for (ValueObject valueObject : sncuIndicatorModels) {

					IndicatorFacilityTimeperiodMapping indFacilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
					indFacilityTimeperiodMapping.setCreatedDate(new Timestamp(new Date().getTime()));
					indFacilityTimeperiodMapping.setFacility(areaRepository.findByAreaId(facilityId)); 
					indFacilityTimeperiodMapping.setIndicator(indicatorRepository.findByIndicatorId(Integer.valueOf(valueObject.getKey())));
					indFacilityTimeperiodMapping.setTimePeriod(succeedingTimePeriod);
					indFacilityTimeperiodMapping.setIsLive(true);

					listOfIndicatorFacilityTimperiod.add(indFacilityTimeperiodMapping);
				}
			}
			
			indicatorFacilityTimeperiodMappingRepository.save(listOfIndicatorFacilityTimperiod);
			
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

}