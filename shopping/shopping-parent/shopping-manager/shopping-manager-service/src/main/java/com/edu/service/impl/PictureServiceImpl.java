package com.edu.service.impl;

import com.edu.common.bean.FtpUtil;
import com.edu.common.bean.IDUtils;
import com.edu.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class PictureServiceImpl implements PictureService {
    @Value("${FTP_HOST}")
    private String host;
    @Value("${FTP_PORT}")
    private int port;
    @Value("${FTP_USER}")
    private String user;
    @Value("${FTP_PWD}")
    private String pwd;
    @Value("${FTP_BASE_URL}")
    private String baseURL;
    @Value("${PIC_BASE_URL}")
    private String picURL;

    @Override
    public Map<String, Object> uploadImages(MultipartFile uploadFile) throws Exception{
        String oldName=uploadFile.getOriginalFilename();
        String newName= IDUtils.genImageName();
        newName=newName+oldName.substring(oldName.lastIndexOf("."));
        String date=new DateTime().toString("/yyyy/MM/dd");
        boolean flag= FtpUtil.uploadFile(host,port,user,pwd,baseURL,date,newName,uploadFile.getInputStream());
        Map<String ,Object> map=new HashMap<>();
        if(flag){
            map.put("error",0);
            map.put("url",picURL+date+"/"+newName);
        }else {
            map.put("error",1);
            map.put("message","上传错误");
        }
        return map;
    }
}
