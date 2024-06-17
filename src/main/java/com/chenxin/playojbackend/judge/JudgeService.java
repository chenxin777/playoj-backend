package com.chenxin.playojbackend.judge;

import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.vo.UserQuestionVO;

/**
 * @author fangchenxin
 * @description 判题服务
 * @date 2024/6/17 17:03
 * @modify
 */
public interface JudgeService {

    /**
     * @description 执行判题
     * @author fangchenxin
     * @date 2024/6/17 23:47
     * @param userQuestionId
     * @return com.chenxin.playojbackend.model.entity.UserQuestion
     */
    UserQuestion doJudge(long userQuestionId);



}
