package event_bus_test.setup;

import io.github.shared.events.Event;

public class SimpleEvent extends Event {

   private final String name;
   private final String phone;

   public SimpleEvent(Object source, String name, String phone) {
      super(source);
      this.name = name;
      this.phone = phone;
   }

   public String getName() {
      return name;
   }

   public String getPhone() {
      return phone;
   }
}
