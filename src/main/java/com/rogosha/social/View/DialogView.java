package com.rogosha.social.View;

import com.rogosha.social.Entities.Dialog;
import com.rogosha.social.Entities.Message;
import com.rogosha.social.Entities.User;
import com.rogosha.social.Services.UsersService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDateTime;

@Route(value = "dialog/:username", layout = MainView.class)
@PermitAll
public class DialogView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    UsersService usersService;

    private final H3 usernameLabel = new H3();
    private User targetUser;
    private User currentUser;
    private Dialog dialog;
    private String name;
    private final Div messagesContainer = new Div();
    private final Button sendButton = new Button("SEND");

    @Override
    @Transactional
    public void beforeEnter(BeforeEnterEvent event) {
        name = event.getRouteParameters().get("username").get();
        usernameLabel.setText(name);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            throw new AccessDeniedException();
        }
        targetUser = usersService.getUser(name);
        currentUser = usersService.getUser(principal.getName());

        TextField textField = new TextField("пиши сюда");
        textField.setWidth("100%");
        sendButton.addClickListener(eventA -> {
            usersService.send(textField.getValue(), currentUser, targetUser);
            updateMessages();
            textField.clear();
        });
        updateMessages();
        Div sender = new Div(textField, sendButton);
        sender.getStyle().set("width", "100%").set("display", "flex").set("justify-content", "center");
        add(messagesContainer, sender);
    }

    @Autowired
    public DialogView(UsersService usersService) {
        messagesContainer.setWidth("100%");
        messagesContainer.getStyle().set("display", "flex")
                .set("flex-direction", "column");
        this.usersService = usersService;
        add(usernameLabel);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        UI.getCurrent().setPollInterval(3000);
        addDetachListener(detachEvent -> UI.getCurrent().setPollInterval(-1));
        UI.getCurrent().addPollListener(pollEvent -> updateMessages());
    }

    @Transactional
    private void updateMessages() {
        messagesContainer.removeAll();
        usersService.init(currentUser, targetUser).getMessages().forEach( message -> {
            Div div = null;
            if (message.getSender() == null){
                div = new Div(new Span(message.getContent()));
                div.getStyle().set("width", "100%").set("display", "flex").set("justify-content", "center");
            } else if (message.getSender().getUsername().equals(targetUser.getUsername())) {
                div = new Div(new Span(message.getSender().getUsername()+ "--->>>  " +  message.getContent()));
                div.getStyle().set("width", "100%").set("display", "flex").set("justify-content", "flex-start");
            } else if (message.getSender().getUsername().equals(currentUser.getUsername())) {
                div = new Div(new Span(message.getContent() + "  <<<---" + message.getSender().getUsername()));
                div.getStyle().set("width", "100%").set("display", "flex").set("justify-content", "flex-end");
            }
            if (div != null) add(div);
            messagesContainer.add(div);
        });
    }
}
