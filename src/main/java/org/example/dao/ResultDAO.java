package org.example.dao;

import org.example.testservice.FindNumberRequest;
import org.example.testservice.Result;

public interface ResultDAO {

    void saveResult(FindNumberRequest request, Result result);
}
