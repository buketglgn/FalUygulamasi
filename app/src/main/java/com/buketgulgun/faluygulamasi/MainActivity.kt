package com.buketgulgun.faluygulamasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.profile_git,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //tıklandıgında ne olacak onun kodları yazılacak.


        if(item.itemId == R.id.profile_git_item) {

            Toast.makeText(applicationContext,"Hakkımızda sayfası açılacak..",Toast.LENGTH_LONG).show()

            //val action2 = FalgonderFragmentDirections.actionFalgonderFragmentToProfilFragment()
            //val action = AnasayfaFragmentDirections.actionAnasayfaFragmentToProfilFragment()
            //Navigation.findNavController(this, R.id.fragmentContainerView).navigate(action)

        }
        return super.onOptionsItemSelected(item)
    }
}