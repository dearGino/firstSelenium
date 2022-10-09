package src.test.java;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClassTest.class })
public class MyTestSuite {

}


//First, we need to create a class where no @Test annotation is used.
//It is a simple class with a single method for testing.
//Since this is the eclipse it will not run the class as a JUnit class
//because of the method with any @Test annotation.
//So, we will create another class with Suite runner.