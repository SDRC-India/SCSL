package org.sdrc.scsl.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sdrc.scsl.exception.UnauthorizedException;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.service.UserService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.DuplicateLoginUserException;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * @author Subrata
 * @author Sarita
 * @since version 1.0.0.0
 * handles Login /logout functionalities
 */

@Controller
public class LoginController implements AuthenticationProvider{
	
	@Autowired
	private ResourceBundleMessageSource errorMessageSource;
	
	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;
	
	private final StateManager stateManager;
	
	@Autowired
	public LoginController(StateManager stateManager){
		this.stateManager = stateManager;
	}
	
	@Autowired
	private UserService userService;
	
	/**
	 * @param isLoggedIn
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String getHomePage(@RequestParam(value="isLoggedIn", required=false) Boolean isLoggedIn,
			RedirectAttributes redirectAttributes,Model model){
		
		if(null!=isLoggedIn && isLoggedIn){
			List<String> errMessgs = new ArrayList<>();
			errMessgs.add(errorMessageSource.getMessage(Constants.Web.SUCCESS_LOGGED_OUT, null, null));
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",errorMessageSource.getMessage("bootstrap.alert.success",null, null));
		}
		return "redirect:/";
		
	}
	
	/**
	 * @param request
	 * @param redirectAttributes
	 * @param username
	 * @param password
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/webLogin", method = RequestMethod.POST)
	public String authorize(HttpServletRequest request, 
							RedirectAttributes redirectAttributes,
							@RequestParam("username") String username,
							@RequestParam("password") String password,
							Model model) throws IOException{
		List<String> errMessgs = new ArrayList<String>();
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = this.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			errMessgs.add(errorMessageSource.getMessage(Constants.Web.INVALID_USERNAME_PASSWORD, null, null));
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",errorMessageSource.getMessage("bootstrap.alert.danger",null, null));
			return "redirect:/";
		} catch (DuplicateLoginUserException e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			errMessgs.add(e.getMessage());
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",errorMessageSource.getMessage("bootstrap.alert.danger",null, null));
			return "redirect:/";
		}
		model.addAttribute("userDetail",((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)));
		return "redirect:/";
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String encodedPassword = passwordEncoder.encodePassword(authentication.getName(),(String)authentication.getCredentials());
		UserModel userModel = userService.findByUsernameAndPassword(authentication.getName(), encodedPassword); 
		if (userModel == null ||!userModel.getPassword().equals(encodedPassword)) 
			throw new BadCredentialsException("Invalid User!");
		
		//if user is already logged-in in any other system, then restrict
		
		if(null!= userService.findActiveUserLoginMeta(userModel.getUserId()))
			throw new DuplicateLoginUserException(
					"The user is already logged in from a different system ! <a href='webLogoutOfDiffSession?userId="
							+userModel.getUserId() + "'>Logout</a> to continue.");
		
		stateManager.setValue(Constants.Web.USER_PRINCIPAL, userModel);
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		String ipAddress = getIpAddr(request);
		String userAgent = request.getHeader("User-Agent");
		long loginMetaId = userService.saveUserLoginMeta(ipAddress, userModel.getUserId(), userAgent);
		stateManager.setValue(Constants.Web.LOGIN_META_ID, loginMetaId);
		userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		userModel.setUserLoginMetaId(loginMetaId);
		stateManager.setValue(Constants.Web.USER_PRINCIPAL, userModel);
		
		return new UsernamePasswordAuthenticationToken(authentication.getName(), (String)authentication.getCredentials(), null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
	
	/**
	 * @param request
	 * @param resp
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping(value = "/webLogout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException {
		
		HttpSession session=request.getSession(false);
		if(session !=null){
			long userLoginMetaId = (long) stateManager.getValue(Constants.Web.LOGIN_META_ID);
			userService.updateLoggedOutStatus(userLoginMetaId,  new Timestamp(new Date().getTime()));
			stateManager.setValue(Constants.Web.USER_PRINCIPAL, null);
			request.getSession().setAttribute(Constants.Web.USER_PRINCIPAL, null);
			request.getSession().invalidate();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes();
			attr.getRequest().getSession(true)
					.removeAttribute(Constants.Web.USER_PRINCIPAL);
			attr.getRequest().getSession(true).invalidate();
	
			//request.logout();
	
			return "redirect:/home?isLoggedIn="+true;
		}
		else{
			request.getSession().invalidate();
			return "redirect:/";
		}
	}
	
	/**
	 * @param request
	 * @param resp
	 * @param redirectAttributes
	 * @param userId
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/webLogoutOfDiffSession", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse resp, RedirectAttributes redirectAttributes, 
			@RequestParam("userId") Integer userId)
			throws IOException, ServletException {
		
		HttpSession session=request.getSession(false);
		if(session !=null){
			Long userLoginMetaId = userService.findByUserId(userId);
			userService.updateLoggedOutStatus(userLoginMetaId,  new Timestamp(new Date().getTime()));
			stateManager.setValue(Constants.Web.USER_PRINCIPAL, null);
	
			List<String> errMessgs = new ArrayList<>();
			errMessgs.add(errorMessageSource.getMessage(Constants.Web.SUCCESS_LOGGED_OUT, null, null));
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",errorMessageSource.getMessage("bootstrap.alert.success",null, null));
			return "redirect:/home?isLoggedIn="+true;
		}
		else{
			request.getSession().invalidate();
			return "redirect:/";
		}
	}
	
	/**
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {      
		   String remoteAddr = request.getHeader("X-FORWARDED-FOR");      
		   
		   if (remoteAddr == null || "".equals(remoteAddr)) {
               remoteAddr = request.getRemoteAddr();
           }
		   return remoteAddr;      
		}
	/**
	 * @return
	 */
	@RequestMapping("/authException")
	public String getauthExceptionPage(){
		throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
	}
	
