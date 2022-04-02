package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LocalDateMethodTest {

    public String generateDate(int Days) {
        return LocalDate.now().plusDays(Days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldSubmitRequest() {
        String planningDate = generateDate(3);

        Configuration.holdBrowserOpen = false;
        Selenide.open("http://0.0.0.0:9999/");
        $("[data-test-id='city'] input").val("Красноярск");
        $("[class='menu-item__control']").click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").val("Лощенов Денис");
        $("[data-test-id='phone'] input").val("+79002001001");
        $("[data-test-id='agreement']").click();
        $("[class=\"button__text\"]").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }
}