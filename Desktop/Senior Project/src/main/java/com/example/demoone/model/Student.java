package com.example.demoone.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Student {

    private final StringProperty fName = new SimpleStringProperty();
    private final StringProperty lName = new SimpleStringProperty();
    private final StringProperty cName = new SimpleStringProperty();
    private List<Flaw> results = Collections.EMPTY_LIST;
    private File csv = new File("");
    String path;

    public Student() {
        this("", "", "","", Collections.EMPTY_LIST);
    }

    public Student(String fName, String lName, String cName, String path, List<Flaw> results) {
        lNameProperty().set(lName);
        fNameProperty().set(fName);
        cNameProperty().set(cName);
        this.results = results;
        this.path = path;
    }

    public StringProperty lNameProperty() {
        return lName ;
    }

    public StringProperty fNameProperty() {
        return fName ;
    }

    public StringProperty cNameProperty() {
        return cName ;
    }

    public String getFlaws() {
        String s = "";
        for (Flaw f : results) {
            s += f.line();
        }
        System.out.println(s);
        return s;
    }

    public void setFile(File f) {
        csv = f;
    }

    public File getFile() {
        return csv;
    }
}
