package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TesteWeb {
    private WebDriver driver;

    Dotenv env = Dotenv.load();
    String url_base = env.get("URL_BASE");

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        driver.get(url_base + "/login");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Erro ao fechar o driver: " + e.getMessage());
            }
        }
    }

    @Test
    public void testLogin() {
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }
}
