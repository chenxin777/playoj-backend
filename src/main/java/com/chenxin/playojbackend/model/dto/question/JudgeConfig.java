package com.chenxin.playojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fangchenxin
 * @description 题目配置
 * @date 2024/6/13 20:18
 * @modify
 */
@Data
public class JudgeConfig implements Serializable {

    private static final long serialVersionUID = 8267493251970073118L;
    /**
     * 时间限制ms
     */
    private Long timeLimit;

    /**
     * 内存限制kb
     */
    private Long memoryLimit;

    /**
     * 堆栈限制kb
     */
    private Long stackLimit;
}
