package service;

import models.Test;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class TestService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    // ✅ Save new test
    public Test addTest(Test test) {
        em.persist(test);
        return test;
    }

    // ✅ Get all tests
    public List<Test> getAllTests() {
        return em.createQuery("SELECT t FROM Test t", Test.class).getResultList();
    }
}
