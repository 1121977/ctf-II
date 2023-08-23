package org.example.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dao.PirateDAO;
import org.example.model.Pirate;
import org.example.services.AuthService;
import org.example.services.AuthServiceImpl;
import org.example.services.TemplateProcessor;
import org.example.services.URIGenerator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class LoginServlet extends CtfHttpServlet {

    final private int MAX_INACTIVE_INTERVAL = 600;
    final private AuthService authService;
    public LoginServlet(TemplateProcessor templateProcessor, PirateDAO pirateDAO, AuthService authService){
        super(templateProcessor, pirateDAO);
        this.authService = authService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var map  =  request.getParameterMap();
        if (authService.authenticate(map.get("login_field")[0], map.get("password_field")[0])) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/app/welcome");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        List<Pirate> pirateList = pirateDAO.findAll();
        Map<String, Object> hash = new HashMap<>();
        response.getWriter().print(templateProcessor.getPage("login.html", hash));
    }
}
