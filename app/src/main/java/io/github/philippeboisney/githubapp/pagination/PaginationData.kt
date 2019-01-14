package io.github.philippeboisney.githubapp.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.github.philippeboisney.githubapp.api.NetworkState

data class PaginationData<T> (
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>,
    // cancel possible running job (called after each user's hit)
    val cancelRunningJob: () -> Unit,
    // retries failed request.
    val retryFailedRequest: () -> Unit,
    // refresh all list
    val refresh: () -> Unit)