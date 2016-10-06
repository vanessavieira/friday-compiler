import com.thoughtworks.selenium.Selenium;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rubenspessoa on 23/09/16.
 */

public class SeleniumTest {

    @Test
    public void test() {
        System.setProperty("webdriver.chrome.driver", "/Users/rubenspessoa/Downloads/chromedriver");

        try {
            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("http://google.com");
            WebElement input  = driver.findElement(By.id("lst-ib"));
            WebElement button = driver.findElement(By.name("btnG"));
            input.sendKeys("Teste de Software");
            button.click();
            List<WebElement> listWebElements = driver.findElements(By.className("r"));
            WebElement linkDiv = listWebElements.get(0);
            String link = linkDiv.findElement(By.tagName("a")).getAttribute("href");
            driver.get(link);

            Assert.assertTrue(driver.getCurrentUrl().contains("wikipedia.org"));

            JavascriptExecutor exec = (JavascriptExecutor) driver;
            String script = "return document.domain";
            String domain = (String) exec.executeScript(script);

            Assert.assertEquals("", "pt.wikipedia.org", domain);


            //ExpectedConditions.urlToBe("https://pt.wikipedia.org/wiki/Teste_de_software");
            //d.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
