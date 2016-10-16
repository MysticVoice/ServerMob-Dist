/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

/**
 *
 * @author Fredrik
 */
@Path("chat")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class ChatService {
    
    public static String LAST_MESSAGE_QUERY = 
            "SELECT c.id, m.id, m.sender_id, m.text, m.created" + 
            "From conversation c "+
            "JOIN message m ON c.id = m.conversation_id "+
            ;
    
    @GET
    @Path("rawLastMessage")
}
