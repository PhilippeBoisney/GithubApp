package io.github.philippeboisney.githubapp.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.github.philippeboisney.githubapp.api.NetworkState

data class PaginationActions (
    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>,
    // cancel possible running job (called after each user's hit)
    val cancelRunningJob: () -> Unit,
    // retries failed request.
    val retryFailedRequest: () -> Unit,
    // refresh all list
    val refresh: () -> Unit)