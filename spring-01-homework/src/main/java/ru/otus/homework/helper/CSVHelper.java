package ru.otus.homework.helper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Slf4j
@UtilityClass
public class CSVHelper {

    /** Чтение csv-файла
     * @return список прочитанных строк
     */
    public List<String[]> read(Resource resource) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.readAll();
        } catch (IOException | CsvException ex) {
            log.error("SurveyDaoImpl.getSurvey read resource Exception", ex);
        }
        return Collections.emptyList();
    }

}
