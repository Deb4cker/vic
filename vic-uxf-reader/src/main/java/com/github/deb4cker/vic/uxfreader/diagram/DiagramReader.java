package com.github.deb4cker.vic.uxfreader.diagram;

import com.github.deb4cker.vic.commons.interfaces.Loggable;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class DiagramReader implements Loggable
{

    private Unmarshaller unmarshaller;
    private File xmlFile;

    public DiagramReader(File xmlFile) {
        try {
            this.xmlFile = xmlFile;
            JAXBContext jaxbContext = JAXBContext.newInstance(Diagram.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            println(e.getMessage());
        }
    }

    public Diagram getDiagram() {
        try {
            return (Diagram) unmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            println("Não foi possível converter o diagrama! Erro: " + e.getMessage());
        }
        return null;
    }

    public Diagram getDiagram(File xmlFile) {
        setXmlFile(xmlFile);
        return getDiagram();
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }
}
