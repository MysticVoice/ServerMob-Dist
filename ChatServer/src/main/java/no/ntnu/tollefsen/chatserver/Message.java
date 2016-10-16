package no.ntnu.tollefsen.chatserver;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author mikael
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Message implements Serializable {

    @Id @GeneratedValue
    private Long id;

    String text;
    
    @XmlJavaTypeAdapter(User.UserAdapter.class)
    @ManyToOne(optional = false,cascade = CascadeType.PERSIST)
    User sender;

    @XmlTransient
    @ManyToOne(optional = false,cascade = CascadeType.PERSIST)
    Conversation conversation;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created = new Date();

    @Version 
    Timestamp updated;    

    public Message() {
    }

    public Message(String text, User sender, Conversation conversation) {
        this.text = text;
        this.sender = sender;
        this.conversation = conversation;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public Conversation getConversation() {
        return conversation;
    }   
    
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
