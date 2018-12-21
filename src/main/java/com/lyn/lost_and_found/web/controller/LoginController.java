package com.lyn.lost_and_found.web.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.TokenUtil;
import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.jay.vito.website.core.cache.SystemDataHolder;
import com.lyn.lost_and_found.config.cache.MessageCodeCache;
import com.lyn.lost_and_found.config.wechat.WxMaConfiguration;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfusers")
public class LoginController extends BaseLFGridController<LfUser, Long> {


    @Autowired
    private LfUserService userService;

    /**
     * 用户授权登陆接口
     *
     * @param map 存放微信授权码code
     * @return 自定义token
     */
    @IgnoreUserAuth
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String, Object> map) {
        String code = String.valueOf(map.get("code"));
        if (StringUtils.isBlank(code)) {
            throw new HttpBadRequestException("登录失败", "FALID_LOGIN");
        }
        //wx.miniapp.configs.appid
        String appid = String.valueOf(SystemDataHolder.getParam("appid"));
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        try {
            //小程序授权
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
//            this.logger.info(session.getSessionKey());
//            String sessionKey = session.getSessionKey();
            String openid = session.getOpenid();
            LfUser user = userService.getByWechatOpenid(openid);
            Long userId = null;
            if (Validator.isNull(user)) {
                LfUser newUser = new LfUser();
                newUser.setWechatOpenid(openid);
                super.save(newUser);
                userId = newUser.getId();
            } else {
                userId = user.getId();
            }
            TokenData tokenData = new TokenData();
            tokenData.setUserId(userId);
            String token = TokenUtil.genToken(tokenData);

            map.clear();
            token = "bearer " + token;
            map.put("token", token);
            return map;
        } catch (WxErrorException e) {
            throw new HttpBadRequestException("code无效", "VALID_CODE");
        }
    }

    /**
     * app用户登陆
     *
     * @param map 手机号+验证码
     * @return token
     */
    @RequestMapping(value = "/appLogin", method = RequestMethod.GET)
    public Map<String, Object> appLogin(@RequestBody Map<String, Object> map) {
        String mobile = String.valueOf(map.get("mobile"));
        String messageCode = String.valueOf(map.get("messageCode"));
        //1.验证验证码
        String validCode = MessageCodeCache.getInstance().getMessageCode(mobile);
        if (Validator.isNull(messageCode) || Validator.isNull(validCode) || !messageCode.equals(validCode)) {
            throw new HttpBadRequestException("验证码无效", "INVALID_MESSAGECODE");
        }
        //2.验证用户是否存在，不存在则创建用户
        if (Validator.isNull(mobile)) {
            throw new HttpBadRequestException("手机为空", "INVALID_MOBILE");
        }
        LfUser user = userService.getByMobile(mobile);
        if (Validator.isNull(user)) {
            user = new LfUser();
            user.setMobile(mobile);
            super.save(user);
        }
        Long userId = user.getId();
        //3.生成token 默认时效3小时
        TokenData tokenData = new TokenData();
        tokenData.setUserId(userId);
        String token = TokenUtil.genToken(tokenData);
        map.clear();
        token = "bearer " + token;
        map.put("token", token);
        return map;
    }

    /**
     * 生成验证码
     *
     * @param map 手机号
     * @return
     */
    @RequestMapping(value = "/bulidMessageCode", method = RequestMethod.POST)
    public boolean bulidMessageCode(@RequestBody Map<String, Object> map) {
        String mobile = String.valueOf(map.get("mobile"));
        boolean buildMessageCode = MessageCodeCache.getInstance().buildMessageCode(mobile);
        return buildMessageCode;
    }

    @RequestMapping(value = "/verifyMobile", method = RequestMethod.POST)
    public boolean verifyMobile(@RequestBody Map<String, Object> map) {
        String mobile = String.valueOf(map.get("mobile"));
        LfUser user = userService.getByMobile(mobile);
        if (Validator.isNull(user)) {
            return false;
        }
        return true;
    }

}
