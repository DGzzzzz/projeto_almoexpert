package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.CadastroItemPage;
import org.dg.pages.LocaisPage;
import org.dg.pages.LoginPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

public class CadastroItemTest {
    private WebDriver driver;
    private CadastroItemPage cadastroItemPage;
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
        cadastroItemPage = new CadastroItemPage(driver);
        login = new LoginPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/itens");
        cadastroItemPage.esperarPaginaListaItensCarregar();

        cadastroItemPage.clickBotaoNovo();
        Assert.assertTrue(driver.getCurrentUrl().contains("cadastro-item"));
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
    public void deveCadastrarNovoItem() {
        cadastroItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroItemPage.setNome("Item DG " + UUID.randomUUID().toString().substring(0, 8));
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoria("386");
        cadastroItemPage.clickSelectUnidadeMedida("283");
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertInatido = cadastroItemPage.textoAlert();
        System.out.println(textoAlertInatido);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveCadastrarItemDescIgual() {
        cadastroItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroItemPage.setNome("Item DG 080b2f7a");
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoria("386");
        cadastroItemPage.clickSelectUnidadeMedida("283");
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertInatido = cadastroItemPage.textoAlert();
        System.out.println(textoAlertInatido);
        Assert.assertEquals("Error: Já existe um Elemento com o mesmo nome!", textoAlertInatido);
    }

    @Test
    public void deveCadastrarItemCodIgual() {
        cadastroItemPage.setCodigo("DG1");
        cadastroItemPage.setNome("Item DG " + UUID.randomUUID().toString().substring(0, 8));
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoria("386");
        cadastroItemPage.clickSelectUnidadeMedida("283");
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertInatido = cadastroItemPage.textoAlert();
        System.out.println(textoAlertInatido);
        Assert.assertEquals("Error: Já existe um Elemento com o mesmo código!", textoAlertInatido);
    }
}
