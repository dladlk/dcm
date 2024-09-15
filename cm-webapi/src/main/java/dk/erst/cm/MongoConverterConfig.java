package dk.erst.cm;

import com.helger.commons.datetime.XMLOffsetDate;
import com.helger.commons.datetime.XMLOffsetTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class MongoConverterConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new DateToXMLOffsetDate(),
                new DateToXMLOffsetTimeConverter(),
                new XMLOffsetDateToDateConverter(),
                new XMLOffsetTimeToTimeConverter()
        ));
    }

    @Slf4j
    @ReadingConverter
    public static class DateToXMLOffsetDate implements Converter<Date, XMLOffsetDate> {
        @Override
        public XMLOffsetDate convert(Date source) {
            log.debug("DateToXMLOffsetDate {}", source);
            return XMLOffsetDate.ofInstant(source.toInstant(), ZoneOffset.UTC);
        }
    }

    @Slf4j
    @WritingConverter
    public static class XMLOffsetDateToDateConverter implements Converter<XMLOffsetDate, Date> {
        @Override
        public Date convert(XMLOffsetDate source) {
            log.debug("XMLOffsetDateToDateConverter {}", source);
            LocalDate localDate = source.toLocalDate();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(source.getOffset() != null ? source.getOffset() : ZoneOffset.UTC);
            return Date.from(Instant.from(zonedDateTime));
        }
    }

    @Slf4j
    @ReadingConverter
    public static class DateToXMLOffsetTimeConverter implements Converter<Date, XMLOffsetTime> {
        @Override
        public XMLOffsetTime convert(Date source) {
            log.debug("DateToXMLOffsetTimeConverter {}", source);
            return XMLOffsetTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
        }
    }

    @Slf4j
    @WritingConverter
    public static class XMLOffsetTimeToTimeConverter implements Converter<XMLOffsetTime, Date> {
        @Override
        public Date convert(XMLOffsetTime source) {
            log.debug("XMLOffsetTimeToTimeConverter {}", source);
            return Date.from(source.toLocalTime().atDate(LocalDate.now()).atZone(source.getOffset() != null ? source.getOffset() : ZoneOffset.UTC).withZoneSameLocal(ZoneOffset.UTC).toInstant());
        }
    }

}
