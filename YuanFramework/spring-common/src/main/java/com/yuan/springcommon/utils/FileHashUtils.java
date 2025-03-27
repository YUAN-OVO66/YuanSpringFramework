package com.yuan.springcommon.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class FileHashUtils {
    private static final String SHA_256 = "SHA-256";
    private static final int BUFFER_SIZE = 8192; // 8KB缓冲区

    /**
     * 计算文件的SHA-256哈希值
     * @param file 要计算哈希的文件
     * @return 文件的SHA-256哈希字符串(小写)
     * @throws IOException 文件读取错误
     * @throws NoSuchAlgorithmException 系统不支持SHA-256算法
     * @throws IllegalArgumentException 文件为null或不存在/不可读
     */
    public static String calculateFileHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        // 参数校验
        Objects.requireNonNull(file, "文件不能为null");
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件内容为空");
        }

        MessageDigest digest = MessageDigest.getInstance(SHA_256);
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        // 将字节数组转换为十六进制字符串
        return bytesToHex(digest.digest());
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串(小写)
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //判断两个哈希值是否相等
    public static Boolean  compareHashes(String hash1, String hash2){
        Objects.requireNonNull(hash1, "第一个哈希值不能为null");
        Objects.requireNonNull(hash2, "第二个哈希值不能为null");

        if (hash1.isEmpty() || hash2.isEmpty()) {
            throw new IllegalArgumentException("哈希值不能为空字符串");
        }

        // 不区分大小写比较
        return hash1.equalsIgnoreCase(hash2);
    }

}
