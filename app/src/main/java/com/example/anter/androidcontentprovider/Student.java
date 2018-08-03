package com.example.anter.androidcontentprovider;

/**
 * AndroidContentProvider
 * <p>
 *
 * @author Anter
 * @date 2018/5/23
 */

public class Student {

    private String id;
    private String name;
    private String surname;
    private String marks;

    public Student() {
    }

    public Student(String name, String surname, String marks) {
        this.name = name;
        this.surname = surname;
        this.marks = marks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMarks() {
        return marks;
    }

    public String getId() {
        return id;
    }
}
