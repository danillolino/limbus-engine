package org.integrationtest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.max5.limbus.Classpath;
import org.max5.limbus.LimbusDefaultComponents;
import org.max5.limbus.LimbusEngine;
import org.max5.limbus.files.InMemoryFilesystemImpl;
import org.max5.limbus.files.LimbusFileService;
import org.max5.limbus.launcher.LimbusStage;
import org.max5.limbus.launcher.LimbusStaging;
import org.max5.limbus.monitoring.MonitoringFactory;
import org.mockito.junit.MockitoJUnitRunner;

@Ignore // schuettec - 18.04.2017 : This test actually does not perform any assertions. Therefore it is set to ignore.
@RunWith(MockitoJUnitRunner.class)
public class TestClientIntegration {

  private static final String DEPLOY_NAME = "deployName";

  private LimbusStage stage;

  private LimbusEngine engine;

  @BeforeClass
  public static void beforeClass() throws Exception {
    LimbusStaging.prepareEnvironment();
    MonitoringFactory
        .configureMonitoring(TestClientIntegration.class.getResource("/org/integrationtest/monitoring.xml"));
  }

  @AfterClass
  public static void afterClass() {
    LimbusStaging.resetEnvironment();
    MonitoringFactory.shutdown();
  }

  @Before
  public void before() throws Exception {

    InMemoryFilesystemImpl filesystem = new InMemoryFilesystemImpl();

    // @formatter:off
     this.stage = LimbusStaging.create(DEPLOY_NAME)
                 .andClasses(TestPluginImpl.class)
                 .withDefaultLimbusComponents(new LimbusDefaultComponents())
                 /*
                  *  schuettec - 18.04.2017 : Add the TestEngine, because the proprietary plugin interface
                  *  org.testclient.TestPlugin is used
                  */
                 .addComponentConfiguration(LimbusEngine.class, TestEngine.class)
                 .addPublicComponentMock(LimbusFileService.class, filesystem)
                 .buildStage();
    // @formatter:on
    stage.startStage();

    this.engine = stage.getComponent(LimbusEngine.class);
  }

  @After
  public void after() {
    if (stage != null) {
      stage.stopStage();
    }
  }

  @Test
  public void test_monitoring_and_undeploy() throws Exception {
    Classpath classpath = engine.getClasspath(DEPLOY_NAME);
    TestPlugin plugin = engine.getPlugin(classpath, TestPluginImpl.class.getName(), TestPlugin.class);
    plugin.callMonitoring();
    engine.undeployPlugin(classpath);
    // TODO - schuettec - 18.04.2017 : How to assert that the plugin could be garbage collected.
  }

  @Test
  public void test_hold_publisher_instance_and_undeploy() throws Exception {
    Classpath classpath = engine.getClasspath(DEPLOY_NAME);
    TestPlugin plugin = engine.getPlugin(classpath, TestPluginImpl.class.getName(), TestPlugin.class);
    plugin.callMonitoring();
    plugin.holdPublisher();
    engine.undeployPlugin(classpath);
    // TODO - schuettec - 18.04.2017 : How to assert that the plugin could be garbage collected.
  }

}