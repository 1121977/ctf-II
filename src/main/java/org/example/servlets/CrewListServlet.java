package org.example.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.CrewJournal;
import org.example.dao.PirateDAO;
import org.example.model.Pirate;
import org.example.services.MailConfirmation;
import org.example.services.TemplateProcessor;
import org.example.services.HashGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrewListServlet extends CtfHttpServlet {

    MailConfirmation mailConfirmation;
    public CrewListServlet(TemplateProcessor templateProcessor, PirateDAO pirateDAO, MailConfirmation mailConfirmation) {
        super(templateProcessor, pirateDAO);
        this.mailConfirmation = mailConfirmation;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            List<Pirate> pirateList = pirateDAO.findAll();
            for(Pirate pirate : pirateList){
                if(pirate.getSessionID() == null || !pirate.getSessionID().equals(httpSession.getId())){
                    pirate.setSessionID("");
                }
            }
            Map<String, Object> hash = new HashMap<>();
            hash.put("pirateList", pirateList);
            response.getWriter().print(templateProcessor.getPage("crewList.html", hash));
        } else {
            response.sendRedirect("/app/login");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            var map = request.getParameterMap();
            List<Pirate> pirateList = pirateDAO.findAll();
            for (Pirate pirate : pirateList) {
                if (Long.parseLong(map.get("id_field")[0]) == pirate.getId()) {
                    pirate.setNewPassword(map.get("password_field1")[0]);
                    String passwordsHash = HashGenerator.hashFrom(pirate.getPassword() + pirate.getNewPassword());
                    pirate.setHashNewAndCurrentPassword(passwordsHash);
                    pirateDAO.update(pirate);
//                    var network  = NetworkInterface.getNetworkInterfaces();
                    String bodyText = "<p>Dear " + pirate.getName() + ", to complete your password changing, please go to this link: <a " + "href=\"http://"
                        + InetAddress.getLocalHost().getHostAddress() + stringRepresentateWebPort() + "/app/confirm?hash=" + passwordsHash + "\">Confirm</a></p>";
                    mailConfirmation.sendEmail(pirate.getEmail(), "renatka@mail.ru", "dPPpmKzay1R5tzA4XeUR", "Confirm password changing", bodyText);
                    break;
                }
            }
            response.sendRedirect("/app/crewList");
        } else {
            response.sendRedirect("/app/login");
        }
    }

    private String stringRepresentateWebPort(){
        return CrewJournal.getWebServerPort() == 80?"":":" + CrewJournal.getWebServerPort();
    }
}
