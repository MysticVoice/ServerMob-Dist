/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Fredrik
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "CHATUSER")
public class User implements Serializable{
    @Id @GeneratedValue
    Long id;
    
    String name;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created = new Date();
    
    @Version
    Timestamp updated;
    
    @XmlTransient
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    List<Conversation> conversations;
    
    public User(){}
    
    public User(String name){this.name = name;}
    
    public Long getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public static class UserAdapter extends XmlAdapter<Long, User>{
        
        @Override
        public Long marshal(User v) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public User unmarshal(Long v) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
