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
public class CadastroItemTest {
    private WebDriver driver;
    private CadastroItemPage cadastroItemPage;
    private LoginPage login;
    private MedidasPage medidasPage;
    private CategoriaPage categoriaPage;

    private String codigo;
    private String nome;
    private String valorMinimo;

    private String unidadeMedida = "Unidade de Medida DG " + UUID.randomUUID().toString().substring(0, 8);
    private String categoria = "Categoria " + UUID.randomUUID().toString().substring(0, 8);

    Dotenv env = Dotenv.load();

    String url_base = env.get("URL_BASE");
    String email = env.get("LOGIN_EMAIL");
    String senha = env.get("LOGIN_SENHA");


    public CadastroItemTest(String codigo, String nome, String valorMinimo) {
        this.codigo = codigo;
        this.nome = nome;
        this.valorMinimo = valorMinimo;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {"DG 01" + UUID.randomUUID().toString().substring(0, 4), "Item DG " + UUID.randomUUID().toString().substring(0, 8), "10"},
                {"DG 02" + UUID.randomUUID().toString().substring(0, 4), "Item DG " + UUID.randomUUID().toString().substring(0, 8), "20"},
                {"DG 03" + UUID.randomUUID().toString().substring(0, 4), "Item DG " + UUID.randomUUID().toString().substring(0, 8), "30"}
        };
    }

    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        cadastroItemPage = new CadastroItemPage(driver);
        login = new LoginPage(driver);
        medidasPage = new MedidasPage(driver);
        categoriaPage = new CategoriaPage(driver);

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
    public void deveCadastrarNovoItemComDadosParametrizados() {
        cadastroItemPage.setCodigo(codigo);
        cadastroItemPage.setNome(nome);
        cadastroItemPage.setValorMinimo(valorMinimo);
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste parametrizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveCadastrarNovoItem() {
        String codigoItem = "DG 0" + UUID.randomUUID().toString().substring(0, 4);
        String nomeItem = "Item DG " + UUID.randomUUID().toString().substring(0, 8);

        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);
    }

    @Test
    public void deveEditarNovoItem() {
        String codigoItem = "DG 0" + UUID.randomUUID().toString().substring(0, 4);
        String nomeItem = "Item DG " + UUID.randomUUID().toString().substring(0, 8);

        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);

        cadastroItemPage.clickBotaoVoltar();
        cadastroItemPage.esperarPaginaListaItensCarregar();
        Assert.assertTrue(driver.getCurrentUrl().contains("itens"));

        cadastroItemPage.filtrarCodigo(codigoItem);
        String nomeEditado = nomeItem + "Editado";
        cadastroItemPage.clickBotaoEditar();
        cadastroItemPage.setNome(nomeEditado);
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertEditado = cadastroItemPage.textoAlert();
        System.out.println(textoAlertEditado);
        Assert.assertEquals("Item editado com sucesso!", textoAlertEditado);
    }

    @Test
    public void deveInativarItem() {
        String codigoItem = "DG 0" + UUID.randomUUID().toString().substring(0, 4);
        String nomeItem = "Item DG " + UUID.randomUUID().toString().substring(0, 8);

        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarPolicia();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);

        cadastroItemPage.clickBotaoVoltar();
        cadastroItemPage.esperarPaginaListaItensCarregar();
        Assert.assertTrue(driver.getCurrentUrl().contains("itens"));

        cadastroItemPage.filtrarDescricao(nomeItem);
        cadastroItemPage.clickBotaoInativar();
        cadastroItemPage.clickBotaoConfirmar();

        String textoAlertInativado = cadastroItemPage.textoAlert();
        System.out.println(textoAlertInativado);
        Assert.assertEquals("Elemento inativado com sucesso!", textoAlertInativado);
    }

    @Test
    public void deveCadastrarItemDescIgual() {
        String codigoItem = "DG 0" + UUID.randomUUID().toString().substring(0, 4);
        String nomeItem = "Item DG " + UUID.randomUUID().toString().substring(0, 8);

        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);

        cadastroItemPage.clickBotaoVoltar();
        cadastroItemPage.esperarPaginaListaItensCarregar();
        Assert.assertTrue(driver.getCurrentUrl().contains("itens"));

        cadastroItemPage.clickBotaoNovo();
        cadastroItemPage.setCodigo("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertJaExisteDesc = cadastroItemPage.textoAlert();
        System.out.println(textoAlertJaExisteDesc);
        Assert.assertEquals("Error: Já existe um Elemento com o mesmo nome!", textoAlertJaExisteDesc);
    }

    @Test
    public void deveCadastrarItemCodIgual() {
        String codigoItem = "DG 0" + UUID.randomUUID().toString().substring(0, 4);
        String nomeItem = "Item DG " + UUID.randomUUID().toString().substring(0, 8);

        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome(nomeItem);
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertSucesso = cadastroItemPage.textoAlert();
        System.out.println(textoAlertSucesso);
        Assert.assertEquals("Item cadastrado com sucesso!", textoAlertSucesso);

        cadastroItemPage.clickBotaoVoltar();
        cadastroItemPage.esperarPaginaListaItensCarregar();
        Assert.assertTrue(driver.getCurrentUrl().contains("itens"));

        cadastroItemPage.clickBotaoNovo();
        cadastroItemPage.setCodigo(codigoItem);
        cadastroItemPage.setNome("DG 0" + UUID.randomUUID().toString().substring(0, 4));
        cadastroItemPage.setValorMinimo("20");
        cadastroItemPage.selecionarExercito();
        cadastroItemPage.setObs("Este item foi adicionado para teste automatizado.");
        cadastroItemPage.clickSelectCategoriaPorTexto(categoria);
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        String textoAlertJaExisteCod = cadastroItemPage.textoAlert();
        System.out.println(textoAlertJaExisteCod);
        Assert.assertEquals("Error: Já existe um Elemento com o mesmo código!", textoAlertJaExisteCod);
    }
}
