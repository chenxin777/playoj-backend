package com.chenxin.playojbackend.judge.strategy;

import com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo;

/**
 * @author fangchenxin
 * @description
 * @date 2024/6/17 19:18
 * @modify
 */
public interface JudgeStrategy {

    /**
     * @description 执行判题
     * @author fangchenxin
     * @date 2024/6/17 19:24
     * @param judgeContext
     * @return com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo
     */
    JudgeInfo doJudge(JudgeContext judgeContext);

}
