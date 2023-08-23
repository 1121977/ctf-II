package org.example.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.dao.PirateDAO;
import org.example.services.AuthService;
import org.example.services.TemplateProcessor;
import org.example.servlets.CrewListServlet;
import org.example.servlets.LoginServlet;
import org.example.servlets.WelcomePageServlet;

public class CrewJournalServer {

//    private static final String START_PAGE_NAME = "index.html";
//    private static final String COMMON_RESOURCES_DIR = "static";

    private final Server server;
    private final PirateDAO pirateDAO;
    private final TemplateProcessor templateProcessor;
    private final AuthService authService;

    public CrewJournalServer(int webServerPort, PirateDAO pirateDAO, TemplateProcessor templateProcessor, AuthService authService) {
        this.pirateDAO = pirateDAO;
        this.server = new Server(webServerPort);
        this.templateProcessor = templateProcessor;
        this.authService = authService;
    }

    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    public void join() {
        try {
            server.join();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initContext(){
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/app");
        ServletHolder welcomePageServletHolder = new ServletHolder(new WelcomePageServlet(templateProcessor,pirateDAO));
        servletContextHandler.addServlet(welcomePageServletHolder, "/welcome");
        ServletHolder crewListServletHolder = new ServletHolder(new CrewListServlet(templateProcessor, pirateDAO));
        servletContextHandler.addServlet(crewListServletHolder, "/crewList");
        ServletHolder loginServletHolder = new ServletHolder(new LoginServlet(templateProcessor, pirateDAO, authService));
        servletContextHandler.addServlet(loginServletHolder, "/login");
        ServletHolder defaultServletHolder = servletContextHandler.addServlet(DefaultServlet.class, "/");
        defaultServletHolder.setInitParameter("resourceBase", "src/main/resources/web/static");
//        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));
        server.setHandler(servletContextHandler);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
//        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase("src/main/resources/web");
        return resourceHandler;
    }
}
