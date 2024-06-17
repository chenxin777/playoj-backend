package com.chenxin.playojbackend.judge.codesandbox;

import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.naming.ldap.ExtendedResponse;

/**
 * @author fangchenxin
 * @description
 * @date 2024/6/17 16:12
 * @modify
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox{

    @Resource
    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("请求参数：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("响应：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
