package ru.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestClass {

    public class Item{
        String name;
        String price;
        public void setName(String n){
            name = n;
        }
        public void setPrice(String p){
            price = p;
        }
    }

    private WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void test() throws Throwable {
        String name="Meizu M5 Note";
        driver.get("https://market.yandex.ru/");
        inputItem(name);
        List<Item> items = getResults();
      /*System.out.println("<--Начало-->");
        for (Item item : items) {
            System.out.println("Название " +item.name + " Цена " + item.price);
        }
        System.out.println("<--Конец-->");*/
        isFind(name,items);
        sorting();
        getSender();
    }

    public void getSender() throws Throwable{
        Thread.sleep(5000);
        driver.findElement(By.xpath("//div[@class='price']")).click();
        String price = driver.findElement(By.xpath("//span[@class='price']")).getText();
        String sender = driver.findElement(By.xpath("//a[@class='link link_outer_yes link_theme_outer link_type_shop-name snippet-card__shop-name n-snippet-card__shop-name shop-history__link i-bem link_js_inited']")).getText();
        System.out.println("Цена товара: " + price);
        System.out.println("Магазин: " + sender);
    }

    public void sorting(){
        driver.findElement(By.xpath("//a[@class='link link_theme_major n-filter-sorter__link i-bem link_js_inited' and text()='по цене']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='filter-applied-results metrika i-bem filter-applied-results_js_inited']")));
    }

    public void isFind(String name,List<Item> results){
        boolean res=false;
        boolean result=false;
        for (Item item : results) {
            res=item.name.contains(name);
            result=result|res;
        }
        System.out.println("Товар найден? = " + result);
    }

    public void inputItem(String name){
        driver.findElement(By.xpath("//input[@class='input__control']")).sendKeys(name);
        driver.findElement(By.xpath("//span[@class='search2__button']")).click();
    }

    public List<Item> getResults(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='layout layout_type_search i-bem']")));
        List<WebElement> list1 = driver.findElements(By.xpath("//div[@class='snippet-list snippet-list_type_vertical island metrika i-bem snippet-list_js_inited metrika_js_inited']/div[@class='n-snippet-card snippet-card clearfix i-bem snippet-card_js_inited n-snippet-card_js_inited']/div[@class='snippet-card__content']/div[@class='snippet-card__table']/div[@class='snippet-card__row']/div[@class='snippet-card__col']/h3[@class='snippet-card__header i-bem snippet-card__header_js_inited']/a"));
        List<WebElement> list2 = driver.findElements(By.xpath("//div[@class='snippet-list snippet-list_type_vertical island metrika i-bem snippet-list_js_inited metrika_js_inited']/div[@class='n-snippet-card snippet-card clearfix i-bem snippet-card_js_inited n-snippet-card_js_inited']/div[@class='snippet-card__info']/a"));
        List<Item> results = new ArrayList<Item>();
        List<String> names = new ArrayList<String>();
        List<String> prices = new ArrayList<String>();
        for (WebElement element1 : list1) {
            names.add(element1.getText());
        }
        for (WebElement element1 : list2) {
            prices.add(element1.getText());
        }
        for(int i = 0; i < names.size(); i++){
            Item p = new Item();
            p.setPrice(prices.get(i));
            p.setName(names.get(i));
            results.add(p);
        }
        return results;
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }


}
