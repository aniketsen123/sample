package com.example.ui.adapter.convertpdf

import android.app.AlertDialog
import android.content.Context

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


class Image_Adapter(val context:Context):RecyclerView.Adapter<Image_Adapter.ViewHolder>() {
    val list: MutableList<Uri> =ArrayList()
    fun upatelist(list1:MutableList<Uri>)
    {
        list.clear()
        list.addAll(list1)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)
    {
          val image=itemview.findViewById<ImageView>(R.id.imageView6)
        val cardView=itemView.findViewById<CardView>(R.id.cardview1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_items, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current:Uri =list[position]
       holder.image.setImageURI(current)
        holder.cardView.setPreventCornerOverlap(false)
          holder.cardView.setOnLongClickListener {
              showdialog(position)

          }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun showdialog(position:Int): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Do you want to delete ?")
        builder.setTitle("Alert !")
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") {
                dialog, which ->

                list.removeAt(position)
            notifyDataSetChanged()



        }

        builder.setNegativeButton("No") {

                dialog, which -> dialog.cancel()
        }

        val alertDialog = builder.create()

        alertDialog.show()
        return true

    }

    }

