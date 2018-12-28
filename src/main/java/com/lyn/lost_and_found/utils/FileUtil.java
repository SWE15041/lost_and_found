package com.lyn.lost_and_found.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    /**
     * 将文件上传到服务端指定地址
     *
     * @param multipartFile
     * @return
     */
    public static String uploadFile(MultipartFile multipartFile) {
//        String targetFilePath = "/home/lyn/picPath";
            String targetFilePath = "/Users/lyn/picPath";

        String fileName = UUID.randomUUID().toString().replace("-", "");
        String fileSuffix = getFileSuffix(multipartFile);
        if (fileSuffix != null) {
            fileName += fileSuffix;
        }
        String pathName = targetFilePath + File.separator + fileName;
        File targetFile = new File(pathName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(multipartFile.getInputStream(), fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathName;
    }

    /**
     * 获取指定文件到后缀名
     *
     * @param multipartFile
     * @return
     */
    public static String getFileSuffix(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        String fileName = multipartFile.getOriginalFilename();
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex == -1) {
            return null;
        } else {
            return fileName.substring(suffixIndex, fileName.length());
        }
    }

    /**
     * 获取指定文件的内容
     *
     * @param pathname
     * @return 二进制流
     */
    public static byte[] get(String pathname) {
        File file = new File(pathname);
        try {
            FileInputStream fis;
            fis = new FileInputStream(file);

            long size = file.length();
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定文件名（路径+文件名）的文件
     *
     * @param pathname
     * @return
     */
    public static boolean delete(String pathname) {
        File file = new File(pathname);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        String pathname="/users/lyn/picPath/5a56ff5210c140369cf607436e57955c.jpg";
        delete(pathname);
    }

}
