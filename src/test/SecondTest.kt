package test

import kotlin.test.*
import org.testng.annotations.*

/**
 * TestNG Test Case
 */
public class TestNGStringTest {

    BeforeTest fun setUp() {
        // set up the test case
    }

    AfterTest fun tearDown() {
        // tear down the test case
    }

    Test(groups=array("fast")) fun testCapitalize() {
        assertEquals("Hello world!", "hello world!".capitalize())
    }
}