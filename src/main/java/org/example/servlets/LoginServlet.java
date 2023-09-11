package org.example.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dao.PirateDAO;
import org.example.model.Pirate;
import org.example.server.CrewJournalServer;
import org.example.services.AuthService;
import org.example.services.HashGenerator;
import org.example.services.TemplateProcessor;
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
        CrewJournalServer.logger.info("from {} requested {}", request.getRemoteAddr(), request.getRequestURI());
        var map  =  request.getParameterMap();
//        if (authService.authenticate(map.get("login_field")[0], map.get("password_field")[0])) {
        var pirates = pirateDAO.findAll();
        for (Pirate pirate: pirates) {
            if (map.get("sha256_field")[0].equals(HashGenerator.hashFrom(pirate.getLogin() + pirate.getPassword()))) {
//            String hash = HashGenerator.hashFrom(map.get("login_field")[0] + map.get("password_field")[0]);
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
//            Pirate pirate = pirateDAO.findByLogin(map.get("login_field")[0]).get();
                pirate.setSessionID(session.getId());
                pirateDAO.update(pirate);
                response.sendRedirect("/app/welcome");
                CrewJournalServer.logger.info("from {} requested {} with successful authentication", request.getRemoteAddr(), request.getRequestURI());
                return;
            }
        }
        response.setStatus(SC_UNAUTHORIZED);
        CrewJournalServer.logger.info("from {} requested {} with unsuccessful authentication", request.getRemoteAddr(), request.getRequestURI());
        response.sendRedirect("/app/login");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Pirate> pirateList = pirateDAO.findAll();
        Map<String, Object> hash = new HashMap<>();
        hash.put("pirateList", pirateList);
        response.getWriter().print(templateProcessor.getPage("login.html", hash));
    }
}
