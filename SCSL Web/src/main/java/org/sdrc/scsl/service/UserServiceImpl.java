package org.sdrc.scsl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.RoleFeaturePermissionScheme;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.domain.UserLoginMeta;
import org.sdrc.scsl.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.RoleFeaturePermissionSchemeRepository;
import org.sdrc.scsl.repository.UserLoginMetaRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.DomainToModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) this is a serviceimpl class holds
 *         all the methods related to user / login
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private MSTUserRepository mSTUserRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private UserLoginMetaRepository userLoginMetaRepository;

	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;

	@Autowired
	private SubmissionManagementService submissionManagementService;

	@Autowired
	private ResourceBundleMessageSource notification;

	@Autowired
	private RoleFeaturePermissionSchemeRepository roleFeaturePermissionSchemeRepository;

	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scsl.service.UserService#findByUsernameAndPassword(java.lang.String,
	 * java.lang.String) find user by username and password
	 */
	@Override
	@Transactional
	public UserModel findByUsernameAndPassword(String username, String password) {
		UserModel userModel = null;

		Map<Integer, Area> areaIdDomainMap = new HashMap<>();
		List<Area> areas = areaRepository.findAll();

		for (Area area : areas) {
			areaIdDomainMap.put(area.getAreaId(), area);
		}

		MSTUser user = mSTUserRepository.findByUsernameAndPassword(username, password);
		if (user != null) {
			userModel = new UserModel();
			userModel.setUserId(user.getUserId());
			userModel.setName(user.getName());
			userModel.setUsername(user.getUserName());
			userModel.setPassword(user.getPassword());
			userModel.setEmailId(user.getEmail());
			userModel.setLive(user.getIsLive());
			userModel.setFacilityId(user.getUserAreaMappings().get(0).getFacility().getAreaId());
			userModel.setHasLr(user.getUserAreaMappings().get(0).getFacility().getHasLr());
			userModel.setRoleName(user.getUserAreaMappings().get(0).getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionScheme().getRole().getRoleName());

			if (user.getUserAreaMappings().get(0).getFacility().getLevel() == 4) { // facility level
				userModel.setFacilityName(user.getUserAreaMappings().get(0).getFacility().getAreaName());

				userModel.setDistrictName(areaIdDomainMap
						.get(user.getUserAreaMappings().get(0).getFacility().getParentAreaId()).getAreaName());
				userModel.setStateName(areaIdDomainMap.get(areaIdDomainMap
						.get(user.getUserAreaMappings().get(0).getFacility().getParentAreaId()).getParentAreaId())
						.getAreaName());

			} else if (user.getUserAreaMappings().get(0).getFacility().getLevel() == 3) { // district level
				userModel.setDistrictName(user.getUserAreaMappings().get(0).getFacility().getAreaName());
				userModel.setStateName(areaIdDomainMap
						.get(user.getUserAreaMappings().get(0).getFacility().getParentAreaId()).getAreaName());
			} else if (user.getUserAreaMappings().get(0).getFacility().getLevel() == 2) { // state level
				userModel.setStateName(user.getUserAreaMappings().get(0).getFacility().getAreaName());
			} else // country level
				userModel.setCountry(user.getUserAreaMappings().get(0).getFacility().getAreaName());

			userModel.setUserAreaModels(DomainToModelConverter.toUserAreaMappingModel(user.getUserAreaMappings()));
			userModel.setAreaLevel(user.getUserAreaMappings().get(0).getFacility().getLevel());
		}
		return userModel;
	}

	// save login meta of user log txn
	@Override
	@Transactional
	public long saveUserLoginMeta(String ipAddress, Integer userId, String userAgent) {
		UserLoginMeta userLoginMeta = new UserLoginMeta();
		userLoginMeta.setUserIpAddress(ipAddress);
		userLoginMeta.setMstUser(new MSTUser(userId));
		userLoginMeta.setLoggedInDateTime(new Timestamp(new Date().getTime()));
		userLoginMeta.setUserAgent(userAgent);
		userLoginMeta.setLoggedIn(true);
		return userLoginMetaRepository.save(userLoginMeta).getUserLogInMetaId();
	}

	// update login meta while signing out
	@Override
	@Transactional
	public void updateLoggedOutStatus(long userLoginMetaId, Timestamp loggedOutDateTime) {

		// while server start up -- update logged in users status only
		if (userLoginMetaId == -1) {

			List<UserLoginMeta> loggedInUsers = userLoginMetaRepository.findByIsLoggedInTrue();
			for (UserLoginMeta userLoginMeta : loggedInUsers) {
				userLoginMeta.setLoggedIn(false);
				userLoginMeta.setLoggedOutDateTime(loggedOutDateTime);
			}
		} else
			userLoginMetaRepository.updateStatus(loggedOutDateTime, userLoginMetaId);
	}

	@Override
	public UserLoginMeta findActiveUserLoginMeta(Integer userId) {
		return userLoginMetaRepository.findByMstUserUserIdAndIsLoggedInTrue(userId);
	}

	/*
	 * @Override
	 * 
	 * @Transactional public String updatePw(){
	 * 
	 * List<MSTUser> mstUsers = mSTUserRepository
	 * .findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdInAndUserAreaMappingsFacilityAreaIdIn(
	 * Arrays.asList(1, 2), Arrays.asList(112, 110, 104, 98, 111, 102, 107, 87, 118,
	 * 115, 114, 109, 116, 113, 117, 79, 93, 99, 90, 80, 74, 108, 101, 78, 83, 94,
	 * 103, 85, 106, 77, 81, 91, 96, 75, 92, 105, 97, 84, 89, 95, 86, 88, 82, 76,
	 * 100));
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); // List<MSTUser> newUsers = new
	 * ArrayList<>();
	 * 
	 * XSSFSheet sheet = workbook.createSheet("userlist");
	 * 
	 * int rowNum = 0; int cellNum = 0; XSSFRow row = null; XSSFCell cell = null;
	 * row = sheet.createRow(rowNum); String userNamerandomNum = "";
	 * 
	 * cell = row.createCell(cellNum); cell.setCellValue("User Id");
	 * 
	 * cellNum++; cell = row.createCell(cellNum); cell.setCellValue("Name");
	 * 
	 * cellNum++; cell = row.createCell(cellNum); cell.setCellValue("User Name");
	 * 
	 * cellNum++; cell = row.createCell(cellNum); cell.setCellValue("Password");
	 * 
	 * cellNum++; cell = row.createCell(cellNum);
	 * cell.setCellValue("Encoded Password");
	 * 
	 * for (MSTUser mstUser : mstUsers) { userNamerandomNum =
	 * mstUser.getUserName()+String.valueOf((int)(Math.random()*9000)+1000);
	 * 
	 * String encodedPassword =
	 * passwordEncoder.encodePassword(mstUser.getUserName(),userNamerandomNum); //
	 * mstUser.setPassword(encodedPassword); // newUsers.add(mstUser);
	 * 
	 * cellNum = 0; rowNum++; row = sheet.createRow(rowNum); //write in workbook
	 * 
	 * cell = row.createCell(cellNum); cell.setCellValue(mstUser.getUserId());
	 * 
	 * cellNum++; cell = row.createCell(cellNum);
	 * cell.setCellValue(mstUser.getName());
	 * 
	 * cellNum++; cell = row.createCell(cellNum);
	 * cell.setCellValue(mstUser.getUserName());
	 * 
	 * cellNum++; cell = row.createCell(cellNum);
	 * cell.setCellValue(userNamerandomNum);
	 * 
	 * cellNum++; cell = row.createCell(cellNum);
	 * cell.setCellValue(encodedPassword); } // mSTUserRepository.save(newUsers);
	 * 
	 * sheet.autoSizeColumn(0); sheet.autoSizeColumn(1); sheet.autoSizeColumn(2);
	 * 
	 * String fileName = applicationMessageSource.getMessage(
	 * Constants.Web.FILE_PATH_PDSA, null, null)+"/user detail_AP.xlsx"; try {
	 * FileOutputStream outputStream = new FileOutputStream(new File(fileName));
	 * workbook.write(outputStream); outputStream.close(); workbook.close(); } catch
	 * (Exception e) { LOGGER.error("error: ",e); } return fileName; }
	 */

	@Override
	public Long findByUserId(Integer userId) {
		UserLoginMeta ulm = userLoginMetaRepository.findByMstUserUserIdAndIsLoggedInTrue(userId);
		return ulm != null ? ulm.getUserLogInMetaId() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.service.UserService#updateByUserId(java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	public boolean updateByUserId(Integer userId, String newPassword) {

		MSTUser mstUser = mSTUserRepository.findByUserIdAndIsLiveTrue(userId);

		if (null != mstUser) {
			String encodedPassword = passwordEncoder.encodePassword(mstUser.getUserName(), newPassword);
			mSTUserRepository.updateByUserId(userId, encodedPassword);
			String message = "<p style='color:#2d3e50;font-size:14px;font-weight:normal;margin:10px 0'>Your  <span class='il'>password</span>  has been changed."
					+ "</p>" + "<p style='margin:30px 0 0 0'>";

			try {
				submissionManagementService.sendMail(Arrays.asList(mstUser.getEmail()), null,
						notification.getMessage(Constants.Web.EMAIL_DISCLAIMER, null, null), message,
						mstUser.getName());
			} catch (Exception e) {
				LOGGER.error("error oocured in updateByUserId: ", e);
			}
			return true;
		} else
			return false;

	}

	@Override
	public Boolean checkCurrentPassword(Integer userId, String currentPassword) {
		MSTUser mstUser = mSTUserRepository.findByUserIdAndIsLiveTrue(userId);
		String encodedPassword = passwordEncoder.encodePassword(mstUser.getUserName(), currentPassword);
		return encodedPassword.equals(mstUser.getPassword());

	}

	/*
	 * @Override public void getPw() {
	 * 
	 * String dmho_medchalencodedPassword =
	 * passwordEncoder.encodePassword("dmho_medchal", "dmho_medchal5698");
	 * 
	 * System.out.println(dmho_medchalencodedPassword);
	 * 
	 * String collector_medchalencodedPassword =
	 * passwordEncoder.encodePassword("collector_medchal", "collector_medchal9651");
	 * 
	 * System.out.println(collector_medchalencodedPassword);
	 * 
	 * String operations_lead = passwordEncoder.encodePassword("operations_lead",
	 * "operations_lead9802");
	 * 
	 * System.out.println(operations_lead);
	 * 
	 * String director_operations =
	 * passwordEncoder.encodePassword("director_operations",
	 * "director_operations2008");
	 * 
	 * System.out.println(director_operations);
	 * 
	 * String quality_advisor = passwordEncoder.encodePassword("quality_advisor",
	 * "quality_advisor9685");
	 * 
	 * System.out.println(quality_advisor);
	 * 
	 * String quality_lead = passwordEncoder.encodePassword("quality_lead",
	 * "quality_lead7452");
	 * 
	 * System.out.println(quality_lead);
	 * 
	 * String senior_lead = passwordEncoder.encodePassword("senior_lead",
	 * "senior_lead2852");
	 * 
	 * System.out.println(senior_lead);
	 * 
	 * String consultant_neonatologist =
	 * passwordEncoder.encodePassword("consultant_neonatologist",
	 * "consultant_neonatologist2514");
	 * 
	 * System.out.println(consultant_neonatologist);
	 * 
	 * String samantha_harrison =
	 * passwordEncoder.encodePassword("samantha_harrison", "samantha_harrison2548");
	 * 
	 * System.out.println(samantha_harrison);
	 * 
	 * String smbuat = passwordEncoder.encodePassword("smb_manni", "smb_manni8921");
	 * 
	 * System.out.println(smbuat);
	 * 
	 * String smbprod = passwordEncoder.encodePassword("smb_manni",
	 * "smb_manni9432");
	 * 
	 * System.out.println(smbprod);
	 * 
	 * }
	 */

	/*
	 * @Override public void getUATPw() {
	 * 
	 * String dmho_medchalencodedPassword =
	 * passwordEncoder.encodePassword("dmho_medchal", "dmho_medchal2698");
	 * 
	 * System.out.println(dmho_medchalencodedPassword);
	 * 
	 * String collector_medchalencodedPassword =
	 * passwordEncoder.encodePassword("collector_medchal", "collector_medchal2019");
	 * 
	 * System.out.println(collector_medchalencodedPassword);
	 * 
	 * String operations_lead = passwordEncoder.encodePassword("operations_lead",
	 * "operations_lead7412");
	 * 
	 * System.out.println(operations_lead);
	 * 
	 * String director_operations =
	 * passwordEncoder.encodePassword("director_operations",
	 * "director_operations2968");
	 * 
	 * System.out.println(director_operations);
	 * 
	 * String quality_advisor = passwordEncoder.encodePassword("quality_advisor",
	 * "quality_advisor0375");
	 * 
	 * System.out.println(quality_advisor);
	 * 
	 * String quality_lead = passwordEncoder.encodePassword("quality_lead",
	 * "quality_lead1490");
	 * 
	 * System.out.println(quality_lead);
	 * 
	 * String senior_lead = passwordEncoder.encodePassword("senior_lead",
	 * "senior_lead2919");
	 * 
	 * System.out.println(senior_lead);
	 * 
	 * String consultant_neonatologist =
	 * passwordEncoder.encodePassword("consultant_neonatologist",
	 * "consultant_neonatologist2555");
	 * 
	 * System.out.println(consultant_neonatologist);
	 * 
	 * String samantha_harrison =
	 * passwordEncoder.encodePassword("samantha_harrison", "samantha_harrison1980");
	 * 
	 * System.out.println(samantha_harrison);
	 * 
	 * }
	 */

	@Override
	public UserLoginMeta findByMstUserUserIdAndUserLogInMetaId(Integer userId, long userLogInMetaId) {
		return userLoginMetaRepository.findByMstUserUserIdAndUserLogInMetaId(userId, userLogInMetaId);
	}

	/*
	 * @Override
	 * 
	 * @Transactional public String createUsers() throws IOException {
	 * 
	 * String fileName =
	 * "F:\\DATA OLD\\Sarita\\The Workspace\\SCSL\\phase -II\\new user xls\\new_user_list.xlsx"
	 * ; Integer roleId = 17; Integer areaId = 1; List<Integer> featureIds =
	 * Arrays.asList(2, 4, 5, 6, 8); List<Integer> permissionIds = Arrays.asList(1);
	 * // view
	 * 
	 * FileInputStream excelFile = new FileInputStream(new File(fileName)); Workbook
	 * workbook = new XSSFWorkbook(excelFile);
	 * 
	 * List<UserModel> userList = new ArrayList<>();
	 * 
	 * // 0->user,
	 * 
	 * Sheet datatypeSheet = workbook.getSheetAt(0); Iterator<Row> iterator =
	 * datatypeSheet.iterator();
	 * 
	 * // get role feature permissions List<RoleFeaturePermissionScheme>
	 * roleFeaturePermissions = roleFeaturePermissionSchemeRepository
	 * .findByRoleRoleIdAndFeaturePermissionMappingFeatureFeatureIdInAndFeaturePermissionMappingPermissionPermissionIdIn(
	 * roleId, featureIds, permissionIds);
	 * 
	 * // get max userid Integer maxCurrentUserId =
	 * mSTUserRepository.findMaxUserId();
	 * 
	 * // add user name and password while (iterator.hasNext()) {
	 * 
	 * Row currentRow = iterator.next(); Iterator<Cell> cellIterator =
	 * currentRow.iterator();
	 * 
	 * // create a user model for each row UserModel userModel = new UserModel();
	 * 
	 * if (currentRow.getRowNum() > 0) { maxCurrentUserId++;
	 * 
	 * userModel.setUserId(maxCurrentUserId);
	 * 
	 * while (cellIterator.hasNext()) {
	 * 
	 * Cell currentCell = cellIterator.next();
	 * 
	 * String cellVal = null; Integer cellIntVal = null; if (currentCell != null) {
	 * if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) { cellVal =
	 * currentCell.getStringCellValue(); } else if (currentCell.getCellType() ==
	 * Cell.CELL_TYPE_NUMERIC) { cellIntVal = (int)
	 * currentCell.getNumericCellValue(); } }
	 * 
	 * if (currentCell != null) { switch (currentCell.getColumnIndex()) { case 0:
	 * userModel.setEmailId(cellVal); break;
	 * 
	 * case 1: userModel.setLead(cellIntVal); break;
	 * 
	 * case 2: userModel.setName(cellVal); break;
	 * 
	 * case 3: userModel.setUsername(cellVal); break;
	 * 
	 * case 4:
	 * userModel.setPassword(passwordEncoder.encodePassword(userModel.getUsername(),
	 * cellVal)); break; default: break; } }
	 * 
	 * }
	 * 
	 * userList.add(userModel); }
	 * 
	 * 
	 * }
	 * 
	 * int userAreaMappingId = 435; // current int userRoleFeaturePermissionId =
	 * 3593; // current
	 * 
	 * List<UserAreaMapping> userAreaMappings = new ArrayList<>();
	 * 
	 * // save user by iterating the list for (UserModel userModel : userList) {
	 * MSTUser mstUser = new MSTUser(); mstUser.setUserId(userModel.getUserId());
	 * mstUser.setEmail(userModel.getEmailId()); mstUser.setIsLive(true);
	 * mstUser.setLead(userModel.getLead()); mstUser.setName(userModel.getName());
	 * mstUser.setUserName(userModel.getUsername());
	 * mstUser.setPassword(userModel.getPassword()); mstUser.setCreatedDate(new
	 * Timestamp(new Date().getTime()));
	 * 
	 * userAreaMappingId++; UserAreaMapping userAreaMapping = new UserAreaMapping();
	 * userAreaMapping.setUserAreaMappingId(userAreaMappingId);
	 * userAreaMapping.setUser(mSTUserRepository.findByUserId(userModel.getUserId())
	 * ); userAreaMapping.setFacility(areaRepository.findByAreaId(areaId));
	 * userAreaMapping.setIsLive(true); userAreaMappings.add(userAreaMapping);
	 * 
	 * }
	 * 
	 * for (UserAreaMapping userAreaMapping : userAreaMappings) {
	 * 
	 * for (RoleFeaturePermissionScheme roleFeaturePermissionScheme :
	 * roleFeaturePermissions) {
	 * 
	 * userRoleFeaturePermissionId++; UserRoleFeaturePermissionMapping
	 * userRoleFeaturePermissionMapping = new UserRoleFeaturePermissionMapping();
	 * userRoleFeaturePermissionMapping.setUserRoleFeaturePermissionId(
	 * userRoleFeaturePermissionId);
	 * userRoleFeaturePermissionMapping.setUserAreaMapping(userAreaMapping);
	 * userRoleFeaturePermissionMapping.setRoleFeaturePermissionScheme(
	 * roleFeaturePermissionScheme); }
	 * 
	 * }
	 * 
	 * return "done"; }
	 */

