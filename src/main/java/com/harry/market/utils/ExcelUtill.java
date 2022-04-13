package com.harry.market.utils;

import com.harry.market.entity.UserDetails;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtill {
    private XSSFSheet sheet;

    public static void main(String[] args) {
        ExcelUtill data = new ExcelUtill("D:\\Code\\project\\coporation\\data.xlsx", "data");
        System.out.println(data.getExcelDateByIndex(1, 0));
    }

    /**
     * 构造函数，初始化excel数据
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    public ExcelUtill(String filePath, String sheetName){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据行和列的索引获取单元格的数据
     * @param row
     * @param column
     * @return
     */
    public String getExcelDateByIndex(int row,int column){
        XSSFRow row1 = sheet.getRow(row);
        String cell = row1.getCell(column).toString();
        return cell;
    }

    public List<UserDetails> getExcelUser(int row,int num) {
        ArrayList<UserDetails> users = new ArrayList<>();

        for (int i = row; i < num ; i++) {
            UserDetails userDetails = new UserDetails();

            String username;
            String nickname;
            String head;
            Integer gender;
            Integer age;
            String tel;
            Integer qq;
            String introduction;

            username = getExcelDateByIndex(i,0);
            nickname = getExcelDateByIndex(i,2);
            head = getExcelDateByIndex(i,33);
            if ("男".equals(getExcelDateByIndex(i,4))) {
                gender = 1;
            } else if ("女".equals(getExcelDateByIndex(i,4))) {
                gender = 2;
            } else {
                gender = 0;
            }

            age = Integer.valueOf(getExcelDateByIndex(i,40).substring(0,2));
            tel = getExcelDateByIndex(i,6).replaceAll(" ","").substring(0,11);
            introduction = getExcelDateByIndex(i,14);

            userDetails.setUsername(username);
            userDetails.setNickname(nickname);
            userDetails.setHead(head);
            userDetails.setGender(gender);
            userDetails.setAge(age);
            userDetails.setTel(tel);
            userDetails.setIntroduction(introduction);

            users.add(userDetails);
        }
        return users;
    }

}