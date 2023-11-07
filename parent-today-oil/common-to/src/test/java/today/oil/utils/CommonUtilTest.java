package today.oil.utils;

import org.junit.jupiter.api.Test;

class CommonUtilTest {

    @Test
    void test01() {
        String src = "D:\\MyProgram\\workplace-01\\parent-today-oil";
        String des = "E:\\log\\jar";

        CommonUtil.moveJarTo(src, des);
    }
}