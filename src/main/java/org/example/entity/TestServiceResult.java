package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "TESTSERVICERESULT")
public class TestServiceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "\"NUMBER\"")
    private int number;

    @Column(name = "FILENAMES")
    private String fileNames;

    @Column(name = "ERROR")
    private String error;

    public TestServiceResult() {
    }

    public TestServiceResult(String code, int number, String fileNames, String error) {
        this.code = code;
        this.number = number;
        this.fileNames = fileNames;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getNumber() {
        return number;
    }

    public String getFileNames() {
        return fileNames;
    }

    public String getError() {
        return error;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "models.Result{" +
                "ID=" + id +
                ", CODE='" + code + '\'' +
                ", NUMBER=" + number +
                ", FILENAMES='" + fileNames + '\'' +
                ", ERROR='" + error + '\'' +
                '}';
    }

}
