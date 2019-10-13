package utils;

import org.example.service.TestServiceEndpoint;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;


public class Test {
    public static void main(String[] args) {
        FindNumberRequest request = new FindNumberRequest();
        request.setN(265);
        TestServiceEndpoint endpoint = new TestServiceEndpoint();
        FindNumberResponse response = endpoint.findNumber(request);
        System.out.println("code = " + response.getResult().getCode());
        System.out.println("error = " + response.getResult().getError());
        System.out.println("fileNames = " + response.getResult().getFileNames());
    }
}
