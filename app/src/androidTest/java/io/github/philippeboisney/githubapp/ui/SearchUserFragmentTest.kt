package io.github.philippeboisney.githubapp.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import io.github.philippeboisney.githubapp.R
import io.github.philippeboisney.githubapp.base.BaseIT
import io.github.philippeboisney.githubapp.di.configureAppComponent
import io.github.philippeboisney.githubapp.di.storageModule
import io.github.philippeboisney.githubapp.ui.user.search.SearchUserFragment
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.dsl.module.Module
import java.net.HttpURLConnection
import android.widget.AutoCompleteTextView
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import io.github.philippeboisney.githubapp.di.storageModuleTest
import io.github.philippeboisney.githubapp.utils.hasItemCount
import io.github.philippeboisney.githubapp.utils.waitForAdapterChangeWithPagination
import org.hamcrest.CoreMatchers.*
import org.junit.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchUserFragmentTest: BaseIT() {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @get:Rule
    var executorRule = CountingTaskExecutorRule()

    // OVERRIDE ---
    override fun isMockServerEnabled() = true

    @Before
    override fun setUp() {
        super.setUp()
        configureCustomDependencies()
        activityRule.launchActivity(null)
    }

    // TESTS ---

    @Test
    fun whenFragmentIsEmpty() {
        onView(withId(R.id.fragment_search_user_empty_list_image)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_search_user_empty_list_title)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_search_user_empty_list_title)).check(matches(withText(containsString(getString(R.string.no_result_found)))))
        onView(withId(R.id.fragment_search_user_empty_list_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun whenUserSearchUsersAndSucceed() {
        mockHttpResponse("search_users.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("detail_user.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("repos_user.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("followers_user.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("search_users_empty.json", HttpURLConnection.HTTP_OK)

        onView(withId(R.id.action_search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText("t"))
        waitForAdapterChangeWithPagination(getRecyclerView(), executorRule, 4)

        onView(withId(R.id.fragment_search_user_rv)).check(matches((hasItemCount(1))))
        onView(allOf(withId(R.id.item_search_user_title), withText("PhilippeBoisney"))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.item_search_user_repositories), withText("1346 - 32 ${getString(R.string.repositories)}"))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.item_search_user_follower_name), withText("UgurMercan"))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.item_search_user_follower_count), withText("+102"))).check(matches(isDisplayed()))
    }

    @Test
    fun whenUserSearchUsersAndFailed() {
        mockHttpResponse("search_users.json", HttpURLConnection.HTTP_BAD_REQUEST)

        onView(withId(R.id.action_search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText("t"))
        Thread.sleep(1000)
        onView(withId(R.id.fragment_search_user_empty_list_image)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_search_user_empty_list_title)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_search_user_empty_list_title)).check(matches(withText(containsString(getString(R.string.technical_error)))))
        onView(withId(R.id.fragment_search_user_empty_list_button)).check(matches(isDisplayed()))
    }

    // UTILS ---

    /**
     * Configure custom [Module] for each [Test]
     */
    private fun configureCustomDependencies() {
        loadKoinModules(configureAppComponent(getMockUrl()).toMutableList().apply { add(storageModuleTest) })
    }

    /**
     * Convenient access to String resources
     */
    private fun getString(id: Int) = activityRule.activity.getString(id)

    /**
     * Convenient access to [SearchUserFragment]'s RecyclerView
     */
    private fun getRecyclerView() = activityRule.activity.findViewById<RecyclerView>(R.id.fragment_search_user_rv)
}