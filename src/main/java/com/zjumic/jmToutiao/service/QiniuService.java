package com.zjumic.jmToutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone0());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "rainn36EhA3Op6mypEI3W5ww4ANUdihBsjS0OdkS";
    String secretKey = "l5TvTWi9uewANDQNUa7oJtZkcCXBC6cKy7iwsqdK";
    String bucket = "jmimage";
    //默认不指定key的情况下，以文件内容的hash值作为文件名

    //密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    private static String QINIU_IMAGE_DOMAIN = "http://pixo5tqal.bkt.clouddn.com/";

    public String saveImage(MultipartFile file) throws IOException {
        try {
            //同本地上传的方法，检查名字
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!JiemeiUtil.isFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "")
                    + "." + fileExt;
            //调用put方法上传
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            if (response.isOK() && response.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(response.bodyString()).get("key");
            } else {
                logger.error("七牛异常" + response.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            //请求失败时打印的异常信息
            logger.error("七牛异常：" + e.getMessage());
            return null;
        }
    }
}


