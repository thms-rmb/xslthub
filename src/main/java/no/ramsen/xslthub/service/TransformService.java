package no.ramsen.xslthub.service;

import net.sf.saxon.s9api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Service
public class TransformService {
    Logger logger = LoggerFactory.getLogger(TransformService.class);
    private final Processor processor;
    private final XsltCompiler compiler;

    private class SaxonLogger extends net.sf.saxon.lib.Logger {
        @Override
        public void println(String s, int i) {
            if (i == INFO) {
                logger.info(s);
            } else if (i == WARNING) {
                logger.warn(s);
            } else if (i == ERROR || i == DISASTER) {
                logger.error(s);
            }
        }
    }

    public TransformService() {
        this.processor = new Processor(false);
        this.processor.getUnderlyingConfiguration().setLogger(new SaxonLogger());
        this.compiler = this.processor.newXsltCompiler();
    }

    private XdmValue parseXML(String xml) throws SaxonApiException {
        var builder = this.processor.newDocumentBuilder();
        var source = new StreamSource(new StringReader(xml));

        return builder.build(source);
    }

    private XdmValue parseJSON(String json) throws SaxonApiException {
        var builder = this.processor.newJsonBuilder();

        return builder.parseJson(json);
    }

    private String apply(XdmValue selection, Source stylesheet) throws SaxonApiException {
        var executable = this.compiler.compile(stylesheet);
        var transformer = executable.load30();

        var writer = new StringWriter();
        var destination = this.processor.newSerializer(writer);
        transformer.applyTemplates(selection, destination);

        return writer.toString();
    }

    public String transformXML(String xml, String xsl) throws SaxonApiException {
        var selection = this.parseXML(xml);
        var stylesheet = new StreamSource(new StringReader(xsl));

        return this.apply(selection, stylesheet);
    }

    public String transformJSON(String json, String xsl) throws SaxonApiException {
        var selection = this.parseJSON(json);
        var stylesheet = new StreamSource(new StringReader(xsl));

        return this.apply(selection, stylesheet);
    }
}
