package com.buketgulgun.faluygulamasi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_anasayfa.*
import kotlinx.android.synthetic.main.fragment_faloku.*


class FalokuFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faloku, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            var gelenId=FalokuFragmentArgs.fromBundle(it).falidd

            context?.let{
                try {
                    val db=it.openOrCreateDatabase("Fal", Context.MODE_PRIVATE,null )
                    val cursor =db.rawQuery("SELECT * FROM fal WHERE id=? ", arrayOf(gelenId.toString()))

                    val adIndex =cursor.getColumnIndex("ad")
                    val soyadIndex= cursor.getColumnIndex("soyad")
                    val burcIndex= cursor.getColumnIndex("burc")
                    val dogumtarihiIndex=cursor.getColumnIndex("dogumtarihi")
                    val meslekIndex=cursor.getColumnIndex("meslek")
                    val iliskiIndex= cursor.getColumnIndex("iliski")

                    while (cursor.moveToNext()){
                        textView3.setText(cursor.getString(adIndex)+" "+cursor.getString(burcIndex)+" "+cursor.getString(iliskiIndex))
                    }
                    cursor.close()
                }catch (e:Exception){
                    e.printStackTrace()

                }
            }


        }
    }
}