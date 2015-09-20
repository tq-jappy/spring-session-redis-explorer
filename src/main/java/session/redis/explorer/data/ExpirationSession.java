package session.redis.explorer.data;

import java.util.List;

/**
 *
 */
public class ExpirationSession {

    private long expiration;

    private List<String> sessionIdList;

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public List<String> getSessionIdList() {
        return sessionIdList;
    }

    public void setSessionIdList(List<String> sessionIdList) {
        this.sessionIdList = sessionIdList;
    }
}
