package org.max5.limbus_integrations;

import org.max5.limbus.LimbusEngineImpl;

public class TestEngine extends LimbusEngineImpl {

  @Override
  protected String[] getPublicAccessPackages() {
    return new String[] {
        TestPlugin.class.getPackage()
            .getName()
    };
  }

}