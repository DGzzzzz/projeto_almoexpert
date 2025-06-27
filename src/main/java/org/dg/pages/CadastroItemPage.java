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

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void esperarPaginaListaItensCarregar() {
        dsl.esperaElementoVisivel(botaoNovo);
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
}
