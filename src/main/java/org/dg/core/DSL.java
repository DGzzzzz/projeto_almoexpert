package org.dg.core;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DSL {
    private WebDriver driver;

    public DSL(WebDriver driver) {
        this.driver = driver;
    }

    public void clickButton(String xpath) {
        WebElement elemento = driver.findElement(By.xpath(xpath));
        elemento.click();
    }

    public void writeText(String id, String text) {
        driver.findElement(By.id(id)).sendKeys(text);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String pegarTextoAlert(By by) {
        return driver.findElement(by).getText();
    }
}
