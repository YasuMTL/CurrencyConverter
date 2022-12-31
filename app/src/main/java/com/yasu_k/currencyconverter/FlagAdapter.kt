package com.yasu_k.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FlagAdapter(
    context: Context,
    flags: Array<Int>,
    currencyNames: List<String>
): BaseAdapter() {

    private var mContext: Context
    private var mFlags: Array<Int>
    private var mCurrencyNames: List<String>
    private var mInflater: LayoutInflater? = null

    init {
        mContext = context
        mFlags = flags
        mCurrencyNames = currencyNames
        mInflater = LayoutInflater.from(context)
    }


    override fun getCount(): Int {
        return mFlags.size
    }

    override fun getItem(position: Int): Any {
        return mCurrencyNames[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = mInflater!!.inflate(R.layout.custom_spinner_currencies, parent,false)
        val icon = view.findViewById<ImageView>(R.id.imageView)
        val currencyNames = view.findViewById<TextView>(R.id.textView)

        val imageToSet = mFlags[position]
        val currencyToSet = mCurrencyNames[position]

        icon.setImageResource(imageToSet)
        currencyNames.text = currencyToSet

        return view!!
    }
}