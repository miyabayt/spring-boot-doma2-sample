import com.sample.web.front.geb.base.TestcontainersWebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.htmlunit.HtmlUnitDriver

reportsDir = "build/reports/geb"

driver = {
    new HtmlUnitDriver()
}

environments {
    "dockerChrome" {
        driver = {
            new TestcontainersWebDriver(new ChromeOptions()
                    .addArguments("--no-sandbox")
                    .addArguments("--disable-gpu")
                    .addArguments("--disable-dev-shm-usage"))
        }
    }
    "dockerFirefox" {
        driver = {
            new TestcontainersWebDriver(new FirefoxOptions())
        }
    }
}

cacheDriver = false
quitDriverOnBrowserReset = true
