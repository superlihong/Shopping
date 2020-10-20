package com.edu.portal.service.impl;

import com.edu.common.bean.HttpClientUtil;
import com.edu.portal.service.ContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {
    @Value("${BASE_URL}")
    private String base_url;
    @Value("${BIG_PIC_URL}")
    private String pic_url;
    @Override
    public String getAll() {
        String result=HttpClientUtil.doGet(base_url+pic_url);
        return result;
    }
}
