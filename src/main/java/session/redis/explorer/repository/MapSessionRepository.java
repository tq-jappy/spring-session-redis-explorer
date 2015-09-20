package session.redis.explorer.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Repository
public class MapSessionRepository extends AbstractSessionRepository {

    @Autowired
    MapSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    public List<MapSession> getAll() {
        return execute(connection -> connection.keys("spring:session:sessions:*".getBytes(CHARSET))
                .stream()
                .filter(key -> connection.type(key).equals(DataType.HASH))
                .map(key -> buildMapSession(new String(key, CHARSET), connection.hGetAll(key)))
                .sorted(Comparator.comparing(MapSession::getCreationTime))
                .collect(Collectors.toList()));
    }

    private MapSession buildMapSession(String key, Map<byte[], byte[]> hGetAll) {
        MapSession session = new MapSession();
        session.setId(key.substring(key.lastIndexOf(":") + 1));
        hGetAll.entrySet().stream()
                .forEach(e -> {
                    String hKey = new String(e.getKey(), CHARSET);
                    switch (hKey) {
                        case "creationTime":
                            session.setCreationTime((Long) deserialize(e.getValue()));
                            break;
                        case "lastAccessedTime":
                            session.setLastAccessedTime((Long) deserialize(e.getValue()));
                            break;
                        case "maxInactiveInterval":
                            session.setMaxInactiveIntervalInSeconds((Integer) deserialize(e.getValue()));
                            break;
                        default:
                            session.setAttribute(hKey, deserialize(e.getValue()));
                            break;
                    }
                });
        return session;
    }
}
