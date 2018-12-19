package com.lyn.lost_and_found.web.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.TokenUtil;
import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.jay.vito.website.core.cache.SystemDataHolder;
import com.jay.vito.website.web.controller.BaseController;
import com.jay.vito.website.web.controller.BaseGridController;
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
public class LoginController  extends BaseLFGridController<LfUser, Long> {


    @Autowired
    private LfUserService userService;

    /**
     * 用户授权登陆接口
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
            Long userId=null;
            if(Validator.isNull(user)){
                LfUser newUser=new LfUser();
                newUser.setWechatOpenid(openid);
                super.save(newUser);
                userId= newUser.getId();
            }else {
                userId=user.getId();
            }
            TokenData tokenData=new TokenData();
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



}
