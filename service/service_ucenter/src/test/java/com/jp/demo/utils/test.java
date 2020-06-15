package com.jp.demo.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Base64;

public class test {
    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String body_seg() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {
            // 本地文件路径
            String filePath = "C:\\Users\\Administrator\\Downloads\\5.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;
            String access_token_url = "https://aip.baidubce.com/oauth/2.0/token?" +
                    "grant_type=client_credentials&client_id=CCZf1mE34eZjt0fl9SogDTZq" +
                    "&client_secret=ddpSAtnBVB1VGPgSOQrHTgQqeCpTEGtv ";
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            String accessToken = "24.420c6c085923020efdb71f61bf3c54fb.2592000.1594554022.282335-20373115";
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            convert(result, 1200, 900);
//            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void convert(String labelmapBase64, int realWidth, int realHeight) {
        try {

            byte[] bytes = Base64.getDecoder().decode(labelmapBase64);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(is);
            BufferedImage newImage = resize(image, realWidth, realHeight);
            BufferedImage grayImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_BYTE_GRAY);
            for(int i= 0 ; i < realWidth ; i++){
                for(int j = 0 ; j < realHeight; j++){
                    int rgb = newImage.getRGB(i, j);
                    grayImage.setRGB(i, j, rgb * 255);  //将像素存入缓冲区
                }
            }
            File newFile = new File("gray.jpg");
            ImageIO.write(grayImage, "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test.body_seg();
    }
}
