package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.LocaisPage;
import org.dg.pages.LoginPage;
import org.dg.pages.MedidasPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

@RunWith(Parameterized.class)
public class LocaisTest {
    private WebDriver driver;
    private LocaisPage locaisPage;
    private LoginPage login;

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");

    private String descricaoLocal;

    public LocaisTest(String descricaoLocal) {
        this.descricaoLocal = descricaoLocal;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[]{
            "Local Teste 1 " + UUID.randomUUID().toString().substring(0, 8),
            "Local Teste 2 " + UUID.randomUUID().toString().substring(0, 8),
            "Local Teste 3 " + UUID.randomUUID().toString().substring(0, 8)
        };
    }

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        locaisPage = new LocaisPage(driver);
        login = new LoginPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/locais");
        locaisPage.esperarPaginaLocaisCarregar();
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
    public void deveCadastrarLocalCorretamente() {
        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao("Local DG " + UUID.randomUUID().toString().substring(0, 8));
        locaisPage.clickBotaoSalvar();

        String textoAlertSucesso = locaisPage.textoAlert();
        /*System.out.println("Texto capturado: " + textoAlertSucesso);*/
        Assert.assertEquals("Local cadastrado com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveCadastrarLocalMesmaDescricao() {
        String descricaoLocal = "Local DG " + UUID.randomUUID().toString().substring(0, 8);

        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao(descricaoLocal);
        locaisPage.clickBotaoSalvar();
        locaisPage.esperarTextoAlert("Local cadastrado com sucesso!");

        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao(descricaoLocal);
        locaisPage.clickBotaoSalvar();

        locaisPage.esperarTextoAlert("Já existe um Local com o mesmo nome!");
        String textoAlertJaExiste = locaisPage.textoAlert();
        Assert.assertEquals("Já existe um Local com o mesmo nome!", textoAlertJaExiste);
    }

    @Test
    public void deveInativarLocalCadastrada() {
        String descricaoLocal = "Local DG " + UUID.randomUUID().toString().substring(0, 8);

        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao(descricaoLocal);
        locaisPage.clickBotaoSalvar();
        locaisPage.esperarTextoAlert("Local cadastrado com sucesso!");

        locaisPage.filtrarDescricao(descricaoLocal);
        locaisPage.clickIconInativar();
        locaisPage.clickBotaoConfirmar();

        locaisPage.esperarTextoAlert("Local inativado com sucesso!");
        String textoAlertInatido = locaisPage.textoAlert();
        Assert.assertEquals("Local inativado com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarLocalCadastrada() {
        String descricaoLocal = "Local DG " + UUID.randomUUID().toString().substring(0, 8);

        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao(descricaoLocal);
        locaisPage.clickBotaoSalvar();
        locaisPage.esperarTextoAlert("Local cadastrado com sucesso!");

        locaisPage.filtrarDescricao(descricaoLocal);
        locaisPage.clickBotaoEditar();
        locaisPage.setDescricao(descricaoLocal + " Editado");
        locaisPage.clickBotaoSalvar();

        locaisPage.esperarTextoAlert("Local editado com sucesso!");
        String textoAlertEditado = locaisPage.textoAlert();
        Assert.assertEquals("Local editado com sucesso!", textoAlertEditado);
    }
}
