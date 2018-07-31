package org.sdrc.scsl.util;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Scope(value = "singleton")
public class SessionStateManager implements StateManager, ApplicationListener<ApplicationEvent> {

	@Autowired 
	private UserService userService;
	
	@Autowired
	private ResourceBundleMessageSource messages;
	
	public SessionStateManager() {
	}

	@Override
	public Object getValue(String key) {
		return session().getAttribute(key);
	}

	@Override
	public void setValue(String key, Object value) {
		session().setAttribute(key, value);
	}

	private HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	/**
	 * This method will update the login meta data on every destruction of session
	 * Means when web.xml will destroy the session then also this method will be called
	 * OR
	 * When the server will startup to set all the login metadata to false or both case
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// if session is destroying
		if(event instanceof HttpSessionDestroyedEvent)
		{
			UserModel user=(UserModel)(((HttpSessionDestroyedEvent) event).getSession().getAttribute(Constants.Web.USER_PRINCIPAL));
		if(user!=null)
		{
			userService.updateLoggedOutStatus(user.getUserLoginMetaId(), new Timestamp(new Date().getTime()));
		}
		}
		// if server is starting up
		else if(event instanceof ContextRefreshedEvent )
		{
			userService.updateLoggedOutStatus(-1, new Timestamp(new Date().getTime()));
			
			//create directories for files uploaded from historical data module. both submitted and rejected ones will be stored
			File legacyFilesUploadedDirectory =  new File(messages.getMessage(Constants.Web.HISTORICAL_FILE_UPLOADED_PATH, null, null)+Constants.Web.HISTORICAL_FILE_UPLOADED);
			File legacyFilesRejectedDirectory =  new File(messages.getMessage(Constants.Web.HISTORICAL_FILE_UPLOADED_PATH, null, null)+Constants.Web.HISTORICAL_FILE_REJECTED);

			if (!legacyFilesUploadedDirectory.exists()) {
				legacyFilesUploadedDirectory.mkdirs();
			}
			if (!legacyFilesRejectedDirectory.exists()) {
				legacyFilesRejectedDirectory.mkdirs();
			}
			
		}
		
	}
}
