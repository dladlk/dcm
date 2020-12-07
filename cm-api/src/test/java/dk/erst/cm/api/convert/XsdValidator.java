package dk.erst.cm.api.convert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import lombok.Getter;
import lombok.Setter;

public class XsdValidator {

	private Validator validator;

	public XsdValidator(String schemaPath) throws Exception {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File(schemaPath));
		validator = schema.newValidator();
	}

	public List<Message> validate(InputStream in) throws SAXException, IOException {
		SchemaErrorHandler errorHandler = new SchemaErrorHandler();
		validator.setErrorHandler(errorHandler);
		validator.validate(new StreamSource(in));
		return errorHandler.getResult();
	}

	public static enum MessageLevel {
		ERROR, FATAL, WARNING;

		public boolean isError() {
			return this != WARNING;
		}
	}

	@Getter
	@Setter
	public static class Message {
		private MessageLevel level;
		private int lineNumber;
		private int columnNumber;
		private String message;

		public Message(MessageLevel level, int lineNumber, int columnNumber, String message) {
			this.level = level;
			this.lineNumber = lineNumber;
			this.columnNumber = columnNumber;
			this.message = message;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(level);
			sb.append(" ");
			sb.append(lineNumber);
			sb.append(":");
			sb.append(columnNumber);
			sb.append(" ");
			sb.append(message);
			return sb.toString();
		}
	}

	private static class SchemaErrorHandler implements ErrorHandler {

		private final List<Message> parseStatusList = new ArrayList<Message>();

		private void add(MessageLevel level, SAXParseException arg0) {
			parseStatusList.add(new Message(level, arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getMessage()));
		}

		public void error(SAXParseException arg0) {
			add(MessageLevel.ERROR, arg0);
		}

		public void fatalError(SAXParseException arg0) {
			add(MessageLevel.FATAL, arg0);
		}

		public void warning(SAXParseException arg0) {
			add(MessageLevel.WARNING, arg0);
		}

		public List<Message> getResult() {
			return Collections.unmodifiableList(parseStatusList);
		}
	}

}
