package io.github.philippeboisney.githubapp.ui.user.search.views

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.philippeboisney.githubapp.R
import io.github.philippeboisney.githubapp.model.User
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_title as title
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_image_profile as image
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_repositories as repositories
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_follower_image as followerImage
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_follower_image_bis as followerImageBis
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_follower_name as followerName
import kotlinx.android.synthetic.main.item_search_user.view.item_search_user_follower_count as followerCount

class SearchUserViewHolder(parent: View): RecyclerView.ViewHolder(parent) {

    // PUBLIC API ---
    fun bindTo(user: User?){
        user?.let {
            loadImage(it.avatarUrl, itemView.image)
            val firstFollower: User? = try { it.followers[0] } catch (e: Exception) { null }
            val secondFollower: User? = try { it.followers[1] } catch (e: Exception) { null }
            firstFollower?.let { loadImage(it.avatarUrl, itemView.followerImage) }
            secondFollower?.let { loadImage(it.avatarUrl, itemView.followerImageBis) }
            itemView.title.text = it.login.capitalize()
            itemView.repositories.text = "${it.totalStars} - ${it.totalRepos} ${itemView.context.getString(R.string.repositories)}"
            itemView.followerName.text = firstFollower?.login?.capitalize()
            itemView.followerCount.text = "+${it.totalFollowers - 1}"
        }
    }

    // UTILS ---
    private fun loadImage(url: String, imageView: ImageView) {
        Glide.with(itemView.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }
}