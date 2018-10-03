package com.safia.Entity;

public class Student {

    private int id;
    private String name;
    private String course;

    public Student(int id, String name, String course){
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public Student(){}

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
