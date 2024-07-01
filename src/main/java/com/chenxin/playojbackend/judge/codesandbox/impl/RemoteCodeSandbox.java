package com.chenxin.playojbackend.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.chenxin.playojbackend.common.ErrorCode;
import com.chenxin.playojbackend.exception.BusinessException;
import com.chenxin.playojbackend.judge.codesandbox.CodeSandbox;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.chenxin.playojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fangchenxin
 * @description 远程代码沙箱
 * @date 2024/6/17 12:28
 * @modify
 */
public class RemoteCodeSandbox implements CodeSandbox {

    public static final String AUTH_REQUEST_HEADER = "auth";

    public static final String AUTH_REQUEST_SECRET = "chenxin";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        String url = "http://localhost:8090/sandbox/execute";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message：" + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
