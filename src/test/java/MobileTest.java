import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MobileTest {
    AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        File app = new File("lib/MobiCalc.1.4.2free.apk");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        caps.setCapability("noReset", false);
        caps.setCapability("fullReset", true);
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.3");
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "C1905");
        caps.setCapability("appPackage", "my.android.calc");
        caps.setCapability("appActivity", "my.android.calc.MainActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void passLicenseAgreement() {
        MobileElement acceptButton = (MobileElement) driver.findElement(By.id("android:id/button1"));
        acceptButton.click();
        acceptButton.click();
        acceptButton.click();
    }

    @Test
    @Parameters({"a", "b"})
    public void testCalculation(int a, int b) {
        int expectedResult = a + b;

        Map<Integer, String> numberIds = new LinkedHashMap<>();
        numberIds.put(0, "b040");
        numberIds.put(1, "b030");
        numberIds.put(2, "b031");
        numberIds.put(3, "b032");
        numberIds.put(4, "b020");
        numberIds.put(5, "b021");
        numberIds.put(6, "b022");
        numberIds.put(7, "b010");
        numberIds.put(8, "b011");
        numberIds.put(9, "b012");

        passLicenseAgreement();

        String numberA = String.valueOf(a);
        String numberB = String.valueOf(b);

        char[] digitsA = numberA.toCharArray();
        char[] digitsB = numberB.toCharArray();

        for (int i = 0; i < digitsA.length; i++) {
            MobileElement digitToType = (MobileElement) driver.findElement(By.id("my.android.calc:id/" + numberIds.get(Character.getNumericValue(digitsA[i]))));
            digitToType.click();
        }

        MobileElement plusButton = (MobileElement) driver.findElement(By.id("my.android.calc:id/b043"));
        plusButton.click();

        for (int i = 0; i < digitsB.length; i++) {
            MobileElement digitToType = (MobileElement) driver.findElement(By.id("my.android.calc:id/" + numberIds.get(Character.getNumericValue(digitsB[i]))));
            digitToType.click();
        }

        MobileElement resultField = (MobileElement) driver.findElement(By.id("my.android.calc:id/result"));

        int actualResult = Integer.parseInt(resultField.getText());
        Assert.assertEquals(expectedResult, actualResult);
    }

    @AfterClass
    public void tearDown() {
        driver.closeApp();
    }
}
