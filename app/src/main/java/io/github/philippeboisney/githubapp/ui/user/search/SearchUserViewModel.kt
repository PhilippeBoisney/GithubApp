package io.github.philippeboisney.githubapp.ui.user.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.philippeboisney.githubapp.api.NetworkState
import io.github.philippeboisney.githubapp.pagination.PaginationActions
import io.github.philippeboisney.githubapp.base.BaseViewModel
import io.github.philippeboisney.githubapp.model.Filters
import io.github.philippeboisney.githubapp.model.User
import io.github.philippeboisney.githubapp.pagination.datasource.UserDataSourceFactory
import io.github.philippeboisney.githubapp.repository.UserRepository
import io.github.philippeboisney.githubapp.storage.SharedPrefsManager

class SearchUserViewModel(repository: UserRepository,
                          private val sharedPrefsManager: SharedPrefsManager): BaseViewModel() {

    // FOR DATA ---
    private val userDataSource = UserDataSourceFactory(repository = repository, scope = ioScope)
    private val paginationActions = createMainActionsForPagination()

    // OBSERVABLES ---
    val users = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState> = paginationActions.networkState

    // PUBLIC API ---

    /**
     * Fetch a list of [User] by name
     * Called each time an user hits a key through [SearchView].
     */
    fun fetchUsersByName(query: String) {
        val search = query.trim()
        if (userDataSource.getQuery() == search) return
        this.createOrUpdatePagedList(search)
        this.cancelPossibleRunningRequest()
    }

    /**
     * Retry possible last paged request (ie: network issue)
     */
    fun refreshFailedRequest(){
        paginationActions.retryFailedRequest.invoke()
    }

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() {
        paginationActions.refresh.invoke()
    }

    /**
     * Returns filter [Filters.ResultSearchUsers] used to sort "search" request
     */
    fun getFilterWhenSearchingUsers() =
        sharedPrefsManager.getFilterWhenSearchingUsers()

    /**
     * Saves filter [Filters.ResultSearchUsers] used to sort "search" request
     */
    fun saveFilterWhenSearchingUsers(filter: Filters.ResultSearchUsers) {
        sharedPrefsManager.saveFilterWhenSearchingUsers(filter)
    }

    /**
     * Returns current search query
     */
    fun getCurrentQuery() = userDataSource.getQuery()

    // UTILS ---

    /**
     * Cancel possible running job
     * to only keep last result searched by user
     */
    private fun cancelPossibleRunningRequest() {
        paginationActions.cancelRunningJob.invoke()
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .setPageSize(5 * 2)
        .build()

    private fun createOrUpdatePagedList(query: String) {
        userDataSource.updateQuery(query, sharedPrefsManager.getFilterWhenSearchingUsers().value)
        paginationActions.refresh.invoke()
    }

    private fun createMainActionsForPagination() = PaginationActions(
        networkState = switchMap(userDataSource.source) { it.getNetworkState() },
        cancelRunningJob = { userDataSource.source.value?.cancelRunningJob() },
        retryFailedRequest = { userDataSource.source.value?.retryFailedQuery() },
        refresh = { userDataSource.source.value?.invalidate() } )
}