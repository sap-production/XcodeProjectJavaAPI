/*
 * #%L
 * xcode-project-reader
 * %%
 * Copyright (C) 2012 SAP AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Plist;

public class JAXBPlistParser
{
  private static final String xmlHeaders = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">";

  public Plist load(String projectFile) throws SAXException, ParserConfigurationException, FileNotFoundException,
        JAXBException
  {
    return load(new File(projectFile));
  }

  public Plist load(File projectFile) throws SAXException, ParserConfigurationException, FileNotFoundException,
        JAXBException
  {
    InputSource project = new InputSource(new FileReader(projectFile));
    return unmarshallPlist(project);
  }

  public Plist load(InputSource project) throws SAXException, ParserConfigurationException,
        JAXBException
  {
    return unmarshallPlist(project);
  }

  private Plist unmarshallPlist(InputSource project) throws SAXException,
        ParserConfigurationException,
        JAXBException
  {
    InputSource dtd = new InputSource(this.getClass().getResourceAsStream("/PropertyList-1.0.dtd"));
    SAXSource ss = createSAXSource(project, dtd);
    JAXBContext ctx = JAXBContext.newInstance(com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb.JAXBPlist.class);
    Unmarshaller unmarshaller = ctx.createUnmarshaller();

    // unexpected elements should cause an error
    unmarshaller.setEventHandler(new ValidationEventHandler() {
      @Override
      public boolean handleEvent(ValidationEvent event)
      {
        return false;
      }
    });

    return (Plist) unmarshaller.unmarshal(ss);
  }

  private SAXSource createSAXSource(InputSource project, final InputSource dtd) throws SAXException,
        ParserConfigurationException
  {
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    xmlReader.setEntityResolver(new EntityResolver() {
      @Override
      public InputSource resolveEntity(String pid, String sid) throws SAXException
      {
        if (sid.equals("http://www.apple.com/DTDs/PropertyList-1.0.dtd"))
          return dtd;
        throw new SAXException("unable to resolve remote entity, sid = " + sid);
      }
    });
    SAXSource ss = new SAXSource(xmlReader, project);
    return ss;
  }

  public void save(Plist plist, String projectFile) throws JAXBException
  {
    save(plist, new File(projectFile));
  }

  public void save(Plist plist, File projectFile) throws JAXBException
  {
    try
    {
      save(plist, new FileWriter(projectFile));
    }
    catch (IOException ex)
    {
      throw new JAXBException(ex);
    }
  }

  public void save(Plist plist, Writer projectFile) throws JAXBException
  {
    marshallPlist(plist, projectFile);
  }

  private void marshallPlist(Plist plist, Writer projectFile) throws JAXBException
  {
    JAXBContext ctx = JAXBContext.newInstance(com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb.JAXBPlist.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    try
    {
      marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", xmlHeaders);
    }
    catch(PropertyException ex)
    {
      marshaller.setProperty("com.sun.xml.bind.xmlHeaders", xmlHeaders);
    }
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    marshaller.marshal(plist, projectFile);
  }

  public void convert(String projectFile, String destinationProjectFile) throws IOException
  {
    convert(new File(projectFile), new File(destinationProjectFile));
  }

  public void convert(File projectFile, File destinationProjectFile) throws IOException
  {
    Process exec = Runtime.getRuntime().exec(
          new String[] { "plutil", "-convert", "xml1", "-o", destinationProjectFile.getAbsolutePath(),
              projectFile.getAbsolutePath() });
    try
    {
      exec.waitFor();
    }
    catch (InterruptedException e)
    {
    }

    if (exec.exitValue() != 0)
    {
      throw new RuntimeException("Could not convert file (Exit Code: " + exec.exitValue() + ")");
    }
  }
}
