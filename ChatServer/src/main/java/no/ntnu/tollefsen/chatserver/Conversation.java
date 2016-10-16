package no.ntnu.tollefsen.chatserver;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import no.ntnu.tollefsen.chatserver.User.UserAdapter;

/**
 *
 * @author mikael
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Conversation implements Serializable {
       
    @Id @GeneratedValue
    Long id;

    @Version 
    Timestamp updated;

    @Temporal(javax.persistence.TemporalType.DATE)
    Date created = new Date();
    
    @OneToMany(mappedBy = "conversation",cascade = CascadeType.ALL)
    List<Message> messages;

    @XmlJavaTypeAdapter(UserAdapter.class)
    @ManyToMany(cascade = {CascadeType.PERSIST})
    List<User> recipients;

    @XmlJavaTypeAdapter(UserAdapter.class)
    @ManyToOne(optional = false,cascade = CascadeType.PERSIST)
    User owner;
    
    public Conversation() {
    }

    public Conversation(User owner, List<User> recipients) {
        this.owner = owner;
        this.recipients = recipients;
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Message> getMessages() {
        if(messages == null) {
            messages = new ArrayList<>();
        }
        
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<User> recipients) {
        this.recipients = recipients;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addMessage(Message message) {
       getMessages().add(message);
    }
}
