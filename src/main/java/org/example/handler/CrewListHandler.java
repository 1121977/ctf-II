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

public class CrewListHandler extends AbstractHandler {

    TemplateProcessor templateProcessor;
    PirateDAO pirateDAO;

    public CrewListHandler(TemplateProcessor templateProcessor, PirateDAO pirateDAO) {
        super();
        this.templateProcessor = templateProcessor;
        this.pirateDAO = pirateDAO;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Pirate> pirateList = pirateDAO.findAll();
        var parameters = baseRequest.getParameterMap();
        for (Map.Entry<String, String[]> parameter : parameters.entrySet()) {
            if (parameter.getKey().equals("hashCode") && parameter.getValue()[0].equals(URIGenerator.hashFrom(pirateList))) {
                baseRequest.setHandled(true);
                Map<String, Object> hash = new HashMap<>();
                hash.put("pirateList", pirateList);
                response.getWriter().print(templateProcessor.getPage("crewList.html", hash));
                break;
            }
        }
    }
}
