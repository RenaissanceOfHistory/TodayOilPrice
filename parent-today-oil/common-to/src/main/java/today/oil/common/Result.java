package today.oil.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 项目公共类-响应客户端请求时调用
 * @create: 2023/10/30
 * @Description:
 * @FileName: Result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;       // 响应码
    private String info;    // 响应信息
    private Object data;    // 响应数据

    /** 成功响应方法
     * @Description:
     * @date: 2023/10/31
     * @return: today.oil.common.Result
     */
    public static Result ok() {
        return ok("success", null);
    }

    public static Result ok(String info) {
        return ok(info, null);
    }

    public static Result ok(String info, Object data) {
        return new Result(200, info, data);
    }

    /** 失败响应方法
     * @Description:
     * @date: 2023/10/31
     * @param code 响应码
     * @return: today.oil.common.Result
     */
    public static Result error(int code) {
        return error(code, "error", null);
    }

    public static Result error(int code, String info) {
        return error(code, info, null);
    }

    public static Result error(int code, String info, Object data) {
        return new Result(code, info, data);
    }
}
