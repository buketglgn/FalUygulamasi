package com.buketgulgun.faluygulamasi

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_profil.*
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Exception


class ProfilFragment : Fragment() {

    var idIndex=999
    var secilenGorsel : Uri?= null
    var secilenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewPRofilfoto.setOnClickListener{
            gorselSec(it)
        }
        buttonKaydet.setOnClickListener{
            kaydet(it)
        }
        silButon.setOnClickListener{
            profilisil(it)
        }
        guncelleButon.setOnClickListener{
            guncelle(it)
        }

        guncelleButon.visibility=View.INVISIBLE
        silButon.visibility=View.INVISIBLE

        context?.let{
            try {


            val dbprofil=it.openOrCreateDatabase("Profil",Context.MODE_PRIVATE,null )
            val cursor =dbprofil.rawQuery("SELECT * FROM profil",null)



            idIndex=cursor.getColumnIndex("id")
            val adIndex =cursor.getColumnIndex("ad")
            val soyadIndex= cursor.getColumnIndex("soyad")
            val burcIndex= cursor.getColumnIndex("burc")
            val dogumtarihiIndex=cursor.getColumnIndex("dogumtarihi")
            val meslekIndex=cursor.getColumnIndex("meslek")
            val gorselIndex= cursor.getColumnIndex("gorsel")



                if(idIndex==999){
                    println("veri yok anlamında")


                }else{
                    while (cursor.moveToNext()) {
                        println("aaaaaaaaaaaa")
                        println(cursor.getInt(idIndex))
                        println(cursor.getString(adIndex))

                        adEditText.setText(cursor.getString(adIndex))
                        soyadEditText.setText(cursor.getString(soyadIndex))
                        burcEditText.setText(cursor.getString(burcIndex))
                        dogumtarihiEditText.setText(cursor.getString(dogumtarihiIndex))
                        meslekEditText.setText(cursor.getString(meslekIndex))

                        val byteDizi=cursor.getBlob(gorselIndex)
                        val bitmapp= BitmapFactory.decodeByteArray(byteDizi,0,byteDizi.size)
                        imageViewPRofilfoto.setImageBitmap(bitmapp)

                        buttonKaydet.visibility=View.INVISIBLE
                        guncelleButon.visibility=View.VISIBLE
                        silButon.visibility=View.VISIBLE
                    }
                }

                cursor.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
        }
    }

    fun kaydet(view: View){

        val ad= adEditText.text.toString()
        val soyad=soyadEditText.text.toString()
        val burc=burcEditText.text.toString()
        val dogumTarihi= dogumtarihiEditText.text.toString()
        val meslek= meslekEditText.text.toString()

        if(secilenBitmap !=null){

            val kucukBitmap= kucukBitmapOlustur(secilenBitmap!!,300)

            val outputStream= ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
            val byteDizisi= outputStream.toByteArray()

            try {
                context?.let{
                    val databaseProfil=it.openOrCreateDatabase("Profil",Context.MODE_PRIVATE,null)
                    databaseProfil.execSQL("CREATE TABLE IF NOT EXISTS profil(id INTEGER PRIMARY KEY,ad VARCHAR,soyad VARCHAR,burc VARCHAR, dogumtarihi VARCHAR, meslek VARCHAR, gorsel BLOB)")
                    val sqlString ="INSERT INTO profil(ad,soyad,burc,dogumtarihi,meslek,gorsel) VALUES (?,?,?,?,?,?)"
                    val statement= databaseProfil.compileStatement(sqlString)
                    statement.bindString(1,ad)
                    statement.bindString(2,soyad)
                    statement.bindString(3,burc)
                    statement.bindString(4,dogumTarihi)
                    statement.bindString(5,meslek)
                    statement.bindBlob(6,byteDizisi)
                    statement.execute()
                }

            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        context?.let{
            Toast.makeText(it,"Profil Kaydedildi..",Toast.LENGTH_LONG).show()
        }

        val action= ProfilFragmentDirections.actionProfilFragmentToAnasayfaFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun gorselSec(view: View){
        activity?.let{
            if(ContextCompat.checkSelfPermission(it.applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //izin verilmedi izin istememiz gerekiyor.
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }else{
                //iizn zaten verilmiş tekrar istemeden galeriye git.
                val galeriIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }

    }
    fun guncelle(view: View){
        val adtext= adEditText.text.toString()
        val soyadtext=soyadEditText.text.toString()
        val burctext=burcEditText.text.toString()
        val dogumTarihitext= dogumtarihiEditText.text.toString()
        val meslektext= meslekEditText.text.toString()
        try {
            context?.let {

                val dbb=it.openOrCreateDatabase("Profil",Context.MODE_PRIVATE,null)
                val strSQL = "UPDATE profil SET ad = adtext WHERE id =idIndex " ;

                dbb.execSQL(strSQL)
                Toast.makeText(it,"Profil Güncellendi..",Toast.LENGTH_LONG).show()
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun profilisil(view: View){
        try {
            context?.let{


                //Toast.makeText(it,"Profil Silindi..",Toast.LENGTH_LONG).show()

                val uyariMesaji= AlertDialog.Builder(it)
                uyariMesaji.setTitle("Profil Silme")
                uyariMesaji.setMessage("Bu profili Silmek istediğinize emin misiniz?")
                uyariMesaji.setPositiveButton("Evet")
                {
                     dialogInterface, i ->
                    val db=it.openOrCreateDatabase("Profil",Context.MODE_PRIVATE,null)
                    db.execSQL("DELETE FROM profil ")

                    Toast.makeText(it,"Profil Silindi..",Toast.LENGTH_LONG).show()

                    val action= ProfilFragmentDirections.actionProfilFragmentToAnasayfaFragment()
                    Navigation.findNavController(view).navigate(action)
                }
                uyariMesaji.setNegativeButton("Hayır")
                {
                        dialogInterface, i ->


                }
                uyariMesaji.show()



            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode ==1){
            if(grantResults.size > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //izni aldık, simdi galeriye git
                val galeriIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //foto seçildikten sonra ne olacak
        if(requestCode ==2 && resultCode==Activity.RESULT_OK && data!=null){
            secilenGorsel= data.data

            //gorseli aldık simdi bitmape cevirmemiz lazım
            try {
                context?.let{
                    if(secilenGorsel !=null){
                        if(Build.VERSION.SDK_INT >=28){
                            val source = ImageDecoder.createSource(it.contentResolver, secilenGorsel!!)
                            secilenBitmap= ImageDecoder.decodeBitmap(source)
                            imageViewPRofilfoto.setImageBitmap(secilenBitmap)
                        }
                        else{
                            secilenBitmap=MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel)
                            imageViewPRofilfoto.setImageBitmap(secilenBitmap)
                        }
                    }
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun kucukBitmapOlustur(kullanicininSectigiBitmap:Bitmap,maximumBoyut:Int):Bitmap{

        var width =  kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height

        val bitmapOrani :Double =width.toDouble() /height.toDouble()
        if(bitmapOrani >1 ){
            //görsel yatay
            width= maximumBoyut
            val kisaltilmisHeight = width /bitmapOrani
            height =kisaltilmisHeight.toInt()
        }
        else{
            height =maximumBoyut
            val kisaltilmisWidth= height * bitmapOrani
            width = kisaltilmisWidth.toInt()
            //görsel dikey
        }
        return  Bitmap.createScaledBitmap(kullanicininSectigiBitmap,width ,height , true)
    }


}