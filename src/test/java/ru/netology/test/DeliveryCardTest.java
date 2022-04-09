package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.Duration.ofSeconds;
import static ru.netology.data.DataGenerator.generateDate;

public class DeliveryCardTest {

    @Test
    void shouldMustScheduleMeetingTime() {
        String planningDate = generateDate(4);
        Configuration.holdBrowserOpen = false;
        Selenide.open("http://0.0.0.0:9999/");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[class='menu-item__control']").click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").val(DataGenerator.fakerName());
        $("[data-test-id='phone'] input").val(DataGenerator.fakerPhone());
        $("[data-test-id='agreement']").click();
        $("[class=\"button__text\"]").click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate));
    }

    @Test
    void shouldSuggestReschedulingMeetingTime() {
        String planningDate = generateDate(4);
        Configuration.holdBrowserOpen = false;
        Selenide.open("http://0.0.0.0:9999/");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[class='menu-item__control']").click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").val(DataGenerator.fakerName());
        $("[data-test-id='phone'] input").val(DataGenerator.fakerPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").should(appear, ofSeconds(10))
                .shouldHave(exactText("Встреча успешно запланирована на  " + planningDate));
        String planningDateLast = generateDate(5);
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").val(planningDateLast);
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .button").should(appear, ofSeconds(10)).click();
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDateLast));
    }
}