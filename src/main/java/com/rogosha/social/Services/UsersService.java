package com.rogosha.social.Services;

import com.rogosha.social.Entities.Dialog;
import com.rogosha.social.Entities.Message;
import com.rogosha.social.Entities.User;
import com.rogosha.social.Repos.DialogRepo;
import com.rogosha.social.Repos.MessageRepo;
import com.rogosha.social.Repos.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UsersService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DialogRepo dialogRepo;

    @Autowired
    private MessageRepo messageRepo;

    public User getUser(String name){
        User user;
        try {
            user = userRepo.findByUsername(name).orElseThrow();
        } catch (NoSuchElementException e) {
            throw e;
        }
        return user;
    }
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach( user -> users.add(user));
        return users;
    }

    @Transactional
    public List<User> getFriends(Principal principal) {
        List<User> users = new ArrayList<>();
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        List<Message> messages = new ArrayList<>();
        dialogRepo.findByUser1OrUser2(user, user).forEach( dialog ->
                messages.add(dialog.getMessages().getLast()));
        messages.sort(Comparator.comparing(Message::getTimeStamp));
        messages.forEach( message -> {
            Dialog dialog = message.getDialog();
            if (dialog.getUser1().equals(user))
                users.add(dialog.getUser2());
            else users.add(dialog.getUser1());
        });
        return users;
    }

    @Transactional
    public Dialog init(User currentUser, User targetUser ) {
        Dialog dialog = dialogRepo.findByUsers(currentUser, targetUser).orElseGet( () -> {
           Dialog newDialog = new Dialog();
           newDialog.setUser1(currentUser);
           newDialog.setUser2(targetUser);
           List<Message> messages = new ArrayList<>();
           Message initMessage = new Message("Dialog initiate", LocalDateTime.now());
           initMessage.setDialog(newDialog);
           newDialog.getMessages().add(new Message("Dialog initiate", LocalDateTime.now()));
           dialogRepo.save(newDialog);
           messageRepo.save(initMessage);
           return newDialog;
        });
        Hibernate.initialize(dialog.getMessages());
        return dialog;
    }

    public void send(String content, User currentUser, User targetUser) {
        if(!content.isEmpty()){
            Message newMessage = new Message();
            newMessage.setSender(currentUser);
            newMessage.setDialog(findDialog(currentUser, targetUser));
            newMessage.setTimeStamp(LocalDateTime.now());
            newMessage.setContent(content);
            messageRepo.save(newMessage);
        }
    }

    public Dialog findDialog(User user1, User user2) {
        Dialog dialog;
        try {
            dialog = dialogRepo.findByUsers(user1, user2).orElseThrow();
        } catch (NoSuchElementException e) {
            dialog = null;
        }
        return dialog;
    }


}
