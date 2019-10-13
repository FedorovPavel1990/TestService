package utils;

import java.io.*;
import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Класс для создания заданного количества тестовых файлов по ~1 Гб в заданной папке репозитория.
 * В результате будут созданы файлы с именами testFile<N> (например, testFile1, testFile2 и т. д.), которые
 * содержат случайные числа, разделенные запятыми
 *
 * @autor Федоров Павел
 */
public class TestFilesGenerator {

    private static final Logger LOG = LogManager.getLogger(TestFilesGenerator.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * Папка в репозитории, где будут созданы тестовые файлы
     * */
    private static final String FOLDER = "tmpTestFiles";
    /**
     * Количество сгенерированных файлов
     */
    private static final int COUNT_OF_FILES = 20;

    public static void main(String[] args) {
        LOG.info("Start file generating");

        File folder = new File(FOLDER);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                LOG.info("Папка {} успешно создана", FOLDER);
            }
        }
        for (int i = 0; i < COUNT_OF_FILES; i++) {
            String filePath = FOLDER + FILE_SEPARATOR + "testFile" + (i + 1);
            File file = new File(filePath);
            try {
                if (file.createNewFile()) {
                    LOG.info("Файл {} успешно создан в папке {}", file.getName(), FOLDER);
                    fillFile(file);
                } else {
                    LOG.info("Файл {} уже существует в папке {}", file.getName(), FOLDER);
                }
            } catch (IOException e) {
                LOG.error("Ошибка ввода/вывода", e);
            }
        }

        LOG.info("End file generating");
    }

    private static void fillFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            long startTime = new Date().getTime();
            for (int j = 0; j < 100_000_000; j++) {
                writer.write(new Random().nextInt() + ",");
            }
            writer.write(String.valueOf(new Random().nextInt()));
            long endTime = new Date().getTime();
            LOG.info("Файл {} заполнен данными за {} мс", file.getName(), endTime - startTime);
        }
    }

    public static String getFolder() {
        return FOLDER;
    }
}
