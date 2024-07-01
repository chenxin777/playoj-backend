package com.chenxin.playojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxin.playojbackend.common.BaseResponse;
import com.chenxin.playojbackend.common.ErrorCode;
import com.chenxin.playojbackend.common.ResultUtils;
import com.chenxin.playojbackend.exception.BusinessException;
import com.chenxin.playojbackend.model.dto.userquestion.QuestionSubmitQueryRequest;
import com.chenxin.playojbackend.model.dto.userquestion.UserQuestionAddRequest;
import com.chenxin.playojbackend.model.entity.User;
import com.chenxin.playojbackend.model.entity.UserQuestion;
import com.chenxin.playojbackend.model.vo.UserQuestionVO;
import com.chenxin.playojbackend.service.UserQuestionService;
import com.chenxin.playojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author chenxin777
 * 
 */
@RestController
//@RequestMapping("/user_question")
@Slf4j
public class UserQuestionController {

    @Resource
    private UserQuestionService userQuestionService;

    @Resource
    private UserService userService;

    /**
     * @param userQuestionAddRequest
     * @param request
     * @return com.chenxin.playojbackend.common.BaseResponse<java.lang.Integer>
     * @description 提交题目
     * @author fangchenxin
     * @date 2024/6/14 00:14
     */
    /*@PostMapping("/")
    public BaseResponse<Long> doUserQuestion(@RequestBody UserQuestionAddRequest userQuestionAddRequest,
                                             HttpServletRequest request) {
        if (userQuestionAddRequest == null || userQuestionAddRequest.getQuestionId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        Long result = userQuestionService.doUserQuestion(userQuestionAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<UserQuestionVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        int current = questionSubmitQueryRequest.getCurrent();
        int pageSize = questionSubmitQueryRequest.getPageSize();
        Page<UserQuestion> userQuestionPage = userQuestionService.page(new Page<>(current, pageSize), userQuestionService.getQueryWrapper(questionSubmitQueryRequest));
        // 脱敏信息
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userQuestionService.getQuestionVOPage(userQuestionPage, loginUser));
    }
*/
}
