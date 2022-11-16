package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String itemImgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public void updateItemImg(String oriImgName, String itemImgName, String imgUrl) {
        this.itemImgName = itemImgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
