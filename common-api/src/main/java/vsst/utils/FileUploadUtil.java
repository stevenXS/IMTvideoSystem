package vsst.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;

/**
* @description: 用于处理文件上传的相关操作，如单份文件上传，文件批量上传等，
 * 如有其它相关文件上传的函数补充请添加到该类中
* @author: Ziqiang Lee
* @date: 2021/3/18
*/
public class FileUploadUtil {
    /**
    * @description: 单一文件上传，并保存在后台指定路径
    * @param: file 前端上传的文件
 * @param: savePath 保存路径
    * @return: java.lang.String 返回新生成的文件名(包含绝对路径的)
    * @author: Ziqiang Lee
    * @date: 2021/3/18
    */
    public static String singleUpload(MultipartFile multipartFile,String savePath) throws IOException {
        String fileName = savePath+"/"+multipartFile.getOriginalFilename();
        File  file = new File(fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        multipartFile.transferTo(file);
        return fileName;
    }
    /**
    * @description: 单个文件上传 ，转成字节数组
    * @param: multipartFile
    * @return: byte[]
    * @author: Ziqiang Lee
    * @date: 2021/3/18
    */
    public static byte[] singleUpload(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()){
            byte[] fileBytes = multipartFile.getBytes();
            return fileBytes;
        }
        return null;
    }

    /**
    * @description: 文件的批量上传并保存在后台指定路径 ，文件保存失败将抛出IOException
    * @param: multipartFiles 前端上传的批量文件
 * @param: savePath  文件保存路径
    * @author: Ziqiang Lee
    * @date: 2021/3/18
    */
    public static void batchUpload(MultipartFile[] multipartFiles,String savePath) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            File file = new File(savePath + "/" + multipartFile.getOriginalFilename());
            if (!file.exists()){
                file.createNewFile();
            }
            multipartFile.transferTo(file);
        }
    }

    public static HashSet<byte[]> batchUpload(MultipartFile[] multipartFiles) throws IOException {
        HashSet<byte[]> bytes = new HashSet<>();
        for (MultipartFile multipartFile : multipartFiles) {
            bytes.add(multipartFile.getBytes());
        }
        return bytes;
    }

}
