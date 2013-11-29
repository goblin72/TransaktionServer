package com.transactions.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 13:49
 */
@Entity
@Table(name = "payment")
public class Payment extends AEntity{
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Payment.Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "point_id",nullable = false)
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_bank_id",nullable = false)
    private ScriptBank scriptBank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "paymentDate", nullable = false)
    private Date paymentDate;

    @Column(name = "track2", nullable = false)
    private String track2;

    @Column(name = "ksn", nullable = false)
    private String ksn;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "payment")
    private List<PaymentParam> parameters;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<PaymentParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<PaymentParam> parameters) {
        this.parameters = parameters;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public ScriptBank getScriptBank() {
        return scriptBank;
    }

    public void setScriptBank(ScriptBank scriptBank) {
        this.scriptBank = scriptBank;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public enum Status
    {
        NOT_CONFIRMED,
        POST,
        PASS,
        REJECTED
    }

    @Override
    public String toString() {
        return "Payment{" +
                "status=" + status +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", track2='" + track2 + '\'' +
                ", ksn='" + ksn + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