	@RequestMapping("/changePassword")
	public String getChangePasswordPage(){
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		if(userModel!=null)
			return "changePassword";
		else
			return "home";
	}
	
	/**
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/updateALoggedInUserPassword")
	@ResponseBody
	public boolean updateALoggedInUserPassword(@RequestParam("newPassword") String newPassword){
		
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		if(userModel!=null)
			return userService.updateByUserId(userModel.getUserId(), newPassword);
		else
			throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
	}
	
	/**
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/checkCurrentPassword")
	@ResponseBody
	public boolean checkCurrentPassword(@RequestParam("currentPassword") String newPassword){
		
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		if(userModel!=null)
			return userService.checkCurrentPassword(userModel.getUserId(), newPassword);
		else
			throw new UnauthorizedException(new Date(), errorMessageSource.getMessage(Constants.Web.UNAUTHORIZED, null, null));
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ModelAndView handleError1(HttpServletRequest request, UnauthorizedException e) {
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("exception", e);
	    modelAndView.addObject("url", request.getRequestURL());
	    
	    modelAndView.setViewName("accessDeniedException");
	    return modelAndView;
		
	}
	
	/*@RequestMapping("/getPw")
	@ResponseBody
	public String getPw(){
		 userService.getPw();
		 return "check";
	}*/
	
/*	@RequestMapping("/getUATPw")
	@ResponseBody
	public String getUATPw(){
		 userService.getUATPw();
		 return "check";
	}*/
	
	/*@RequestMapping("/userCreate")
	@ResponseBody
	public String createUsers() throws IOException{
		return userService.createUsers();
	}*/
	
/*	@RequestMapping("/updatePwsAP")
	@ResponseBody
	public String updatePw(){
		return userService.updatePw();
	}*/
	
/*	@RequestMapping("/updateAnUserPassword")
	@ResponseBody
	public boolean updateAnUserPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password){
		return userService.updateByUserId(userId, password);
	}*/
	
	
}
