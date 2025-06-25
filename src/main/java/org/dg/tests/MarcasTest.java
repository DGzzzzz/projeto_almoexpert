package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.LocaisPage;
import org.dg.pages.LoginPage;
import org.dg.pages.MarcasPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

public class MarcasTest {
    private WebDriver driver;
    private MarcasPage marcasPage;
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
        marcasPage = new MarcasPage(driver);
        login = new LoginPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/marcas");
        marcasPage.esperarPaginaLocaisCarregar();
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
    public void deveCadastrarMarcaCorretamente() {
        marcasPage.clickBotaoNovo();
        marcasPage.setDescricao("Marca DG " + UUID.randomUUID().toString().substring(0, 8));
        marcasPage.clickBotaoSalvar();

        String textoAlertSucesso = marcasPage.textoAlert();
        /*System.out.println("Texto capturado: " + textoAlertSucesso);*/
        Assert.assertEquals("Marca cadastrada com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveCadastrarMarcaMesmaDescricao() {
        marcasPage.clickBotaoNovo();
        marcasPage.setDescricao("ABCD");
        marcasPage.clickBotaoSalvar();

        String textoAlertJaExiste = marcasPage.textoAlert();
        Assert.assertEquals("Já existe uma Marca com o mesmo nome!", textoAlertJaExiste);
    }

    @Test
    public void deveInativarMarcaCadastrada() {
        marcasPage.filtrarDescricao("Marca DG Inativar");
        marcasPage.clickIconInativar();
        marcasPage.clickBotaoConfirmar();

        String textoAlertInatido = marcasPage.textoAlert();
        Assert.assertEquals("Marca inativada com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarMarcaCadastrada() {
        marcasPage.filtrarDescricao("Marca DG Editar");
        marcasPage.clickBotaoEditar();
        marcasPage.setDescricao("Marca DG Editado ok");
        marcasPage.clickBotaoSalvar();

        String textoAlertEditado = marcasPage.textoAlert();
        Assert.assertEquals("Marca editada com sucesso!", textoAlertEditado);
    }
}
