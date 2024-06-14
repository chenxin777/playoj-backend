package com.chenxin.playojbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxin.playojbackend.common.ErrorCode;
import com.chenxin.playojbackend.constant.CommonConstant;
import com.chenxin.playojbackend.exception.BusinessException;
import com.chenxin.playojbackend.exception.ThrowUtils;
import com.chenxin.playojbackend.mapper.UserQuestionMapper;
import com.chenxin.playojbackend.model.dto.userquestion.QuestionSubmitQueryRequest;
import com.chenxin.playojbackend.model.dto.userquestion.UserQuestionAddRequest;
import com.chenxin.playojbackend.model.entity.Question;
import com.chenxin.playojbackend.model.entity.User;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.chenxin.playojbackend.model.enums.QuestionSubmitStatusEnum;
import com.chenxin.playojbackend.model.vo.UserQuestionVO;
import com.chenxin.playojbackend.service.QuestionService;
import com.chenxin.playojbackend.service.UserQuestionService;
import com.chenxin.playojbackend.service.UserService;
import com.chenxin.playojbackend.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fangchenxin
 * @description 针对表【user_question(题目提交表)】的数据库操作Service实现
 * @createDate 2024-06-13 18:33:58
 */
@Service
public class UserQuestionServiceImpl extends ServiceImpl<UserQuestionMapper, UserQuestion>
        implements UserQuestionService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Override
    public Long doUserQuestion(UserQuestionAddRequest userQuestionAddRequest, User loginUser) {
        // 判断语言
        String language = userQuestionAddRequest.getLanguage();
        QuestionSubmitLanguageEnum questionSubmitLanguageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(questionSubmitLanguageEnum), ErrorCode.PARAMS_ERROR, "编程语言错误");

        // 判断题目是否存在
        Long questionId = userQuestionAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(question), ErrorCode.PARAMS_ERROR, "题目不存在");

        String code = userQuestionAddRequest.getCode();
        ThrowUtils.throwIf(StringUtils.isBlank(code), ErrorCode.PARAMS_ERROR, "提交代码为空");
        Long userId = loginUser.getId();
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(questionId);
        userQuestion.setUserId(userId);
        userQuestion.setLanguage(language);
        userQuestion.setJudgeInfo("{}");
        userQuestion.setCode(code);
        // todo 初始状态
        userQuestion.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        boolean res = this.save(userQuestion);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return userQuestion.getId();
    }

    @Override
    public QueryWrapper<UserQuestion> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<UserQuestion> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.isEmpty(questionSubmitQueryRequest)) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId) && questionId > 0, "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId) && userId > 0, "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public Page<UserQuestionVO> getQuestionVOPage(Page<UserQuestion> userQuestionPage, User loginUser) {
        List<UserQuestion> userQuestionList = userQuestionPage.getRecords();
        Page<UserQuestionVO> userQuestionVOPage = new Page<>(userQuestionPage.getCurrent(), userQuestionPage.getSize(), userQuestionPage.getTotal());
        if (CollUtil.isEmpty(userQuestionList)) {
            return userQuestionVOPage;
        }
        // 1. 关联查询用户信息
        List<UserQuestionVO> userQuestionVOList = userQuestionList.stream().map(userQuestion -> getUserQuestionVO(userQuestion, loginUser)).collect(Collectors.toList());
        userQuestionVOPage.setRecords(userQuestionVOList);
        return userQuestionVOPage;
    }

    @Override
    public UserQuestionVO getUserQuestionVO(UserQuestion userQuestion, User loginUser) {
        UserQuestionVO userQuestionVO = UserQuestionVO.objToVo(userQuestion);
        // 脱敏:仅本人和管理员能看见提交的代码
        Long userId = loginUser.getId();
        if (!Objects.equals(userId, userQuestion.getUserId()) && !userService.isAdmin(loginUser)) {
            userQuestionVO.setCode(null);
        }
        return userQuestionVO;
    }
}




