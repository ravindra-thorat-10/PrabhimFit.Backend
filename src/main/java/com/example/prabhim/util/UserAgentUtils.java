package com.example.prabhim.util;

import jakarta.servlet.http.HttpServletRequest;

public class UserAgentUtils {

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "127.0.0.1";
        }
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp.trim();
        }
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr == null || remoteAddr.isBlank() || "0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return "127.0.0.1";
        }
        return remoteAddr;
    }

    public static String getDeviceType(HttpServletRequest request) {
        if (request == null) {
            return "Desktop";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Desktop";
        }
        String ua = userAgent.toLowerCase();
        if (ua.contains("tablet") || ua.contains("ipad")) {
            return "Tablet";
        } else if (ua.contains("mobile") || ua.contains("iphone") || ua.contains("android")) {
            return "Mobile";
        }
        return "Desktop";
    }

    public static String getBrowser(HttpServletRequest request) {
        if (request == null) {
            return "Chrome";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            return "Chrome";
        }
        String ua = userAgent.toLowerCase();
        if (ua.contains("edg")) {
            return "Edge";
        } else if (ua.contains("chrome") && !ua.contains("chromium")) {
            return "Chrome";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("firefox")) {
            return "Firefox";
        } else if (ua.contains("postman")) {
            return "Postman";
        }
        return "Chrome";
    }

    public static String getOs(HttpServletRequest request) {
        if (request == null) {
            return "Windows";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            return "Windows";
        }
        String ua = userAgent.toLowerCase();
        if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("mac os") || ua.contains("macos") || ua.contains("macintosh")) {
            return "macOS";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) {
            return "iOS";
        } else if (ua.contains("linux")) {
            return "Linux";
        }
        return "Windows";
    }
}
