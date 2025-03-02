package session;

import java.util.HashMap;
import java.util.Map;

public class TestSession {

    private static Map<String, Object> sessionState;

    public static Map<String, Object> getSessionState() {
        sessionState = sessionState == null ? new HashMap<String, Object>() : sessionState;
        return sessionState;
    }

    public static synchronized boolean setSessionVariable(String key, Object value) {
        getSessionState().put(key, value);
        return true;
    }

    public static Object getSessionVariable(String key) {
        return getSessionState().get(key);
    }

    public static synchronized <T> T sessionVariableCalled(String key) {
        return (T) getSessionState().get(key);
    }

    public static void addAll(Map<String, Object> sessionMap) {
        getSessionState().putAll(sessionMap);
    }

    public static boolean isContains(String key) {
        return getSessionState().containsKey(key);
    }
}
