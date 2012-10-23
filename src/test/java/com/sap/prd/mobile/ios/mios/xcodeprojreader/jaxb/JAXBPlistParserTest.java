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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Array;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Plist;

public class JAXBPlistParserTest
{
  public static final String fileName = "src/test/resources/project.pbxproj.xml";
  public static final String fileNameOpenStep = "src/test/resources/project.pbxproj";

  @Test
  public void load() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    Plist plist = parser.load(fileName);
    assertNotNull(plist);
    assertEquals("1.0", plist.getVersion());
    Dict dict = plist.getDict();
    assertNotNull(dict);

    Dict objects = (Dict) dict.get("objects");
    Dict group = (Dict) objects.get("83FC273A15580501000A0343");
    assertEquals("PBXGroup", group.get("isa"));

    Array children = (Array) group.get("children");
    assertEquals(3, children.size());
    assertEquals("83FC274F15580501000A0343", children.get(0));
    assertEquals("83FC274815580501000A0343", children.get(1));
    assertEquals("83FC274615580501000A0343", children.get(2));
  }

  @Test
  public void convertOpenStepToXML() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    try
    {
      parser.load(fileNameOpenStep);
      Assert.fail();
    }
    catch (javax.xml.bind.UnmarshalException e)
    {
      if (!(e.getCause().getClass() == SAXParseException.class))
      {
        Assert.fail();
      }
    }

    File xmlProj = File.createTempFile("project", ".pbxproj");
    xmlProj.deleteOnExit();

    Plist plist = null;
    try
    {
      parser.convert(fileNameOpenStep, xmlProj.getAbsolutePath());
      plist = parser.load(xmlProj.getAbsolutePath());
    }
    catch (Exception ex)
    {
      // Skip this test if the conversion or loading failed and we're not in Mac OS X
      if (!System.getProperty("os.name").equals("Mac OS X"))
        return;
    }
    assertEquals("1.0", plist.getVersion());
  }

  @Test(expected = javax.xml.bind.UnmarshalException.class)
  public void loadWithUnexpectedElements() throws Exception
  {
    String xml = "<plist><dict><a></a></dict></plist>";
    InputSource source = new InputSource(new StringReader(xml));

    JAXBPlistParser parser = new JAXBPlistParser();
    parser.load(source);
  }

  @Test(expected = javax.xml.bind.UnmarshalException.class)
  public void loadWithUnexpectedDTD() throws Exception
  {
    String xml = "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.xyz.com/DTDs/PropertyList-1.0.dtd\">\n"
          +
          "<plist version=\"1.0\"></plist>";
    InputSource source = new InputSource(new StringReader(xml));

    JAXBPlistParser parser = new JAXBPlistParser();
    parser.load(source);
  }

  @Test
  public void save() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    Plist plist = parser.load(fileName);

    File output = File.createTempFile("proj", ".pbxproj");
    output.deleteOnExit();
    parser.save(plist, output.getAbsolutePath());

    DocumentBuilder db = initDocumentBuilder();
    Document expected = db.parse(new File(fileName));
    Document actual = db.parse(output);
    assertXMLEqual(expected, actual);
  }

  @Test
  public void saveToWriter() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    Plist plist = parser.load(fileName);

    Writer output = new StringWriter();
    parser.save(plist, output);

    DocumentBuilder db = initDocumentBuilder();
    Document expected = db.parse(new File(fileName));
    Document actual = db.parse(new ByteArrayInputStream(output.toString().getBytes()));
    assertXMLEqual(expected, actual);
  }

  private DocumentBuilder initDocumentBuilder() throws Exception
  {
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    db.setEntityResolver(new EntityResolver() {
      @Override
      public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException
      {
        return new InputSource(new StringReader(""));
      }
    });
    return db;
  }

  private void assertXMLEqual(Document expected, Document actual) throws Exception
  {
    XMLUnit.setIgnoreWhitespace(true);
    XMLUnit.setIgnoreComments(true);
    XMLAssert.assertXMLEqual(expected, actual);
  }
}
