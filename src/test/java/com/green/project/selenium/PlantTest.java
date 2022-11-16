package com.green.project.selenium;

import com.green.project.entity.Plant;
import com.green.project.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PlantTest {

    @Autowired
    PlantRepository plantRepository;

//    @Test
    public void savePlants() {
        String id = "webdriver.chrome.driver";
        String path = "/Users/yeon/Downloads/chromedriver";

        System.setProperty(id, path);

        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true);

        WebDriver driver = new ChromeDriver(options);

        String url = "http://api.nongsaro.go.kr/sample/rest/garden/garden.jsp";
        driver.get(url); // 해당 사이트로 접속


        List<Plant> plantList = new ArrayList<>();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        for (int page = 20; page <= 20; page++) {
            js.executeScript("fncGoPage(" + page +")");
            for (int line = 0; line < 10; line++) {
                Plant plant = new Plant();
                // trs 안에 td 이미지와 td 이름이 있다.
                List<WebElement> trs = driver.findElements(By.cssSelector("body > table > tbody > tr"));
                // 첫 번째 td 안에 이미지 경로 찾기
                String imgSrc = trs.get(line).findElements(By.tagName("td")).get(0).findElement(By.tagName("img")).getAttribute("src").toString();
                plant.setImgSrc(imgSrc);
                // 두 번째 td 안에 한글 식물 이름 가져오기
                String plantName = trs.get(line).findElements(By.tagName("td")).get(1).findElement(By.tagName("a")).getText().toString();
                plant.setPlantName(plantName);
                // fncDtl 추출하기
                String contentNo = imgSrc.substring(imgSrc.lastIndexOf("/") + 1).split("_")[0];
                // 이미지 경로와 fncDtl 출력해서 확인하기
                System.out.println(plantName);
                System.out.println(imgSrc);
                System.out.println(contentNo);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                // 이름 클릭해서 상세 정보로 들어가기
                js.executeScript("fncDtl('" + contentNo + "')");

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }

                // detailTrs 안에 th 제목과 td 내용이 있다.
                List<WebElement> detailTrs = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }

                // 가져오고 싶은 정보
                // 이미지 소스 주소도 저장하기
                // 1. 식물학명 plantName1
                // 2. 식물영명 plantName2
                // 3. 유통명 plantName3
                // 5. 원산지 정보 origin
                // 6. 조언 정보 advice
                // 8. 성장 높이 정보 height
                // 9. 성장 넓이 정보 width
                // 10. 잎 형태 정보 leafShape
                // 13. 번식 시기 정보 breeding
                // 28. 기능성 정보 functional
                System.out.println(detailTrs.size());

                // Entity
                plant.setPlantName1(detailTrs.get(0).findElement(By.tagName("td")).getText());
                plant.setPlantName2(detailTrs.get(1).findElement(By.tagName("td")).getText());
                plant.setPlantName3(detailTrs.get(2).findElement(By.tagName("td")).getText());
                plant.setOrigin(detailTrs.get(4).findElement(By.tagName("td")).getText());
                plant.setAdvice(detailTrs.get(5).findElement(By.tagName("td")).getText());
                plant.setHeight(detailTrs.get(7).findElement(By.tagName("td")).getText());
                plant.setWidth(detailTrs.get(8).findElement(By.tagName("td")).getText());
                plant.setLeafShape(detailTrs.get(9).findElement(By.tagName("td")).getText());
                plant.setBreeding(detailTrs.get(12).findElement(By.tagName("td")).getText());
                plant.setFunctional(detailTrs.get(27).findElement(By.tagName("td")).getText());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                System.out.println("================================================");
                System.out.println(plant.toString());
                driver.navigate().back();

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }

                plantList.add(plant);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                System.out.println("================================================");
                plantList.forEach(System.out::println);
            }
        }

        plantRepository.saveAll(plantList);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        driver.quit();

    }
}
