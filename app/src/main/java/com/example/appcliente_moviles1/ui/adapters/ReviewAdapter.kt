package com.example.appcliente_moviles1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente_moviles1.databinding.ReviewItemBinding
import com.example.appcliente_moviles1.models.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.binding.lblReviewerName.text = when {
            review.user != null -> "${review.user.name} ${review.user.last_name}"
            review.user_id != null -> "Usuario ${review.user_id}"
            else -> "Usuario desconocido"
        }
        holder.binding.lblReview.text = review.comment ?: "No hay comentario"
        holder.binding.lblReviewRating.text = "${review.rating} ★"
    }

    override fun getItemCount() = reviews.size
}
