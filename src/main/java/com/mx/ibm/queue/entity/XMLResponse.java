package com.mx.ibm.queue.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.ToString;

@ToString
@XmlRootElement(name = "cepMessage")
public class XMLResponse {

    private Header header;

    @XmlElement
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
    
    public XMLResponse() {
    }

    public XMLResponse(Header header, Content content) {
        super();
        this.header = header;
    }
}
