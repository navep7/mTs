package com.belaku.knowing

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TweetsRvAdapter(var context: Context, tweets: ArrayList<String>) :
    RecyclerView.Adapter<TweetsRvAdapter.ViewHolder>() {
    var tweets: ArrayList<String> = tweets

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tweetView.text = tweets[position]
    }

    override fun getItemCount(): Int {
        return tweets.size;
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tweetView: TextView = view.findViewById(R.id.tx_tw) as TextView

    }
}
