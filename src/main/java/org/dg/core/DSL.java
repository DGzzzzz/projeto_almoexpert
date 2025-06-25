package org.dg.core;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        WebElement elemento = driver.findElement(by);
        elemento.click();
    }

    public void writeText(String id, String text) {
        WebElement input = driver.findElement(By.id(id));
        input.clear();
        input.sendKeys(text);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String pegarTextoAlert(By by) {
        return driver.findElement(by).getText();
    }

    public void esperaElementoVisivel(By by) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
