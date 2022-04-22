package com.example.springboot.security.validation;

import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.jasig.cas.client.validation.AbstractCasProtocolUrlBasedTicketValidator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.jasig.cas.client.validation.TicketValidationException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.*;

public class CasRestServiceTicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {

    public CasRestServiceTicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    @Override
    protected String getUrlSuffix() {
        return "serviceValidate";
    }

    @Override
    protected Assertion parseResponseFromServer(String response) throws TicketValidationException {
        final String error = parseAuthenticationFailureFromResponse(response);

        if (CommonUtils.isNotBlank(error)) {
            throw new TicketValidationException(error);
        }

        final String principal = parsePrincipalFromResponse(response);

        if (CommonUtils.isEmpty(principal)) {
            throw new TicketValidationException("No principal was found in the response from the CAS server.");
        }

        final Assertion assertion;
        final Map<String, Object> attributes = extractCustomAttributes(response);
        assertion = new AssertionImpl(new AttributePrincipalImpl(principal, attributes));

        customParseResponse(response, assertion);

        return assertion;
    }

    protected String parsePrincipalFromResponse(final String response) {
        return XmlUtils.getTextForElement(response, "user");
    }

    protected String parseAuthenticationFailureFromResponse(final String response) {
        return XmlUtils.getTextForElement(response, "authenticationFailure");
    }

    protected Map<String, Object> extractCustomAttributes(final String xml) {
        final SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        try {
            final SAXParser saxParser = spf.newSAXParser();
            final XMLReader xmlReader = saxParser.getXMLReader();
            final CustomAttributeHandler handler = new CustomAttributeHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xml)));
            Map<String, Object> result = handler.getAttributes();
            if(result.containsKey("password")){
                result.remove("password");
            }
            return result;
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private class CustomAttributeHandler extends DefaultHandler {

        private Map<String, Object> attributes;

        private boolean foundAttributes;

        private String currentAttribute;

        private StringBuilder value;

        @Override
        public void startDocument() throws SAXException {
            this.attributes = new HashMap<String, Object>();
        }

        @Override
        public void startElement(final String namespaceURI, final String localName, final String qName,
                                 final Attributes attributes) throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = true;
            } else if (this.foundAttributes) {
                this.value = new StringBuilder();
                this.currentAttribute = localName;
            }
        }

        @Override
        public void characters(final char[] chars, final int start, final int length) throws SAXException {
            if (this.currentAttribute != null) {
                value.append(chars, start, length);
            }
        }

        @Override
        public void endElement(final String namespaceURI, final String localName, final String qName)
                throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = false;
                this.currentAttribute = null;
            } else if (this.foundAttributes) {
                final Object o = this.attributes.get(this.currentAttribute);

                if (o == null) {
                    this.attributes.put(this.currentAttribute, this.value.toString());
                } else {
                    final List<Object> items;
                    if (o instanceof List) {
                        items = (List<Object>) o;
                    } else {
                        items = new LinkedList<Object>();
                        items.add(o);
                        this.attributes.put(this.currentAttribute, items);
                    }
                    items.add(this.value.toString());
                }
            }
        }

        public Map<String, Object> getAttributes() {
            return this.attributes;
        }
    }

    protected void customParseResponse(final String response, final Assertion assertion)
            throws TicketValidationException {
        // nothing to do
    }
}
