package com.dzenis_ska.nutrition

import android.app.AlertDialog
import android.util.Log
import com.dzenis_ska.nutrition.databinding.DialogAddKcalBinding

class DialogHelper(val act: MainActivity) {
    var rootDialogElement: DialogAddKcalBinding? = null
    fun createDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        rootDialogElement = DialogAddKcalBinding.inflate(act.layoutInflater)
        val view = rootDialogElement!!.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        rootDialogElement!!.apply {
            fab.setOnClickListener{
                if(edDialog.text.isNotEmpty()){
                    Log.d("!!!", "tex")
                    val text = edDialog.text.toString()
                    act.showResult(index, text)
                    dialog.dismiss()
                }

            }

        }


    }



}