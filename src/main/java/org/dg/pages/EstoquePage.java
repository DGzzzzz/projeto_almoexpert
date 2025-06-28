package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EstoquePage {
    private DSL dsl;

    public EstoquePage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By botaoNovo = By.xpath("//button[contains(.,'Novo') and @data-bs-target='#cadastrar_movimentacao']");
    private By botaoSalvarModal = By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space(text())='Salvar']");

    private By MsgAlert = By.xpath("//div[contains(@class,'p-toast-detail')]");
    private By obsId = By.id("observacoes");

    private By selectItem = By.id("cadastro_elemento");
    private By selectSubItem = By.id("cadastro_elementoItem");
    private By selectTipo = By.id("cadastroTipo");

    public void clickBotaoNovo() {
        dsl.clickButton(botaoNovo);
    }

    public void clickBotaoSalvarModal() {
        dsl.clickButton(botaoSalvarModal);
    }

    public void esperarPaginaEstoqueCarregar() {
        dsl.esperaElementoVisivel(botaoNovo);
    }

    public void selectItem(String item) {
        dsl.selectPorTexto(selectItem, item);
    }

    public void selectSubItem(String subItem) {
        dsl.selectPorTexto(selectSubItem, subItem);
    }

    public void selectTipo(String tipo) {
        dsl.selectPorValor(selectTipo, tipo);
    }

    public void setDataAndHora(String dataHora) {
        dsl.writeText("cadastroData", dataHora);
    }

    public void setQuantidade(String quantidade) {
        dsl.writeText("cadastroQuantidade", quantidade);
    }

    public void setObs(String texto) {
        dsl.escreverTextArea(obsId, texto);
    }

    public boolean movEstaNaTabela(String nomeItem) {
        By linhaItem = By.xpath("//table//tr[td[contains(text(),'" + nomeItem + "')]]");
        try {
            dsl.esperaElementoVisivel(linhaItem);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String textoAlert() {
        return dsl.pegarTextoAlert(MsgAlert);
    }
}
