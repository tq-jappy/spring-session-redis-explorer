package session.redis.explorer.web;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * helper
 */
@Component
public class DateDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return "date";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Formatter());
        return map;
    }

    public static class Formatter {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public String format(long epochMilli) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli),
                    ZoneId.of("Asia/Tokyo"));
            return zonedDateTime.format(FORMATTER);
        }
    }
}
