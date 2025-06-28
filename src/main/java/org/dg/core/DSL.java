package org.dg.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DSL {
    private WebDriver driver;
    private WebDriverWait wait;

    public DSL(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void clickButton(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        try {
            WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(by));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
            try {
                elemento.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            }
        } catch (TimeoutException e) {
            System.out.println("Botão não encontrado ou não clicável: " + by.toString());
            throw e;
        }
    }

    public void writeText(String id, String text) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        java.util.List<WebElement> inputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(id)));
        WebElement input = null;
        for (WebElement el : inputs) {
            if (el.isDisplayed() && el.isEnabled()) {
                input = el;
                break;
            }
        }
        if (input == null) throw new RuntimeException("Nenhum input visível e habilitado encontrado para id: " + id);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
        input.clear();
        input.sendKeys(text);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String pegarTextoAlert(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return alert.getText();
    }

    public void esperaElementoVisivel(By by) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void selectPorValor(By by, String valor) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(valor);
    }

    public void selectPorTexto(By by, String texto) {
        Select select = new Select(driver.findElement(by));
        select.selectByVisibleText(texto);
    }

    public void marcarCheckBox(By by) {
        WebElement checkbox = driver.findElement(by);
        checkbox.click();
    }

    public void escreverTextArea(By by, String texto) {
        WebElement textarea = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(by));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", textarea);
        textarea.clear();
        textarea.sendKeys(texto);
    }

    public void esperarModalFechar(String id) {
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.attributeToBe(By.id(id), "style", "display: none;")
        );
    }
}
