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
    private By alertSucesso = By.xpath("/div[contains(@class, 'p-toast-message-success')]//div[contains(@class, 'p-toast-detail')]");

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void setDescricao(String descricao) {
        dsl.writeText("descricao", descricao);
    }

    public void clickBotaoSalvar() {
        dsl.clickButton(botaoSalvar);
    }

    public void esperarPaginaMedidasCarregar() {
        dsl.esperaElementoVisivel(botaoNovo);
    }

    public String textoAlertSucesso() {
        return dsl.pegarTextoAlert(alertSucesso);
    }
}
