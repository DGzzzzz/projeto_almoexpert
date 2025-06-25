package org.dg.pages;

import org.dg.core.DSL;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CategoriaPage {
	private DSL dsl;
	
	public CategoriaPage(WebDriver driver) {
		dsl = new DSL(driver);
	}

	private By botaoNovo = By.xpath("//button[normalize-space(text())='Novo' and @data-bs-toggle='modal']");
	private By botaoSalvar = By.xpath("//button[normalize-space(text())='Salvar']");
	private By MsgAlert = By.xpath("//div[contains(@class,'p-toast-detail')]");
	
	public void clickBotaoNovo() {
		dsl.clickButton(botaoNovo);
	}

	public void esperarPaginaCategoriaCarregar() {
		dsl.esperaElementoVisivel(botaoNovo);
	}
	
	public void setDescricao(String descricao) {
		dsl.writeText("nome", descricao);
	}
	
	public void clickbotaosalvar() {
		dsl.clickButton(botaoSalvar);
	}
	
	public String pegaralerta() {
		return dsl.pegarTextoAlert(MsgAlert);
	}
}
