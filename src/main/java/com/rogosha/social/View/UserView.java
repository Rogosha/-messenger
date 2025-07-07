package com.rogosha.social.View;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;


@Route(value = "/", layout = MainView.class)
@PermitAll
public class UserView  extends VerticalLayout {

    public UserView() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            throw new AccessDeniedException();
        }
        H1 hello = new H1("Hello " + principal.getName() + "!");
        H2 welcome = new H2("Welcome to corporate messenger by Rogosha!");

        add(hello, welcome);
    }
}

