package com.univ.many2many.twoway;

/**
 * Univ
 * 2017/1/12 19:04
 */

import java.util.HashSet;
import java.util.Set;

/**
 * Category和Item是双向多对多关系，从Category可以找出所有与之关联的Item，反之亦然
 * 单向的多对多关系是存在的，不过也需要中间表。
 */
public class Item {

    private Long id;
    private String name;

    private Set<Category> categorySet = new HashSet<Category>();

    public Item() {
    }

    public Item(String name) {
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

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }
}
