package session.redis.explorer.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Repository;
import session.redis.explorer.data.ExpirationSession;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Repository
public class ExpirationSessionRepository extends AbstractSessionRepository {

    @Autowired
    ExpirationSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    public List<ExpirationSession> getAll() {
        return execute(connection -> connection.keys("spring:session:expirations:*".getBytes(CHARSET))
                .stream()
                .filter(key -> connection.type(key).equals(DataType.SET))
                .map(key -> buildExpirationSession(new String(key, CHARSET), connection.sMembers(key)))
                .collect(Collectors.toList()));
    }

    private ExpirationSession buildExpirationSession(String key, Set<byte[]> sMembers) {
        ExpirationSession session = new ExpirationSession();
        session.setExpiration(Long.valueOf(key.substring(key.lastIndexOf(":") + 1)));
        session.setSessionIdList(
                sMembers.stream()
                        .map(member -> (String) deserialize(member))
                        .collect(Collectors.toList())
        );
        return session;
    }
}
