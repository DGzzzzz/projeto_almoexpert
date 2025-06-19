package org.dg.pages;

import org.dg.core.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private DSL dsl;

    public LoginPage(WebDriver driver) {
        dsl = new DSL(driver);
    }

    private By botaoEntrar = By.xpath("//button[@type='submit']");
    private By imgHome = By.xpath("//img[@alt='Logo']");

    public void setEmail(String email) {
        dsl.writeText("email", email);
    }

    public void setPassword(String password) {
        dsl.writeText("senha", password);
    }

    public void clickEntrar() {
        dsl.clickButton(botaoEntrar);
    }

    public String alertObrg() {
        return dsl.pegarTextoAlert(By.className("p-toast-detail"));
    }

    public String alertInvalidos() {
        return dsl.pegarTextoAlert(By.xpath("//div[contains(@class, 'alert') and contains(@class, 'alert-warning') and contains(text(), 'Usuário ou senha inválidos.')]"));
    }

    public String getUrlAtual() {
        return dsl.getUrl();
    }

    public void fazerLogin(String email, String senha) {
        setEmail(email);
        setPassword(senha);
        clickEntrar();
    }

    public void esperarPaginaFrontCarregar() {
        dsl.esperaElementoVisivel(imgHome);
    }
}
