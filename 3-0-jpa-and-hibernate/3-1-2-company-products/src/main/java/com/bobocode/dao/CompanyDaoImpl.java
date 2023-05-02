package com.bobocode.dao;

import com.bobocode.model.Company;
import com.bobocode.util.ExerciseNotCompletedException;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CompanyDaoImpl implements CompanyDao {
    private EntityManagerFactory entityManagerFactory;

    public CompanyDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Company findByIdFetchProducts(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.unwrap(Session.class).setDefaultReadOnly(true);
        return em.find(Company.class, id);
    }
}
