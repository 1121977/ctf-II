package org.example;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.dao.DAO;
import org.example.dao.DAOImpl;
import org.example.dao.PirateDAO;
import org.example.dao.PirateDAOImpl;
import org.example.model.Pirate;
import org.example.server.ScrewJournalServer;
import org.h2.tools.Csv;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class ScrewJournal {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";



    public static void main(String[] args) {

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
                pirateList.forEach(pirate -> pirate.setId(pirateList.indexOf(pirate)));
                pirateList.forEach(pirateDAO::save);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        List<Pirate> pirateList = pirateDAO.findAll();
        System.out.println("Pirates in list are " + pirateList.size());

        pirateList.stream().forEach(System.out::println);

        ScrewJournalServer screwJournalServer = new ScrewJournalServer(WEB_SERVER_PORT, pirateDAO,
                null, null);

/*        screwJournalServer.start();
        screwJournalServer.join();*/
        System.out.println("Hello, CTF!");

    }
}
