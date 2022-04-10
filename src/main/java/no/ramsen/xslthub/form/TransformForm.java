package no.ramsen.xslthub.form;

import no.ramsen.xslthub.SourceType;

public class TransformForm {
    private SourceType sourceType;
    private String source;
    private String xsl;
    private String result;

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getXsl() {
        return xsl;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TransformForm(SourceType sourceType, String source, String xsl, String result) {
        this.sourceType = sourceType;
        this.source = source;
        this.xsl = xsl;
        this.result = result;
    }

    public TransformForm(String source, String xsl) {
        this(SourceType.XML, source, xsl, "");
    }

    public TransformForm() {
        this(SourceType.XML, "", "", "");
    }
}
