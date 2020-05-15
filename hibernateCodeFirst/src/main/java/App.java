import engine.Engine;

/*

 -- ! to Create Demo DB with (Students,Teachers derived from Person derived from BasicEntity) demoHcf mark
 package as included ! --

 */

public class App {
    public static void main(String[] args) {

        Engine engine = new Engine("jpa_inheritance");
        engine.run();
    }
}
