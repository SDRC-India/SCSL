package org.sdrc.scsl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.scsl.domain.AggregateData;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.TXNPlanning;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.exception.UnauthorizedException;
import org.sdrc.scsl.model.mobile.AreaModel;
import org.sdrc.scsl.model.web.TimePeriodModel;
import org.sdrc.scsl.model.web.UserAreaModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.repository.AggregateDataRepository;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.TXNPlanningRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.repository.TypeDetailRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Debi
 * @author Subrat
 * @author Sarita
 * This class is responsible for reporting module
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private StateManager stateManager;

	@Autowired
	private AggregateDataRepository aggregateDataRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private TypeDetailRepository typeDetailRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private ServletContext context;

	@Autowired
	private TXNPlanningRepository txnPlanningRepository;

	@Autowired
	private MSTUserRepository mstUserRepository;
	
	@Autowired
	private ResourceBundleMessageSource areashortnames;
	
	@Autowired
	private ResourceBundleMessageSource errorMessageSource;
	
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_TIME_FORMATTER_NO_HYPHEN));
	private SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER));
	private SimpleDateFormat requiredDateFormat = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_TIME_FORMATTER_HYPHEN));
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.ReportService#createReport(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * this method gets called when user click on download report of any report type
	 */
	@SuppressWarnings("resource")
	@Override
	@Transactional
	public String createReport(Integer facilityId, Integer reporttypeId, Integer districtId, Integer facilityTypeId,
			Integer facilitySizeId, Integer stateId, String startDate, String endDate, 
			String periodicity,String startDateStr,String endDateStr) throws Exception {
		
		UserModel userModel = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
	
		if(null!=userModel){
			
			//we are getting last area id to use it in the filename
			Integer lastAreaId = null;
			List<Integer> ahiAreaIds = new ArrayList<>();
			
			//ahi associate login
			if (userModel.getRoleName().equals("AHI Associate") && userModel.getAreaLevel() == Integer
					.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)))// move to prop while
																								// deploying war
			{
				for(UserAreaModel uam : userModel.getUserAreaModels()) {
					ahiAreaIds.add(Integer.parseInt(uam.getAreaModel().getKey()));
				}
				
			}
			if (facilityId != null)
				lastAreaId = facilityId;
			else if ((userModel.getAreaLevel() == Integer
					.parseInt(messages.getMessage(Constants.Web.COUNTRY_LEVEL_ID, null, null)) && stateId == null
					&& districtId == null)
					|| (userModel.getAreaLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.STATE_LEVEL_ID, null, null))
							&& districtId == null)
					|| (userModel.getAreaLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.DISTRICT_LEVEL_ID, null, null))
							&& facilityId == null)
					|| userModel.getAreaLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)))
				lastAreaId = userModel.getFacilityId();
			else if (districtId != null && facilityId == null) {
				lastAreaId = districtId;
			} else if (stateId != null && districtId == null && facilityId == null) {
				lastAreaId = stateId;
			}
			
			if (userModel.getAreaLevel() == Integer
					.parseInt(messages.getMessage(Constants.Web.STATE_LEVEL_ID, null, null)) && districtId == null)
				stateId = lastAreaId;
			else if (userModel.getAreaLevel() == Integer
					.parseInt(messages.getMessage(Constants.Web.DISTRICT_LEVEL_ID, null, null)) && facilityId == null)
				districtId = lastAreaId;
			else if (userModel.getAreaLevel() == Integer
					.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)))
				facilityId = lastAreaId;
			
			String areaName = areaRepository.findByAreaId(lastAreaId).getAreaName() ;

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			XSSFSheet sheet = xssfWorkbook.createSheet("Sheet1");
			String date = timeFormatter.format(new Date());
			String fileName = null;
			if (reporttypeId == Integer.valueOf("1")) {
				// for raw data
				xssfWorkbook = generateExcelSheetForRawData(xssfWorkbook, facilityId, districtId, facilityTypeId,
						facilitySizeId, stateId, Integer.parseInt(startDate), Integer.parseInt(endDate), lastAreaId,
						periodicity, ahiAreaIds);
				fileName = "Raw_Data_" + (ahiAreaIds.isEmpty() ? areaName : "Report" )+ "_" + startDateStr+"_"+endDateStr+"_"+date
						+ messages.getMessage(Constants.Web.EXT, null, null);
			} else if (reporttypeId == Integer.valueOf("2")) {
				// for data completeness
				xssfWorkbook = generateExcelSheetForDataCompleteness(xssfWorkbook, sheet, stateId, facilityId, districtId,
						facilityTypeId, facilitySizeId, Integer.parseInt(startDate), Integer.parseInt(endDate), periodicity, ahiAreaIds);
				fileName = "Summary_data_" + (ahiAreaIds.isEmpty() ? areaName : "Report" )+  "_" + startDateStr+"_"+endDateStr+"_"+date
						+ messages.getMessage(Constants.Web.EXT, null, null);
			} else if (reporttypeId == Integer.valueOf("3")) {
				// for aggregate data report
				xssfWorkbook = generateExcelSheetForAggregateReport(xssfWorkbook, lastAreaId, 
						Integer.parseInt(startDate), Integer.parseInt(endDate), userModel.getAreaLevel(), periodicity);
				fileName = "Aggregate_Data_" + areaName + "_" + startDateStr+"_"+endDateStr+"_"+date
						+ messages.getMessage(Constants.Web.EXT, null, null);
			} else {
				//for planning report
				xssfWorkbook = generateExcelSheetForPlanningReport(xssfWorkbook, startDate, endDate);
				fileName = "Planning_Report_" + (ahiAreaIds.isEmpty() ? areaName : "Report" )+ "_" + startDateStr+"_"+endDateStr+"_"+date
						+ messages.getMessage(Constants.Web.EXT, null, null);
			}

			FileOutputStream outputStream = new FileOutputStream(new File(fileName));
			if (xssfWorkbook != null) {
				xssfWorkbook.write(outputStream);
				outputStream.close();
				xssfWorkbook.close();
				return fileName;
			} else
				outputStream.close();
			return null;
		}else{
			throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
		}
		
	}

	/**
	 * @param xssfWorkbook
	 * @param facilityId
	 * @param districtId
	 * @param facilityTypeId
	 * @param facilitySizeId
	 * @param stateId
	 * @param startDate
	 * @param endDate
	 * @param lastAreaId
	 * @param periodicity
	 * @return
	 * @throws IOException
	 * this method gets called when report type is raw data type
	 */
	private XSSFWorkbook generateExcelSheetForRawData(XSSFWorkbook xssfWorkbook, Integer facilityId, Integer districtId,
			Integer facilityTypeId, Integer facilitySizeId, Integer stateId, Integer startDate, Integer endDate,
			Integer lastAreaId, String periodicity, List<Integer> ahiAreaIds) throws IOException {

		List<Object[]> listOfAggregateData = null;
		List<AggregateData> listOfAggregateDataByArea = null;
		
		List<Integer> facilityIds = new ArrayList<>();

		List<Integer> wave = Arrays.asList(1,2,3); //in report we are considering all wave
		
		//get facility ids according to the selected area 
		
		//when ahi associate is logged in
		if(!ahiAreaIds.isEmpty()) {
			facilityIds.addAll(ahiAreaIds);
		}else if (facilityId != null) {//when facility is selected
			 facilityIds.add(facilityId);
		}else if(stateId == null && districtId == null && facilitySizeId != null){ // when only facility size is selected
			facilityIds = areaRepository.fetchAllDataByFacilitySize(facilitySizeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId == null && districtId == null && facilityTypeId != null){ // when only facility type is selected
			facilityIds = areaRepository.fetchAllDataByFacilityType(facilityTypeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId != null && facilitySizeId != null && districtId == null ){ //when state and facility size is selected
			facilityIds = areaRepository.fetchDataByStateAndFacilitySize(stateId,facilitySizeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId != null && facilityTypeId != null && districtId == null ){//when state and facility type is selected
			facilityIds = areaRepository.fetchDataByStateAndFacilityType(stateId,facilityTypeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if (facilitySizeId != null && districtId != null) { //district and facility size is selected (state is selected then only district, so need to check for state)
			facilityIds = areaRepository.fetchDataByFacilitySize(facilitySizeId, districtId, wave);
		} else if (facilityTypeId != null && districtId != null) {//district and facility type is selected (state is selected then only district, so need to check for state)
			facilityIds = areaRepository.fetchDataByFacilityType(facilityTypeId, districtId, wave);
		} else if (stateId != null && districtId == null && facilityId == null) {//when only state is selected
			facilityIds = areaRepository.fetchDataByState(stateId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if (districtId != null && facilityId == null) {//when state and district is selected
			facilityIds = areaRepository.fetchDataByDistrict(districtId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		} else { //when no area/are type is selected, find all failities
			facilityIds = areaRepository.fetchAllData(wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}
		
		//get the child area data
		
		int facilityTypeSizeId = 0;
		if(facilitySizeId == null && facilityTypeId != null){ //only type selected
			facilityTypeSizeId = facilityTypeId;
		}else if(facilitySizeId != null){
			facilityTypeSizeId = facilitySizeId;
		}else{ //when nothing is selected
			facilityTypeSizeId = Integer.parseInt(
					messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null));
		}
		
		//get aggregated area
		//if any facility is selected then set type id as total, as we are persisting total for the facility
		listOfAggregateDataByArea = aggregateDataRepository.findByAreaIdAndAggreagteType(startDate, endDate, lastAreaId,
				lastAreaId==facilityId ? Integer.parseInt( messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)) : facilityTypeSizeId, periodicity, 0); //WAVE 0-->ALL
		if(!facilityIds.isEmpty())
			listOfAggregateData = aggregateDataRepository.findByFacilityTypeIdsAndFacility(facilityIds, startDate, endDate, periodicity);
		
		FileInputStream inputStream = new FileInputStream(
				new File(context.getRealPath(messages.getMessage(Constants.Web.RAW_DATA_XLS_TEMPLATE, null, null))));
		
		xssfWorkbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = xssfWorkbook.createSheet();
		
		Font font = xssfWorkbook.createFont();
		font.setFontName("Calibri");
		font.setBold(true);
		font.setFontHeightInPoints((short)12);

		//set cell styles
		XSSFCellStyle headerStyle = xssfWorkbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setWrapText(true);
		
		XSSFCellStyle timeHeaderStyle = xssfWorkbook.createCellStyle();
		timeHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		timeHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(166, 166, 166)));
		timeHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		timeHeaderStyle.setWrapText(true);
		timeHeaderStyle.setFont(font);
		
		if (null!= listOfAggregateData && !listOfAggregateData.isEmpty()) {
			int j = 1;
			Map<Integer, AggregateData> aggregateMap = new HashMap<>();

			List<TimePeriod> listOfTimeperiods = timePeriodRepository
					.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc(startDate, endDate,periodicity);

			Map<Integer, String> areaMap = new HashMap<>();
			Map<String, Integer> indicatorCountMap = new HashMap<String, Integer>();

			for (int i = 0; i < listOfAggregateData.size(); i++) {
				Object[] objects = listOfAggregateData.get(i);
				AggregateData agdt = (AggregateData) objects[0];
				aggregateMap.put(i, agdt);
				areaMap.put(agdt.getArea().getAreaId(), agdt.getArea().getAreaName());

				if ((null != agdt.getIndicator().getIsProfile() && agdt.getIndicator().getIsProfile()
						&& agdt.getNumeratorValue() != null)
						|| (null == agdt.getIndicator().getIsProfile() && agdt.getDenominatorValue() != null
								&& agdt.getNumeratorValue() != null)) {
					indicatorCountMap.put(
							agdt.getIndicator().getIndicatorId() + "_" + agdt.getTimePeriod().getTimePeriodId(),
							!indicatorCountMap.containsKey(
									agdt.getIndicator().getIndicatorId() + "_" + agdt.getTimePeriod().getTimePeriodId())
											? 1
											: indicatorCountMap.get(agdt.getIndicator().getIndicatorId() + "_"
													+ agdt.getTimePeriod().getTimePeriodId()) + 1);
				}

			}
			//set sheet names from as area short names from areashortnames. 
			for (Entry<Integer, String> area : areaMap.entrySet()) {
				xssfWorkbook.setSheetName(xssfWorkbook.getSheetIndex(xssfWorkbook.getSheetName(j)),areashortnames.getMessage(area.getKey().toString(), null, null));
				j++;
			}

			//remove unused sheets from template as by default it contains 87 facilities
			for (int i = xssfWorkbook.getNumberOfSheets() - 1; i >= j; i--) {
				xssfWorkbook.removeSheetAt(i);
			}
			//if a facility is selected then remove aggregate sheet
			if (facilityId != null) {
				xssfWorkbook.removeSheetAt(0);
			}

			//iterate over valid sheets to fill data
			for (int l = 0; l < xssfWorkbook.getNumberOfSheets(); l++) {
				sheet = xssfWorkbook.getSheetAt(l);
				int rowId = 0;
				int columnId = 6; //starting column
				Row row = sheet.getRow(rowId);
				row.setHeightInPoints(32);
				
				//iterate for required timeperiods
				for (TimePeriod listOfTimeperiod : listOfTimeperiods) {
					Cell cell = row.getCell(columnId);
					if(null==cell){
						cell = row.createCell(columnId);
					}
					cell.setCellValue(listOfTimeperiod.getTimePeriod());
					cell.setCellStyle(timeHeaderStyle);
					columnId++;
				}
				Cell cell0 = sheet.getRow(0).getCell(0);
				// Aggregate sheet
				if (facilityId == null && l == 0 && listOfAggregateDataByArea != null) {
					
					cell0.setCellValue(listOfAggregateDataByArea.get(0).getArea().getAreaName().toUpperCase());
					cell0.setCellStyle(cell0.getCellStyle());
					
					xssfWorkbook.setSheetName(l,
							sheet.getSheetName() + "_" + listOfAggregateDataByArea.get(0).getArea().getAreaName());
					for (int k = 0; k < listOfAggregateDataByArea.size(); k++) {
						AggregateData aggregateDatas = listOfAggregateDataByArea.get(k);
						sheet = createSheetForRawData(aggregateDatas, sheet, 
								indicatorCountMap, headerStyle);
					}
				} else {
					Set<Entry<Integer, AggregateData>> iterateMaps = aggregateMap.entrySet();
					for (Entry<Integer, AggregateData> iterateMap : iterateMaps) {
						AggregateData aggregateData = iterateMap.getValue();
						String requiredAreaName =  areashortnames.getMessage(aggregateData.getArea().getAreaId().toString(), null, null) ;

						if (sheet.getSheetName().equals(requiredAreaName)) {

							cell0.setCellValue(aggregateData.getArea().getAreaName().toUpperCase());
							cell0.setCellStyle(cell0.getCellStyle());
							sheet = createSheetForRawData(aggregateData, sheet,
									indicatorCountMap, headerStyle);

						}
					}
				}

			}
			return xssfWorkbook;
		} else
			return null;
	}

	/**
	 * @param aggregateData
	 * @param sheet
	 * @param indicatorCountMap
	 * @param headerStyle
	 * @return
	 * this method is responsible to set values
	 */
	private XSSFSheet createSheetForRawData(AggregateData aggregateData, XSSFSheet sheet,
			 Map<String, Integer> indicatorCountMap, XSSFCellStyle headerStyle) {
		
		if(aggregateData.getArea().getLevel() == Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null))){
			//labor room availability information at the starting of profile entry
			Row laborRoomRow = sheet.getRow(3); //Labor room available row
			Cell laborRoomCell = laborRoomRow.getCell(1); //value row
			
			laborRoomCell.setCellValue("Labor room available: " + ( aggregateData.getArea().getHasLr() ? "Yes" : "No"));
			laborRoomCell.setCellStyle(laborRoomCell.getCellStyle());
		}
		
		Integer rowNoTosetValue = getrowNoTosetValue(sheet, aggregateData.getIndicator().getIndicatorId());
		Integer columnNoTosetValue = getcolumnNoTosetValue(sheet, aggregateData.getTimePeriod().getTimePeriod());
		
		
		
		if(null!=rowNoTosetValue){
			
			Row row = sheet.getRow(rowNoTosetValue);
			Cell cell = row.getCell(columnNoTosetValue);
			
			if((rowNoTosetValue < 26 && sheet.getSheetName().contains("Aggregate_") ) || rowNoTosetValue < 15 ){
				
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(
						aggregateData.getPercentage() == null ? aggregateData.getNumeratorValue().toString() : aggregateData.getPercentage().toString());
				cell.setCellStyle(headerStyle);
				
			}
			
			else{
				
				
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(
						aggregateData.getNumeratorValue() == null ? "" : aggregateData.getNumeratorValue().toString());
				cell.setCellStyle(headerStyle);

				rowNoTosetValue++;
				row = sheet.getRow(rowNoTosetValue);
				cell = row.getCell(columnNoTosetValue);
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(
						aggregateData.getDenominatorValue() == null ? "" : aggregateData.getDenominatorValue().toString());
				cell.setCellStyle(headerStyle);

				rowNoTosetValue++;
				row = sheet.getRow(rowNoTosetValue);
				cell = row.getCell(columnNoTosetValue);
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(aggregateData.getPercentage() == null ? "" : aggregateData.getPercentage().toString());
				cell.setCellStyle(headerStyle);

				
			}
			
			//No Of facility Reporting
			if (aggregateData.getArea().getLevel() < Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null))) {
				rowNoTosetValue++;
				row = sheet.getRow(rowNoTosetValue);
				cell = row.getCell(columnNoTosetValue);
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(indicatorCountMap.containsKey(aggregateData.getIndicator().getIndicatorId() + "_"
						+ aggregateData.getTimePeriod().getTimePeriodId()) ? 
						indicatorCountMap.get(aggregateData.getIndicator().getIndicatorId() + "_"
						+ aggregateData.getTimePeriod().getTimePeriodId()).toString() : "");
				cell.setCellStyle(headerStyle);
			}
		}

	

		return sheet;
	}

	/**
	 * @param sheet
	 * @param timePeriod
	 * @return column no.
	 */
	private Integer getcolumnNoTosetValue(XSSFSheet sheet, String timePeriod) {
		Integer columnNo = null;
		Row row = sheet.getRow(0);
		for (int i = 6; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell.getStringCellValue().equals(timePeriod)){
				columnNo = i;
				break;
			}

		}
		return columnNo;
	}

	/**
	 * @param sheet
	 * @param indicatorId
	 * @return row number
	 */
	private Integer getrowNoTosetValue(XSSFSheet sheet, Integer indicatorId) {
		Integer rowNo = null;
		for (Integer i = 1; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(5); //new column
			if (null!=cell && 
					cell.getCellType()==Cell.CELL_TYPE_NUMERIC && Integer.valueOf((int) cell.getNumericCellValue()) == indicatorId){
				rowNo = i;
				break;
			}
		}

		return rowNo;
	}

	/**
	 * @param xssfWorkbook
	 * @param sheet
	 * @param stateId
	 * @param facilityId
	 * @param districtId
	 * @param facilityTypeId
	 * @param facilitySizeId
	 * @param startDate
	 * @param endDate
	 * @param periodicity
	 * @return
	 * this method gets called when summary data report type is selected
	 */
	private XSSFWorkbook generateExcelSheetForDataCompleteness(XSSFWorkbook xssfWorkbook, XSSFSheet sheet,
			Integer stateId, Integer facilityId, Integer districtId, Integer facilityTypeId, Integer facilitySizeId,
			Integer startDate, Integer endDate, String periodicity, List<Integer> ahiAreaIds) {

		// Data completeness
		List<Integer> facilityIds = new ArrayList<>();
		List<Object[]> listOfAggregateData = new ArrayList<>();
		
		List<Integer> wave = Arrays.asList(1,2,3); //in report we are considering all wave
		//when ahi associate is logged in
		if (!ahiAreaIds.isEmpty()) {
			facilityIds.addAll(ahiAreaIds);
		} else if (facilityId != null) { // when facility is selected
			facilityIds.add(facilityId);
		}else if(stateId == null && districtId == null && facilitySizeId != null){ // when only facility size is selected
			facilityIds = areaRepository.fetchAllDataByFacilitySize(facilitySizeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId == null && districtId == null && facilityTypeId != null){ // when only facility type is selected
			facilityIds = areaRepository.fetchAllDataByFacilityType(facilityTypeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId != null && facilitySizeId != null && districtId == null ){ //when state and facility size is selected
			facilityIds = areaRepository.fetchDataByStateAndFacilitySize(stateId,facilitySizeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if(stateId != null && facilityTypeId != null && districtId == null ){//when state and facility type is selected
			facilityIds = areaRepository.fetchDataByStateAndFacilityType(stateId,facilityTypeId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if (facilitySizeId != null && districtId != null) { //district and facility size is selected (state is selected then only district, so need to check for state)
			facilityIds = areaRepository.fetchDataByFacilitySize(facilitySizeId, districtId, wave);
		} else if (facilityTypeId != null && districtId != null) {//district and facility type is selected (state is selected then only district, so need to check for state)
			facilityIds = areaRepository.fetchDataByFacilityType(facilityTypeId, districtId, wave);
		} else if (stateId != null && districtId == null && facilityId == null) {//when only state is selected
			facilityIds = areaRepository.fetchDataByState(stateId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}else if (districtId != null && facilityId == null) {//when state and district is selected
			facilityIds = areaRepository.fetchDataByDistrict(districtId, wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		} else { //when no area/are type is selected, find all failities
			facilityIds = areaRepository.fetchAllData(wave, Integer .parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		}

		// getting number of data completion by facility
		
		if(!facilityIds.isEmpty())
			listOfAggregateData = aggregateDataRepository.fetchByDataCompletion(startDate, endDate,
				facilityIds, periodicity);
		if (!listOfAggregateData.isEmpty()) {
			// getting timeperiods in between start date and end date
			List<TimePeriod> listOfTimeperiods = timePeriodRepository
					.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc(startDate, endDate,
							periodicity); 

			// getting all area
			List<Area> listOfArea = areaRepository.findAll();
			Map<Integer, Area> areaMap = new HashMap<>();
			listOfArea.forEach(area -> areaMap.put(area.getAreaId(), area));

			Map<Integer, String> timeperiodMap = new HashMap<>();
			XSSFRow row = null;
			XSSFCell cell = null;
			// =========================Style start==========================
			XSSFCellStyle headerStyle = xssfWorkbook.createCellStyle();
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 207, 117)));
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setWrapText(true);

			XSSFCellStyle headerStyle1 = xssfWorkbook.createCellStyle();
			headerStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headerStyle1.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 195, 156)));
			headerStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle1.setWrapText(true);

			XSSFCellStyle headerStyle2 = xssfWorkbook.createCellStyle();
			headerStyle2.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headerStyle2.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 223, 156)));
			headerStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle2.setWrapText(true);

			XSSFCellStyle contentStyle = xssfWorkbook.createCellStyle();
			contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
			contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			contentStyle.setWrapText(true);

			XSSFCellStyle below100style = xssfWorkbook.createCellStyle();
			below100style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 222, 0)));
			below100style.setAlignment(CellStyle.ALIGN_CENTER);
			below100style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			below100style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			below100style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			below100style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			below100style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			below100style.setBorderTop(HSSFCellStyle.BORDER_THIN);

			XSSFCellStyle forNostyle = xssfWorkbook.createCellStyle();
			forNostyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 192, 192)));
			forNostyle.setAlignment(CellStyle.ALIGN_CENTER);
			forNostyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			forNostyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			forNostyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			forNostyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			forNostyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			forNostyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

			XSSFCellStyle nostyle = xssfWorkbook.createCellStyle();
			nostyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));
			nostyle.setAlignment(CellStyle.ALIGN_CENTER);
			nostyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			nostyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			nostyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			nostyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			nostyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			nostyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

			XSSFCellStyle nodatacell = xssfWorkbook.createCellStyle();
			nodatacell.setFillForegroundColor(new XSSFColor(new java.awt.Color(234, 237, 237)));
			nodatacell.setAlignment(CellStyle.ALIGN_CENTER);
			nodatacell.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			nodatacell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			nodatacell.setBorderRight(HSSFCellStyle.BORDER_THIN);
			nodatacell.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			nodatacell.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			nodatacell.setBorderTop(HSSFCellStyle.BORDER_THIN);

			XSSFCellStyle nocelldata = xssfWorkbook.createCellStyle();
			nocelldata.setAlignment(CellStyle.ALIGN_CENTER);
			nocelldata.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			nocelldata.setBorderRight(HSSFCellStyle.BORDER_THIN);
			nocelldata.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			nocelldata.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			nocelldata.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// =========================Style end==========================
			int rowNum = 0;
			int cellNum = 0;
			int slNo = 1;
			row = sheet.createRow(rowNum);
			List<String> valueList = Arrays.asList("Sl.No.", "State", "District", "Facility",
					"Facility Type", "Facility Size", "Month");
			for (int i = 0; i < valueList.size(); i++) {
				cell = row.createCell(cellNum);
				row.setHeight((short) 450);
				if (i == valueList.size() - 1) {
					int timeSize = listOfTimeperiods.size() * 2 - 1;
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, cellNum + timeSize)); // merge
																										// cell
				}
				sheet.setColumnWidth(cellNum, 4000);
				cell.setCellValue(valueList.get(i));
				cell.setCellStyle(headerStyle);
				cellNum++;
			}
			rowNum++;
			cellNum = 6;
			row = sheet.createRow(rowNum);
			for (TimePeriod timePeriod : listOfTimeperiods) { // setting time
																// periods as
																// header
				timeperiodMap.put(timePeriod.getTimePeriodId(), timePeriod.getTimePeriod());
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, cellNum, cellNum + 1));
				cell = row.createCell(cellNum);
				row.setHeight((short) 400);
				cell.setCellValue(timePeriod.getTimePeriod());
				cell.setCellStyle(headerStyle1);
				cellNum += 2;
			}

			rowNum++;
			cellNum = 6;
			row = sheet.createRow(rowNum);
			List<String> headerList = Arrays.asList("Data Entry", "% of Completion");
			for (int i = 0; i < listOfTimeperiods.size(); i++) {
				cell = row.createCell(cellNum);
				sheet.setColumnWidth(cellNum, 3000);
				cell.setCellValue(headerList.get(0));
				cell.setCellStyle(headerStyle2);
				cellNum++;
				cell = row.createCell(cellNum);
				sheet.setColumnWidth(cellNum, 4000);
				cell.setCellValue(headerList.get(1));
				cell.setCellStyle(headerStyle2);
				cellNum++;
			}
			rowNum++;
			cellNum = 0;
			row = sheet.createRow(rowNum);
			List<Integer> facility = new ArrayList<>();
			for (Object[] object : listOfAggregateData) {
				List<String> name = null;
				Integer rowNumToSetValue = getDataFacilityValueRow(areaMap, sheet, rowNum, object,
						listOfAggregateData) != null
								? Integer.parseInt(
										getDataFacilityValueRow(areaMap, sheet, rowNum, object, listOfAggregateData)
												.getKey())
								: null;
				
				facility.add((Integer) object[0]);
				if (rowNumToSetValue == null) {
					name = getValue(areaMap, object[0]);
					cell = row.createCell(cellNum);
					cell.setCellValue(slNo); // for SL. No.
					cell.setCellStyle(contentStyle);

					cellNum++;
					for (int i = 0; i < name.size(); i++) {
						cell = row.createCell(cellNum);
						cell.setCellValue(name.get(i)); // Setting value for
														// State, District,
														// Facility, Facility
														// type, Facility size
														// from list "name"
						cell.setCellStyle(contentStyle);
						cellNum++;
					}
					for (int j = 0; j < listOfTimeperiods.size(); j++) {
						cell = row.createCell(cellNum);
						cell.setCellValue("No");
						cell.setCellStyle(nocelldata);
						cellNum++;
						cell = row.createCell(cellNum);
						cell.setCellStyle(nodatacell);
						cellNum++;
					}
				}

			
				String isLR = getDataFacilityValueRow(areaMap, sheet, rowNum, object, listOfAggregateData) != null
						? getDataFacilityValueRow(areaMap, sheet, rowNum, object, listOfAggregateData).getValue()
						: null;
				
				cellNum++;
				List<String> dataValue = getDataMonthValueColumn(sheet, timeperiodMap, object, listOfTimeperiods, isLR);
				if (rowNumToSetValue == null) {
					cell = row.createCell(Integer.valueOf(dataValue.get(0)));
					cell.setCellValue(dataValue.get(1));
					cell.setCellStyle(nostyle);
					cellNum = Integer.valueOf(dataValue.get(0)) + 1;
					cell = row.createCell(cellNum);
					cell.setCellValue(Double.valueOf(dataValue.get(2)) == 0 ? null : dataValue.get(2));

					if (Double.valueOf(dataValue.get(2)) < 100)
						cell.setCellStyle(below100style);
					else
						cell.setCellStyle(forNostyle);
					if (dataValue.get(1).equals("No"))
						cell.setCellStyle(forNostyle);

					slNo++; // increasing slno
				} else {
					row = sheet.getRow(rowNumToSetValue);
					cell = row.createCell(Integer.valueOf(dataValue.get(0)));
					cell.setCellValue(dataValue.get(1));
					cell.setCellStyle(nostyle);
					cellNum = Integer.valueOf(dataValue.get(0)) + 1;
					cell = row.createCell(cellNum);
					cell.setCellValue(Double.valueOf(dataValue.get(2)) == 0 ? null : dataValue.get(2));

					if (Double.valueOf(dataValue.get(2)) < 100)
						cell.setCellStyle(below100style);
					else
						cell.setCellStyle(forNostyle);
					if (dataValue.get(1).equals("No"))
						cell.setCellStyle(forNostyle);

					--rowNum;
				}
				cellNum = 0; // resetting cellNum
				rowNum++; // increasing rowNum
				row = sheet.createRow(rowNum);
			}

			facilityIds.removeAll(facility);
			for (Integer facilityIde : facilityIds) {
				cellNum = 0;

				row = sheet.createRow(rowNum);
				if (facilityIde != null) {
					List<String> name = getValue(areaMap, facilityIde);
					cell = row.createCell(cellNum);
					cell.setCellValue(slNo); // for SL. No.
					cell.setCellStyle(contentStyle);

					cellNum++;
					for (int i = 0; i < name.size(); i++) {
						cell = row.createCell(cellNum);
						cell.setCellValue(name.get(i)); // Setting value for
														// State, District,
														// Facility, Facility
														// type, Facility size
														// from list "name"
						cell.setCellStyle(contentStyle);

						cellNum++;
					}
					for (int j = 0; j < listOfTimeperiods.size(); j++) {
						cell = row.createCell(cellNum);
						cell.setCellValue("No");
						cell.setCellStyle(nocelldata);
						cellNum++;
						cell = row.createCell(cellNum);
						cell.setCellStyle(nodatacell);
						cellNum++;
					}
				}
				rowNum++;
				slNo++;
			}
			return xssfWorkbook;
		} else
			return null;
	}

	/**
	 * @param areaMap
	 * @param sheet
	 * @param rowNum
	 * @param object
	 * @param listOfAggregateData
	 * @return
	 */
	private ValueObject getDataFacilityValueRow(Map<Integer, Area> areaMap, XSSFSheet sheet, int rowNum, Object[] object,
			List<Object[]> listOfAggregateData) {
		ValueObject valueObject = null;
		Integer rowNumToSetValue = null;
		rowNum = 3;
		Boolean isLr=null;
		for (int i = 0; i < listOfAggregateData.size(); i++) {
			if (sheet.getRow(rowNum)!=null && sheet.getRow(rowNum).getCell(3) != null) {
				if (areaMap.get(object[0]).getAreaName().equals(sheet.getRow(rowNum).getCell(3).getStringCellValue())) {
					rowNumToSetValue = rowNum;
					isLr = areaMap.get(object[0]).getHasLr();
				}
			} else
				continue;
			rowNum++;
		}
		
		if(rowNumToSetValue != null){
			valueObject = new ValueObject();
			valueObject.setKey(rowNumToSetValue.toString());
			valueObject.setValue(isLr.toString());
		}
		return valueObject;
	}

	/**
	 * @param sheet
	 * @param timeperiodMap
	 * @param object
	 * @param listOfTimeperiods
	 * @param isLR
	 * @return
	 */
	private List<String> getDataMonthValueColumn(XSSFSheet sheet, Map<Integer, String> timeperiodMap, Object[] object,
			List<TimePeriod> listOfTimeperiods, String isLR) {
		Integer cellNum = 6; // month starts from 6th no. cell counting from 0
		List<String> dataValueList = new ArrayList<>();
		Integer cellNumToSetValue = null;
		for (int i = 0; i < listOfTimeperiods.size(); i++) {
			if (sheet.getRow(1).getCell(cellNum) != null) {
				if (timeperiodMap.get(object[1]) == sheet.getRow(1).getCell(cellNum).getStringCellValue()) {
					cellNumToSetValue = cellNum;
				}
			} else
				continue;
			cellNum += 2;
		}
		dataValueList.add(null == cellNumToSetValue ? null : cellNumToSetValue.toString());
		dataValueList.add(((BigInteger) object[2]).intValue() == 0 ? "No" : "Yes"); // converting
																					// BigInteger
																					// to
																					// int
																					// type
		//if the facility has no lr then subtract THREE intermediate mapping from the denominator.
		dataValueList.add(((BigInteger) object[2]).doubleValue() == 0 ? "0"
				: String.valueOf(Math.round(
						((((BigInteger) object[2]).doubleValue()) * 100) / 
						( isLR.equalsIgnoreCase("true") ? ((BigInteger) object[3]).doubleValue() : ((BigInteger) object[3]).doubleValue()-3)) 
						));
		
		return dataValueList;
	}

	/**
	 * @param areaMap
	 * @param object
	 * @return
	 */
	private List<String> getValue(Map<Integer, Area> areaMap, Object object) {
		List<String> list = new ArrayList<>();
		list.add(areaMap.get(areaMap.get(areaMap.get((Integer) object).getParentAreaId()).getParentAreaId())
				.getAreaName()); // for state
		list.add(areaMap.get(areaMap.get((Integer) object).getParentAreaId()).getAreaName());// for
																								// district
		list.add(areaMap.get((Integer) object).getAreaName()); // for facility
		list.add(null == areaMap.get((Integer) object).getFacilityType() ? null
				: areaMap.get((Integer) object).getFacilityType().getTypeDetail()); // for
																					// facility
																					// type
		list.add(null == areaMap.get((Integer) object).getFacilitySize() ? null
				: areaMap.get((Integer) object).getFacilitySize().getTypeDetail()); // for
																					// facility
																					// size

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.ReportService#getAreaForReportFilterData(java.lang.String)
	 * get list of area
	 */
	@Override
	public Map<String, Object> getAreaForReportFilterData(String viewName) {

		UserModel userModel = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		List<Area> listOfAreas = null;
		if (userModel.getAreaLevel() == 2 && !viewName.equals("facilityView")) {
			listOfAreas = areaRepository.findAllByParentParentAreaId(userModel.getFacilityId());
		} else if (userModel.getAreaLevel() == 3 && !viewName.equals("facilityView")) {
			listOfAreas = areaRepository.findAllByParentAreaId(userModel.getFacilityId());
		} else if (userModel.getAreaLevel() == 4 && !viewName.equals("facilityView")) {
			listOfAreas = areaRepository.findbyAreaId(userModel.getFacilityId());
		} else { //for facilityView page and in report for M&E login
			listOfAreas = areaRepository.findAllByOrderByLevelAscAreaNameAsc();
		}

		Map<String, Object> areaMap = new HashMap<>();
		List<AreaModel> listOfAreaModel = new ArrayList<>();
		AreaModel areaModel = null;
		for (Area listOfArea : listOfAreas) {
			areaModel = new AreaModel();
			areaModel.setAreaId(listOfArea.getAreaId());
			areaModel.setAreaName(listOfArea.getAreaName());
			areaModel.setFacilitySize(
					listOfArea.getFacilitySize() == null ? 0 : listOfArea.getFacilitySize().getTypeDetailId());
			areaModel.setFacilitySizeName(
					listOfArea.getFacilitySize() == null ? null : listOfArea.getFacilitySize().getTypeDetail());
			areaModel.setFacilityType(
					listOfArea.getFacilityType() == null ? 0 : listOfArea.getFacilityType().getTypeDetailId());
			areaModel.setFacilityTypeName(
					listOfArea.getFacilityType() == null ? null : listOfArea.getFacilityType().getTypeDetail());
			areaModel.setLevel(listOfArea.getLevel());
			areaModel.setParentAreaId(listOfArea.getParentAreaId());
			areaModel.setWave(listOfArea.getWave() == null ? 0 : listOfArea.getWave());
			areaModel.setHasLR(listOfArea.getHasLr());
			listOfAreaModel.add(areaModel);
		}
		areaMap.put("areaList", listOfAreaModel);
		areaMap.put("areaLevel", String.valueOf(userModel.getAreaLevel()));
		areaMap.put("arealevelId", String.valueOf(userModel.getFacilityId()));
		return areaMap;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.ReportService#getAllTimePeriodForReport(java.lang.String, java.lang.String)
	 * get timeperiod list
	 */
	@Override
	public List<TimePeriodModel> getAllTimePeriodForReport(String viewName, String periodicity) {

		List<TimePeriod> listOfTimePeriod = null;
		
		//get last timeperiods where the last month time period and current time period is restricted from the selection
		//if it the periodicity is quarterly or yearly 
		if(!periodicity.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null))){
			Integer quarterTimeCount = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc(
					messages.getMessage(Constants.Web.TIMEPERIOD_QUARTER_PERIODICITY, null, null)).size();
			listOfTimePeriod = timePeriodRepository.findTopNthByPeriodicityOrderByTimePeriodIdDesc(periodicity,new PageRequest(0, quarterTimeCount));
		}else {
			//for monthly periodicity.
//			if(viewName.equals("legacy")) {
//				listOfTimePeriod = timePeriodRepository.findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(
//					periodicity,new PageRequest(0, 60)); //60 is max column in historical template
//			}else { //for report give all tp
			Integer timeCount = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc(
					messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null)).size();
			listOfTimePeriod = timePeriodRepository.findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(periodicity,
					new PageRequest(0, timeCount));
			}
		List<TimePeriodModel> listOfTimePeriodModel = new ArrayList<>();
		for (TimePeriod timePeriod : listOfTimePeriod) {
			TimePeriodModel timePeriodModel = new TimePeriodModel();
			timePeriodModel.setTimePeriodId(timePeriod.getTimePeriodId());
			timePeriodModel.setTimePeriod(timePeriod.getTimePeriod());

			listOfTimePeriodModel.add(timePeriodModel);

		}
		// reverse to asc order as query cannot asc while getting the top result
		// less than
