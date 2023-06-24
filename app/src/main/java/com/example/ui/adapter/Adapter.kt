package com.example.ui.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.adapter.convertpdf.Convert
import com.example.ui.R

import com.example.ui.model


class Adapter(val context:Context, val list: ArrayList<model>):RecyclerView.Adapter<Adapter.ViewHolder>() {
    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)
    {
          val image=itemview.findViewById<ImageView>(R.id.imageView2)
        val data=itemview.findViewById<TextView>(R.id.textView2)
        val cardView=itemView.findViewById<CardView>(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current =list[position]
        val drawable: Drawable? = getDrawable(context,current.img)
        holder.image.setImageDrawable(drawable)
        if(isDarkTheme(context))
        {
            val drawable: Drawable? = getDrawable(context,current.img)
            holder.image.setImageDrawable(drawable)
        }
        else
        {
            val drawable: Drawable? = getDrawable(context,current.img1)
            holder.image.setImageDrawable(drawable)
        }
        holder.data.text=current.text
        holder.cardView.setOnClickListener{
            if(current.text=="Pdf Convert")
            {
                     val intent=Intent(it.context, Convert::class.java)
                it.context.startActivity(intent)
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun isDarkTheme(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}