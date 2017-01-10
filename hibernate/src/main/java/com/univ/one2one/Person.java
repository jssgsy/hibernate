package com.univ.one2one;

/**
 * Univ
 * 2017/1/10 19:14
 */

/**
 * 和IDCard是双向一对一关联
 */
public class Person {

    private Integer id;
    private String name;
    private IDCard idCard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IDCard getIdCard() {
        return idCard;
    }

    public void setIdCard(IDCard idCard) {
        this.idCard = idCard;
    }
}
