package com.vipin.mygatekotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vipin.mygatekotlin.R
import com.vipin.mygatekotlin.model.DataClass
import java.io.ByteArrayInputStream

/**
 * Created by vipin.c on 25/06/2019
 */
class RecyclerAdapter() : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    lateinit var mList: List<DataClass>
    lateinit var mContext: Context

    constructor(mList: List<DataClass>, mContext: Context) : this() {
        this.mList = mList
        this.mContext = mContext
    }

    fun setList(list: List<DataClass>){
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_view, p0, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.username.text = "User " + mList[p1].id
        p0.randomNumber.text = "# " + mList[p1].randomNum

        val getImageByteArray = mList[p1].image
        val stream = ByteArrayInputStream(getImageByteArray)
        val imageBitmap = BitmapFactory.decodeStream(stream)
        p0.imageView.setImageBitmap(imageBitmap)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById<ImageView>(R.id.image_id)!!
        val username = itemView.findViewById<TextView>(R.id.user_name)!!
        val randomNumber = itemView.findViewById<TextView>(R.id.random_no)!!
    }
}