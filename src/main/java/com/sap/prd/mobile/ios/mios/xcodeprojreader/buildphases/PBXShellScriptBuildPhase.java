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

import com.sap.prd.mobile.ios.mios.xcodeprojreader.BuildFile;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.Dict;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ElementFactory;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ProjectFile;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.ReferenceArray;

public class PBXShellScriptBuildPhase extends BuildPhase
{
  public static final String isa = PBXShellScriptBuildPhase.class.getSimpleName();

  public PBXShellScriptBuildPhase(ProjectFile projectFile)
  {
    this(projectFile, projectFile.createDict());
  }

  public PBXShellScriptBuildPhase(ProjectFile projectFile, Dict dict)
  {
    super(projectFile, dict);
  }

  public void setDefaultValues()
  {
    Dict d = getDict();
    d.setString("isa", isa);
    d.setArray("files", getProjectFile().createArray());
    d.setArray("inputPaths", getProjectFile().createArray());
    d.setArray("outputPaths", getProjectFile().createArray());
    d.setString("runOnlyForDeploymentPostprocessing", "0");
    d.setString("shellPath", "/bin/sh");
  }

  public ReferenceArray<BuildFile> getFiles()
  {
    return new ReferenceArray<BuildFile>(getProjectFile(), getDict().getOrCreateAndSetArray("files"),
          new BuildFileFactory());
  }

  public ReferenceArray<BuildFile> getInputPaths()
  {
    return new ReferenceArray<BuildFile>(getProjectFile(), getDict().getOrCreateAndSetArray("inputPaths"),
          new BuildFileFactory());
  }

  public ReferenceArray<BuildFile> getOutputPaths()
  {
    return new ReferenceArray<BuildFile>(getProjectFile(), getDict().getOrCreateAndSetArray("outputPaths"),
          new BuildFileFactory());
  }

  public String getRunOnlyForDeploymentPostprocessing()
  {
    return getDict().getString("runOnlyForDeploymentPostprocessing");
  }

  public String getShellPath()
  {
    return getDict().getString("shellPath");
  }

  public String getShellScript()
  {
    return getDict().getString("shellScript");
  }

  public void setShellScript(String script)
  {
    getDict().setString("shellScript", script);
  }

  private static class BuildFileFactory implements ElementFactory<BuildFile>
  {
    @Override
    public BuildFile create(ProjectFile projectFile, Dict dict)
    {
      return new BuildFile(projectFile, dict);
    }
  }
}
