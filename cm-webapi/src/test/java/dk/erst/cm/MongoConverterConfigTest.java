package dk.erst.cm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.helger.commons.datetime.XMLOffsetDate;
import com.helger.commons.datetime.XMLOffsetTime;

import dk.erst.cm.MongoConverterConfig.DateToXMLOffsetDate;
import dk.erst.cm.MongoConverterConfig.DateToXMLOffsetTimeConverter;
import dk.erst.cm.MongoConverterConfig.XMLOffsetDateToDateConverter;
import dk.erst.cm.MongoConverterConfig.XMLOffsetTimeToTimeConverter;

class MongoConverterConfigTest {

	@Test
	void testDateConversions() {
		DateToXMLOffsetDate dateToXml = new DateToXMLOffsetDate();
		XMLOffsetDateToDateConverter xmlToDate = new XMLOffsetDateToDateConverter();

		ZoneOffset[] testZones = new ZoneOffset[] { ZoneOffset.UTC, null };

		for (int i = 0; i < testZones.length; i++) {
			ZoneOffset fromZone = testZones[i];

			LocalDate fromLocalDate = LocalDate.now();
			Date convert = xmlToDate.convert(XMLOffsetDate.of(fromLocalDate, fromZone));

			XMLOffsetDate res = dateToXml.convert(convert);
			assertEquals(fromLocalDate, res.toLocalDate(), "Zone " + fromZone);
			assertEquals(Optional.ofNullable(fromZone).orElse(ZoneOffset.UTC), res.getOffset(), "Zone " + fromZone);
		}
	}

	@Test
	void testTimeConversions() {
		DateToXMLOffsetTimeConverter timeToXml = new DateToXMLOffsetTimeConverter();
		XMLOffsetTimeToTimeConverter xmlToTime = new XMLOffsetTimeToTimeConverter();

		// TODO: Implement Zones other than UTC...
		ZoneOffset[] testZones = new ZoneOffset[] { null,
				// ZoneOffset.ofHours(2),
				// ZoneOffset.MAX, ZoneOffset.MIN,
				ZoneOffset.UTC };

		for (int i = 0; i < testZones.length; i++) {
			ZoneOffset fromZone = testZones[i];
			LocalTime fromLocalTime = LocalTime.now();
			Date convert = xmlToTime.convert(XMLOffsetTime.of(fromLocalTime, fromZone));

			System.out.println(convert);

			XMLOffsetTime res = timeToXml.convert(convert);
			assertEquals(fromLocalTime, res.toLocalTime(), "Zone " + fromZone);
			assertEquals(Optional.ofNullable(fromZone).orElse(ZoneOffset.UTC), res.getOffset(), "Zone " + fromZone);
		}
	}

}
