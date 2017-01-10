package com.univ.one2one;

/**
 * Univ
 * 2017/1/10 19:16
 */

/**
 * 身份证
 */
public class IDCard {
    private Integer id;
    private String number;//身份证号码

    private Person person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