//	this method will read a file of users and create the scripts in a excel file of user, user area mapping and user role feature permission mapping
	@Override
	public String createUsers() throws IOException {

		String fileName = "F:\\DATA OLD\\Sarita\\The Workspace\\SCSL\\phase -II\\new user xls\\new_user_list.xlsx";
		String fileOutName = "F:\\DATA OLD\\Sarita\\The Workspace\\SCSL\\phase -II\\new user xls\\new_user_list_script.xlsx";
		Integer roleId = 17;
		Integer areaId = 2;
		List<Integer> featureIds = Arrays.asList(2, 4, 5, 6, 8); // only dashboard
		List<Integer> permissionIds = Arrays.asList(1); // view
		int userAreaMappingId = 441; // current
		int userRoleFeaturePermissionId = 3623; // current
		
		FileInputStream excelFile = new FileInputStream(new File(fileName));
		Workbook workbook = new XSSFWorkbook(excelFile);

		List<UserModel> userList = new ArrayList<>();

		// 0->user,

		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();

		// get role feature permissions
		List<RoleFeaturePermissionScheme> roleFeaturePermissions = roleFeaturePermissionSchemeRepository
				.findByRoleRoleIdAndFeaturePermissionMappingFeatureFeatureIdInAndFeaturePermissionMappingPermissionPermissionIdIn(
						roleId, featureIds, permissionIds);

		// get max userid
		Integer maxCurrentUserId = mSTUserRepository.findMaxUserId();

		// add user name and password
		while (iterator.hasNext()) {

			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();

			// create a user model for each row
			UserModel userModel = new UserModel();

			if (currentRow.getRowNum() > 0) {
				maxCurrentUserId++;

				userModel.setUserId(maxCurrentUserId);

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					String cellVal = null;
					Integer cellIntVal = null;
					if (currentCell != null) {
						if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
							cellVal = currentCell.getStringCellValue();
						} else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							cellIntVal = (int) currentCell.getNumericCellValue();
						}
					}

					if (currentCell != null) {
						switch (currentCell.getColumnIndex()) {
						case 0:
							userModel.setEmailId(cellVal);
							break;

						case 1:
							userModel.setLead(cellIntVal);
							break;

						case 2:
							userModel.setName(cellVal);
							break;

						case 3:
							userModel.setUsername(cellVal);
							break;

						case 4:
							userModel.setPassword(passwordEncoder.encodePassword(userModel.getUsername(), cellVal));
							break;
						default:
							break;
						}
					}

				}

				userList.add(userModel);
			}

		}

		Sheet writeUserSheet = workbook.getSheetAt(1);

		List<UserAreaMapping> userAreaMappings = new ArrayList<>();

		int rowNum = 0;
		
		Row row = writeUserSheet.createRow(rowNum++);
		int colNum = 0;

		Cell headCell = row.createCell(colNum);
		headCell.setCellValue("USER ID");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("EMAIL ID");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("LEAD");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("NAME");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("USER NAME");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("PASSWORD");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("DATE TIME");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("SCRIPT");
		
		
		// save user by iterating the list
		for (UserModel userModel : userList) {

			row = writeUserSheet.createRow(rowNum++);

			colNum = 0;

			Cell cell = row.createCell(colNum);
			cell.setCellValue(userModel.getUserId());

			if (userModel.getEmailId() != null) {
				colNum++;
				cell = row.createCell(colNum);
				cell.setCellValue(userModel.getEmailId());
			}

//			colNum++;
//			cell = row.createCell(colNum);
//			cell.setCellValue(true);

			if (userModel.getLead() != null) {
				colNum++;
				cell = row.createCell(colNum);
				cell.setCellValue(userModel.getLead());
			}

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(userModel.getName());

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(userModel.getUsername());

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(userModel.getPassword());

//			colNum++;
//			cell = row.createCell(colNum);
//			cell.setCellValue(new Timestamp(new Date().getTime()));
			
			colNum++;
			cell = row.createCell(colNum);
//			cell.setCellValue("INSERT INTO public.mst_user( id, email, is_live, lead, name, password, user_name) VALUES ("+" " + userModel.getUserId()+
//					",'"+ userModel.getEmailId()+"','TRUE',"+userModel.getLead()+",'"+userModel.getName()+"'"+",'"+userModel.getPassword()+"', '"+userModel.getUsername()+"'"+");");

			cell.setCellValue("INSERT INTO public.mst_user( id, is_live, name, password, user_name) VALUES ("+" " + userModel.getUserId()+
					",'TRUE','"+userModel.getName()+"'"+",'"+userModel.getPassword()+"', '"+userModel.getUsername()+"'"+");");
			
			userAreaMappingId++;
			UserAreaMapping userAreaMapping = new UserAreaMapping();
			userAreaMapping.setUserAreaMappingId(userAreaMappingId);
			userAreaMapping.setUser(new MSTUser(userModel.getUserId()));
			userAreaMapping.setFacility(areaRepository.findByAreaId(areaId));
			userAreaMapping.setIsLive(true);
			userAreaMappings.add(userAreaMapping);

		}

		List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings = new ArrayList<>();
		Sheet writeUserAreaSheet = workbook.getSheetAt(2);
		
		rowNum = 0;
		
		row = writeUserAreaSheet.createRow(rowNum++);
		colNum = 0;
		
		headCell = row.createCell(colNum);
		headCell.setCellValue("UserAreaMappingId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("AreaId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("UserId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("Script");
		
		for (UserAreaMapping userAreaMapping : userAreaMappings) {

			row = writeUserAreaSheet.createRow(rowNum++);

			colNum = 0;

			Cell cell = row.createCell(colNum);
			cell.setCellValue(userAreaMapping.getUserAreaMappingId());

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(userAreaMapping.getFacility().getAreaId());

//			colNum++;
//			cell = row.createCell(colNum);
//			cell.setCellValue(true);

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(userAreaMapping.getUser().getUserId());
			
			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue("INSERT INTO public.user_area_mapping( id, is_live, facility_id_fk, user_fk) VALUES ( "+userAreaMapping.getUserAreaMappingId()
							+",'TRUE', "+userAreaMapping.getFacility().getAreaId()+", "+userAreaMapping.getUser().getUserId()+");");

			for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissions) {

				userRoleFeaturePermissionId++;
				UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping = new UserRoleFeaturePermissionMapping();
				userRoleFeaturePermissionMapping.setUserRoleFeaturePermissionId(userRoleFeaturePermissionId);
				userRoleFeaturePermissionMapping.setUserAreaMapping(userAreaMapping);
				userRoleFeaturePermissionMapping.setRoleFeaturePermissionScheme(roleFeaturePermissionScheme);
				
				userRoleFeaturePermissionMappings.add(userRoleFeaturePermissionMapping);
			}

		}

		Sheet writeUserRoleFeaturePermissionSheet = workbook.getSheetAt(3);
		rowNum = 0;
		
		row = writeUserRoleFeaturePermissionSheet.createRow(rowNum++);
		colNum = 0;
		
		headCell = row.createCell(colNum);
		headCell.setCellValue("UserRoleFeaturePermissionId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("RoleFeaturePermissionSchemeId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("UserAreaMappingId");
		
		colNum++;
		headCell = row.createCell(colNum);
		headCell.setCellValue("Script");

		for (UserRoleFeaturePermissionMapping urm : userRoleFeaturePermissionMappings) {

			row = writeUserRoleFeaturePermissionSheet.createRow(rowNum++);

			colNum = 0;

			Cell cell = row.createCell(colNum);
			cell.setCellValue(urm.getUserRoleFeaturePermissionId());

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(urm.getRoleFeaturePermissionScheme().getRoleFeaturePermissionSchemeId());

			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue(urm.getUserAreaMapping().getUserAreaMappingId());
			
			colNum++;
			cell = row.createCell(colNum);
			cell.setCellValue("INSERT INTO public.user_role_feature_permission(user_role_feature_permission_id, role_feature_permission_scheme_id,user_area_mapping_id_fk)VALUES ("+
					urm.getUserRoleFeaturePermissionId()+", "+urm.getRoleFeaturePermissionScheme().getRoleFeaturePermissionSchemeId()+", "+urm.getUserAreaMapping().getUserAreaMappingId()+");");
		}
		FileOutputStream fileOut = new FileOutputStream(new File(fileOutName));
		workbook.write(fileOut);
		workbook.close();

		return "done";
	}

}
