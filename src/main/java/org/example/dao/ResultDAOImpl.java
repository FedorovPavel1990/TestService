package org.example.dao;

import org.example.entity.TestServiceResult;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.Result;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ResultDAOImpl implements ResultDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveResult(FindNumberRequest request, Result result) {
        TestServiceResult testServiceResult = new TestServiceResult(result.getCode(),
                request.getN(),
                result.getFileNames().toString(),
                result.getError());

        sessionFactory.getCurrentSession().save(testServiceResult);
    }
}
