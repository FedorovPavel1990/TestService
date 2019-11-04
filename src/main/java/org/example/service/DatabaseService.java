package org.example.service;

import org.example.dao.ResultDAO;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    @Autowired
    private ResultDAO resultDAO;

    @Transactional
    public void addResultInDB(FindNumberRequest request, FindNumberResponse response) {
        resultDAO.saveResult(request, response.getResult());
    }
}
