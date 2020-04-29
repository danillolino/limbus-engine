module com.remondis.limbus.system {
  exports com.remondis.limbus.system;

  requires com.remondis.limbus.api;
  requires com.remondis.limbus.system.api;
  requires com.remondis.limbus.events;
  requires com.remondis.limbus.utils;

  requires org.slf4j;
  requires java.desktop;
}