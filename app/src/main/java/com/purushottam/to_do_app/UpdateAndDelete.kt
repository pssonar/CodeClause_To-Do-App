package com.purushottam.to_do_app

interface UpdateAndDelete{
    fun modifyItem(itemUID :String ,isDone:Boolean)
    fun onItemDelete(itemUID:String)
}