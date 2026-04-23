package com.exam.service.factory;

import com.exam.model.Question;
import com.exam.model.DifficultyLevel;
import com.exam.model.QuestionOption;
import java.util.ArrayList;
import java.util.List;

public class QuestionFactory {
    public static Question createQuestion(String content, DifficultyLevel difficulty,
            List<String> optionTexts, int correctIndex, String topic) {
        Question question = new Question();
        question.setContent(content);
        question.setDifficulty(difficulty);
        question.setCorrectOptionIndex(correctIndex);
        question.setTopic(topic != null ? topic : "General");
        question.setType(Question.QuestionType.MCQ);

        List<QuestionOption> options = new ArrayList<>();
        for (String text : optionTexts) {
            QuestionOption option = new QuestionOption();
            option.setText(text);
            option.setQuestion(question);
            options.add(option);
        }
        question.setOptions(options);
        return question;
    }

    // Overload for backward compatibility
    public static Question createQuestion(String content, DifficultyLevel difficulty,
            List<String> optionTexts, int correctIndex) {
        return createQuestion(content, difficulty, optionTexts, correctIndex, "General");
    }
}
