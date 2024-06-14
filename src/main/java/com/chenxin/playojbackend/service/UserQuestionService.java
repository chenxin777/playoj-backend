package com.chenxin.playojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxin.playojbackend.model.dto.userquestion.QuestionSubmitQueryRequest;
import com.chenxin.playojbackend.model.dto.userquestion.UserQuestionAddRequest;
import com.chenxin.playojbackend.model.entity.User;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.vo.UserQuestionVO;

/**
 * @author fangchenxin
 * @description 针对表【user_question(题目提交表)】的数据库操作Service
 * @createDate 2024-06-13 18:33:58
 */
public interface UserQuestionService extends IService<UserQuestion> {

    Long doUserQuestion(UserQuestionAddRequest questionAddRequest, User loginUser);

    QueryWrapper<UserQuestion> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    Page<UserQuestionVO> getQuestionVOPage(Page<UserQuestion> userQuestionPage, User loginUser);

    UserQuestionVO getUserQuestionVO(UserQuestion userQuestion, User loginUser);

}
