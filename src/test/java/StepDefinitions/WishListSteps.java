package StepDefinitions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WishListSteps {
	
	WebDriver driver =null;
	WebDriverWait wait = null;
	double leastPrice=0;
	String leastPriceProdName="";
	String leastPriceEleId="";
	
	@Given("open browser")
	public void open_browser() {
		System.out.println("Inside Open Browser Step :::");
		String projectPath = System.getProperty("user.dir");
		System.out.println("Project Path is :::"+projectPath);
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_win32/chromedriver.exe");
		
		driver = new ChromeDriver();
	
	}
	
	@Given("I want to add four different products to my wishlist")
	public void i_want_to_add_four_different_products_to_my_wishlist() {

		System.out.println("Adding the Products !!!");
	    
	    driver.navigate().to("https://testscriptdemo.com/");
	    driver.findElement(By.linkText("Shop")).click();
	    
	    wait = new WebDriverWait(driver, Duration.ofMillis(2));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Shop")));
	    
	    String textVal=driver.findElement(By.xpath("//*[@id=\"site-content\"]/div/div/article/h1")).getText();
	    
	    if(textVal.equals("Shop"))
	    	System.out.println("Shop Page Loaded , Test Case Passed !!!");
	    else{
	    	System.out.println("Shop Page Not Loaded ,Test Case Failed");
	    	throw new RuntimeException("Shop Page Not Loaded ,Test Case Failed");
	    }
	    
	    wait = new WebDriverWait(driver, Duration.ofSeconds(50));
	    
	    driver.findElement(By.xpath("//a[@href='?add_to_wishlist=14']")).click();
	    validateProductAdd();
        
        driver.findElement(By.xpath("//a[@href='?add_to_wishlist=15']")).click();
        validateProductAdd();
        
        driver.findElement(By.xpath("//a[@href='?add_to_wishlist=18']")).click();
        validateProductAdd();
        
        driver.findElement(By.xpath("//a[@href='?add_to_wishlist=20']")).click();
        validateProductAdd();
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
	}

	private void validateProductAdd() {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("yith-wcwl-message")));
		
		if(driver.findElement(By.id("yith-wcwl-message")).getText().equals("Product added!"))
		   System.out.println("Product Added , Test Case Passed !!!");
		else
		   System.out.println("Product Not Added,Test Case Failed !!!");		
	}

	@When("I view my wishlist table")
	public void i_view_my_wishlist_table() {
		System.out.println("View the Wishlist Table !!!");
		driver.findElement(By.xpath("//*[@id=\"blog\"]/div[3]/div[1]/div/div/div[3]/div[3]/a")).click();
		
		if(driver.findElement(By.xpath("//*[@id=\"yith-wcwl-form\"]/div[1]/div[1]/h2")).getText().equals("My wishlist"))
			System.out.println("My Wishlist Page is Loaded, Test Case Passed !!!");
		else
			System.out.println("Wishlist Page Not Loaded , test case failed");
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(50));
	}

	@Then("I find total four selected items in my wishlist")
	public void i_find_total_four_selected_items_in_my_wishlist() {
		System.out.println("Find the Four Selected Products !!!");

		if(driver.findElements(By.xpath("//*[@id=\"yith-wcwl-form\"]/table/tbody/tr")).size() == 4)
			System.out.println("Found 4 products in the wish list , Test Case Passed !!!");
		else
			System.out.println("Test Case Failed !!!");
	}

	@When("I search for lowest price product")
	public void i_search_for_lowest_price_product() {
		System.out.println("Searching the Lowest Price !!!");
		
		List<WebElement> rows=driver.findElements(By.xpath("//*[@id=\"yith-wcwl-form\"]/table/tbody/tr"));
		List<String> rowidsList = new ArrayList<String>();
		double tmpVal=0;
		String tmpProdName="";
				
		for(WebElement ele:rows)
		{
			rowidsList.add(ele.getAttribute("id"));
		}
		
		for(String eleIds:rowidsList) {

			String xpathProdEle="//*[@id=\""+eleIds+"\"]/td[3]";
			String xpathPriceEle="//*[@id=\""+eleIds+"\"]/td[4]";
			String prodName=driver.findElement(By.xpath(xpathProdEle)).getText();
			String orgPrice=driver.findElement(By.xpath(xpathPriceEle)).getText();
			double finalAmt=0;
			
			if(orgPrice.contains(" "))
			{
				String tmpPrice=orgPrice.split(" ")[1];
				finalAmt=Double.parseDouble(tmpPrice.substring(1,tmpPrice.length()));
			}else
				finalAmt=Double.parseDouble(orgPrice.substring(1,orgPrice.length()));
			
			if(tmpVal==0)
				tmpVal=finalAmt;
			
			if(tmpVal>finalAmt)
				tmpVal=finalAmt;
			
			tmpProdName=prodName;
			leastPriceEleId=eleIds;
		}
		
		leastPriceProdName=tmpProdName;
		leastPrice=tmpVal;
		
		if(leastPriceProdName!= null && leastPriceProdName.length()>0)
			System.out.println("Identified the Least Price Product , Test Case Passed !!!");
		else
			System.out.println("Test Case Failed !!!");
	}

	@When("I am able to add the lowest price item to my cart")
	public void i_am_able_to_add_the_lowest_price_item_to_my_cart() {
		System.out.println("Adding the lowest price Items !!!");
		String addToCartXpath="//*[@id=\""+leastPriceEleId+"\"]/td[6]/a";
	
		List<WebElement> rows=driver.findElements(By.xpath("//*[@id=\"yith-wcwl-form\"]/table/tbody/tr"));
	
		for(WebElement ele:rows)
		{
			if(ele.getAttribute("id").equals(leastPriceEleId)) {
				ele.findElement(By.xpath(addToCartXpath)).click();
				System.out.println(" Test "+driver.findElement(By.xpath("//*[@id=\"yith-wcwl-form\"]/div[1]/div")).getText());
			}
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"yith-wcwl-form\"]/div[1]/div")));
		wait = new WebDriverWait(driver, Duration.ofSeconds(50));

	}

	@Then("I am able to verify the item in my cart")
	public void i_am_able_to_verify_the_item_in_my_cart() {
		System.out.println("Verify Items in the cart !!!");
				
		if(driver.findElement(By.xpath("//*[@id=\"blog\"]/div[2]/div[1]/div/div/div[3]/div[1]/div/div/a/i")).getText().equals("1"))
			System.out.println("Viried the item in Cary , Test Case Passed !!! ");
		else
			System.out.println("Test Case Failed !!!");
		
	}
}
