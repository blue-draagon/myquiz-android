package com.kajs.android.learn.myquizz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionBank {
    private List<Question> questionList;
    private Integer nextQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Integer getCurrentQuestionIndex() {
        return nextQuestionIndex != null ? nextQuestionIndex : 0;
    }

    private void shuffleQuestionList(List<Question> questionList){
        Random random = new Random();
        List<Integer> randomIndex = new ArrayList<>();
        do {
            int index = random.nextInt(questionList.size());
            if (!randomIndex.contains(index)) {
                randomIndex.add(index);
            }
        } while (randomIndex.size() < questionList.size());
        this.questionList = new ArrayList<>();
        for (Integer index : randomIndex) {
            this.questionList.add(questionList.get(index));
        }
    }

    public Question getQuestion(Integer index) {
        if (index != null && index < questionList.size()) {
            return questionList.get(index);
        }
        if (nextQuestionIndex == null || nextQuestionIndex >= questionList.size()){
            this.shuffleQuestionList(questionList);
            nextQuestionIndex = 0;
        }
        return questionList.get(nextQuestionIndex++);
    }
}
