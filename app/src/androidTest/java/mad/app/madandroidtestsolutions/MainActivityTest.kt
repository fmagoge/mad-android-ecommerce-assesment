package mad.app.madandroidtestsolutions


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Rule
    @JvmField
    public  var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun test_isMainActivityInView(){
        val activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun recyclerViewTest(){


    }

    @After
    fun tearDown() {
    }
}