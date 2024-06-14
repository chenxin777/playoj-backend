package com.chenxin.playojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fangchenxin
 * @description 题目用例
 * @date 2024/6/13 20:18
 * @modify
 */
@Data
public class JudgeCase implements Serializable {

    private static final long serialVersionUID = -124696501760358950L;
    private String input;

    private String output;
}
