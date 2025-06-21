package org.dg.tests;

import org.dg.pages.LoginPage;
import org.dg.pages.MedidasPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
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
        medidasPage.setDescricao("Medida 123456");
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
        medidasPage.filtrarDescricao("Medida 1234");
        medidasPage.clickIconInativar();
        medidasPage.clickBotaoConfirmar();

        String textoAlertInatido = medidasPage.textoAlert();
        Assert.assertEquals("Medida inativada com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarMedidaCadastrada() {
        medidasPage.filtrarDescricao("Medida 123456Medida 1234567");
        medidasPage.clickBotaoEditar();
        medidasPage.setDescricao("Medida 1234567");
        medidasPage.clickBotaoSalvar();

        String textoAlertEditado = medidasPage.textoAlert();
        Assert.assertEquals("Medida editada com sucesso!", textoAlertEditado);

        By descricaoEditada = medidasPage.pegarDescricaoFiltrada("Medida 1234567");
        String descricaoNaTabela = driver.findElement(descricaoEditada).getText();
        Assert.assertEquals("Medida 1234567", descricaoNaTabela);
    }
}
