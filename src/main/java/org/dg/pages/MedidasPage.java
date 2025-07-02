package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MedidasPage {
    private DSL dsl;

    public MedidasPage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By botaoNovo = By.xpath("//button[normalize-space(text())='Novo' and @data-bs-toggle='modal']");
    private By botaoSalvar = By.xpath("//button[normalize-space(text())='Salvar']");
    private By botaoConfirmar = By.xpath("//button[normalize-space(text())='Confirmar']");
    private By iconInativar = By.xpath("//i[contains(@class, 'fa-ban') and @ptooltip='Inativar']");
    private By botaoFiltrar = By.xpath("//button[contains(@class, 'btn-dark') and contains(., 'Filtrar')]");
    private By iconEditar = By.xpath("//i[contains(@class, 'p-element') and @ptooltip='Editar']");

    private By MsgAlert = By.xpath("//div[contains(@class,'p-toast-detail')]");
    private By TituloAlert = By.xpath("//div[contains(@class,'p-toast-message-text')]");

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void setDescricao(String descricao) {
        dsl.writeText("descricao", descricao);
    }

    public void clickBotaoSalvar() {
        dsl.clickButton(botaoSalvar);
    }

    public void clickBotaoConfirmar() {
        dsl.clickButton(botaoConfirmar);
    }

    public void esperarPaginaMedidasCarregar() {
        dsl.esperaElementoVisivel(botaoNovo);
    }

    public String textoAlert() {
        return dsl.pegarTextoAlert(MsgAlert);
    }

    public void clickIconInativar() {
        dsl.clickButton(iconInativar);
    }

    public void clickBotaoEditar() {
        dsl.clickButton(iconEditar);
    }

    public void filtrarDescricao(String texto) {
        dsl.writeText("filtro_descricao", texto);
        dsl.clickButton(botaoFiltrar);
    }

    public void esperarTextoAlert(String textoEsperado) {
        dsl.esperarTextoAlert(MsgAlert, textoEsperado);
    }

    public void esperarInputDescricao() {
        dsl.esperaElementoVisivel(By.id("nome"));
    }
}
