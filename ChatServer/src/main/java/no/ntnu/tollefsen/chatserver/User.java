package no.ntnu.tollefsen.chatserver;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
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
 * @author mikael
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "CHATUSER")
public class User implements Serializable {
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
    
    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    
    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getUpdated() {
        return updated;
    }
    
    public List<Conversation> getConversations() {
        return conversations;
    }

    public Date getCreated() {
        return created;
    }    
    
    public static class UserAdapter extends XmlAdapter<Long, User> {
        @Override
        public User unmarshal(Long v) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Long marshal(User v) throws Exception {
            return v != null ? v.getId() : null;
        }       
    }    
}
