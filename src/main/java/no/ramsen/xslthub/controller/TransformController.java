package no.ramsen.xslthub.controller;

import net.sf.saxon.s9api.SaxonApiException;
import no.ramsen.xslthub.form.TransformForm;
import no.ramsen.xslthub.service.TransformService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Controller
public class TransformController {
    private final TransformService transformService;

    @GetMapping("/transform")
    public String transformGet(
            @ModelAttribute TransformForm transformForm,
            Model model,
            @RequestHeader Map<String, String> headers
    ) {
        var source = transformForm.getSource();
        var xsl = transformForm.getXsl();
        String result;
        try {
            result = switch (transformForm.getSourceType()) {
                case XML -> transformService.transformXML(source, xsl);
                case JSON -> transformService.transformJSON(source, xsl);
            };
        } catch (SaxonApiException e) {
            result = e.getMessage();
        }

        model.addAttribute("result", result);
        model.addAttribute("transformForm", transformForm);

        if (headers.getOrDefault("hx-request", "false").equals("true")) {
            return "transform :: [#result]";
        } else {
            return "transform";
        }
    }

    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }
}
