package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_detail_img")
@Getter
@Setter
public class ItemDetailImg extends BaseEntity {

    @Id
    @Column(name = "item_detail_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String itemDetailImgName;

    private String oriImgName;

    private String imgUrl;

    public void updateItemDetailImg(String oriImgName, String itemDetailImgName, String imgUrl) {
        this.itemDetailImgName = itemDetailImgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
