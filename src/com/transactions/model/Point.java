package com.transactions.model;


import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 20.04.13
 * Time: 19:58
 */
@Entity
@Table(name = "point")
public class Point{

    @Id
    @Column(name = "point_id", nullable = false)
    private String pointId;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(100) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Point.Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "point_company", joinColumns = @JoinColumn(name = "point_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> partners;

    public List<Company> getPartners() {
        return partners;
    }

    public void setPartners(List<Company> partners) {
        this.partners = partners;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Point.Status getStatus() {
        return status;
    }

    public void setStatus(Point.Status status) {
        this.status = status;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    @Override
    public String toString() {
        return "Point{" +
                "pointId='" + pointId + '\'' +
                ", status=" + status +
                '}';
    }

    public enum Status
    {
        TEST, ACTIVE, INACTIVE, BLOCKED
    }
}

