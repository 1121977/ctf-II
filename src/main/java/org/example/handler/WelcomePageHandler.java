package org.example.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.example.dao.PirateDAO;
import org.example.model.Pirate;
import org.example.services.TemplateProcessor;
import org.example.services.URIGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomePageHandler extends AbstractHandler {
    private TemplateProcessor templateProcessor;
    private PirateDAO pirateDAO;
    public WelcomePageHandler(TemplateProcessor templateProcessor, PirateDAO pirateDAO){
        super();
        this.templateProcessor = templateProcessor;
        this.pirateDAO = pirateDAO;
    }
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameterMap().isEmpty() && target.equals("/")){
            List<Pirate> pirateList = pirateDAO.findAll();
            baseRequest.setHandled(true);
            String hashCode = URIGenerator.hashFrom(pirateList);
            Map<String, Object> hash = new HashMap<>();
            hash.put("hashCode", hashCode);
            response.getWriter().print(templateProcessor.getPage("index.html", hash));
        }
    }
}
