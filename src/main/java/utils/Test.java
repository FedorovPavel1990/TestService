package utils;

import org.example.service.TestServiceLogic;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;

import java.util.Date;


public class Test {
    public static void main(String[] args) {
        testServiceTest();
    }

    private static void testServiceTest() {
        TestServiceLogic testServiceLogic = new TestServiceLogic();
        FindNumberRequest request = new FindNumberRequest();
        request.setN(12);
        long startTime = new Date().getTime();
        FindNumberResponse response = testServiceLogic.findNumber(request);
        long endTime = new Date().getTime();
        System.out.println("Ответ получен за " + (endTime - startTime) + " мс");
        System.out.println("code = " + response.getResult().getCode());
        System.out.println("error = " + response.getResult().getError());
        System.out.println("fileNames = " + response.getResult().getFileNames());
    }
}
