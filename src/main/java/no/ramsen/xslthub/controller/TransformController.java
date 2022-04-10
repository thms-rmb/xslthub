package no.ramsen.xslthub.controller;

import net.sf.saxon.s9api.SaxonApiException;
import no.ramsen.xslthub.form.TransformForm;
import no.ramsen.xslthub.service.TransformService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransformController {
    private final TransformService transformService;

    @GetMapping("/transform")
    public String transformGet(Model model) {
        var defaultXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <collection xmlns="http://www.loc.gov/MARC21/slim">
                    <record>
                        <datafield tag="245" ind1=" " ind2=" ">
                            <subfield code="a">The Way of Kings</subfield>
                        </datafield>
                    </record>
                </collection>
                """;
        var defaultXsl = """
                <xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                  <xsl:mode on-no-match="shallow-copy" />
                </xsl:stylesheet>
                """;
        /*var jsonXsl = """
                <xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:array="http://www.w3.org/2005/xpath-functions/array">
                <xsl:output indent="yes" />
                  <xsl:template match=".">
                    <xsl:sequence select="json-to-xml(serialize(., map{'method': 'json'}))" />
                  </xsl:template>
                </xsl:stylesheet>
                """;*/
        model.addAttribute("transformForm", new TransformForm(defaultXml, defaultXsl));

        return "transform";
    }

    @PostMapping("/transform")
    public String transformPost(@ModelAttribute TransformForm transformForm, Model model) {
        String result;
        try {
            result = switch (transformForm.getSourceType()) {
                case XML -> transformService.transformXML(transformForm.getSource(), transformForm.getXsl());
                case JSON -> transformService.transformJSON(transformForm.getSource(), transformForm.getXsl());
            };
        } catch (SaxonApiException e) {
            result = e.getMessage();
        }
        transformForm.setResult(result);

        model.addAttribute("transformForm", transformForm);

        return "transform :: [#result]";
    }

    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }
}
