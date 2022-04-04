package ru.otus.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.MultiVariantQuestion;

@DisplayName("MultiVariantQuestion test")
public class MultiVariantQuestionTest {

    @Test
    void testMultiVariantQuestionNullAnswers() {
        MultiVariantQuestion multiVariantQuestion = new MultiVariantQuestion("testQuestion", "testAnswer", null);
        assertEquals(1, multiVariantQuestion.getAnswers().size());
    }

    @Test
    void testMultiVariantQuestionSomeAnswers() {
        String[] testAnswers = new String[]{"1", "2", "3", "4"};
        MultiVariantQuestion multiVariantQuestion = new MultiVariantQuestion("testQuestion", "testAnswer", testAnswers);
        assertEquals(5, multiVariantQuestion.getAnswers().size());
    }

    @Test
    void testMultiVariantQuestionNoDuplicates() {
        String[] testAnswers = new String[]{"testAnswer", "2", "3", "testAnswer"};
        MultiVariantQuestion multiVariantQuestion = new MultiVariantQuestion("testQuestion", "testAnswer", testAnswers);
        assertEquals(3, multiVariantQuestion.getAnswers().size());
    }
}
