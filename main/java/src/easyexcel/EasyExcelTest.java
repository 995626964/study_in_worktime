package easyexcel;

import com.alibaba.excel.EasyExcel;

public class EasyExcelTest {
    public static void main(String[] args) {
        String url = "C:\\Users\\zhangzhiqiang\\Desktop\\1.xlsx";
        EasyExcel.read(url,DemoData.class,new DemoDataListener()).sheet().doRead();
    }
}
