package org.dg.tests;

import org.dg.pages.LoginPage;
import org.dg.pages.MedidasPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MedidasTest {
    private WebDriver driver;
    private MedidasPage medidasPage;
    private LoginPage login;

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        medidasPage = new MedidasPage(driver);
        login = new LoginPage(driver);

        driver.get("http://" + "35.209.123.161/front/login");
        login.fazerLogin("stephan.armand@aluno.feliz.ifrs.edu.br", "abc123");
        login.esperarPaginaFrontCarregar();

        driver.get("http://" + "35.209.123.161/front/medidas");
        medidasPage.esperarPaginaMedidasCarregar();
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
    public void deveCadastrarMedidaCorretamente() {
        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao("DG1");
        medidasPage.clickBotaoSalvar();

        String textoAlertSucesso = medidasPage.textoAlertSucesso();
        Assert.assertEquals("Medida editada com sucesso!", textoAlertSucesso);
    }
}
