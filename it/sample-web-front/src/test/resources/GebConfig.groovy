import com.sample.web.front.geb.base.TestcontainersWebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.htmlunit.HtmlUnitDriver

reportsDir = "build/reports/geb"

driver = {
    new HtmlUnitDriver()
}

waiting {
    timeout = 30
    retryInterval = 0.2
}

environments {
    "dockerChrome" {
        driver = {
            new TestcontainersWebDriver(new ChromeOptions()
                    .addArguments("--no-sandbox")
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
