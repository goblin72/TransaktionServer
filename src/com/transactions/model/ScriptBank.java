package com.transactions.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 18.06.13
 * Time: 20:57
 */
@Entity
@Table(name = "script_bank", uniqueConstraints=@UniqueConstraint(name = "unq_script_bank", columnNames = {"bank_id", "script_id"}))
public class ScriptBank extends AEntity
{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "script_id")
    private Script script;

    @Column(name = "terminalId", nullable = false)
    private String terminalId;

    @Column(name = "emplId", nullable = false)
    private String emplId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getEmplId() {
        return emplId;
    }

    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }
}
