package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DeliveryCardTest {

    public String generateDate(int Days) {
        return LocalDate.now().plusDays(Days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldMustScheduleMeetingTime() {
        String planningDate = generateDate(5);

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
        $(Selectors.withText("Успешно!")).shouldBe(visible);
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate));
    }

    @Test
    void shouldSuggestReschedulingMeetingTime() {
        String planningDate = generateDate(4);
        String planningDateLast = generateDate(5);

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
        $("[data-test-id=date] input").sendKeys(planningDateLast);
        $("[class=\"button__text\"]").click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] .button__text").click();
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate));
    }
}