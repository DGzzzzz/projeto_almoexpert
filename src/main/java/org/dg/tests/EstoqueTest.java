package org.dg.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.dg.pages.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

public class EstoqueTest {
    private WebDriver driver;
    private EstoquePage estoquePage;
    private LocaisPage locaisPage;
    private MarcasPage marcasPage;
    private CadastroItemPage cadastroItemPage;
    private MedidasPage medidasPage;
    private CadastroSubItemPage cadastroSubItemPage;
    private LoginPage login;

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


    @Before
    public void setUp() {
        String GECKO_DRIVER = System.getProperty("user.dir") + "\\src\\main\\resources\\webdrivers\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER);

        driver = new FirefoxDriver();
        estoquePage = new EstoquePage(driver);
        locaisPage = new LocaisPage(driver);
        login = new LoginPage(driver);
        marcasPage = new MarcasPage(driver);
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
        cadastroItemPage.clickSelectCategoriaPorValor("386");
        cadastroItemPage.clickSelectUnidadeMedidaPorTexto(unidadeMedida);
        cadastroItemPage.esperarPaginaItens();
        cadastroItemPage.clickBotaoSalvar();

        driver.get(url_base + "/itens");
        cadastroItemPage.esperarPaginaListaItensCarregar();
        cadastroItemPage.filtrarDescricao(itemNome);
        cadastroItemPage.clickBotaoEditar();
        cadastroSubItemPage.clickBotaoSubItens();
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

        driver.get(url_base + "/movimentacoes");
        estoquePage.esperarPaginaEstoqueCarregar();
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
    public void deveCadastrarMovimentacaoCorretamente() {
        estoquePage.clickBotaoNovo();
        estoquePage.selectItem(itemNome);
        estoquePage.selectSubItem(subItemNome);
        estoquePage.selectTipo("E");
        estoquePage.setDataAndHora("2023-10-01T10:00");
        estoquePage.setQuantidade("23");
        estoquePage.setObs("Movimentação de teste automatizado.");
        estoquePage.clickBotaoSalvarModal();

        Assert.assertTrue(estoquePage.movEstaNaTabela(itemNome));
    }

    @Test
    public void deveCadastrarMovimentacaoSaidaSemEstoque() {
        estoquePage.clickBotaoNovo();
        estoquePage.selectItem(itemNome);
        estoquePage.selectSubItem(subItemNome);
        estoquePage.selectTipo("S");
        estoquePage.setDataAndHora("2023-10-01T10:00");
        estoquePage.setQuantidade("55555");
        estoquePage.setObs("Movimentação de teste automatizado.");
        estoquePage.clickBotaoSalvarModal();

        String textoAlert = estoquePage.textoAlert();
        System.out.println("Texto capturado: " + textoAlert);
        Assert.assertEquals("Estoque insuficiente.", textoAlert);
    }

    @Test
    public void deveCadastrarMovimentacaoSaidaComEstoque() {
        estoquePage.clickBotaoNovo();
        estoquePage.selectItem(itemNome);
        estoquePage.selectSubItem(subItemNome);
        estoquePage.selectTipo("E");
        estoquePage.setDataAndHora("2023-10-01T10:00");
        estoquePage.setQuantidade("10");
        estoquePage.setObs("Movimentação de teste automatizado.");
        estoquePage.clickBotaoSalvarModal();

        Assert.assertTrue(estoquePage.movEstaNaTabela(itemNome));

        estoquePage.clickBotaoNovo();
        estoquePage.selectItem(itemNome);
        estoquePage.selectSubItem(subItemNome);
        estoquePage.selectTipo("S");
        estoquePage.setDataAndHora("2023-10-01T11:00");
        estoquePage.setQuantidade("5");
        estoquePage.setObs("Movimentação de teste automatizado.");
        estoquePage.clickBotaoSalvarModal();

        String textoAlert = estoquePage.textoAlert();
        System.out.println("Texto capturado: " + textoAlert);

        Assert.assertEquals("Movimentação cadastrada com sucesso!", textoAlert);
    }
}
