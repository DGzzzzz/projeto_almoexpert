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
        String descricaoMedida = "Medida DG " + UUID.randomUUID().toString().substring(0, 8);

        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao(descricaoMedida);
        medidasPage.clickBotaoSalvar();
        medidasPage.esperarTextoAlert("Medida cadastrada com sucesso!");

        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao(descricaoMedida);
        medidasPage.clickBotaoSalvar();

        medidasPage.esperarTextoAlert("Já existe uma Unidade de Medida com a mesma descrição!");
        String textoAlertJaExiste = medidasPage.textoAlert();
        Assert.assertEquals("Já existe uma Unidade de Medida com a mesma descrição!", textoAlertJaExiste);
    }

    @Test
    public void deveInativarMedidaCadastrada() {
        String descricaoMedida = "Medida DG " + UUID.randomUUID().toString().substring(0, 8);

        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao(descricaoMedida);
        medidasPage.clickBotaoSalvar();
        medidasPage.filtrarDescricao(descricaoMedida);
        medidasPage.clickIconInativar();
        medidasPage.clickBotaoConfirmar();

        medidasPage.esperarTextoAlert("Medida inativada com sucesso!");
        String textoAlertInatido = medidasPage.textoAlert();
        Assert.assertEquals("Medida inativada com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarMedidaCadastrada() {
        String descricaoMedida = "Medida DG " + UUID.randomUUID().toString().substring(0, 8);

        medidasPage.clickBotaoNovo();
        medidasPage.setDescricao(descricaoMedida);
        medidasPage.clickBotaoSalvar();
        medidasPage.filtrarDescricao(descricaoMedida);
        medidasPage.clickBotaoEditar();
        medidasPage.setDescricao(descricaoMedida + " Editada");
        medidasPage.clickBotaoSalvar();

        medidasPage.esperarTextoAlert("Medida editada com sucesso!");
        String textoAlertEditado = medidasPage.textoAlert();
        Assert.assertEquals("Medida editada com sucesso!", textoAlertEditado);
    }
}
