package org.example.dao;

import org.example.model.Pirate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PirateDAOImpl extends DAOImpl<Pirate> implements PirateDAO
{
    public PirateDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Pirate.class);
    }

    @Override
    public Optional<Pirate> findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        var query = session.createQuery("select p from " + entityClass.getName() + " p where p.login = :login", entityClass).setParameter("login", login);
        Pirate pirate = query.getSingleResult();
        tx.commit();
        session.close();
        return Optional.of(pirate);
    }
}
