package com.smalldolphin.shop.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/18 19:24
 * @Modified by:
 */
@Controller
@RequestMapping("/captcha")
public class SysCaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @GetMapping("/captchaImage")
    public ModelAndView getCaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        //ServletOutputStream将对象转换成二进制数据向浏览器输出(字节流)
        ServletOutputStream out = null;
        try {
        HttpSession session = request.getSession();
        /**
         * 控制ajax页面缓存
         * 固定的配置
         */
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //获取前台传过来的验证码类型是char还是math
        String type = request.getParameter("type");
        String captchaStr = null;
        String code = null;
        //将图片加载到内存
        BufferedImage bi = null;
        //如果验证码类型为char(这里限定了是char，没有写math的实现)
        if ("char".equals(type)) {
            //先生成文字
            captchaStr = code = captchaProducer.createText();
            //最后将文字放入图片
            bi = captchaProducer.createImage(captchaStr);
        }
        //将字符串(验证码)放入session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);
        out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        //清空缓冲区的数据
        out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
