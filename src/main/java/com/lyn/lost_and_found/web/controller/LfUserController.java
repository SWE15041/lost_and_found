package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class LfUserController extends BaseLFGridController<LfUser, Long> {

    @Autowired
    private LfUserService userService;

    /**
     * 完善用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public boolean update(@RequestBody LfUser user) {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        if (Validator.isNull(user)) {
            throw new HttpBadRequestException("信息为空", "NULL_USER_INFO");
        }
        user.setId(currentUserId);
        userService.updateNotNull(user);
        return true;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public LfUser get() {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        LfUser user = userService.get(currentUserId);
        return user;
    }


}
