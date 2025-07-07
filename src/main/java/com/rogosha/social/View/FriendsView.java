package com.rogosha.social.View;

import com.rogosha.social.Services.UsersService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Route(value = "friends", layout = MainView.class)
@PermitAll
public class FriendsView extends VerticalLayout {
    @Autowired
    public FriendsView(UsersService usersService) {

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        H2 friendsH2 = new H2("Friends:");
        add(friendsH2);
        usersService.getFriends(principal).forEach( currentFriend -> {
            if (currentFriend != null && currentFriend.getUsername() != null){
                add(new RouterLink(currentFriend.getUsername(), DialogView.class,
                        new RouteParameters("username", currentFriend.getUsername())));
                }
            }
        );

        H2 usersH2 = new H2("Start dialog:");
        add(usersH2);
        usersService.getUsers().forEach( currentUser -> {
            if (currentUser != null && currentUser.getUsername() != null &&
                    principal != null &&!(currentUser.getUsername().equals(principal.getName())) ){
                add(new RouterLink(currentUser.getUsername(), DialogView.class,
                        new RouteParameters("username", currentUser.getUsername())));
            }
        });
    }
}
