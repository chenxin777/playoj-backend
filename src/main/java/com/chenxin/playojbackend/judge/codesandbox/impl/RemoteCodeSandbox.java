package com.chenxin.playojbackend.judge.codesandbox.impl;

import com.chenxin.playojbackend.judge.codesandbox.CodeSandbox;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author fangchenxin
 * @description 远程代码沙箱
 * @date 2024/6/17 12:28
 * @modify
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        return executeCodeResponse;
    }
}
