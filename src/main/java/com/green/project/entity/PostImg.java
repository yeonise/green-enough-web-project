package com.green.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_img")
@Getter
@Setter
public class PostImg extends BaseEntity {
    @Id
    @Column(name = "post_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String postImgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public void updateItemImg(String oriImgName, String postImgName, String imgUrl) {
        this.postImgName = postImgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
