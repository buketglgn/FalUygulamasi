package com.buketgulgun.faluygulamasi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gelenkutusu.*


class GelenkutusuFragment : Fragment() {
    private lateinit var recyclerAdapterFallar: RecyclerAdapterFallar

    var falListesi =ArrayList<String>()
    var falIdListesi= ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gelenkutusu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapterFallar = RecyclerAdapterFallar(falListesi,falIdListesi)
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.adapter= recyclerAdapterFallar

        sqlVeriAlma()

    }

    fun sqlVeriAlma(){
        try {
            context?.let {
                val db=it.openOrCreateDatabase("Fal", Context.MODE_PRIVATE,null )
                val cursor =db.rawQuery("SELECT * FROM fal",null)

                val idIndex=cursor.getColumnIndex("id")
                val adIndex =cursor.getColumnIndex("ad")
                val soyadIndex= cursor.getColumnIndex("soyad")

                falListesi.clear()
                falIdListesi.clear()

                while (cursor.moveToNext()){
                    falListesi.add("Fal Numarası:  "+ cursor.getInt(idIndex).toString()+" Ad Soyad: " + cursor.getString(adIndex)+" "+cursor.getString(soyadIndex))
                    falIdListesi.add(cursor.getInt(idIndex))
                }
                recyclerAdapterFallar.notifyDataSetChanged()
                cursor.close()

            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}