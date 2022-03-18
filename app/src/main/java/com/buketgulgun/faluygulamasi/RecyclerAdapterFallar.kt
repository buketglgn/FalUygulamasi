package com.buketgulgun.faluygulamasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row_fallar.view.*

class RecyclerAdapterFallar (val fallistesi:ArrayList<String>, val falIdListesi:ArrayList<Int>) :RecyclerView.Adapter<RecyclerAdapterFallar.FallarHolder>(){
    class  FallarHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FallarHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row_fallar,parent,false)
        return  FallarHolder(view)
    }

    override fun onBindViewHolder(holder: FallarHolder, position: Int) {
        holder.itemView.recycler_row_fal_text.text=fallistesi[position]
        holder.itemView.setOnClickListener{
            val action= GelenkutusuFragmentDirections.actionGelenkutusuFragment2ToFalokuFragment(falIdListesi[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return fallistesi.size
    }
}