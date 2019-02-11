package io.github.philippeboisney.githubapp.repository

import io.github.philippeboisney.githubapp.base.BaseUT
import io.github.philippeboisney.githubapp.di.configureAppComponent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import retrofit2.HttpException
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class UserRepositoryTest: BaseUT() {

    // FOR DATA ---
    private val userRepository by inject<UserRepository>()

    // OVERRIDE ---
    override fun isMockServerEnabled() = true

    override fun setUp() {
        super.setUp()
        startKoin(configureAppComponent(getMockUrl()))
    }

    // TESTS ---

    @Test
    fun `search users by name and succeed`() {
        mockHttpResponse("search_users.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("detail_user.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("repos_user.json", HttpURLConnection.HTTP_OK)
        mockHttpResponse("followers_user.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val users = userRepository.searchUsersWithPagination("FAKE", -1, -1, "FAKE")
            assertEquals(1, users.size)
            assertEquals("PhilippeBoisney", users.first().login)
            assertEquals(103, users.first().totalFollowers)
            assertEquals(32, users.first().totalRepos)
            assertEquals(1346, users.first().totalStars)
            assertEquals(2, users.first().followers.size)
            assertEquals("UgurMercan", users.first().followers[0].login)
            assertEquals("https://avatars0.githubusercontent.com/u/7712975?v=4", users.first().followers[0].avatarUrl)
            assertEquals("Balasnest", users.first().followers[1].login)
            assertEquals("https://avatars3.githubusercontent.com/u/6050520?v=4", users.first().followers[1].avatarUrl)
        }
    }

    @Test(expected = HttpException::class)
    fun `search users by name and fail`() {
        mockHttpResponse("search_users.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            userRepository.searchUsersWithPagination("FAKE", -1, -1, "FAKE")
        }
    }
}