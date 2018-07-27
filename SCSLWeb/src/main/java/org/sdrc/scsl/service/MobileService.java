package org.sdrc.scsl.service;

import org.sdrc.scsl.model.mobile.LoginDataModel;
import org.sdrc.scsl.model.mobile.MasterDataModel;
import org.sdrc.scsl.model.mobile.SyncModel;
import org.sdrc.scsl.model.mobile.SyncResult;

/**
 * This interface has methods that is going to deal with mobile business logic 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:27:17 pm
 * @author subhadarshani Patra (subhadarshani@sdrc.co.in)
 */
public interface MobileService {

	/**
	 * This method is going to validate the login and give master data to user. 
	 * @param LoginDataModel user information that have come from mobile
	 * @since 1.0.0
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:29:31 pm
	 */
	MasterDataModel getMasterData(LoginDataModel loginDataModel);
	
	/**
	 * This method is going to deal with the transactional data that come from the mobile device and send the processed output
	 * @param SyncModel data that have come from mobile.
	 * @since 1.0.0 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:33:57 pm
	 * @author Subhadarshani patra (subhadarshani@sdrc.co.in)
	 */
	SyncResult sync(SyncModel syncModel);
}
