package com.github.deb4cker.vic.uxfreader.diagram;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

import static com.github.deb4cker.vic.commons.ApplicationIO.STANDARD_OUT;

public class DiagramReader {

    private Unmarshaller unmarshaller;
    private File xmlFile;

    public DiagramReader(File xmlFile) {
        try {
            this.xmlFile = xmlFile;
            JAXBContext jaxbContext = JAXBContext.newInstance(Diagram.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            STANDARD_OUT.println(e.getMessage());
        }
    }

    public Diagram getDiagram() {
        try {
            return (Diagram) unmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            STANDARD_OUT.println("Não foi possível converter o diagrama! Erro: " + e.getMessage());
        }
        return null;
    }
}
