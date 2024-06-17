package com.chenxin.playojbackend.judge.codesandbox;

import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author fangchenxin
 * @description 代码沙箱接口定义
 * @date 2024/6/17 11:40
 * @modify
 */
public interface CodeSandbox {

    /**
     * @description 执行代码
     * @author fangchenxin
     * @date 2024/6/17 12:26
     * @param executeCodeRequest
     * @return com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
