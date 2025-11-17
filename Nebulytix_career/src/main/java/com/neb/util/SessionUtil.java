package com.neb.util;


import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    public static boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    public static boolean isHR(HttpSession session) {
        return "HR".equals(session.getAttribute("role"));
    }

    public static boolean isEmployee(HttpSession session) {
        return "EMPLOYEE".equals(session.getAttribute("role"));
    }
}
