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
package com.sap.prd.mobile.ios.mios.xcodeprojreader.buildphases;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ProjectFile;

public class PBXCopyFilesBuildPhase extends BuildPhase
{
  public PBXCopyFilesBuildPhase(ProjectFile projectFile)
  {
    this(projectFile, projectFile.createDict());
  }

  PBXCopyFilesBuildPhase(ProjectFile projectFile, Dict buildConfigurationList)
  {
    super(projectFile, buildConfigurationList);
  }
}
