package com.purushottam.to_do_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView

class ToDoAdapter(context: Context,toDoList: MutableList<ToDoModel>): BaseAdapter() {

    private val inflater:LayoutInflater= LayoutInflater.from(context)
    private var itemList=toDoList
    private var updateAndDelete:UpdateAndDelete =context as UpdateAndDelete

    override fun getCount(): Int {
//        TODO("Not yet implemented")
        return itemList.size
    }

    override fun getItem(position: Int): Any {
//        TODO("Not yet implemented")
        return  itemList.get(position)
    }

    override fun getItemId(position: Int): Long {
//        TODO("Not yet implemented")
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val UID:String=itemList.get(position).UID as String
        val itemTextData=itemList.get(position).itemDataText as String
        val done:Boolean=itemList.get(position).done as Boolean

        val view:View
        val viewHolder:ListViewHolder

        if(convertView==null){
            view=inflater.inflate(R.layout.row_itemlayout,parent,false)
            viewHolder=ListViewHolder(view)
            view.tag=viewHolder
        }
        else{
            view=convertView
            viewHolder=view.tag as ListViewHolder
        }

        viewHolder.textLabel.text=itemTextData
        viewHolder.isDone.isChecked=done

        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID, !done)
        }

        viewHolder.isDeleted.setOnClickListener{
            updateAndDelete.onItemDelete(UID)
        }

        return view

//        TODO("Not yet implemented")
    }

   private class ListViewHolder(row:View?) {

       val textLabel:TextView=row!!.findViewById(R.id.item_textView) as TextView
       val isDone:CheckBox=row!!.findViewById(R.id.checkbox) as CheckBox
       val isDeleted:ImageButton=row!!.findViewById(R.id.close) as ImageButton
    }
}