package com.edu.test;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

public class FtpTest {
    @Test
    public void test2() throws Exception {
        InputStream local = new FileInputStream("C:\\Users\\ASUS\\Desktop\\1.jpg");
        FtpUtil.uploadFile("192.168.152.130",21,"ftpuser","hong",
                "/home/ftpuser/www/images","/2020/09/21","hello2.jpg",local);
    }
    @Test
    public void test() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("192.168.152.130", 21);
        ftpClient.user("ftpuser");
        ftpClient.pass("hong");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");//保存位置
        InputStream local = new FileInputStream("C:\\Users\\ASUS\\Desktop\\1.jpg");
        ftpClient.storeFile("hello.jpg", local);
        ftpClient.logout();//关闭连接
    }
}
