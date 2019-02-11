package io.github.philippeboisney.githubapp.ui.user.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.philippeboisney.githubapp.api.NetworkState
import io.github.philippeboisney.githubapp.pagination.PaginationData
import io.github.philippeboisney.githubapp.base.BaseViewModel
import io.github.philippeboisney.githubapp.model.Filters
import io.github.philippeboisney.githubapp.model.User
import io.github.philippeboisney.githubapp.pagination.datasource.UserDataSourceFactory
import io.github.philippeboisney.githubapp.repository.UserRepository
import io.github.philippeboisney.githubapp.storage.SharedPrefsManager

class SearchUserViewModel(private val repository: UserRepository,
                          private val sharedPrefsManager: SharedPrefsManager): BaseViewModel() {

    // FOR DATA ---
    private val query = MutableLiveData<String>()
    private val paginationData = map(query) { createOrUpdatePagedList(it) }

    // OBSERVABLES ---
    val users: LiveData<PagedList<User>> = switchMap(paginationData) { it.pagedList }
    val networkState: LiveData<NetworkState> = switchMap(paginationData) { it.networkState }

    init { query.value = "" }

    // PUBLIC API ---

    /**
     * Fetch a list of [User] by name
     * Called each time an user hits a key through [SearchView].
     */
    fun fetchUsersByName(query: String) {
        val search = query.trim()
        if (this.query.value == search) return
        this.query.value = search
        this.cancelPossibleRuningRequest()
    }

    /**
     * Retry possible last paged request (ie: network issue)
     */
    fun refreshFailedRequest(){
        paginationData.value?.retryFailedRequest?.invoke()
    }

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() {
        paginationData.value?.refresh?.invoke()
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
    fun getCurrentQuery() = query.value

    // UTILS ---

    /**
     * Cancel possible running job
     * to only keep last result searched by user
     */
    private fun cancelPossibleRuningRequest() {
        paginationData.value?.cancelRunningJob?.invoke()
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .setPageSize(5 * 2)
        .build()

    private fun createOrUpdatePagedList(query: String): PaginationData<User> {
        val config = pagedListConfig()
        val factory = UserDataSourceFactory(repository, query, sharedPrefsManager.getFilterWhenSearchingUsers().value, ioScope)
        val pagedList = LivePagedListBuilder(factory, config).build()
        return PaginationData(
            pagedList = pagedList,
            networkState = switchMap(factory.source) { it.getNetworkState() },
            cancelRunningJob = { factory.source.value?.cancelRunningJob() },
            retryFailedRequest = { factory.source.value?.retryFailedQuery() },
            refresh = { factory.source.value?.invalidate()} )
    }
}