package com.learncode.Utils;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.learncode.Entity.User;
import com.learncode.Sv.UserSv;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtil {
    public static void refreshSession(HttpSession httpSession, UserSv userSv) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null)
            user = userSv.findById(user.getId()).orElse(null);
        if (user != null) {
            Collections.reverse(user.getOrders());
            httpSession.setAttribute("user", user);
        } else
            httpSession.invalidate();
    }
}
