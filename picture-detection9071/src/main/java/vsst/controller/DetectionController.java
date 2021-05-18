package vsst.controller;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vsst.commonResult.CommonResult;
import vsst.utils.FileUploadUtil;

import javax.annotation.Resource;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@Slf4j
public class DetectionController {

    private static final String DIO_URL = "http://python-flask-dio";
    @Resource
    private RestTemplate restTemplate;

    @PostMapping(value = "/detect/object")
    public CommonResult objectDetection(@RequestParam("file")MultipartFile multipartFile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss:mmm");

        Map<String,String> image = new HashMap<>();
        try {
            byte[] imageBytes = multipartFile.getBytes();
            String imageBase64 = Base64.encode(imageBytes);

            image.put("imageData",imageBase64);
            image.put("imageName",multipartFile.getOriginalFilename());
            log.info(dateFormat.format(new Date())+"图片的目标检测，图片名"+multipartFile.getOriginalFilename());
        } catch (IOException e) {
            log.error(dateFormat.format(new Date())+e.getMessage());
        }
       String result = restTemplate.postForObject(DIO_URL + "/yolo/img", image,String.class);
        if(result!=null){
            return CommonResult.success(result);
        }
       return CommonResult.error(500,"python服务器异常，请稍后重试或联系管理员");

    }
    /**
    * @description: 行人重识别
    * @param: pictures
    * @return: vsst.commonResult.CommonResult
    * @author: Ziqiang Lee
    * @date: 2021/3/22
    */
    @PostMapping(value = "/detect/personrid")
    public CommonResult personRid(@RequestParam("files")MultipartFile[] pictures) {
        HashSet<byte[]> bytes = null;
        try {
            bytes = FileUploadUtil.batchUpload(pictures);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = restTemplate.postForObject(DIO_URL + "/personRid", CommonResult.success(bytes), String.class);
        if(response!=null){
            return CommonResult.success();
        }
        return CommonResult.error(500,"服务器未能接受数据，请稍后重试或联系管理员");
    }



}
