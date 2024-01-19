package xin.weixin.pay.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.weixin.service.orderForm.OrderFormService;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/pro/pay")
public class WechatPayCallbackController {
    @Value("${wxpay.apiV3Key}")
    private String wxApiV3Key;

    @Autowired
    OrderFormService orderFormService;

    @PostMapping("/notify")
    public void payCallback(@RequestBody String encryptedNotification, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(encryptedNotification);

            String ciphertext = rootNode.path("resource").path("ciphertext").asText();
            String associatedData = rootNode.path("resource").path("associated_data").asText();
            String nonce = rootNode.path("resource").path("nonce").asText();

            // 使用您的 API v3 密钥进行解密
            byte[] apiV3KeyBytes = wxApiV3Key.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec key = new SecretKeySpec(apiV3KeyBytes, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);

            JsonNode decryptedJson = objectMapper.readTree(decryptedString);
            System.out.println("结果：" + decryptedJson);


            String jsonString = objectMapper.writeValueAsString(decryptedJson);
            WeChatPayNotification notification = JSON.parseObject(jsonString, WeChatPayNotification.class);
            if (notification.getTradeState().equals("SUCCESS")) {
                System.out.println("交易成功！" + notification.getOutTradeNo());
                orderFormService.paySucceed(notification.getOutTradeNo());
            } else {
                System.out.println("交易失败！" + notification.getOutTradeNo());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

