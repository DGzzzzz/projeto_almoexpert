package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CadastroItemPage {
    private DSL dsl;

    public CadastroItemPage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By botaoNovo = By.xpath("//a[contains(.,'Novo')]");
    private By botaoSalvar = By.cssSelector("button[type='submit']");
    private By titulo = By.xpath("//h5[contains(.,'Cadastro de Item')]");
    private By MsgAlert = By.xpath("//*[contains(@class,'p-toast-detail')]");
    private By CampoCodigo = By.id("codigo");
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

    public void clickSelectCategoria(String categoria) {
        dsl.selectPorValor(selectCategoria, categoria);
    }

    public void clickSelectUnidadeMedida(String unidade) {
        dsl.selectPorValor(selectUnidade, unidade);
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
}
