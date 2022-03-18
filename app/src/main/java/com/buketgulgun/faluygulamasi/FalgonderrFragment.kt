package com.buketgulgun.faluygulamasi

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.fragment_falgonderr.*
import kotlinx.android.synthetic.main.fragment_profil.*
import java.io.ByteArrayOutputStream


class FalgonderrFragment : Fragment() {
    var secilenGorsel1 : Uri?= null
    var secilenBitmap1 : Bitmap? = null
    var byteDizisi1 :ByteArray?=null
    var byteDizisi2 :ByteArray?=null
    var byteDizisi3 :ByteArray?=null
    var secilendeger: String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_falgonderr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonfalgonder.setOnClickListener{
            faligonder(it)
        }
        imageView2.setOnClickListener{
            gorselSec(it)
            secilendeger="ilkfoto"
        }
        imageView3.setOnClickListener{
            gorselSec(it)
            secilendeger="ikincifoto"
        }
        imageView4.setOnClickListener{
            gorselSec(it)
            secilendeger="ucuncufoto"
        }


        try {
            context?.let{
                val database=it.openOrCreateDatabase("Profil", Context.MODE_PRIVATE,null )
                val cursor =database.rawQuery("SELECT * FROM profil",null)

                val idIndex=cursor.getColumnIndex("id")
                val adIndex =cursor.getColumnIndex("ad")
                val soyadIndex= cursor.getColumnIndex("soyad")
                val burcIndex= cursor.getColumnIndex("burc")
                val dogumtarihiIndex=cursor.getColumnIndex("dogumtarihi")
                val meslekIndex=cursor.getColumnIndex("meslek")

                while (cursor.moveToNext()) {
                    ad1EditText.setText(cursor.getString(adIndex))
                    soyad1EditText.setText(cursor.getString(soyadIndex))
                    burc1EditText3.setText(cursor.getString(burcIndex))
                    dogumtarihi1EditText5.setText(cursor.getString(dogumtarihiIndex))
                    meslek1EditText4.setText(cursor.getString(meslekIndex))
                }
                cursor.close()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun faligonder(view: View){
        val ad= ad1EditText.text.toString()
        val soyad= soyad1EditText.text.toString()
        val burc= burc1EditText3.text.toString()
        val dogumTarihi= dogumtarihi1EditText5.text.toString()
        val meslek= meslek1EditText4.text.toString()
        val iliski= iliskiEditText.text.toString()

        try {
            context?.let{
                val db=it.openOrCreateDatabase("Fal",Context.MODE_PRIVATE,null)
                db.execSQL("CREATE TABLE IF NOT EXISTS fal(id INTEGER PRIMARY KEY,ad VARCHAR,soyad VARCHAR,burc VARCHAR, dogumtarihi VARCHAR, meslek VARCHAR,iliski VARCHAR, gorsel1 BLOB,gorsel2 BLOB,gorsel3 BLOB)")
                val sqlString ="INSERT INTO fal(ad,soyad,burc,dogumtarihi,meslek,iliski,gorsel1,gorsel2,gorsel3) VALUES (?,?,?,?,?,?,?,?,?)"
                val statement= db.compileStatement(sqlString)
                statement.bindString(1,ad)
                statement.bindString(2,soyad)
                statement.bindString(3,burc)
                statement.bindString(4,dogumTarihi)
                statement.bindString(5,meslek)
                statement.bindString(6,iliski)
                statement.bindBlob(7,byteDizisi1)
                statement.bindBlob(8,byteDizisi2)
                statement.bindBlob(9,byteDizisi3)
                statement.execute()
                Toast.makeText(it,"Fal Gönderildi..",Toast.LENGTH_LONG).show()
                val action=FalgonderrFragmentDirections.actionFalgonderrFragmentToAnasayfaFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }catch (e:Exception){

        }



    }
    fun gorselSec(view: View){
        activity?.let{
            if(ContextCompat.checkSelfPermission(it.applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //izin verilmedi izin istememiz gerekiyor.
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }else{
                //iizn zaten verilmiş tekrar istemeden galeriye git.
                val galeriIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
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
        if(requestCode ==2 && resultCode== Activity.RESULT_OK && data!=null){
            secilenGorsel1= data.data

            //gorseli aldık simdi bitmape cevirmemiz lazım
            try {
                context?.let{
                    if(secilenGorsel1 !=null){
                        if(Build.VERSION.SDK_INT >=28){

                            if(secilendeger=="ilkfoto"){
                                val source = ImageDecoder.createSource(it.contentResolver, secilenGorsel1!!)
                                secilenBitmap1= ImageDecoder.decodeBitmap(source)
                                imageView2.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                 byteDizisi1= outputStream.toByteArray()

                            }else if(secilendeger=="ikincifoto"){
                                val source = ImageDecoder.createSource(it.contentResolver, secilenGorsel1!!)
                                secilenBitmap1= ImageDecoder.decodeBitmap(source)
                                imageView3.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                byteDizisi2= outputStream.toByteArray()
                                //imageView3.setSelected(false)

                            }else if(secilendeger=="ucuncufoto"){
                                val source = ImageDecoder.createSource(it.contentResolver, secilenGorsel1!!)
                                secilenBitmap1= ImageDecoder.decodeBitmap(source)
                                imageView4.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                byteDizisi3= outputStream.toByteArray()
                            }else{
                                Toast.makeText(it, "olmadıı", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            //secilenBitmap1=MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel1)
                            if(secilendeger=="ilkfoto"){
                                secilenBitmap1=MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel1)
                                imageView2.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                byteDizisi1= outputStream.toByteArray()
                            }else if(secilendeger=="ikincifoto"){
                                secilenBitmap1=MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel1)
                                imageView3.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                byteDizisi2= outputStream.toByteArray()
                            }else if(secilendeger=="ucuncufoto"){
                                secilenBitmap1=MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel1)
                                imageView4.setImageBitmap(secilenBitmap1)
                                val kucukBitmap= kucukBitmapOlustur(secilenBitmap1!!,300)
                                val outputStream= ByteArrayOutputStream()
                                kucukBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
                                byteDizisi3= outputStream.toByteArray()
                            }
                            else{
                                Toast.makeText(it, "olmadıı", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }

            }catch (e: java.lang.Exception){
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