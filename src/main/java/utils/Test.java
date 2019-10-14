package utils;

import org.example.service.TestServiceEndpoint;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;

import java.util.Date;


public class Test {
    public static void main(String[] args) {
        FindNumberRequest request = new FindNumberRequest();
        request.setN(2365);
        TestServiceEndpoint endpoint = new TestServiceEndpoint();
        long startTime = new Date().getTime();
        FindNumberResponse response = endpoint.findNumber(request);
        long endTime = new Date().getTime();
        System.out.println("Ответ получен за " + (endTime - startTime) + " мс");
        System.out.println("code = " + response.getResult().getCode());
        System.out.println("error = " + response.getResult().getError());
        System.out.println("fileNames = " + response.getResult().getFileNames());
    }
}
