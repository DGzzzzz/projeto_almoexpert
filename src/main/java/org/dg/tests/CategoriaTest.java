package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.CategoriaPage;
import org.dg.pages.LoginPage;
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
public class CategoriaTest {
    private WebDriver driver;
    private CategoriaPage categoriaPage;
    private LoginPage login;

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");

    private String descricaoPage;

    public CategoriaTest(String descricaoPage) {
        this.descricaoPage = CategoriaTest.this.descricaoPage;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[]{
                "Categoria DG Teste 1 " + UUID.randomUUID().toString().substring(0, 8),
                "Categoria DG Teste 2 " + UUID.randomUUID().toString().substring(0, 8),
                "Categoria DG Teste 3 " + UUID.randomUUID().toString().substring(0, 8)
        };
    }

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        categoriaPage = new CategoriaPage(driver);
        login = new LoginPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/categorias");
        categoriaPage.esperarPaginaCategoriasCarregar();
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
    public void deveCadastrarCategoriaCorretamente() {
        categoriaPage.clickBotaoNovo();
        categoriaPage.setDescricao("Categoria DG " + UUID.randomUUID().toString().substring(0, 8));
        categoriaPage.clickBotaoSalvar();

        String textoAlertSucesso = categoriaPage.textoAlert();
        /*System.out.println("Texto capturado: " + textoAlertSucesso);*/
        Assert.assertEquals("Categoria cadastrada com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveCadastrarCategoriaMesmaDescricao() {
        String descricaoCategoria = "Categoria DG " + UUID.randomUUID().toString().substring(0, 8);

        categoriaPage.clickBotaoNovo();
        categoriaPage.setDescricao(descricaoCategoria);
        categoriaPage.clickBotaoSalvar();
        categoriaPage.esperarTextoAlert("Categoria cadastrada com sucesso!");

        categoriaPage.filtrarDescricao(descricaoCategoria);
        categoriaPage.clickBotaoNovo();
        categoriaPage.esperarInputDescricao();
        categoriaPage.setDescricao(descricaoCategoria);
        categoriaPage.clickBotaoSalvar();

        categoriaPage.esperarTextoAlert("Já existe uma Categoria com o mesmo nome!");
        String textoAlertJaExiste = categoriaPage.textoAlert();
        Assert.assertEquals("Já existe uma Categoria com o mesmo nome!", textoAlertJaExiste);
    }

    @Test
    public void deveInativarCategoriaCadastrada() {
        String descricaoCategoria = "Categoria DG " + UUID.randomUUID().toString().substring(0, 8);

        categoriaPage.clickBotaoNovo();
        categoriaPage.setDescricao(descricaoCategoria);
        categoriaPage.clickBotaoSalvar();
        categoriaPage.filtrarDescricao(descricaoCategoria);
        categoriaPage.clickIconInativar();
        categoriaPage.clickBotaoConfirmar();

        categoriaPage.esperarTextoAlert("Categoria inativada com sucesso!");
        String textoAlertInatido = categoriaPage.textoAlert();
        Assert.assertEquals("Categoria inativada com sucesso!", textoAlertInatido);
    }

    @Test
    public void deveEditarCategoriaCadastrada() {
        String descricaoCategoria = "Categoria DG " + UUID.randomUUID().toString().substring(0, 8);

        categoriaPage.clickBotaoNovo();
        categoriaPage.setDescricao(descricaoCategoria);
        categoriaPage.clickBotaoSalvar();

        categoriaPage.filtrarDescricao(descricaoCategoria);
        categoriaPage.clickBotaoEditar();
        categoriaPage.setDescricao(descricaoCategoria + " Editada");
        categoriaPage.clickBotaoSalvar();

        categoriaPage.esperarTextoAlert("Categoria editada com sucesso!");
        String textoAlertEditado = categoriaPage.textoAlert();
        Assert.assertEquals("Categoria editada com sucesso!", textoAlertEditado);
    }
}
