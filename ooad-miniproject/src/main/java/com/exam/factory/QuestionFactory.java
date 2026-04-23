package com.exam.factory;

import com.exam.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

// [PATTERN: Factory Method] — Hides question creation complexity
// [GRASP: Creator] — Responsible for creating Question objects
@Component
public class QuestionFactory {

    public Question createQuestion(String content, DifficultyLevel level, String topic,
                                   List<String> optionTexts, Integer correctOptionIndex) {
        Question q = new Question();
        q.setContent(content);
        q.setDifficulty(level);
        q.setTopic(topic);
        q.setType(Question.QuestionType.MCQ);
        q.setCorrectOptionIndex(correctOptionIndex);

        if (optionTexts != null) {
            for (String text : optionTexts) {
                QuestionOption opt = new QuestionOption();
                opt.setText(text);
                opt.setQuestion(q);
                q.getOptions().add(opt);
            }
        }
        return q;
    }
}
