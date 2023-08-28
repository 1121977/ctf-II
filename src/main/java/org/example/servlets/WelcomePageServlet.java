package org.example.servlets;

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

public class WelcomePageServlet extends CtfHttpServlet {
    public WelcomePageServlet(TemplateProcessor templateProcessor, PirateDAO pirateDAO){
        super(templateProcessor, pirateDAO);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null){
            Map<String, Object> hash = new HashMap<>();
            response.getWriter().print(templateProcessor.getPage("index.html", hash));
        } else {
            response.sendRedirect("/app/login");
        }
    }

}
