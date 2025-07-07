package com.rogosha.social.View;

import com.rogosha.social.Auth.AuthRequest;
import com.rogosha.social.Services.AuthService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    @Autowired
    AuthService authService;

    public RegisterView() {
        H2 heading = new H2("Register Page");
        TextField usernameField = new TextField("Username");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");

        Button registerButton = new Button("Register", event -> {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setUsername(usernameField.getValue());
            authRequest.setEmail(emailField.getValue());
            authRequest.setPassword(passwordField.getValue());

            authService.register(authRequest);

//            RestTemplate restTemplate = new RestTemplate();
//            try{
//                String response = restTemplate.postForObject(
//                        "http://localhost:8080/auth/register", authRequest, String.class);
//                Notification.show(response);
//                getUI().ifPresent(ui -> ui.navigate("login"));
//            } catch (Exception e) {
//                Notification.show("Registration failed: " + e.getMessage());
//            }
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        Button loginButton = new Button("login", event -> {
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        add(heading, usernameField, emailField, passwordField, registerButton, loginButton);
    }
}
