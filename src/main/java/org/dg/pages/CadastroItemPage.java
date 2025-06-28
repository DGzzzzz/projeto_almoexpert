package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CadastroItemPage {
    private DSL dsl;

    public CadastroItemPage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By titulo = By.xpath("//h5[contains(.,'Cadastro de Item')]");
    private By MsgAlert = By.xpath("//*[contains(@class,'p-toast-detail')]");

    private By botaoNovo = By.xpath("//a[contains(.,'Novo')]");
    private By botaoSalvar = By.cssSelector("button[type='submit']");
    private By botaoEditar = By.xpath("//i[contains(@class, 'p-element') and @ptooltip='Editar']");
    private By botaoVoltar = By.xpath("//a[contains(.,'Voltar')]");
    private By botaoFiltrar = By.xpath("//button[contains(@class, 'btn-dark') and contains(., 'Filtrar')]");
    private By iconInativar = By.xpath("//i[contains(@class, 'fa-ban') and @ptooltip='Inativar']");
    private By botaoConfirmar = By.xpath("//button[normalize-space(text())='Confirmar']");

    private By selectFiltroStatus = By.id("filtro_status");
    private By selectStatus = By.id("status");

    private By selectCategoria = By.id("categoriaId");
    private By selectUnidade = By.id("unidadeMedidaId");
    private By checkPoliciaFederal = By.id("monitoradoPF");
    private By checkExercito = By.id("exercito");
    private By campoObservacoes = By.id("observacoes");

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void clickBotaoSalvar() {
        dsl.clickButton(botaoSalvar);
    }

    public void clickBotaoEditar() {
        dsl.clickButton(botaoEditar);
    }

    public void clickBotaoVoltar() {
        dsl.clickButton(botaoVoltar);
    }

    public void clickBotaoInativar() {
        dsl.clickButton(iconInativar);
    }

    public void clickBotaoConfirmar() {
        dsl.clickButton(botaoConfirmar);
    }

    public void esperarPaginaListaItensCarregar() {
        dsl.esperaElementoVisivel(botaoNovo);
    }

    public void esperarPaginaItens() {
        dsl.esperaElementoVisivel(titulo);
    }

    public void setCodigo(String codigo) {
        dsl.writeText("codigo", codigo);
    }

    public void setNome(String nome) {
        dsl.writeText("nome", nome);
    }

    public void setValorMinimo(String valorMinimo) {
        dsl.writeText("quantidadeMinima", valorMinimo);
    }

    public void clickSelectCategoriaPorValor(String categoria) {
        dsl.selectPorValor(selectCategoria, categoria);
    }

    public void clickSelectCategoriaPorTexto(String categoria) {
        dsl.selectPorTexto(selectCategoria, categoria);
    }

    public void clickSelectUnidadeMedidaPorValor(String unidade) {
        dsl.selectPorValor(selectUnidade, unidade);
    }

    public void clickSelectUnidadeMedidaPorTexto(String unidade) {
        dsl.selectPorTexto(selectUnidade, unidade);
    }

    public void selecionarStatus(String status) {
        dsl.selectPorValor(selectStatus, status);
    }

    public void selecionarPolicia() {
        dsl.marcarCheckBox(checkPoliciaFederal);
    }

    public void selecionarExercito() {
        dsl.marcarCheckBox(checkExercito);
    }

    public void setObs(String texto) {
        dsl.escreverTextArea(campoObservacoes, texto);
    }

    public String textoAlert() {
        return dsl.pegarTextoAlert(MsgAlert);
    }

    public void filtrarCodigo(String codigo) {
        dsl.writeText("filtro_codigo", codigo);
        dsl.clickButton(botaoFiltrar);
    }

    public void filtrarDescricao(String descricao) {
        dsl.writeText("filtro_nome", descricao);
        dsl.clickButton(botaoFiltrar);
    }

    public void filtrarStatus(String status) {
        dsl.selectPorValor(selectFiltroStatus, status);
        dsl.clickButton(botaoFiltrar);
    }
}