//		Collections.reverse(listOfTimePeriodModel);
		return listOfTimePeriodModel;
	}
	
	/**
	 * @param xssfWorkbook
	 * @param lastAreaId
	 * @param startDate
	 * @param endDate
	 * @param loginLevel
	 * @param periodicity
	 * @return
	 * @throws NoSuchMessageException
	 * @throws IOException
	 *  for Aggregate_Data Download
	 */
	private XSSFWorkbook generateExcelSheetForAggregateReport(XSSFWorkbook xssfWorkbook,
			Integer lastAreaId, Integer startDate, Integer endDate, Integer loginLevel, String periodicity) throws NoSuchMessageException, IOException {

		List<Object[]> listOfCountForParentId = null;
		List<Object[]> listOfCountForChildIds = null;
		Integer totalTypeId = Integer.parseInt(
				messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null));
		
		List<AggregateData> listOfAggregateDatas = aggregateDataRepository.fetchByParentAreaId(startDate, endDate, lastAreaId,periodicity, totalTypeId, 0);
		
		if (loginLevel == 1 && lastAreaId == Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null))) {
			listOfCountForParentId = aggregateDataRepository.fetchByCountryCount(startDate, endDate, periodicity, totalTypeId, 0);
			listOfCountForChildIds = aggregateDataRepository.fetchByStateCount(startDate, endDate, periodicity, totalTypeId, 0);
		} else { //state level
			listOfCountForParentId = aggregateDataRepository.fetchBySelectedStateId(startDate, endDate, lastAreaId, periodicity, totalTypeId, 0);
			listOfCountForChildIds = aggregateDataRepository.fetchDistictBySelectedStateId(startDate, endDate, lastAreaId, periodicity, totalTypeId, 0);
		}

		FileInputStream inputStream = new FileInputStream(new File(
				context.getRealPath(messages.getMessage(Constants.Web.AGGREGATE_DATA_XLS_TEMPLATE, null, null))));
		
		
		xssfWorkbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = xssfWorkbook.createSheet();
		Map<Integer, String> areaMap = new LinkedHashMap<>();
		
		Font font = xssfWorkbook.createFont();
		font.setFontName("Calibri");
		font.setBold(true);
		font.setFontHeightInPoints((short)12);

		XSSFCellStyle timeHeaderStyle = xssfWorkbook.createCellStyle();
		timeHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		timeHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		timeHeaderStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(166, 166, 166)));
		timeHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		timeHeaderStyle.setWrapText(true);
		timeHeaderStyle.setFont(font);

		if (!listOfAggregateDatas.isEmpty()) {
			List<TimePeriod> listOfTimeperiods = timePeriodRepository
					.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc(startDate, endDate,periodicity);
			
			for (int i = 0; i < listOfAggregateDatas.size(); i++) {
				areaMap.put(listOfAggregateDatas.get(i).getArea().getAreaId(),
						listOfAggregateDatas.get(i).getArea().getAreaName());
			}
			
			int j = 0;
			for (Entry<Integer, String> area : areaMap.entrySet()) {
				xssfWorkbook.setSheetName(xssfWorkbook.getSheetIndex(xssfWorkbook.getSheetName(j)), area.getValue());
				j++;
			}
			for (int i = xssfWorkbook.getNumberOfSheets() - 1; i >= j; i--) {
				xssfWorkbook.removeSheetAt(i);
			}

			for (int l = 0; l < xssfWorkbook.getNumberOfSheets(); l++) {
				sheet = xssfWorkbook.getSheetAt(l);
				int rowId = 0;
				int columnId = 6;
				Cell cell0 = sheet.getRow(0).getCell(0);
				Row row = sheet.getRow(rowId);
				row.setHeightInPoints(32);
				for (TimePeriod listOfTimeperiod : listOfTimeperiods) {
				
					Cell cell = row.getCell(columnId);
					if(null==cell)
						cell = row.createCell(columnId);
					cell.setCellValue(listOfTimeperiod.getTimePeriod());
					cell.setCellStyle(timeHeaderStyle);
					columnId++;
				}
				for (AggregateData listOfAggregateData : listOfAggregateDatas) {

					AggregateData aggregateData = listOfAggregateData;

					if (sheet.getSheetName().equals(aggregateData.getArea().getAreaName())) {

						cell0.setCellValue(aggregateData.getArea().getAreaName());
						cell0.setCellStyle(cell0.getCellStyle());
						sheet = createSheetForAggregateData(aggregateData, sheet, xssfWorkbook, listOfCountForParentId,
								listOfCountForChildIds, loginLevel);

					}
				}
			}
			return xssfWorkbook;
		} else
			return null;
	}

	/**
	 * @param aggregateData
	 * @param sheet
	 * @param xssfWorkbook
	 * @param listOfCountForParentIds
	 * @param listOfCountForChildIds
	 * @param loginLevel
	 * @return
	 */
	private XSSFSheet createSheetForAggregateData(AggregateData aggregateData, XSSFSheet sheet,
			XSSFWorkbook xssfWorkbook, List<Object[]> listOfCountForParentIds, List<Object[]> listOfCountForChildIds,
			Integer loginLevel) {

		Integer rowNoTosetValue = getrowNoTosetValue(sheet, aggregateData.getIndicator().getIndicatorId());
		Integer columnNoTosetValue = getcolumnNoTosetValue(sheet, aggregateData.getTimePeriod().getTimePeriod());

		if(null!=rowNoTosetValue){
			
			XSSFCellStyle headerStyle = xssfWorkbook.createCellStyle();
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setWrapText(true);

			Row row = sheet.getRow(rowNoTosetValue);

			Cell cell = row.getCell(columnNoTosetValue);
			
			
			if(rowNoTosetValue < 26 ){
				
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(
						aggregateData.getPercentage() == null ? aggregateData.getNumeratorValue().toString() : aggregateData.getPercentage().toString());
				cell.setCellStyle(headerStyle);
				
			}else{
				if(null==cell)
					cell = row.createCell(columnNoTosetValue);
				cell.setCellValue(
						aggregateData.getNumeratorValue() == null ? "" : aggregateData.getNumeratorValue().toString());
				cell.setCellStyle(headerStyle);

				rowNoTosetValue++;
				Row row2 = sheet.getRow(rowNoTosetValue);
				Cell cell2 = row2.getCell(columnNoTosetValue);
				if(null==cell2)
					cell2 = row2.createCell(columnNoTosetValue);
				cell2.setCellValue(
						aggregateData.getDenominatorValue() == null ? "" : aggregateData.getDenominatorValue().toString());
				cell2.setCellStyle(headerStyle);

				rowNoTosetValue++;
				Row row3 = sheet.getRow(rowNoTosetValue);
				Cell cell3 = row3.getCell(columnNoTosetValue);
				if(null==cell3)
					cell3 = row3.createCell(columnNoTosetValue);
				cell3.setCellValue(aggregateData.getPercentage() == null ? "" : aggregateData.getPercentage().toString());
				cell3.setCellStyle(headerStyle);
			}

			
	         
			// for ParentAreaId 
			for (Object[] listOfCountForParentId : listOfCountForParentIds) {
				if (loginLevel == 1) {
					if (sheet.getSheetName().equals("India")
							&& listOfCountForParentId[1] == aggregateData.getIndicator().getIndicatorId()
							&& listOfCountForParentId[2] == aggregateData.getTimePeriod().getTimePeriodId()) {
						sheet = insertCountForFistSheet(sheet, rowNoTosetValue, columnNoTosetValue,
								listOfCountForParentId[0].toString(), headerStyle);

					}
				} else {
					if (sheet.getSheetName().equals(listOfCountForParentId[3].toString())
							&& listOfCountForParentId[1] == aggregateData.getIndicator().getIndicatorId()
							&& listOfCountForParentId[2] == aggregateData.getTimePeriod().getTimePeriodId()) {
						sheet = insertCountForFistSheet(sheet, rowNoTosetValue, columnNoTosetValue,
								listOfCountForParentId[0].toString(), headerStyle);
					}
				}
			}
			
			//for childAreaId
			for (Object[] listOfCountForChildId : listOfCountForChildIds) {
				if (sheet.getSheetName().equals(listOfCountForChildId[3].toString())
						&& listOfCountForChildId[1] == aggregateData.getIndicator().getIndicatorId()
						&& listOfCountForChildId[2] == aggregateData.getTimePeriod().getTimePeriodId()) {
					sheet = insertCountForFistSheet(sheet, rowNoTosetValue, columnNoTosetValue,
							listOfCountForChildId[0].toString(), headerStyle);
				}
			}
		}
		
		return sheet;

	}

	/**
	 * @param sheet
	 * @param rowNoTosetValue
	 * @param columnNoTosetValue
	 * @param cellValue
	 * @param headerStyle
	 * @return
	 */
	private XSSFSheet insertCountForFistSheet(XSSFSheet sheet, Integer rowNoTosetValue, Integer columnNoTosetValue,
			String cellValue, XSSFCellStyle headerStyle) {
		rowNoTosetValue++;
		Row row4 = sheet.getRow(rowNoTosetValue);
		Cell cell4 = row4.getCell(columnNoTosetValue);
		
		if(null==cell4)
			 cell4 = row4.createCell(columnNoTosetValue);
		
		cell4.setCellValue(cellValue);
		cell4.setCellStyle(headerStyle);
		return sheet;
	}

	/**
	 * @param xssfWorkbook
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * For planning report type
	 */
	private XSSFWorkbook generateExcelSheetForPlanningReport(XSSFWorkbook xssfWorkbook, String startDate,
			String endDate) throws IOException, ParseException {
		UserModel userModel = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);

		Date strtDate =requiredDateFormat.parse(requiredDateFormat.format(sdf.parse(startDate)));
		Date eDate = requiredDateFormat.parse(requiredDateFormat.format(sdf.parse(endDate)));

		List<TXNPlanning> listOftxnPlannings = null;
		if (userModel.getAreaLevel() == 1)
			listOftxnPlannings = txnPlanningRepository.findByCountryId(new java.sql.Timestamp(strtDate.getTime()), new java.sql.Timestamp(eDate.getTime()));
		else if (userModel.getAreaLevel() == 2)
			listOftxnPlannings = txnPlanningRepository.findByStateId(new java.sql.Timestamp(strtDate.getTime()), new java.sql.Timestamp(eDate.getTime()), userModel.getUserId());
		else if (userModel.getAreaLevel() == 3)
			listOftxnPlannings = txnPlanningRepository.findByDistrictId(new java.sql.Timestamp(strtDate.getTime()), new java.sql.Timestamp(eDate.getTime()), userModel.getUserId());
		else if (userModel.getAreaLevel() == 4)
			listOftxnPlannings = txnPlanningRepository.findByFacilityId(new java.sql.Timestamp(strtDate.getTime()), new java.sql.Timestamp(eDate.getTime()), userModel.getUserId());

		if (!listOftxnPlannings.isEmpty()) {
			FileInputStream inputStream = new FileInputStream(
					new File(context.getRealPath(messages.getMessage(Constants.Web.PLANNING_REPORT_TEMPLATE, null, null))));
		xssfWorkbook = new XSSFWorkbook(inputStream);
		xssfWorkbook.setSheetName(0, "Planning Report");
		XSSFSheet sheet = xssfWorkbook.getSheet("Planning Report");
		Map<Integer, String> areaIdNameMap = new HashMap<>();
		Map<Integer, Integer> areaIdParentIdMap = new HashMap<>();
		
		List<Area> areas = areaRepository.findAll();
		for (Area area : areas) {
			areaIdNameMap.put(area.getAreaId(), area.getAreaName());
			areaIdParentIdMap.put(area.getAreaId(), area.getParentAreaId());
		}
		
		XSSFCellStyle headerStyle = xssfWorkbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setWrapText(true);
		
//		Map<Integer, Integer> noOfPlanedVisits = new HashMap<>();
//		Map<Integer, Integer> noOfVisits = new HashMap<>();
//		Map<Integer, Object> listOfUserMap = new HashMap<>();
		
//		Map<Integer, Set<Integer>> noOfFacilityVisited = new HashMap<>();
		Set<Integer> facilitycount = null;
//		Map<Integer, Integer> noOfGroupVisit = new HashMap<>();
		
		
//		=========================================================
		
		Map<String, Integer> userAreaNoOfPlanedVisits = new HashMap<>();
		Map<String, Integer> userAreaNoOfVisits = new HashMap<>();
		Map<String, Object> userAreaTxnPlanMap = new HashMap<>();
		
		Map<String, Set<Integer>> userAreaNoOfFacilityVisited = new HashMap<>();
		Map<String, Integer> userAreaNoOfGroupVisit = new HashMap<>();

		for (TXNPlanning txnPlanning : listOftxnPlannings) {
			
			facilitycount = new HashSet<>();
//			listOfUserMap.put(txnPlanning.getMstUser().getUserId(), txnPlanning);
			
//			new code
			userAreaTxnPlanMap.put(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId(), txnPlanning);

			// noOfPlanned & Visit(if isLive is true)
			if (txnPlanning.getIsLive()) {
//				noOfPlanedVisits.put(txnPlanning.getMstUser().getUserId(),
//						!noOfPlanedVisits.containsKey(txnPlanning.getMstUser().getUserId()) ? 1
//								: noOfPlanedVisits.get(txnPlanning.getMstUser().getUserId()) + 1);
				
				
//				=================new code
				
				userAreaNoOfPlanedVisits.put(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId(), 
						!userAreaNoOfPlanedVisits.containsKey(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) ? 1
								: userAreaNoOfPlanedVisits.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) + 1);
			}

			// no Of Visit & no Of Facility Visit
			if (txnPlanning.getVisitedDate() != null) {

//				noOfVisits.put(txnPlanning.getMstUser().getUserId(),
//						!noOfVisits.containsKey(txnPlanning.getMstUser().getUserId()) ? 1
//								: noOfVisits.get(txnPlanning.getMstUser().getUserId()) + 1);
				
//				--------------------new code
				userAreaNoOfVisits.put(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId(),
						!userAreaNoOfVisits.containsKey(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) ? 1
								: userAreaNoOfVisits.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) + 1);
				
				

				// no Of Facility Visited
				facilitycount.add(txnPlanning.getFacility().getAreaId());
//				if (!noOfFacilityVisited.containsKey(txnPlanning.getMstUser().getUserId()))
//					noOfFacilityVisited.put(txnPlanning.getMstUser().getUserId(), facilitycount);
//				else {
//					Set<Integer> facilitycount2 = noOfFacilityVisited.get(txnPlanning.getMstUser().getUserId());
//					facilitycount2.add(txnPlanning.getFacility().getAreaId());
//				}
				
//				----------------------new code
				if (!userAreaNoOfFacilityVisited.containsKey(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()))
					userAreaNoOfFacilityVisited.put(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId(), facilitycount);
				else {
					Set<Integer> facilitycount2 = userAreaNoOfFacilityVisited.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId());
					facilitycount2.add(txnPlanning.getFacility().getAreaId());
				}

			}

			// no Of Group Visit(If tag E-mailId != null)
			if (txnPlanning.getTagEmail() != null) {
//				noOfGroupVisit.put(txnPlanning.getMstUser().getUserId(),
//						!noOfGroupVisit.containsKey(txnPlanning.getMstUser().getUserId()) ? 1
//								: noOfGroupVisit.get(txnPlanning.getMstUser().getUserId()) + 1);
				
//				new code
				userAreaNoOfGroupVisit.put(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId(),
						!userAreaNoOfGroupVisit.containsKey(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) ? 1
								: userAreaNoOfGroupVisit.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) + 1);
			}
		}

			Integer rowId = 6;
			Row row = null;
			Cell cell = null;
			
			// From
			Row row2 = sheet.getRow(2);
			Cell cell2 = row2.getCell(3);
			cell2.setCellValue("From: " + startDate);

			// To
			cell2 = row2.getCell(4);
			cell2.setCellValue("To: " +endDate);
			
			Set<Entry<String,Object>> iteratelistOfUserMaps = userAreaTxnPlanMap.entrySet();
			
			for (Entry<String, Object> iteratelistOfUserMap : iteratelistOfUserMaps) {
				
				TXNPlanning txnPlanning = (TXNPlanning) iteratelistOfUserMap.getValue();
				
				Integer columnId = 0;
				
				//State Name
				row = sheet.createRow(rowId);
				cell = row.createCell(columnId);
				cell.setCellValue(
						areaIdNameMap.get(areaIdParentIdMap.get(txnPlanning.getFacility().getParentAreaId())));
				cell.setCellStyle(headerStyle);
				
				//District Name
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(areaIdNameMap.get(txnPlanning.getFacility().getParentAreaId()));
				cell.setCellStyle(headerStyle);

				//Lead Name
				columnId++;
				cell = row.createCell(columnId);
				if (txnPlanning.getMstUser().getLead() != null) {
					cell.setCellValue(mstUserRepository.findByUserId(txnPlanning.getMstUser().getLead()).getName());
				}
				cell.setCellStyle(headerStyle);
				
				//Name Of Person
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(txnPlanning.getMstUser().getName());
				cell.setCellStyle(headerStyle);
				
				//Designation	
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(
						txnPlanning.getMstUser().getUserAreaMappings().get(0).getUserRoleFeaturePermissionMappings()
								.get(0).getRoleFeaturePermissionScheme().getRole().getRoleName());
				cell.setCellStyle(headerStyle);

				//No Of Planned & Visit
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(userAreaNoOfPlanedVisits.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()));
				cell.setCellStyle(headerStyle);
				
				// No Of Visit
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(userAreaNoOfVisits.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) == null ? 0
						: userAreaNoOfVisits.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()));
				cell.setCellStyle(headerStyle);

				//No Of Facility Visit
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(userAreaNoOfFacilityVisited.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) == null ? 0
						: userAreaNoOfFacilityVisited.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()).size());
				cell.setCellStyle(headerStyle);

				//No of group Visit
				columnId++;
				cell = row.createCell(columnId);
				cell.setCellValue(userAreaNoOfGroupVisit.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()) == null ? 0
						: userAreaNoOfGroupVisit.get(txnPlanning.getMstUser().getUserId()+"_"+txnPlanning.getFacility().getParentAreaId()));
				cell.setCellStyle(headerStyle);

				rowId++;
			}

			return xssfWorkbook;
		} else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.ReportService#getFacilityTypeAndSize()
	 * get all facility type and size
	 */
	@Override
	public Map<String, List<AreaModel>> getFacilityTypeAndSize() {
		
		 Map<String, List<AreaModel>> typeSizeKeyDetailsMap = new HashMap<>();
		 List<AreaModel> valueObjects = new ArrayList<>();
		 List<AreaModel>  valueObjects2 = new ArrayList<>();
		 
		 List<TypeDetail> typeDetails = typeDetailRepository.findByTypeTypeIdInOrderByTypeTypeIdAsc(Arrays.asList(7,8)); //get it from prop file
		 AreaModel areaModel = null;
		 for (TypeDetail typeDetail : typeDetails) {
			 
			 if(typeDetail.getType().getTypeId() == 7){ //Facility Type
				 
				 areaModel = new AreaModel();
				 areaModel.setFacilityType(typeDetail.getTypeDetailId());
				 areaModel.setFacilityTypeName(typeDetail.getTypeDetail());
				 valueObjects.add(areaModel);
				 typeSizeKeyDetailsMap.put("type", valueObjects);
			 }else{//Facility Size
				 areaModel = new AreaModel();
				 areaModel.setFacilitySize(typeDetail.getTypeDetailId());
				 areaModel.setFacilitySizeName(typeDetail.getTypeDetail());
				 valueObjects2.add(areaModel);
				 typeSizeKeyDetailsMap.put("size", valueObjects2);
			 }
			
		}
		 
		 return typeSizeKeyDetailsMap;
	}
	
}
