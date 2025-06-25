package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.LoginPage;
import org.dg.pages.MedidasPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

public class MedidasTest {
    private WebDriver driver;
    private MedidasPage medidasPage;
    private LoginPage login;

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");


    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        medidasPage = new MedidasPage(driver);
        login = new LoginPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/medidas");
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
        medidasPage.setDescricao("Medida DG " + UUID.randomUUID().toString().substring(0, 8));
        medidasPage.clickBotaoSalvar();

        String textoAlertSucesso = medidasPage.textoAlert();
        /*System.out.println("Texto capturado: " + textoAlertSucesso);*/
        Assert.assertEquals("Medida cadastrada com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveCadastrarMedidaMesmaDescricao() {
        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao("Medida 123456");
        medidasPage.clickBotaoSalvar();

        String textoAlertJaExiste = medidasPage.textoAlert();
        Assert.assertEquals("Já existe uma Unidade de Medida com a mesma descrição!", textoAlertJaExiste);
    }

    @Test
    public void deveInativarMedidaCadastrada() {
        medidasPage.filtrarDescricao("Medida DG Inativar");
        medidasPage.clickIconInativar();
        medidasPage.clickBotaoConfirmar();

        String textoAlertInatido = medidasPage.textoAlert();
        Assert.assertEquals("Medida inativada com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarMedidaCadastrada() {
        medidasPage.filtrarDescricao("Medida DG Editar");
        medidasPage.clickBotaoEditar();
        medidasPage.setDescricao("Medida DG Editada ok");
        medidasPage.clickBotaoSalvar();

        String textoAlertEditado = medidasPage.textoAlert();
        Assert.assertEquals("Medida editada com sucesso!", textoAlertEditado);
    }
}
