package com.edu.portal.bean;

import org.springframework.util.StringUtils;

//购物项实体bean
public class CartItem {
    private Long id;
    private String title;
    private String image;
    private String[] images;
    private long price;
    private int num;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getImages() {
        String im=getImage();
        if(!StringUtils.isEmpty(im)){
            return im.split(",");
        }
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
