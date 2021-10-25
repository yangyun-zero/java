package com.yangyun.io.nio;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述:
 * @Author: 手握日月摘星辰，世间无我这般人 - yun.Yang
 * @Date: 2021/8/18 20:23
 */
public class FileReadAndWriteutil {

    public static final String CHINESE_REGEX = "[\u4e00-\u9fa5]" ;
    public static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile(CHINESE_REGEX);

    public static void getFiles(String path) throws  Exception {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
//                    System.out.println("目录：" + files[i].getPath());
                    getFiles(files[i].getPath());
                } else {
//                    System.out.println("文件：" + files[i].getPath());
                    String path1 = files[i].getPath();
                    String s = path1.substring(path1.lastIndexOf("\\") + 1, path1.lastIndexOf("."));
                    BufferedReader bufferedReader = readFile(path1);
                    writeFileContent(bufferedReader, s);
                }

            }
        } else {
//            System.out.println("文件：" + file.getPath());
            String path1 = file.getPath();
            String s = path1.substring(path1.lastIndexOf("\\")  + 1, path1.lastIndexOf("."));
            BufferedReader bufferedReader = readFile(file.getPath());
            writeFileContent(bufferedReader, s);
        }
    }

    private static void writeFileContent (BufferedReader br, String fileName) throws  Exception{
        String s = null;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null){
            if (s.contains("@Size")
                    || s.contains("@Max")
                    || s.contains("@NotNull")
                    || s.contains("@Length")
                    || s.contains("@NotBlank")
//                    || !s.startsWith("@ParamOperationLog")
                    || s.contains("@Pattern")
            ){
                if (!s.contains("//")){
                    Matcher matcher = SPECIAL_CHARACTER_PATTERN.matcher(s);
                    if (matcher.find()) {
                        String substring = s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\""));
                        writeFile("dto.txt", substring);
                        writeFile("classname.txt", fileName);
                        writeFile("except.txt", "异常提示");
                        writeFile("project.txt", "物料中台");
                        writeFile("enum.txt", "枚举写死");
                    }
//                    System.out.println(s);
//                    String substring = s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\""));
//                    System.out.println(substring);
                }
            }
        }
    }

    private static BufferedReader readFile(String file) {
        BufferedReader br = null;
        try {
//            String path = EnumRead.class.getClassLoader().getResource(file).getPath();
            File cfgFile = ResourceUtils.getFile(file);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile), "UTF-8"));
        } catch (Exception e) {

        }
        return br;
    }

    public static void writeFile(String fileName, String sql) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true)));
            out.write(sql + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}