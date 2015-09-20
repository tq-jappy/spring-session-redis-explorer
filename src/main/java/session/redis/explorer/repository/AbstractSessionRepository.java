package session.redis.explorer.repository;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 *
 */
abstract class AbstractSessionRepository {

    protected static final Charset CHARSET = StandardCharsets.UTF_8;

    private RedisConnectionFactory redisConnectionFactory;

    protected AbstractSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    protected <T> T execute(Function<RedisConnection, T> block) {
        RedisConnection connection = getConnection();
        try {
            return block.apply(connection);
        } finally {
            connection.close();
        }
    }

    /**
     * @param data
     * @return
     */
    protected Object deserialize(byte[] data) {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            return new String(data, CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RedisConnection getConnection() {
        return redisConnectionFactory.getConnection();
    }
}
