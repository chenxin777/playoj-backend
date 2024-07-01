package com.chenxin.playojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author chenxin777
 * 
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    private static final long serialVersionUID = 3410471960797302073L;
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例(json数组)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置(json对象)
     */
    private JudgeConfig judgeConfig;
}