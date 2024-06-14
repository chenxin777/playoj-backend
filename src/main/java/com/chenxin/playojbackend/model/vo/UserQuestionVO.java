package com.chenxin.playojbackend.model.vo;

import cn.hutool.json.JSONUtil;
import com.chenxin.playojbackend.model.dto.userquestion.JudgeInfo;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author fangchenxin
 * @description
 * @date 2024/6/14 16:30
 * @modify
 */
@Data
public class UserQuestionVO {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 用户提交代码
     */
    private String code;

    /**
     * 判题状态(0待判题、1判题中、2成功、3失败)
     */
    private Integer status;

    /**
     * 判题信息(json对象)
     */
    private JudgeInfo judgeInfo;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 题目信息
     */
    private QuestionVO questionVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param userQuestionVO
     * @return
     */
    public static UserQuestion voToObj(UserQuestionVO userQuestionVO) {
        if (userQuestionVO == null) {
            return null;
        }
        UserQuestion userQuestion = new UserQuestion();
        BeanUtils.copyProperties(userQuestionVO, userQuestion);
        JudgeInfo judgeInfo = userQuestionVO.getJudgeInfo();
        if (ObjectUtils.isNotEmpty(judgeInfo)) {
            userQuestion.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        return userQuestion;
    }

    /**
     * 对象转包装类
     *
     * @param userQuestion
     * @return
     */
    public static UserQuestionVO objToVo(UserQuestion userQuestion) {
        if (userQuestion == null) {
            return null;
        }
        UserQuestionVO userQuestionVO = new UserQuestionVO();
        BeanUtils.copyProperties(userQuestion, userQuestionVO);
        String judgeInfoStr = userQuestion.getJudgeInfo();
        userQuestionVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return userQuestionVO;
    }

}
