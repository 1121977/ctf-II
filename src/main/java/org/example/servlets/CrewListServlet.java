package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.PirateDAO;
import org.example.model.Pirate;
import org.example.services.TemplateProcessor;
import org.example.services.URIGenerator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrewListServlet extends CtfHttpServlet {
    public CrewListServlet(TemplateProcessor templateProcessor, PirateDAO pirateDAO){
        super(templateProcessor, pirateDAO);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            var parameters = request.getParameterMap();
            for (Map.Entry<String, String[]> parameter : parameters.entrySet()) {
                if (parameter.getKey().equals("hashCode")) {
                    List<Pirate> pirateList = pirateDAO.findAll();
                    if(parameter.getValue()[0].equals(URIGenerator.hashFrom(pirateList))) {
                        Map<String, Object> hash = new HashMap<>();
                        hash.put("pirateList", pirateList);
                        response.getWriter().print(templateProcessor.getPage("crewList.html", hash));
                        break;
                    }
                }
            }
        } else {
            response.sendRedirect("/app/login");
        }
    }
}
