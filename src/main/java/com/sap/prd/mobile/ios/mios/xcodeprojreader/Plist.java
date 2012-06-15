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
package com.sap.prd.mobile.ios.mios.xcodeprojreader;

/**
 * The Plist root object. An Xcode project file for example uses the plist format to store its
 * information.
 */
public interface Plist
{

  String getVersion();

  void setVersion(String value);

  /**
   * @return the root dict of the Plist.
   */
  Dict getDict();

  /**
   * Factory method. Creates a new array but does not add it to the plist.
   */
  Array createArray();

  /**
   * Factory method. Creates a new dict but does not add it to the plist.
   */
  Dict createDict();
}