package org.sdrc.scsl.core;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scsl.domain.UserLoginMeta;
import org.sdrc.scsl.exception.UnauthorizedException;
import org.sdrc.scsl.model.web.FeaturePermissionMappingModel;
import org.sdrc.scsl.model.web.UserAreaModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.service.UserService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * 			This is an intercepter gets called whenever a request hits
 * 			A 401 Unauthorized response
 *         	should be used for missing or bad authentication, and a 403 Forbidden
 *         	response should be used afterwards, when the user is authenticated
 *         	but isn’t authorized to perform the requested operation on the given
 *         	resource
 */
@Component
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {
	
	private final StateManager stateManager;
	private final ResourceBundleMessageSource errorMessageSource;
	
//	@Autowired
//	public AuthorizeInterceptor() {
//	}
	@Autowired
	public AuthorizeInterceptor(StateManager stateManager, ResourceBundleMessageSource errorMessageSource) {
		this.stateManager = stateManager;
		this.errorMessageSource = errorMessageSource;
	}


	@Autowired
	private UserService userService;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 * @Sarita
	 * This is pre handler method returns true if authorization is valid else throws appropriate exception
	 * This method is used to intercept the request before it’s handed over to the handler method. 
	 * This method should return ‘true’ to let Spring know to process the request through another spring interceptor or to send it to handler method if there are no further spring interceptors.
	 *	If this method returns ‘false’ Spring framework assumes that request has been handled by the spring interceptor 
	 *	itself and no further processing is needed. We should use response object to send response to the client request in this case.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		UserModel user = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		
		//a referer will be present only if it has come from the application and request from a browser
		//restrict resources access in url
		if(request.getHeader("referer")==null && (request.getRequestURI().contains("resources") || request.getRequestURI().contains("webjars"))){
			throw new AccessDeniedException(errorMessageSource.getMessage(Constants.Web.ACCESS_DENIED, null, null));
		}
		
		//set HttpOnly in cookie
		//is an additional flag included in a Set-Cookie HTTP response header. 
		//Using the HttpOnly flag when generating a cookie helps mitigate the risk of client side script accessing the protected cookie (if the browser supports it).
		Cookie cookie = new Cookie("timestamp", Long.toString(new Date().getTime()));
        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
        response.addCookie(cookie);
		
		if (!(handler instanceof HandlerMethod)) {

			// if at any point of time when two same user tries to login into the system
			// then an error message will be shown as "The user is already logged in
			// from a different system Logout to continue"
			// when s(he) will click on this logout link it will log out the other
			// user also.
			// any further action of the logged out user will invalidate the session
			if (user != null) {
				UserLoginMeta sameUserLoginMeta = userService.findByMstUserUserIdAndUserLogInMetaId(user.getUserId(), user.getUserLoginMetaId());
				//if user is not null but it is logged out then we will invalidate the session another user has logged it out
				if (!sameUserLoginMeta.isLoggedIn()) {
					invalidateSession(request);
					response.setStatus(401);
					throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
				}

			}
			return true;
		}

		Authorize authorize = ((HandlerMethod) handler).getMethodAnnotation(Authorize.class);

		if (authorize == null) {
			// if at any point of time when two same user tries to log in the system
			// then an error message will be shown as "The user is already logged in
			// from a different system Logout to continue"
			// when s(he) will click on this logout it will log out the other
			// user also.
			// any further action will invalidate the session
			if (user != null) {
				UserLoginMeta sameUserLoginMeta = userService.findByMstUserUserIdAndUserLogInMetaId(user.getUserId(), user.getUserLoginMetaId());
				//if user is not null but it is logged out then we will invalidate the session another user has logged it out
				if (!sameUserLoginMeta.isLoggedIn()) {
					invalidateSession(request);
					response.setStatus(401);
					throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
				}

			}
			return true;
		}

		if (user == null) {
			response.setStatus(401);
			throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
		}
		//when a user log out another user,  stateManager is still present, but loginmeta is null, then also invalidate the session
		UserLoginMeta sameUserLoginMeta = userService.findByMstUserUserIdAndUserLogInMetaId(user.getUserId(), user.getUserLoginMetaId());
		//if user is not null but it is logged out then we will invalidate the session another user has logged it out
		if (!sameUserLoginMeta.isLoggedIn()) {
			invalidateSession(request);
			response.setStatus(401);
			throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
		}
		//get all features and permissions from authorize and compare with user's attached feature and permission
		List<String> feature = Arrays.asList(authorize.feature().split(","));
		String permission = authorize.permission();

		List<UserAreaModel> userAreaModels = user.getUserAreaModels() ;

		//when handler contains the desired feature permission, this will return true else an access denied exception will be thrown
		if (null != userAreaModels) {
			for (UserAreaModel userAreaModel : userAreaModels) {
				if (userAreaModel.getUserRoleFeaturePermissionMappings() != null) {
					for (int i = 0; i < userAreaModel.getUserRoleFeaturePermissionMappings().size(); i++) {
						FeaturePermissionMappingModel fpMapping = userAreaModel.getUserRoleFeaturePermissionMappings()
								.get(i).getRoleFeaturePermissionSchemeModel().getFeaturePermissionMapping();
						if (feature.contains(fpMapping.getFeature().getFeatureName())
								&& permission.equals(fpMapping.getPermission().getPermissionName())) {
							return true;
						}
					}

				}
			}
		}
		response.setStatus(403);
		throw new AccessDeniedException(errorMessageSource.getMessage(Constants.Web.ACCESS_DENIED, null, null));
	}

	/**
	 * @param request
	 * invalidate a session
	 */
	private void invalidateSession(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.Web.USER_PRINCIPAL, null);
		request.getSession().invalidate();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		attr.getRequest().getSession(true).removeAttribute(Constants.Web.USER_PRINCIPAL);
		attr.getRequest().getSession(true).invalidate();
	}
	
	 /* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 * This HandlerInterceptor interceptor method is called when HandlerAdapter has invoked the handler but DispatcherServlet is yet to render the view.
	 * This method can be used to add additional attribute 
	 * to the ModelAndView object to be used in the view pages.
	 */
	@Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	        response.setHeader("Strict-Transport-Security","max-age=31536000 ; includeSubDomains");
	        response.setHeader("X-Content-Type-Options", "nosniff");
	        response.setHeader("X-Frame-Options", "DENY");
	        response.setHeader("X-XSS-Protection", "1; mode=block");
//	        response.setHeader("Content-Security-Policy", "default-src 'self'");

	        super.postHandle(request, response, handler, modelAndView);
	    }

}
