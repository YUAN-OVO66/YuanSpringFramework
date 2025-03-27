package com.yuan.springcommon.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@Service
public class FileUtils {

    //@Value("${file-util.max-size}")
    private static final String MAX_FILE_SIZE = String.valueOf(100*1024*1024);
    //@Value("${file-util.upload-path}")
    private static final String UPLOAD_PATH = "C:\\Users\\LV\\Desktop\\YUAN\\FileUpload\\FileUpload\\file";
    //文件上传工具类

    /*
     * @Dse 文件上传
     * @param file
     * @param subDir
     * @return filePath
    * */
    public static String uploadFile(MultipartFile file,String subDir) throws IOException {
        //检查文件是否为空
        if (file.isEmpty()) {
            return "文件为空";
        }
        //检查文件大小
        if (file.getSize() > Long.parseLong(MAX_FILE_SIZE)) {
            return "文件大小超过限制";
        }
        //获取文件文件类型
        String typeDir = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String realFileName=file.getOriginalFilename();
        assert realFileName != null;
        String fileSuffix=realFileName.substring(realFileName.lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replace("-","")+fileSuffix;
        String serverPath = UPLOAD_PATH + File.separator + subDir + typeDir; // 拼接子目录路径
        File newfile = new File(serverPath);
        if (!newfile.exists()) {
            newfile.mkdirs(); // 创建完整目录结构
        }
        File fileName = new File(newfile, newFileName);
        try{
            file.transferTo(fileName);
            return UPLOAD_PATH+subDir+"/"+newFileName;
        }catch (IOException e) {
            e.printStackTrace(); // 打印异常栈信息，便于调试
            throw new IOException("上传失败,IO异常: " + e.getMessage()); // 带上原始异常信息
        }
    }
}
