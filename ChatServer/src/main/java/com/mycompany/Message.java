/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Fredrik
 */
public class Message {
    @Id @GeneratedValue
    Long id;
    
    String text;
    

    @XmlJavaTypeAdapter(User.UserAdapter.class)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    User sender;
    
    @XmlTransient
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    Conversation conversation;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created = new Date();
}
