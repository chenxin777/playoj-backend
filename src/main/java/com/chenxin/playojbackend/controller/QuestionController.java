package com.chenxin.playojbackend.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxin.playojbackend.annotation.AuthCheck;
import com.chenxin.playojbackend.common.BaseResponse;
import com.chenxin.playojbackend.common.DeleteRequest;
import com.chenxin.playojbackend.common.ErrorCode;
import com.chenxin.playojbackend.common.ResultUtils;
import com.chenxin.playojbackend.constant.UserConstant;
import com.chenxin.playojbackend.exception.BusinessException;
import com.chenxin.playojbackend.exception.ThrowUtils;
import com.chenxin.playojbackend.manager.RedisLimiterManager;
import com.chenxin.playojbackend.model.dto.question.*;
import com.chenxin.playojbackend.model.dto.userquestion.QuestionSubmitQueryRequest;
import com.chenxin.playojbackend.model.dto.userquestion.UserQuestionAddRequest;
import com.chenxin.playojbackend.model.entity.Question;
import com.chenxin.playojbackend.model.entity.User;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.vo.QuestionVO;
import com.chenxin.playojbackend.model.vo.UserQuestionVO;
import com.chenxin.playojbackend.service.QuestionService;
import com.chenxin.playojbackend.service.UserQuestionService;
import com.chenxin.playojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author chenxin777
 * 
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private UserQuestionService userQuestionService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (ObjectUtils.isNotEmpty(tags)) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCaseList = questionAddRequest.getJudgeCase();
        if (ObjectUtils.isNotEmpty(judgeCaseList)) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseList));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (ObjectUtil.isNotEmpty(judgeConfig)) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        questionService.validQuestion(question, true);
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        question.setFavourNum(0);
        question.setThumbNum(0);
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (ObjectUtils.isNotEmpty(tags)) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCaseList = questionUpdateRequest.getJudgeCase();
        if (ObjectUtils.isNotEmpty(judgeCaseList)) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseList));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (ObjectUtil.isNotEmpty(judgeConfig)) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * @description 管理员或自己查看题目详情
     * @author fangchenxin
     * @date 2024/6/15 20:39
     * @param id
     * @param request
     * @return com.chenxin.playojbackend.common.BaseResponse<com.chenxin.playojbackend.model.entity.Question>
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (!question.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (ObjectUtils.isNotEmpty(tags)) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCaseList = questionEditRequest.getJudgeCase();
        if (ObjectUtils.isNotEmpty(judgeCaseList)) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseList));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (ObjectUtil.isNotEmpty(judgeConfig)) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * @param userQuestionAddRequest
     * @param request
     * @return com.chenxin.playojbackend.common.BaseResponse<java.lang.Integer>
     * @description 提交题目
     * @author fangchenxin
     * @date 2024/6/14 00:14
     */
    @PostMapping("/user_question/do")
    public BaseResponse<Long> doUserQuestion(@RequestBody UserQuestionAddRequest userQuestionAddRequest,
                                             HttpServletRequest request) {
        if (userQuestionAddRequest == null || userQuestionAddRequest.getQuestionId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        // 限流
        redisLimiterManager.doRateLimit("question_submit_" + loginUser.getId());
        Long result = userQuestionService.doUserQuestion(userQuestionAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * @description 题目提交（管理员）
     * @author fangchenxin
     * @date 2024/7/1 17:42
     * @param questionSubmitQueryRequest
     * @param request
     * @return com.chenxin.playojbackend.common.BaseResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.chenxin.playojbackend.model.vo.UserQuestionVO>>
     */
    @PostMapping("/user_question/list/page")
    public BaseResponse<Page<UserQuestionVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        if (questionSubmitQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        int current = questionSubmitQueryRequest.getCurrent();
        int pageSize = questionSubmitQueryRequest.getPageSize();
        Page<UserQuestion> userQuestionPage = userQuestionService.page(new Page<>(current, pageSize), userQuestionService.getQueryWrapper(questionSubmitQueryRequest));
        // 脱敏信息
        return ResultUtils.success(userQuestionService.getQuestionVOPage(userQuestionPage, loginUser));
    }

    /**
     * @description 我的题目提交
     * @author fangchenxin
     * @date 2024/7/1 17:41
     * @param questionSubmitQueryRequest
     * @param request
     * @return com.chenxin.playojbackend.common.BaseResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.chenxin.playojbackend.model.vo.UserQuestionVO>>
     */
    @PostMapping("/my/user_question/list/page")
    public BaseResponse<Page<UserQuestionVO>> listMyQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        if (questionSubmitQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        questionSubmitQueryRequest.setUserId(loginUser.getId());
        int current = questionSubmitQueryRequest.getCurrent();
        int pageSize = questionSubmitQueryRequest.getPageSize();
        Page<UserQuestion> userQuestionPage = userQuestionService.page(new Page<>(current, pageSize), userQuestionService.getQueryWrapper(questionSubmitQueryRequest));
        // 脱敏信息
        return ResultUtils.success(userQuestionService.getQuestionVOPage(userQuestionPage, loginUser));
    }

}
