package org.example.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.PirateDAO;
import org.example.services.HashGenerator;
import org.example.services.TemplateProcessor;

import java.io.IOException;

public class FlagFromFlagHashServlet extends CtfHttpServlet{
    public FlagFromFlagHashServlet(TemplateProcessor templateProcessor, PirateDAO pirateDAO) {
        super(templateProcessor, pirateDAO);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        String flagHash = request.getParameterMap().get("hash_flag_field")[0];
        try {
            response.getOutputStream().print(pirateDAO.findFlagByFlagHash(flagHash));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
