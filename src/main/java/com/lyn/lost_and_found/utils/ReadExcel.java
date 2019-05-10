package com.lyn.lost_and_found.utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.*;

/**
 * 来源：https://www.cnblogs.com/liyafei/p/8146136.html
 */
public class ReadExcel {
    /**
     * fileNumbers={100, 150, 200, 250, 300};{500, 1000, 2000, 5000, 10000}
     */
    private static Integer[] fileNumbers = {500, 1000, 2000, 5000, 10000};
//    private static String corpusDirPrefix = "E:\\lyn\\毕设\\语料库\\生语料库";
//    private static String corpusExcelPath = "E:\\lyn\\毕设\\语料库\\失物招领生语料库.xls";
    private static String corpusDirPrefix = "/Users/colleague/毕业设计/失物招领数据/生语料库/";
    private static String corpusExcelPath = "/Users/colleague/毕业设计/失物招领数据/失物招领生语料库.xls";

    public static void main(String[] args) {
        ReadExcel obj = new ReadExcel();
        // 此处为我创建Excel路径
        File file = new File(corpusExcelPath);
        List excelList = obj.readExcel(file);
        //1生成语料库到本地
        obj.buildCorpus(excelList);
        //2生成测试基础数据到本地
        for (Integer fileNumber : fileNumbers) {
            obj.writeToFile(excelList, fileNumber);
        }
    }

    /**
     * 去读Excel的方法readExcel，该方法的入口参数为一个File对象
     */
    public List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList = new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList = new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if (cellinfo.isEmpty()) {
                            continue;
                        }
                        innerList.add(cellinfo);
//
//                        if (i != 0 && j == 2) {
//                            innerList.add(cellinfo);
//                            String filename = UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".txt";
//                            String path = "E:\\lyn\\毕设\\语料库\\生语料库\\" + filename;
//                            FileUtil.writeFile(path, cellinfo);
//                            System.out.print(cellinfo + "(" + i + "," + j + ")");
//                        }
                    }
                    outerList.add(i, innerList);
//                    System.out.println();
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定生成的文件数量
     *
     * @param excelList
     * @param numbers
     */
    public void writeToFile(List excelList, int numbers) {
        System.out.println("===============================================list中的数据打印出来=========================================================");
        int cnt = 0;
        Map<String, Integer> map = new HashMap<>(numbers);
        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            String titleInfo = null;
            for (int j = 0; j < list.size(); j++) {
                String cellinfo = String.valueOf(list.get(j)).trim();
                String content = titleInfo + " " + cellinfo;
                if (i != 0 && j == 2 && list.get(j) != null && content.length() >= 50 && content.length() <= 250) {
                    if (!map.containsKey(content)) {
                        cnt++;
                        if (cnt > numbers) {
                            break;
                        }
                        map.put(content, 1);
                        System.out.println(content + "位置：(" + i + "," + j + ") 字符数：" + content.length() + " cnt: " + cnt);
                    }
                } else if (i != 0 && j == 1 && list.get(j) != null) {
                    titleInfo = String.valueOf(list.get(j)).trim();
                }
            }
        }
        FileUtil.deleteDir(new File(corpusDirPrefix + "/" + numbers));
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            String cellinfo = next.getKey();
            String filename = UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".txt";
            String path = corpusDirPrefix + "/" + numbers + "/" + filename;
            FileUtil.writeFile(path, cellinfo);
        }

    }

    /**
     * 抽取excel(i,2)数据，生成语料库数据到本地
     *
     * @param excelList
     */
    public void buildCorpus(List excelList) {
        FileUtil.deleteDir(new File(corpusDirPrefix));
        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            String titleInfo = null;
            for (int j = 0; j < list.size(); j++) {
                if (i != 0 && j == 2 && list.get(j) != null) {
                    String cellinfo = String.valueOf(list.get(j)).trim();
                    String filename = UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".txt";
                    String path = corpusDirPrefix + "/" + filename;
                    FileUtil.writeFile(path, titleInfo + " " + cellinfo);
                    System.out.println(titleInfo + "=========================================" + cellinfo);
                } else if (i != 0 && j == 1 && list.get(j) != null) {
                    titleInfo = String.valueOf(list.get(j)).trim();
                }
            }
        }
    }
}

