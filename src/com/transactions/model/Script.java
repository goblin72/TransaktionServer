package com.transactions.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:23
 */

@Entity
@Table(name = "script")
public class Script extends AEntity{

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "script_card_type", joinColumns = @JoinColumn(name = "script_id"), inverseJoinColumns = @JoinColumn(name = "card_type_id"))
    private List<CardType> cardTypes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "script")
    private List<ScriptBank> scriptBank;


    public List<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }
}
