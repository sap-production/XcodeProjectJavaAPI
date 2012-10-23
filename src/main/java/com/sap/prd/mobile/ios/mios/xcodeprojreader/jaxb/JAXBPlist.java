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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Array;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Plist;

@XmlRootElement(name = "plist")
public class JAXBPlist implements Plist
{
  private String version;
  private Dict dict;

  @Override
  @XmlAttribute
  public String getVersion()
  {
    return version;
  }

  @Override
  public void setVersion(String value)
  {
    this.version = value;
  }

  @Override
  @XmlElement(name = "dict", required = true)
  @XmlJavaTypeAdapter(JAXBDictAdapter.class)
  public Dict getDict()
  {
    return dict;
  }

  public void setDict(Dict dict)
  {
    this.dict = dict;
  }

  @Override
  public Array createArray()
  {
    return new ArrayListArray();
  }

  @Override
  public Dict createDict()
  {
    return new LinkedHashMapDict();
  }
}
