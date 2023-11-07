package today.oil.utils;

import lombok.SneakyThrows;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/** 通用工具类
 * @create: 2023/11/3
 * @Description:
 * @FileName: CommonUtil
 */
public class CommonUtil {
    private CommonUtil() {}

    /** 断言 condition，错误时抛出 cls 异常并显示 message 信息
     * @Description:
     * @date: 2023/11/6
     * @param cls 异常类
     * @param condition 条件
     * @param message 条件为false时显示信息
     * @return: void
     */
    @SneakyThrows
    public static <E extends Throwable> void require(Class<E> cls,
                                                     final boolean condition,
                                                     final String message) {
        if (!condition) {
            throw cls.getConstructor(String.class)
                    .newInstance(message);
        }
    }

    public static boolean moveJarTo(String srcDir, String desDir) {
        File src = new File(srcDir);
        require(IllegalArgumentException.class, src.exists() && src.isDirectory(), "Not directory: " + srcDir);

        File des = new File(desDir);
        if (!des.exists()) //noinspection ResultOfMethodCallIgnored
            des.mkdirs();

        final Queue<File> queue = new LinkedList<>();
        queue.offer(src);
        while (!queue.isEmpty()) {
            File dir = queue.poll();
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) {
                    queue.offer(file);
                    continue;
                }

                if (file.getName().endsWith("jar")) {
                    copyFile(file.getAbsolutePath(), desDir);
                }
            }
        }
        return true;
    }

    private static boolean copyFile(String src, String des) {
        final File srcFile = new File(src);
        final File desFile = new File(des, srcFile.getName());

        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(desFile));
             BufferedInputStream input = new BufferedInputStream(new FileInputStream(srcFile))) {
            final byte[] buff = new byte[1024];
            int len;
            while ((len = input.read(buff)) != -1) {
                output.write(buff, 0, len);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return desFile.exists();
    }
}
