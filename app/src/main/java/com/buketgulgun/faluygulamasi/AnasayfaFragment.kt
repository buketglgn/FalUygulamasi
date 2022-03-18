package com.buketgulgun.faluygulamasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_anasayfa.*


class AnasayfaFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anasayfa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView.setOnClickListener{
            falgonder(it)
        }
        buttonprofilegit.setOnClickListener{
            profilegit(it)
        }
        buttonGelenkutusu.setOnClickListener{
            gelenkutusunagit(it)
        }
    }
    fun falgonder(view: View){
        val action= AnasayfaFragmentDirections.actionAnasayfaFragmentToFalgonderrFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun profilegit(view: View){
        val action= AnasayfaFragmentDirections.actionAnasayfaFragmentToProfilFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun gelenkutusunagit(view: View){
        val action= AnasayfaFragmentDirections.actionAnasayfaFragmentToGelenkutusuFragment2()
        Navigation.findNavController(view).navigate(action)
    }



}