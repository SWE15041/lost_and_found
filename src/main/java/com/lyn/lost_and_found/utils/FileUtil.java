package com.lyn.lost_and_found.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {

    /**
     * 文档数
     */
    private static Long fileNum = 0L;

    /**
     * 将文件上传到服务端指定地址
     *
     * @param multipartFile
     * @return
     */
    public static String uploadFile(MultipartFile multipartFile) {
        String targetFilePath = "/home/lyn/picPath";
//            String targetFilePath = "/Users/lyn/picPath";

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

    /**
     * 计算指定目录下文件的数量
     *
     * @param dir
     * @return
     */
    public static Long cntFileNum(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                cntFileNum(file);
            } else {
                fileNum++;
            }
        }
        return fileNum;
    }

    /**
     * 获取文件内容
     * @param pathname
     * @return
     */
    public static List<String> getFileContent(String pathname) {
        // 声明一个可变长的stringBuffer对象
        List<String> sb = new ArrayList<>();
        try {
            /*
             * 读取完整文件
             */
            Reader reader = new FileReader(pathname);
            String encoding = ((FileReader) reader).getEncoding();
//            System.out.println("编码：" + encoding);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(pathname)), "UTF-8");
            // 这里我们用到了字符操作的BufferedReader类
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                sb.add(str.trim());
//                System.out.println(str);
            }
            // 注意这两个关闭的顺序
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
    public static void main(String[] args) {
        String pathname = "/users/lyn/picPath/5a56ff5210c140369cf607436e57955c.jpg";
        delete(pathname);
    }
}
