package org.example.dao;

import org.example.model.Pirate;
import org.hibernate.SessionFactory;

public class PirateDAOImpl extends DAOImpl<Pirate> implements PirateDAO
{
    public PirateDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Pirate.class);
    }
}
