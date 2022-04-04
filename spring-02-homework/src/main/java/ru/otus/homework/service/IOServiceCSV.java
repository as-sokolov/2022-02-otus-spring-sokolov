package ru.otus.homework.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IOServiceCSV implements IOService {

    private Resource csvResource;

    public IOServiceCSV(Resource csvResource) {
        this.csvResource = csvResource;
    }

    /** Чтение csv-файла
     * @return список прочитанных строк
     */
    public List<String[]> read() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvResource.getInputStream()))) {
            return reader.readAll();
        } catch (IOException | CsvException ex) {
            log.error("SurveyDaoImpl.getSurvey read resource Exception", ex);
        }
        return Collections.emptyList();
    }
}
