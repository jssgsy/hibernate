package com.univ.many2many.oneway;

/**
 * Univ
 * 2017/1/12 19:04
 */

/**
 *  Category和Item是单向多对多关系，从Category可以找出所有与之关联的Item。
 *
 * 单向的多对多关系是存在的，不过也需要中间表。
 */
public class ItemOne {

    private Long id;
    private String name;

    public ItemOne() {
    }

    public ItemOne(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
