package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CadastroSubItemPage {
    private DSL dsl;

    public CadastroSubItemPage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By MsgAlert = By.xpath("//*[contains(@class,'p-toast-detail')]");

    private By botaoNovo = By.xpath("//button[contains(.,'Novo')]");
    private By botaoSalvar = By.cssSelector("button[type='submit']");
    private By botaoSalvarModal = By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space(text())='Salvar']");
    private By botaoSubItens = By.xpath("//button[contains(.,'Subitens') and @id='nav-subitem-tab']");
    private By botaoEditar = By.xpath("//i[contains(@class, 'p-element') and @ptooltip='Editar']");
    private By botaoFiltrar = By.xpath("//button[contains(@class, 'btn-dark') and contains(., 'Filtrar')]");
    private By botaoVoltar = By.xpath("//a[contains(.,'Voltar')]");

    private By selectMarca = By.id("marcaId");
    private By selectLocal = By.id("localId");

    public void filtrarSubItemNome(String nomeSubitem) {
         dsl.writeText("nome", nomeSubitem);
         dsl.clickButton(botaoFiltrar);
    }

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void clickBotaoVoltar() {
        dsl.clickButton(botaoVoltar);
    }

    public void clickBotaoSalvar() {
        dsl.clickButton(botaoSalvar);
    }

    public void clickBotaoSalvarModal() {
        dsl.clickButton(botaoSalvarModal);
    }

    public void clickBotaoEditar() {
        dsl.clickButton(botaoEditar);
    }

    public void clickBotaoSubItens() {
        dsl.clickButton(botaoSubItens);
    }

    public void setCodigo(String codigo) {
        dsl.writeText("codigo", codigo);
    }

    public void setDescricao(String descricao) {
        dsl.writeText("nome", descricao);
    }

    public void setQuantidade(String quantidade) {
        dsl.writeText("quantidade", quantidade);
    }

    public void setValidade(String data) {
        dsl.writeText("validade", data);
    }

    public void clickSelectMarcaPorTexto(String texto) {
        dsl.selectPorTexto(selectMarca, texto);
    }
    
    public void clickSelectLocalPorTexto(String texto) {
        dsl.selectPorTexto(selectLocal, texto);
    }

    public String textoAlert() {
        return dsl.pegarTextoAlert(MsgAlert);
    }

    public boolean subitemEstaNaTabela(String nomeSubitem) {
        By linhaSubitem = By.xpath("//table//tr[td[contains(text(),'" + nomeSubitem + "')]]");
        try {
            dsl.esperaElementoVisivel(linhaSubitem);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void esperarModalSubItemFechar() {
        dsl.esperarModalFechar("cadastrar_item");
    }
}
