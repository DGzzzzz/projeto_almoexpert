package org.dg.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DSL {
    private WebDriver driver;

    public DSL(WebDriver driver) {
        this.driver = driver;
    }

    public void clickButton(String id) {
        driver.findElement(By.id(id)).click();
    }

    public void writeText(String id, String text) {
        driver.findElement(By.id(id)).sendKeys(text);
    }
}
