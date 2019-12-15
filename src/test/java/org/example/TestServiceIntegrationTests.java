package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.AsyncService;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.NumberFinder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestServiceIntegrationTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);
//----------------------------------------------------------------------------------
//    @Autowired
//    private TestServiceLogic logic;
//
//    private File testFolder;
//
//    @BeforeEach
//    public void createTempFolder() throws IOException {
//        testFolder = Files.createTempDirectory("tmpTestFiles").toFile();
//        testFolder.deleteOnExit();
//
//        for (int i = 0; i < 5; i++) {
//            File file = Files.createFile(Paths.get(testFolder.getAbsolutePath() + "/testFile" + (i + 1))).toFile();
//            file.deleteOnExit();
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//                for (int j = 0; j < 100; j++) {
//                    writer.write((new Random().nextInt(99999) - j * 1000) + ",");
//                }
//                writer.write(String.valueOf(100001 + i));
//            }
//        }
//
//        logic.setFolder(testFolder.getAbsolutePath());
//    }
//
//    @AfterEach
//    public void deleteTempFolder() throws IOException {
//        if (testFolder != null) {
//            File[] filesInTempFolder = testFolder.listFiles();
//            for (File file : filesInTempFolder) {
//                Files.delete(file.toPath());
//            }
//            Files.delete(testFolder.toPath());
//        }
//    }
//
//
//    @Test
//    public void test_TestServiceLogic_OK() {
//        FindNumberRequest request = new FindNumberRequest();
//        request.setN(100005);
//        FindNumberResponse response = logic.findNumber(request);
//        Result result = response.getResult();
//
//        String expectedCode = ResultCodes.FindNumber_00.getCode();
//        List<String> expectedFileNamesList = Collections.singletonList("testFile5");
//
//        Assert.assertEquals(result.getCode(), expectedCode);
//        Assert.assertThat(result.getFileNames(), is(expectedFileNamesList));
//    }
//----------------------------------------------------------------------------------
    @Autowired
    private AsyncService asyncService;

    @Test
    public void test_TestAsyncService() throws IOException, ExecutionException, InterruptedException {
        String str = "123,1231,23,124,124,12,41,24,12,4,12,41,24,124";




        NumberFinder numberFinder = mock(NumberFinder.class);
        when(numberFinder.findNumberInFile(new File("Test"), 1)).thenReturn(true);
        boolean actual = asyncService.asyncFindNumberInFile(new File("Test"),1, new ArrayList<>()).get();
        Assert.assertTrue("AsyncService - FAILED", actual);
    }
}
