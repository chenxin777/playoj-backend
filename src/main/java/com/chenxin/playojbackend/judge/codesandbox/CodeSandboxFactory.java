package com.chenxin.playojbackend.judge.codesandbox;

import com.chenxin.playojbackend.judge.codesandbox.impl.ExampleCodeSandbox;
import com.chenxin.playojbackend.judge.codesandbox.impl.RemoteCodeSandbox;
import com.chenxin.playojbackend.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * @author fangchenxin
 * @description 代码沙箱工厂
 * @date 2024/6/17 15:24
 * @modify
 */
public class CodeSandboxFactory {

    /**
     * @description 创建代码沙箱示例
     * @author fangchenxin
     * @date 2024/6/17 15:32
     * @param type
     * @return com.chenxin.playojbackend.judge.codesandbox.CodeSandbox
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }



}
