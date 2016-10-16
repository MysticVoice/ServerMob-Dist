package no.ntnu.tollefsen.chatserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author mikael
 */
@Path("chat")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class ChatService {
    
    public static String LAST_MESSAGE_QUERY = 
        "SELECT c.id, m.id, m.sender_id, m.text, m.created " +
        "FROM conversation c " +
        "JOIN message m ON c.id = m.conversation_id " +
        "LEFT JOIN message m2 " +
        "ON c.id = m2.conversation_id AND " +
        "(m.created < m2.created OR m.created = m2.created AND m.id < m2.id) " +
        "WHERE m2.id IS NULL ORDER BY c.created";
    
    @PersistenceContext
    EntityManager em;
    
    @Resource(mappedName="jdbc/chat")
    DataSource dataSource;
    
    @GET
    @Path("users")
    public List<User> getAllUsers() {
        return em.createQuery("select u from User u",User.class).getResultList();
    }

    @GET
    @Path("conversations")
    public List<Conversation> getConversations() {
        return em.createQuery("select c from Conversation c",Conversation.class).getResultList();
    }
    

    @GET
    @Path("rawLastMessage")
    public List<LastConversation> getJDBCLastConversation() {
        List<LastConversation> result = new ArrayList<>();
        
        try(Connection c = dataSource.getConnection();
            Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(LAST_MESSAGE_QUERY);
            while(rs.next()) {
                result.add(new LastConversation(
                    rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4),rs.getDate(5))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    @GET
    @Path("rawJSONLastMessage")
    public JsonArray getJDBCJSONLastConversation() {
        JsonArrayBuilder b = Json.createArrayBuilder();
        try(Connection c = dataSource.getConnection();
            Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(LAST_MESSAGE_QUERY);
           
            while(rs.next()) {
                JsonObjectBuilder cb = Json.createObjectBuilder();
                cb.add("id", rs.getLong(1))
                  .add("message_id", rs.getLong(2))
                  .add("sender_id", rs.getLong(3))
                  .add("text", rs.getString(4))
                  .add("created", rs.getDate(5).toString());
                b.add(cb);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return b.build();
    }
    
    @GET
    @Path("conversations/{userid}")
    public List<Conversation> getConversations(@PathParam("userid") Long userid) {
        return em.createQuery("select c from Conversation c where c.owner.id = :id")
                 .setParameter("id", userid)
                 .getResultList();
    }

    @GET
    @Path("conversationfromdate/{userid}")
    public List<Conversation> getUpdatedConversations(
                @PathParam("userid") Long userid, 
                @QueryParam("from") String date) throws ParseException {
        Date from = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").parse(date);
        return em.createQuery("SELECT c FROM Conversation c INNER JOIN c.messages m " +
                              "WHERE c.owner.id = :id AND m.created >= :date")
                .setParameter("id", userid)
                .setParameter("date",from)
                .getResultList();
    }    
    
    @GET
    @Path("messages")
    public List<Message> getMessages() {
        return em.createQuery("select m from Message m",Message.class).getResultList();
    }
    
    @GET
    @Path("messages/send")
    public Message sendMessage(
            @QueryParam("conversationid") Long conversationid,
            @QueryParam("senderid") Long senderId, @QueryParam("text")String text) {
        Message result = null;
        
        Conversation conversation = em.find(Conversation.class, conversationid);
        User sender = em.getReference(User.class, senderId);
        if(conversation != null && sender != null) {
            result = new Message(text, sender, conversation);
            em.persist(result);
        }
        
        return result;
    }

    @GET
    @Path("createconversation")
    public Conversation createConversation(
            @QueryParam("ownerid") Long ownerId, 
            @QueryParam("recipientids") List<Long> recipientIds) {
        Conversation result = null;
        
        User owner = em.find(User.class, ownerId);
        List<User> recipients = em.createQuery("select u from User u where u.id in :ids", User.class)
                .setParameter("ids",recipientIds)
                .getResultList();
        if(owner != null && recipients.size() > 0) {
            result = new Conversation(owner, recipients);
            em.persist(result);
        }
        
        return result;
    }
    
    @GET
    @Path("createdata")
    public List<Conversation> createConversations() {
        List<Conversation> result = new ArrayList<>();
        
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            users.add(new User("User #" + i));
        }
        
        for(User user : users) {
            for(int i = 0; i < 10; i++) {
                Conversation c = new Conversation(user,getRandomUser(users));
                
                for(int j = 0; j < 10; j++ ) {
                    c.addMessage(
                        new Message("Text from user " + user.getName() + " #" + j,user,c)
                    );
                }
                em.persist(c);
                result.add(c);
            }
        }
        
        return result;
    }
    
    private static List<User> getRandomUser(List<User> users) {
        List result = new ArrayList(users);
        Collections.shuffle(users);
        return result.subList(0, 10);
    }
    
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @NoArgsConstructor
    public static class LastConversation {
        long id;
        List<LastMessage> messages;

        public LastConversation(long id, long messageid, long senderid, String text, Date created) {
            this.id = id;
            this.messages = new ArrayList<>();
            this.messages.add(new LastMessage(messageid,senderid,text,created));
        }        
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @AllArgsConstructor @NoArgsConstructor     
    public static class LastMessage {
        long id;
        long sender;
        String text;
        Date created;
    }
}