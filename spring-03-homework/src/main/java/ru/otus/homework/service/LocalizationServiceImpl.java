package ru.otus.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Service
@Slf4j
public class LocalizationServiceImpl implements LocalizationService {

    private final String locale;

    private final MessageSource messageSource;

    public LocalizationServiceImpl(@Value("${survey.locale}") String locale, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public String getLocalizedString(String code) {
        return messageSource.getMessage(code, null, StringUtils.isEmpty(locale) ? Locale.getDefault() : Locale.forLanguageTag(locale));
    }
}
