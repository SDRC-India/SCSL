package org.sdrc.scsl.service;

import java.io.IOException;
import java.sql.Timestamp;

import org.sdrc.scsl.domain.UserLoginMeta;
import org.sdrc.scsl.model.web.UserModel;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
* this is a service holds all the methods related to user / login
*
*/
public interface UserService {

	/**
	 * @param username
	 * @param password
	 * @return
	 * find user by username and password
	 */
	UserModel findByUsernameAndPassword(String username, String password);

	/**
	 * @param ipAddress
	 * @param userId
	 * @param userAgent
	 * @return
	 * save user login meta of a logged in user
	 */
	long saveUserLoginMeta(String ipAddress, Integer userId, String userAgent);

	/**
	 * @param userLoginMetaId
	 * @param loggedOutDateTime
	 * update logout time in login meta after logout
	 */
	void updateLoggedOutStatus(long userLoginMetaId, Timestamp loggedOutDateTime);

	/**
	 * @param userId
	 * @return
	 * get active user login meta
	 */
	UserLoginMeta findActiveUserLoginMeta(Integer userId);

//	String updatePw();

	/**
	 * @param userId
	 * @return
	 */
	Long findByUserId(Integer userId);

	/**
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	boolean updateByUserId(Integer userId, String newPassword);

	/**
	 * @param userId
	 * @param currentPassword
	 * @return
	 */
	Boolean checkCurrentPassword(Integer userId, String currentPassword);

//	void getPw();

	UserLoginMeta findByMstUserUserIdAndUserLogInMetaId(Integer userId, long userLogInMetaId);

//	String createUsers() throws IOException;

//	void getUATPw();


}
