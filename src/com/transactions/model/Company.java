package com.transactions.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:15
 */
@Entity
@Table(name = "company")
public class Company{

    @Id
    @Column(name = "code", nullable = false)
    private String code;

//    @Column(name = "status", nullable = false, columnDefinition = "varchar(100) default 'ENABLE'")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Company.Status status;

    @Column(name = "inn")
    private String inn;

    @Column(name = "contract")
    private String contract;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "email")
    private String email;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "script_id")
//    private Script script;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_script", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "script_id"))
    private List<Script> scripts;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public enum Status
    {
        ENABLE,DISABLE
    }

    @Override
    public String toString() {
        return "Company{" +
                "code='" + code + '\'' +
                ", status=" + status +
                ", inn='" + inn + '\'' +
                ", contract='" + contract + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
