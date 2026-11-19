package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryReplanTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldReplanMeetingSuccessfully() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(5);

        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(firstDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='success-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstDate));

        $("[data-test-id='city'] input")
                .press(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE)
                .setValue(user.getCity());

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE)
                .setValue(secondDate);

        $("[data-test-id='name'] input")
                .press(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE)
                .setValue(user.getName());

        $("[data-test-id='phone'] input")
                .press(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE)
                .setValue(user.getPhone());

        if (!$("[data-test-id='agreement'] input").isSelected()) {
            $("[data-test-id='agreement']").click();
        }

        $("button.button").click();

        $("[data-test-id='replan-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Необходимо подтверждение"));

        $("[data-test-id='replan-notification'] button").click();

        $("[data-test-id='success-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondDate));
    }
}
