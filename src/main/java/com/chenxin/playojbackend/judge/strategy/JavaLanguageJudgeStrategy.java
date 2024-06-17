package com.chenxin.playojbackend.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.chenxin.playojbackend.model.dto.question.JudgeCase;
import com.chenxin.playojbackend.model.dto.question.JudgeConfig;
import com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo;
import com.chenxin.playojbackend.model.entity.Question;
import com.chenxin.playojbackend.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @author fangchenxin
 * @description
 * @date 2024/6/17 19:29
 * @modify
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        // 代码执行信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        // 代码执行时间
        Long time = judgeInfo.getTime();
        // 占用内存
        Long memory = judgeInfo.getMemory();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);
        // 测试用例输入
        List<String> inputList = judgeContext.getInputList();
        // 代码执行输出
        List<String> outputList = judgeContext.getOutputList();
        // 测试用例
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        // 题目信息
        Question question = judgeContext.getQuestion();

        // 默认成功状态
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        // 判断沙箱执行结果中 输出数量和预期输出是否一致
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        // 依次判断输出结果是否和预期相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        // 获取题目中的时间、内存限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        // todo Java程序本身需要额外执行10秒
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if (time - JAVA_PROGRAM_TIME_COST> timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
        return judgeInfoResponse;
    }
}
