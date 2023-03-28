package com.purushottam.to_do_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(),UpdateAndDelete {

    lateinit var database: DatabaseReference
    var toDoList:MutableList<ToDoModel>?=null
    lateinit var adapter: ToDoAdapter
    private var listViewItem:ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        listViewItem=findViewById(R.id.item_listView) as ListView

        database = FirebaseDatabase.getInstance().reference

        fab.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("Add TODO items")
            alertDialog.setTitle("Enter To Do item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add") { dialog, i ->
                val todoItemData = ToDoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false

                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key

                newItemData.setValue(todoItemData)
                dialog.dismiss()
                Toast.makeText(this, "item saved", Toast.LENGTH_LONG).show()

            }
            alertDialog.show()
        }
        toDoList= mutableListOf<ToDoModel>()

        adapter= ToDoAdapter(this,toDoList!!)
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
                toDoList!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")

                Toast.makeText(applicationContext,"No item Added",Toast.LENGTH_LONG).show()
            }

        })


    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items=snapshot.children.iterator()
        if(items.hasNext()){
            val toDoIndexedvalue=items.next()
            val itemsIterator=toDoIndexedvalue.children.iterator()

            while(itemsIterator.hasNext()){
                val currentitem=itemsIterator.next()
                val toDoItemData=ToDoModel.createList()
                val map=currentitem.getValue() as HashMap<String,Any>

                toDoItemData.UID=currentitem.key
                toDoItemData.done=map.get("done") as Boolean?
                toDoItemData.itemDataText=map.get("itemDataText") as String?
                toDoList!!.add(toDoItemData)

            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
//        TODO("Not yet implemented")
        val itemReference =database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
//        TODO("Not yet implemented")
        val itemReference=database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}
