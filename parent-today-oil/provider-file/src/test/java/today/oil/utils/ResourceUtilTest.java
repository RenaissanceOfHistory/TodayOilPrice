package today.oil.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.yaml.snakeyaml.events.Event;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class ResourceUtilTest {

    @Test
    void getFileInClassPath() {
    }

    @Test
    void toByte() throws IOException {
        final String LOCAL_PATH = System.getProperty("user.dir") + "/static/upload/img/";
        File dir = new File(LOCAL_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println(dir.exists());
        System.out.println(dir.getAbsolutePath());
    }
}