package com.chenxin.playojbackend.model.dto.userquestion;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fangchenxin
 * @description
 * @date 2024/6/13 21:45
 * @modify
 */
@Data
public class UserQuestionAddRequest implements Serializable {

    private static final long serialVersionUID = -6468328935038179331L;
    /**
     * 编程语言
     */
    private String language;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户提交代码
     */
    private String code;
}
