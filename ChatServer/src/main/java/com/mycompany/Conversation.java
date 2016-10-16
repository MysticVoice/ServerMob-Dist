/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author Fredrik
 */
public class Conversation {
    @Id @GeneratedValue
    Long id;
    List<User> recipients;
    
    @Version
    Timestamp updated;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created = new Date();
    
    @OneToMany(mappedBy = "conversation",cascade = {CascadeType.PERSIST})
    User owner;
    
    public Conversation()
    {}
    
    public Conversation(User owner, List<User> recipients)
    {
        this.owner = owner;
        this.recipients = recipients;
    }
    
    public Long getId(){
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
}
