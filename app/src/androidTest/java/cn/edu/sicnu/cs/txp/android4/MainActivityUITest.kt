package cn.edu.sicnu.cs.txp.android4

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.platform.app.InstrumentationRegistry
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import org.junit.Assert.assertEquals
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 主界面的 UI 测试（Espresso）。
 */
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity_displaysDefaultInfo() {
        onView(withId(R.id.tv_name)).check(matches(withText(R.string.default_name)))
        onView(withId(R.id.tv_gender)).check(matches(withText(R.string.default_gender)))
        onView(withId(R.id.tv_student_id)).check(matches(withText(R.string.default_student_id)))
    }

    @Test
    fun clickCardGameButton_opensCardGameActivity() {
        onView(withId(R.id.btn_card_game)).perform(click())
        onView(withId(R.id.tv_score)).check(matches(withText(R.string.score_zero)))
        onView(withId(R.id.game_view)).check { view, error ->
            if (error != null) throw error
            val board = view as RecyclerView
            val landscape = board.resources.configuration.orientation ==
                android.content.res.Configuration.ORIENTATION_LANDSCAPE
            assertEquals(if (landscape) 30 else 24, board.adapter?.itemCount)
        }
    }

    @Test
    fun clickEditInfoButton_opensEditActivity() {
        onView(withId(R.id.btn_edit_info)).perform(click())
        onView(withId(R.id.et_name)).check(matches(withText(R.string.default_name)))
    }

    @Test
    fun aboutMenuOpensDialog() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(context)
        onView(withText(R.string.menu_about)).perform(click())
        onView(withText(context.getString(R.string.about_message, context.getString(R.string.app_name))))
            .check(matches(withText(context.getString(R.string.about_message, context.getString(R.string.app_name)))))
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun tappingAndResettingBoardUpdatesScore() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        onView(withId(R.id.btn_card_game)).perform(click())
        onView(withContentDescription(context.getString(R.string.card_position_desc, 1))).perform(click())
        onView(withId(R.id.tv_score)).check(matches(withText("-1")))
        onView(withId(R.id.btn_reset)).perform(click())
        onView(withId(R.id.tv_score)).check(matches(withText(R.string.score_zero)))
    }
}
