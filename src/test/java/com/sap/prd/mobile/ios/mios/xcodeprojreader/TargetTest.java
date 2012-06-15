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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.buildphases.BuildPhase;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.buildphases.PBXFrameworksBuildPhase;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.buildphases.PBXResourcesBuildPhase;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.buildphases.PBXSourcesBuildPhase;

public class TargetTest
{
  private Target target;

  @Before
  public void before() throws Exception
  {
    target = ProjectFileTest.load().getProject().getTargets().get(0);
  }

  @Test
  public void buildConfigurationList()
  {
    assertNotNull(target.getBuildConfigurationList());
  }

  @Test
  public void buildPhases()
  {
    ReferenceArray<BuildPhase> buildPhases = target.getBuildPhases();
    assertEquals(3, buildPhases.size());
    assertEquals(PBXSourcesBuildPhase.class.getSimpleName(), buildPhases.get(0).getIsA());
    assertEquals(PBXFrameworksBuildPhase.class.getSimpleName(), buildPhases.get(1).getIsA());
    assertEquals(PBXResourcesBuildPhase.class.getSimpleName(), buildPhases.get(2).getIsA());
  }
}
