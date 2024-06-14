package com.chenxin.playojbackend.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxin.playojbackend.model.dto.question.QuestionQueryRequest;
import com.chenxin.playojbackend.model.entity.Question;
import com.chenxin.playojbackend.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fangchenxin
 * @description 针对表【question(题目表)】的数据库操作Service
 * @createDate 2024-06-13 18:33:45
 */
public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean add);

    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);
}
