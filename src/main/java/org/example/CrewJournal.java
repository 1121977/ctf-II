package org.example;

import com.opencsv.bean.CsvToBeanBuilder;
import org.example.dao.PirateDAO;
import org.example.dao.PirateDAOImpl;
import org.example.model.Pirate;
import org.example.services.AuthService;
import org.example.services.AuthServiceImpl;
import org.example.services.TemplateProcessor;
import org.example.services.TemplateProcessorImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.example.server.*;
import java.io.FileReader;
import java.util.List;

public class CrewJournal {
    private static final int WEB_SERVER_PORT = 8080;
//    private static final String TEMPLATES_DIR = "/home/renatka/bankProject/CTFService/src/main/resources/web";
    private static final String TEMPLATES_DIR = "src/main/resources/web";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";



    public static void main(String[] args) throws Exception {

        String csvFileName = null;
        SessionFactory sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure(HIBERNATE_CFG_FILE).build())
                .addAnnotatedClass(Pirate.class).buildMetadata().buildSessionFactory();
        PirateDAO pirateDAO = new PirateDAOImpl(sessionFactory);
        for (int i=0; i<args.length; i++){
            if (args[i].equals("-csvInput") && i < args.length - 1) {
                csvFileName = args[i + 1];
                break;
            }
        }
        if(csvFileName!=null){
            try(FileReader fileReader = new FileReader(csvFileName)) {
                List<Pirate> pirateList = new CsvToBeanBuilder<Pirate>(fileReader)
                        .withType(Pirate.class)
                        .withSeparator(';')
                        .build().parse();
                pirateList.forEach(pirateDAO::save);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        List<Pirate> pirateList = pirateDAO.findAll();
        System.out.println("Pirates in list are " + pirateList.size());
        AuthService authService = new AuthServiceImpl(pirateDAO);

        CrewJournalServer crewJournalServer = new CrewJournalServer(WEB_SERVER_PORT, pirateDAO, templateProcessor, authService);
        crewJournalServer.start();
        crewJournalServer.join();

    }
}
