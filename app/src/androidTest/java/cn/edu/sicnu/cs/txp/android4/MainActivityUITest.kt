package cn.edu.sicnu.cs.txp.android4

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun launch() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_cancel)).perform(click())
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun mainActivityDisplaysPersonalInfoAndRestoresSavedEdit() {
        val field = onView(withId(R.id.et_name))
        var original = ""
        field.check { view, error ->
            if (error != null) throw error
            original = (view as android.widget.EditText).text.toString()
        }
        try {
            field.perform(replaceText("Persistence Test"), closeSoftKeyboard())
            onView(withId(R.id.btn_save)).perform(click())
            scenario.close()
            scenario = ActivityScenario.launch(MainActivity::class.java)
            field.check(matches(withText("Persistence Test")))
            field.perform(replaceText("Unsaved"), closeSoftKeyboard())
            onView(withId(R.id.btn_cancel)).perform(click())
            field.check(matches(withText("Persistence Test")))
        } finally {
            field.perform(replaceText(original), closeSoftKeyboard())
            onView(withId(R.id.btn_save)).perform(click())
        }
    }

    @Test
    fun tabsShowSeparateGamesAndRestoreSelectedTab() {
        onView(withText(R.string.tab_pair)).perform(click())
        onView(withId(R.id.tv_rules)).check(matches(withText(context.getString(R.string.match_rules, 2))))
        onView(withId(R.id.game_view)).check { view, error ->
            if (error != null) throw error
            assertEquals(24, (view as RecyclerView).adapter?.itemCount)
        }
        onView(withText(R.string.tab_trio)).perform(click())
        scenario.recreate()
        onView(withId(R.id.tv_rules)).check(matches(withText(context.getString(R.string.match_rules, 3))))
    }

    @Test
    fun gameProgressSurvivesClosingAndReopeningAndIsIndependentPerMode() {
        onView(withText(R.string.tab_trio)).perform(click())
        onView(withId(R.id.btn_reset)).perform(click())
        onView(withText(R.string.tab_pair)).perform(click())
        onView(withId(R.id.btn_reset)).perform(click())
        onView(withContentDescription(context.getString(R.string.card_position_desc, 1))).perform(click())
        scenario.close()
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withText(R.string.tab_trio)).perform(click())
        assertScore(0)
        onView(withText(R.string.tab_pair)).perform(click())
        assertScore(-1)
        onView(withId(R.id.btn_reset)).perform(click())
        assertScore(0)
    }

    @Test
    fun aboutMenuOpensDialog() {
        openActionBarOverflowOrOptionsMenu(context)
        onView(withText(R.string.menu_about)).perform(click())
        onView(withText(context.getString(R.string.about_message, context.getString(R.string.app_name))))
            .check(matches(withId(android.R.id.message)))
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun assertScore(score: Int) {
        onView(withId(R.id.tv_score)).check(matches(withText(context.getString(R.string.game_score, score))))
    }
}
