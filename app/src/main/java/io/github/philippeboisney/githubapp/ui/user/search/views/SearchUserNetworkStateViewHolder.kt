package io.github.philippeboisney.githubapp.ui.user.search.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.philippeboisney.githubapp.api.NetworkState
import kotlinx.android.synthetic.main.item_search_user_network_state.view.item_search_user_network_state_progress_bar as progressBar
import kotlinx.android.synthetic.main.item_search_user_network_state.view.item_search_user_network_state_button as retryButton
import kotlinx.android.synthetic.main.item_search_user_network_state.view.item_search_user_network_state_title as retryTitle

class SearchUserNetworkStateViewHolder(parent: View): RecyclerView.ViewHolder(parent) {

    // PUBLIC API ---
    fun bindTo(networkState: NetworkState?, callback: SearchUserAdapter.OnClickListener){
        hideViews()
        setVisibleRightViews(networkState)
        itemView.retryButton.setOnClickListener { callback.onClickRetry() }
    }

    // UTILS ---
    private fun hideViews() {
        itemView.retryButton.visibility = View.GONE
        itemView.retryTitle.visibility = View.GONE
        itemView.progressBar.visibility = View.GONE
    }

    private fun setVisibleRightViews(networkState: NetworkState?) {
        when (networkState) {
            NetworkState.FAILED -> {
                itemView.retryButton.visibility = View.VISIBLE
                itemView.retryTitle.visibility = View.VISIBLE
            }
            NetworkState.RUNNING -> {
                itemView.progressBar.visibility = View.VISIBLE
            }
        }
    }
}