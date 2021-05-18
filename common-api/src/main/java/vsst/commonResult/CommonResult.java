package vsst.commonResult;



import lombok.Data;

/**
* @description: t统一封装返回json数据格式
* @author: Ziqiang Lee
* @date: 2021/3/17
*/
@Data
public class CommonResult {
    //响应码
    private int code;
    //响应是否成功
    private boolean success;
    //响应信息
    private String message;
    //返回数据
    private  Object data;

    /**
    * @description: 构造函数设置为私有，只允许静态方法调用
    * @param:
    * @return:
    * @author: Ziqiang Lee
    * @date: 2021/3/17
    */
    private CommonResult(){

    }

   /**
   * @description: 操作成功的返回json信息调用函数
   * @param:
   * @return: vsst.commonResult.ApiResult
   * @author: Ziqiang Lee
   * @date: 2021/3/17
   */
    public static CommonResult success(){
        return success("成功",null);
    }
    public static CommonResult success(Object data){
        return success("成功",data);
    }
    public static CommonResult success(String message, Object data){
        CommonResult result = new CommonResult();
        result.setCode(200);
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
    * @description: 返回错误信息的json格式调用函数，必须返回错误状态码和错误信息
    * @param: code
 * @param: message
    * @return: vsst.commonResult.ApiResult
    * @author: Ziqiang Lee
    * @date: 2021/3/17
    */
    public static CommonResult error(int code, String message){
        CommonResult result = new CommonResult();
        result.setCode(code);
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

}
