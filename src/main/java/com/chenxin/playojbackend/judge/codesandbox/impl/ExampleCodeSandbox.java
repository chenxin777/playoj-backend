package com.chenxin.playojbackend.judge.codesandbox.impl;

import java.util.List;

import com.chenxin.playojbackend.judge.codesandbox.CodeSandbox;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo;
import com.chenxin.playojbackend.model.enums.JudgeInfoMessageEnum;
import com.chenxin.playojbackend.model.enums.QuestionSubmitStatusEnum;

/**
 * @author fangchenxin
 * @description 示例代码沙箱（仅用于调试）
 * @date 2024/6/17 12:28
 * @modify
 */
public class ExampleCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
