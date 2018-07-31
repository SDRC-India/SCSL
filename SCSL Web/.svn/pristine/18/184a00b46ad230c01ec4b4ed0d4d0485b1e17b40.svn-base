package org.sdrc.scsl.web.controller;

import org.sdrc.scsl.model.mobile.LoginDataModel;
import org.sdrc.scsl.model.mobile.MasterDataModel;
import org.sdrc.scsl.model.mobile.SyncModel;
import org.sdrc.scsl.model.mobile.SyncResult;
import org.sdrc.scsl.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The following controller class will deal with all the request (login and sync) that comes from mobile device
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 3:51:47 pm
 */
@RestController
public class MobileController {
	
	@Autowired
	private MobileService mobileService;

	/**
	 * When user try to login, the following method will deal with that functionality
	 * @param LoginDataModel user information that have come from mobile
	 * @since 1.0.0 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:46:01 pm
	 */
	@RequestMapping( value = "login", method = RequestMethod.POST)
	public MasterDataModel login(@RequestBody LoginDataModel loginDataModel){
		return mobileService.getMasterData(loginDataModel);
	}
	
	/**
	 * When mobile device will send transaction data to server, following method is going to deal with it
	 * @param SyncModel data that have come from mobile.
	 * @since 1.0.0 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:36:14 pm
	 */
	@RequestMapping( value = "sync", method = RequestMethod.POST)
	public SyncResult sync(@RequestBody SyncModel syncModel){		
		return mobileService.sync(syncModel);
	}
}