package org.example.dao;

import org.example.entity.TestServiceResult;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

public class ResultDAOImpl implements ResultDAO {

    @Override
    public void saveResult(FindNumberRequest request, Result result) {
        TestServiceResult testServiceResult = new TestServiceResult(result.getCode(),
                request.getN(),
                result.getFileNames().toString(),
                result.getError());

        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            session.save(testServiceResult);
            t.commit();
            session.close();
        }
    }
}
