package org.QinTai.Read.pojo;

import javax.persistence.*;

/**
 * @author wuangjing
 * @create 2020/10/16-14:30
 * @Description:
 */
@Entity
@Table(name = "t_item")
public class ItemTs {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;//'小说标题'
    private String pir;//'小说封面图'
    private String author;//'小说作者'
    private String bidn;//'小说简介'
    private String updatese;//'小说更新章节'
    private String content;//小说具体阅读地址
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

    public String getPir() {
        return pir;
    }

    public void setPir(String pir) {
        this.pir = pir;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBidn() {
        return bidn;
    }

    public void setBidn(String bidn) {
        this.bidn = bidn;
    }

    public String getUpdatese() {
        return updatese;
    }

    public void setUpdatese(String updatese) {
        this.updatese = updatese;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
