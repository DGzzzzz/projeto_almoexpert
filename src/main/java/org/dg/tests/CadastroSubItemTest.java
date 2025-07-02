package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.*;
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
public class CadastroSubItemTest {
    private WebDriver driver;
    private LocaisPage locaisPage;
    private MarcasPage marcasPage;
    private CadastroItemPage cadastroItemPage;
    private MedidasPage medidasPage;
    private CategoriaPage categoriaPage;
    private CadastroSubItemPage cadastroSubItemPage;
    private LoginPage login;

    private String codigo;
    private String nome;
    private String quantidade;

    private String subItemNome = "Subitem DG " + UUID.randomUUID().toString().substring(0, 8);
    private String itemNome = "Item DG " + UUID.randomUUID().toString().substring(0, 8);
    private String unidadeMedida = "Unidade de Medida DG " + UUID.randomUUID().toString().substring(0, 8);
    private String categoria = "Categoria " + UUID.randomUUID().toString().substring(0, 8);
    private String marca = "Marca DG " + UUID.randomUUID().toString().substring(0, 8);
    private String local = "Local DG " + UUID.randomUUID().toString().substring(0, 8);

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");

    public CadastroSubItemTest(String codigo, String nome, String quantidade) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
            {"DG " + UUID.randomUUID().toString().substring(0, 4), "SubItem " + UUID.randomUUID().toString().substring(0, 8), "10"},
            {"DG " + UUID.randomUUID().toString().substring(0, 4), "SubItem " + UUID.randomUUID().toString().substring(0, 8), "20"},
            {"DG " + UUID.randomUUID().toString().substring(0, 4), "SubItem " + UUID.randomUUID().toString().substring(0, 8), "30"}
        };
    }

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        locaisPage = new LocaisPage(driver);
        login = new LoginPage(driver);
        marcasPage = new MarcasPage(driver);
        categoriaPage = new CategoriaPage(driver);
        cadastroItemPage = new CadastroItemPage(driver);
        medidasPage = new MedidasPage(driver);
        cadastroSubItemPage = new CadastroSubItemPage(driver);

        driver.get(url_base + "/login");
        login.fazerLogin(email, senha);
        login.esperarPaginaFrontCarregar();

        driver.get(url_base + "/medidas");
        medidasPage.esperarPaginaMedidasCarregar();
        medidasPage.filtrarDescricao(unidadeMedida);
        if( medidasPage.textoAlert().contains("Medida não encontrada.")) {
            medidasPage.clickBotaoNovo();
            medidasPage.setDescricao(unidadeMedida);
            medidasPage.clickBotaoSalvar();
        }

        driver.get(url_base + "/categorias");
        categoriaPage.esperarPaginaCategoriasCarregar();
        categoriaPage.clickBotaoNovo();
        categoriaPage.setDescricao(categoria);
        categoriaPage.clickBotaoSalvar();

        driver.get(url_base + "/locais");
        locaisPage.esperarPaginaLocaisCarregar();
        locaisPage.clickBotaoNovo();
        locaisPage.setDescricao(local);
        locaisPage.clickBotaoSalvar();
        String textoAlertSucesso = locaisPage.textoAlert();
        System.out.println("Texto capturado: " + textoAlertSucesso);
        Assert.assertEquals("Local cadastrado com sucesso!", textoAlertSucesso);


        driver.get(url_base + "/marcas");
        marcasPage.esperarPaginaLocaisCarregar();
        marcasPage.filtrarDescricao(marca);
        if( marcasPage.textoAlert().contains("Marca não encontrada.")) {
            marcasPage.clickBotaoNovo();
            marcasPage.setDescricao(marca);
            marcasPage.clickBotaoSalvar();
        }

        driver.get(url_base + "/itens");
        cadastroItemPage.esperarPaginaListaItensCarregar();

        cadastroItemPage.clickBotaoNovo();
        Assert.assertTrue(driver.getCurrentUrl().contains("cadastro-item"));
        cadastroItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 8));
        cadastroItemPage.setNome(itemNome);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        driver.get(url_base + "/itens");
        cadastroItemPage.esperarPaginaListaItensCarregar();
        cadastroItemPage.filtrarDescricao(itemNome);
        cadastroItemPage.clickBotaoEditar();
        cadastroSubItemPage.clickBotaoSubItens();
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
    public void deveCadastrarSubItemCorretamente() {
        cadastroSubItemPage.clickBotaoNovo();
        cadastroSubItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroSubItemPage.setDescricao(subItemNome);
        cadastroSubItemPage.setQuantidade("10");
        cadastroSubItemPage.setValidade("2025-12-31");
        cadastroSubItemPage.clickSelectLocalPorTexto(local);
        cadastroSubItemPage.clickSelectMarcaPorTexto(marca);

        cadastroSubItemPage.clickBotaoSalvarModal();
        cadastroSubItemPage.esperarModalSubItemFechar();
        Assert.assertTrue(cadastroSubItemPage.subitemEstaNaTabela(subItemNome));

        cadastroSubItemPage.clickBotaoSalvar();
        String textoAlertSucesso = cadastroSubItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item editado com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveEditarSubItemCorretamente() {
        cadastroSubItemPage.clickBotaoNovo();
        cadastroSubItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroSubItemPage.setDescricao(subItemNome);
        cadastroSubItemPage.setQuantidade("10");
        cadastroSubItemPage.setValidade("2025-12-31");
        cadastroSubItemPage.clickSelectLocalPorTexto(local);
        cadastroSubItemPage.clickSelectMarcaPorTexto(marca);

        cadastroSubItemPage.clickBotaoSalvarModal();
        cadastroSubItemPage.esperarModalSubItemFechar();
        Assert.assertTrue(cadastroSubItemPage.subitemEstaNaTabela(subItemNome));

        cadastroSubItemPage.clickBotaoSalvar();
        String textoAlertSucesso = cadastroSubItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item editado com sucesso!", textoAlertSucesso);
        cadastroSubItemPage.clickBotaoVoltar();

        cadastroItemPage.filtrarDescricao(itemNome);
        cadastroItemPage.clickBotaoEditar();
        cadastroSubItemPage.clickBotaoSubItens();

        cadastroSubItemPage.filtrarSubItemNome(subItemNome);
        cadastroSubItemPage.clickBotaoEditar();
        cadastroSubItemPage.setDescricao(subItemNome + " Editado");
        cadastroSubItemPage.clickBotaoSalvarModal();
        Assert.assertTrue(cadastroSubItemPage.subitemEstaNaTabela(subItemNome));
    }
}
