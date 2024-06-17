package com.chenxin.playojbackend.judge;

import com.chenxin.playojbackend.judge.strategy.DefaultJudgeStrategy;
import com.chenxin.playojbackend.judge.strategy.JavaLanguageJudgeStrategy;
import com.chenxin.playojbackend.judge.strategy.JudgeContext;
import com.chenxin.playojbackend.judge.strategy.JudgeStrategy;
import com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * @author fangchenxin
 * @description 判题管理
 * @date 2024/6/17 23:01
 * @modify
 */
@Service
public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        UserQuestion userQuestion = judgeContext.getUserQuestion();
        String language = userQuestion.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (QuestionSubmitLanguageEnum.JAVA.getValue().equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
