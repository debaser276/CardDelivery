package ru.netology.domain;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Condition.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setupAll() {
        Configuration.headless = true;
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldFormSend() {
        $("[data-test-id='city'] [placeholder='Город']").sendKeys("Казань");
        SelenideElement dateElement = $("[data-test-id='date'] [placeholder='Дата встречи']");
        dateElement.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        LocalDate date = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateElement.sendKeys(date.format(formatter));
        $("[data-test-id='name'] [name='name']").sendKeys("Иванов Иван");
        $("[data-test-id='phone'] [name='phone']").sendKeys("+79999999999");
        $("[data-test-id='agreement']").click();
        $(withText("Забронировать")).click();

        $(".notification__title").shouldHave(text("Успешно!"), Duration.ofSeconds(15));
    }

    @Test
    void shouldPopupListAppearWithCities() {
        $("[data-test-id='city'] [placeholder='Город']").sendKeys("Сам");

        List<String> actual = $$(".menu-item__control").texts();
        List<String> expected = Arrays.asList("Петропавловск-Камчатский", "Самара");

        assertEquals(expected, actual);
    }

    @Test
    void shouldCalendarPickDateInWeek() {
        $(".icon_name_calendar").click();
        LocalDate date = LocalDate.now().plusDays(7);
        if (date.getDayOfMonth() < 8) {
            $("[data-step='1']").click();
        }
        $(".calendar__layout").$(withText(String.valueOf(date.getDayOfMonth()))).click();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String actual = $("[data-test-id='date'] [placeholder='Дата встречи']").getValue();
        String expected = date.format(formatter);

        assertEquals(expected, actual);
    }

}
