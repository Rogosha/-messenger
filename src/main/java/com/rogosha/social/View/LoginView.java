package com.rogosha.social.View;

import com.rogosha.social.Repos.UserRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;



@Route(value = "login" )
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    @Autowired
    private UserRepo userRepo;

    public LoginView() {
        setSizeFull();
        var login = new LoginForm();
        login.setAction("login");
        Button registerButton = new Button("Register", event -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });
        add(login, registerButton);
    }
}
