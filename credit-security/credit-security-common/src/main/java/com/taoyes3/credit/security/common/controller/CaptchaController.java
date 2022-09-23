package com.taoyes3.credit.security.common.controller;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author taoyes3
 * @date 2022/9/22 14:41
 */
// @RestController
// @RequestMapping("/captcha")
// public class CaptchaController {
//     @Resource
//     private CaptchaService captchaService;
//    
//     @PostMapping("/get")
//     public ResponseEntity<ResponseModel> get(@RequestBody CaptchaVO captchaVO) {
//         return ResponseEntity.ok(captchaService.get(captchaVO));
//     }
//    
//     @PostMapping("/check")
//     public ResponseEntity<ResponseModel> check(@RequestBody CaptchaVO captchaVO) {
//         ResponseModel responseModel;
//         try {
//             responseModel = captchaService.check(captchaVO);
//         } catch (Exception e) {
//             return ResponseEntity.ok(ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR));
//         }
//         return ResponseEntity.ok(responseModel);
//     }
// }
