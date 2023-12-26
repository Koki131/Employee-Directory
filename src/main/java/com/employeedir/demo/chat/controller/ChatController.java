package com.employeedir.demo.chat.controller;


import com.employeedir.demo.chat.model.*;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private List<ChatMessage> globalMessages = new ArrayList<>();
    private Set<ChatUser> users = new HashSet<>();

    private Map<String, Map<String, Integer>> messageCount = new HashMap<>();

    private Map<Pair, List<PrivateMessage>> privateMessages = new HashMap<>();

    private ChatUser sender;


    @Autowired
    private UserService userService;



    @GetMapping("/joinChat")
    public String getChat(Model model, Principal principal) {

        User principalUser = userService.findUserByName(principal.getName());


        ChatUser user = new ChatUser(principalUser.getUserName(), principalUser.getImage(), principalUser.getImageData());

        model.addAttribute("currentUser", user);



        if (sender != null) {
            model.addAttribute("notificationSender", sender);
        }


        return "/chat/chat";
    }


    @MessageMapping("/chat.join")
    @SendTo("/topic/join")
    public JoinMessage userJoin(ChatUser user) {

        Map<String, PrivateMessage> allConvos = new HashMap<>();

        // Handle user join event, add user to the chat, and send a notification to the chat

        users.add(user);



        for (ChatUser usr : users) {
            if (!usr.getUserName().equals(user.getUserName())) {
                allConvos.put(usr.getUserName(), null);
            } else {
                usr.setImage(user.getImage());
            }
        }


        for (Pair p : privateMessages.keySet()) {

            List<PrivateMessage> messages = privateMessages.get(p);
            String sender = messages.get(messages.size()-1).getSender().getUserName();
            String recipient = messages.get(messages.size()-1).getRecipient();
            PrivateMessage lastMessage = messages.get(messages.size()-1);

            boolean valid = p.getSender().equals(user.getUserName()) || p.getRecipient().equals(user.getUserName());

            if (valid && allConvos.containsKey(recipient)) {
                allConvos.put(recipient, lastMessage);
            } else if (valid && allConvos.containsKey(sender)) {
                allConvos.put(sender, lastMessage);
            }


        }

        return new JoinMessage(users, globalMessages, messageCount, allConvos);
    }


    @MessageMapping("/chat.send")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(ChatMessage message) {

        // Handle chat message and broadcast it to the chat
        globalMessages.add(message);


        return new ChatMessage(message.getSender(), message.getContent(), globalMessages);

    }



    @MessageMapping("/chat.private.{recipient}")
    public void sendPrivateMessage(@DestinationVariable String recipient, PrivateMessage message) {


        List<PrivateMessage> sessionMsgs;

        ChatUser rec = null;
        ChatUser sdr = null;

        for (ChatUser usr : users) {

            if (usr.getUserName().equals(recipient)) {
                rec = usr;
            }

            if (usr.getUserName().equals(message.getSender().getUserName())) {
                sdr = usr;
            }
        }

        assert rec != null && sdr != null;

        Pair pair1 = new Pair(sdr.getUserName(), rec.getUserName());
        Pair pair2 = new Pair(rec.getUserName(), sdr.getUserName());

        boolean isValid = pair1.getSender().equals(pair2.getRecipient()) && pair1.getRecipient().equals(pair2.getSender());




        if (privateMessages.get(pair1) != null && isValid)  {
            privateMessages.get(pair1).add(message);
            sessionMsgs = new ArrayList<>(privateMessages.get(pair1));
        } else if (privateMessages.get(pair2) != null && isValid) {
            privateMessages.get(pair2).add(message);
            sessionMsgs = new ArrayList<>(privateMessages.get(pair2));
        } else {
            sessionMsgs = new ArrayList<>();
            sessionMsgs.add(message);
            privateMessages.put(pair1, sessionMsgs);
        }

        if (message.getContent().equals("")) {
            if (privateMessages.get(pair1) != null) {
                privateMessages.get(pair1).remove(message);
            }
            else {
                privateMessages.get(pair2).remove(message);
            }
            sessionMsgs.remove(message);
        }


        if (messageCount.get(sdr.getUserName()) != null) {

            Map<String, Integer> temp = messageCount.get(sdr.getUserName());

            temp.putIfAbsent(recipient, 0);
            temp.put(recipient, temp.get(recipient) + 1);

            messageCount.put(sdr.getUserName(), temp);

        } else {

            Map<String, Integer> temp = new HashMap<>();
            temp.put(recipient, 1);
            messageCount.put(sdr.getUserName(), temp);

        }


        simpMessagingTemplate.convertAndSendToUser(recipient, "/private/" + sdr.getUserName(),
                new PrivateMessage(message.getSender(), message.getRecipient(),
                        message.getContent(), sessionMsgs));

    }


    @MessageMapping("/chat.privjet.{sender}.{recipient}")
    public void displayPrivateMessages(@DestinationVariable String sender, @DestinationVariable String recipient) {


        Pair pair1 = new Pair(sender, recipient);
        Pair pair2 = new Pair(recipient, sender);

        boolean isValid = pair1.getSender().equals(pair2.getRecipient()) && pair1.getRecipient().equals(pair2.getSender());

        if (messageCount.get(recipient) != null) {

            Map<String, Integer> temp = messageCount.get(recipient);
            temp.put(sender, 0);
            messageCount.put(recipient, temp);
        }

        if (privateMessages.get(pair1) != null && isValid) {
            simpMessagingTemplate.convertAndSendToUser(recipient, "/privateMessages/" + sender,
                    new SenderRecipientMessages(privateMessages.get(pair1)));

        } else if (privateMessages.get(pair2) != null && isValid) {
            simpMessagingTemplate.convertAndSendToUser(recipient, "/privateMessages/" + sender,
                    new SenderRecipientMessages(privateMessages.get(pair2)));
        }

    }

    @MessageMapping("/chat.notify.{recipient}")
    public void notification(@DestinationVariable String recipient, PrivateMessage message) {


        sender = message.getSender();

        simpMessagingTemplate.convertAndSendToUser(recipient, "/notify", message);

    }




}
