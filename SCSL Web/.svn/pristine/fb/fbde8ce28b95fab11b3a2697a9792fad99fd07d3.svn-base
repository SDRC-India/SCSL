package org.sdrc.scsl.util;

import java.util.EnumSet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.annotation.WebListener;

/**
 * @author Sarita Panigrahi
 * @date 11-05-2018
 * This listener has been added to restrict jsession id in url
 *
 */
@WebListener
public class SessionTrackingModeSetter implements ServletContextListener {

    @Override
    public void contextInitialized (ServletContextEvent event) {
        event.getServletContext()
             .setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

    @Override
    public void contextDestroyed (ServletContextEvent sce) {
    }
}