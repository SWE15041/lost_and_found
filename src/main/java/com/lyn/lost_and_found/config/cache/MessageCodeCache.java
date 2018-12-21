package com.lyn.lost_and_found.config.cache;

import com.jay.vito.common.util.validate.Validator;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MessageCodeCache {
    private String apikey = "bc7a912879e2bb5dfba641017c9c664b";
    private String textConstant = "【云片网】您的验证码是";
    private Random randomNum = new Random();
    private static MessageCodeCache messageCodeCache;
    /**
     * 键：手机号mobile
     * 值：验证码值
     */
    private Map<String, Object> messageCode;

    private MessageCodeCache() {
    }

    public static MessageCodeCache getInstance() {
        if (messageCodeCache == null) {
            synchronized (MessageCodeCache.class) {
                if (messageCodeCache == null) {
                    messageCodeCache = new MessageCodeCache();
                }
            }
        }
        return messageCodeCache;
    }

    /**
     * 根据手机号生成短信验证码,并存放到缓存中
     *
     * @param mobile 手机号
     * @return
     */
    public boolean buildMessageCode(String mobile) {
        //初始化client,apikey作为所有请求的默认值(可以为空)
        YunpianClient client = new YunpianClient().init();
        Map<String, String> message = client.newParam(2);
        message.put(YunpianClient.APIKEY, apikey);
        message.put(YunpianClient.MOBILE, mobile);
        //生成6个i0-9范围内的整数；
        int[] random = randomNum.ints(4, 0, 9).toArray();
        String text = textConstant;
        String randomNum = "";
        for (int i : random) {
            randomNum += i;
        }
        text += randomNum;
        message.put(YunpianClient.TEXT, text);
        Result<SmsSingleSend> sendResult = client.sms().single_send(message);
        SmsSingleSend data = sendResult.getData();
        System.out.println("返回结果：" + data);
        client.close();
        //todo 保存验证码的值
        if (messageCode == null) {
            synchronized (MessageCodeCache.class) {
                if (messageCode == null) {
                    messageCode = new HashMap<>();
                    messageCode.put(mobile, "1234");
                }
            }
        }
        return true;
    }

    /**
     * 根据手机号获取短信验证码
     *
     * @param mobile 手机号
     * @return 短信验证码的值
     */
    public String getMessageCode(String mobile) {
        String messageCodeValue = String.valueOf(messageCode.get(mobile));
        if (Validator.isNull(messageCodeValue)) {
            return null;
        }
        return messageCodeValue;
    }

    /**
     * 根据手机号删除对应的短信验证码
     *
     * @param mobile
     * @return
     */
    public boolean deleteMessageCode(String mobile) {
        Iterator<Map.Entry<String, Object>> iterator = messageCode.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            if (key.equals(mobile)) {
                messageCode.remove(key);
            }
        }
        return true;
    }

}
