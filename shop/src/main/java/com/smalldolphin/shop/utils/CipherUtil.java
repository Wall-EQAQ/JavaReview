package com.smalldolphin.shop.utils;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
     * @Description:    对称密钥密码算法工具类
 * @Created by Administrator on 2022/4/13 23:15
 * @Modified by:
 */
public class CipherUtil {

    /**
     *  生成随机密钥
     * @param keyBitSize    字节大小
     * @param algorithmName 算法名称
     * @return
     */
    public static Key generateNewKey(int keyBitSize, String algorithmName) {
        KeyGenerator kg;
        try{
            kg = KeyGenerator.getInstance(algorithmName);
        }catch (NoSuchAlgorithmException e) {
            String message = "Unable to acquire " + algorithmName + "algorithm. This is required to function";
            throw new IllegalStateException(message, e);
        }
        kg.init(keyBitSize);
        return kg.generateKey();
    }
}
