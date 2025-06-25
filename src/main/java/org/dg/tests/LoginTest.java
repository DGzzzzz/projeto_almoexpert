package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.LoginPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        driver.get(url_base + "/login");

        loginPage = new LoginPage(driver);
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
    public void deveLogarCorretamente() {
        loginPage.setEmail(email);
        loginPage.setPassword(senha);
        loginPage.clickEntrar();

        Assert.assertTrue("URL esperada deveria conter 'front'", loginPage.getUrlAtual().contains("front"));
    }

    @Test
    public void deveRetornarAlertCamposObrigatorios() {
        loginPage.setEmail(email);
        loginPage.clickEntrar();

        String textoAlertObrg = loginPage.alertObrg();
        Assert.assertEquals("Um ou mais campos obrigatórios não preenchidos.", textoAlertObrg);
    }

    @Test
    public void deveRetornarAlertDadosInvalidos() {
        loginPage.setEmail(email);
        loginPage.setPassword("1");
        loginPage.clickEntrar();

        String textoAlertInvalidos = loginPage.alertInvalidos();
        Assert.assertEquals("Usuário ou senha inválidos.", textoAlertInvalidos);
    }
}
