package org.example.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.example.dao.PirateDAO;
import org.example.handler.CrewListHandler;
import org.example.handler.WelcomePageHandler;
import org.example.services.TemplateProcessor;

public class CrewJournalServer {

//    private static final String START_PAGE_NAME = "index.html";
//    private static final String COMMON_RESOURCES_DIR = "static";

    private final Server server;
    private final PirateDAO pirateDAO;
    private final TemplateProcessor templateProcessor;

    public CrewJournalServer(int webServerPort, PirateDAO pirateDAO, TemplateProcessor templateProcessor) {
        this.pirateDAO = pirateDAO;
        this.server = new Server(webServerPort);
        this.templateProcessor = templateProcessor;
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
        ResourceHandler resourceHandler = createResourceHandler();
//        ServletContextHandler servletContextHandler = createServletContextHandler();
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new WelcomePageHandler(templateProcessor, pirateDAO));
        handlers.addHandler(new CrewListHandler(templateProcessor, pirateDAO));
        handlers.addHandler(resourceHandler);
//        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));
        server.setHandler(handlers);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
//        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase("src/main/resources/web");
        return resourceHandler;
    }
}
