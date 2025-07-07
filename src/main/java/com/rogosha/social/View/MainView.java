package com.rogosha.social.View;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
public class MainView extends AppLayout{
    public MainView() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Messenger");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        VerticalLayout menu = new VerticalLayout();
        menu.add(new RouterLink("Main Page", UserView.class));
        menu.add(new RouterLink("Friends", FriendsView.class));
        menu.add(new RouterLink("Exit", LoginView.class));

        addToNavbar(toggle, title);
        addToDrawer(menu);
    }

}
