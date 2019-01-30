package com.mertavincan.materialnothesapla

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.mertavincan.materialnothesapla.R.color.*
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.satir_gorunumu.*
import kotlinx.android.synthetic.main.satir_gorunumu.view.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    var tumDersBilgilerim=ArrayList<Dersler>(20)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        

        editText_dersPuaniGiris.setText("")
        if(rootLayout.childCount == 0){

            buttonOrtHesapla.visibility = View.INVISIBLE

        }else
            buttonOrtHesapla.visibility = View.VISIBLE

        var mAnim=AnimationUtils.loadAnimation(this,R.anim.swiperight)

        var mAnim2=AnimationUtils.loadAnimation(this,R.anim.swipeleft)

        var mAnim3=AnimationUtils.loadAnimation(this,R.anim.frombottom)



        editText_dersPuaniGiris.textWatcher { afterTextChanged {

            if (!editText_dersPuaniGiris.isEmpty){

                editText_agirlikliOrtSonuc.setText("${spinner_dersSaatiGiris.selectedItem.toString().toDouble()*editText_dersPuaniGiris.text.toString().toDouble()}")

            }

        } }

        spinner_dersSaatiGiris.onItemSelectedListener =object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(editText_dersPuaniGiris.text.isNullOrEmpty()){
                        editText_agirlikliOrtSonuc.setText("${spinner_dersSaatiGiris.selectedItem.toString().toDouble()*0.0}")

                    }else{
                        editText_agirlikliOrtSonuc.setText("${spinner_dersSaatiGiris.selectedItem.toString().toDouble()*editText_dersPuaniGiris.text.toString().toDouble()}")

                    }

            }

        }
        dersEkle.setOnClickListener {

            if (spinner_dersAdiGiris.selectedItemPosition!=0 && !editText_dersPuaniGiris.text.isNullOrEmpty() && editText_dersPuaniGiris.text.toString().toDouble()>=0 && editText_dersPuaniGiris.text.toString().toDouble()<=100){

                var mInflater=layoutInflater.inflate(R.layout.satirgorunumuex,null,false)

                if(rootLayout.childCount%2==0) {

                    mInflater.button_deleteItem.setOnClickListener {

                        mInflater.myLinear.startAnimation(mAnim)

                        object : CountDownTimer(400,1000){

                            override fun onFinish() {

                                rootLayout.removeView(mInflater)

                                if(rootLayout.childCount == 0){

                                    buttonOrtHesapla.visibility = View.INVISIBLE

                                }else{

                                    buttonOrtHesapla.visibility = View.VISIBLE


                                }
                            }

                            override fun onTick(millisUntilFinished: Long) {

                            }
                        }.start()

                    }

                    bilgileriTasi(mInflater)

                    rootLayout.addView(mInflater)

                    buttonOrtHesapla.visibility = View.VISIBLE

                    buttonOrtHesapla.animation=mAnim3


                    mInflater.myLinear.startAnimation(mAnim2)

                    rootLayout.getChildAt(rootLayout.childCount-1).background = ContextCompat.getDrawable(applicationContext, dark)

                    mInflater.text1.setTextColor(resources.getColor(R.color.white,theme))

                    mInflater.text2.setTextColor(resources.getColor(R.color.white,theme))

                    mInflater.text3.setTextColor(resources.getColor(R.color.white,theme))

                    mInflater.satirDersAdi.setTextColor(resources.getColor(R.color.white,theme))

                    mInflater.satirDersSaati.setTextColor(resources.getColor(R.color.white,theme))

                    mInflater.satirPuan.setTextColor(resources.getColor(R.color.white,theme))

                    sifirla()

                }else{

                    mInflater.button_deleteItem.setOnClickListener {

                        mInflater.myLinear.startAnimation(mAnim)

                        object : CountDownTimer(400,1000){

                            override fun onFinish() {

                                rootLayout.removeView(mInflater)

                                if(rootLayout.childCount == 0){

                                    buttonOrtHesapla.visibility = View.INVISIBLE

                                }else
                                    buttonOrtHesapla.visibility = View.VISIBLE
                            }
                            override fun onTick(millisUntilFinished: Long) {
                            }
                        }.start()

                    }
                    bilgileriTasi(mInflater)

                    rootLayout.addView(mInflater)

                    buttonOrtHesapla.visibility = View.VISIBLE

                    buttonOrtHesapla.animation=mAnim3

                    mInflater.myLinear.startAnimation(mAnim2)


                    rootLayout.getChildAt(rootLayout.childCount-1).background = ContextCompat.getDrawable(applicationContext, white)

                    sifirla()
                }

            }else{
                Toast.makeText(applicationContext,"Lütfen geçerli ders ve puan bilgileri giriniz.",Toast.LENGTH_LONG).show()
            }


        }
        buttonOrtHesapla.setOnClickListener {
            ortalamaHesapla()
        }

    }

    fun sifirla(){
        spinner_dersAdiGiris.setSelection(0)

        spinner_dersSaatiGiris.setSelection(0)

        editText_dersPuaniGiris.setText("")
    }
    fun bilgileriTasi(inflater:View){
        inflater.satirDersSaati.setText(spinner_dersSaatiGiris.selectedItem.toString())

        inflater.satirDersAdi.setText(spinner_dersAdiGiris.selectedItem.toString())

        inflater.satirPuan.setText(editText_dersPuaniGiris.text)

    }

    fun ortalamaHesapla(){
        var toplamNot=0.0

        var toplamSaat=0.0

        for (i in 0 until rootLayout.childCount){

            var tekSatir=rootLayout.getChildAt(i)

            var geciciDers=Dersler(

                tekSatir.satirDersSaati.text.toString().toDouble(),
                tekSatir.satirPuan.text.toString().toDouble()
                )

            tumDersBilgilerim.add(geciciDers)
        }

        for (i in tumDersBilgilerim){

            toplamNot+=i.dersPuani*i.dersSaati

            toplamSaat+=i.dersSaati

        }

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Sonuç")

        if (toplamNot/toplamSaat>70 && toplamNot/toplamSaat<85){
            builder.setMessage("Ortalamanız:${toplamNot/toplamSaat} \nBu ortalamaya göre Teşekkür belgesi almaya hak kazandınız.")
        }else if (toplamNot/toplamSaat>=85){
            builder.setMessage("Ortalamanız:${toplamNot/toplamSaat} \nBu ortalamaya göre Takdir belgesi almaya hak kazandınız.")
        }else {
            builder.setMessage("Ortalamanız:${toplamNot/toplamSaat} \nBu ortalamaya göre herhangi bir belge almaya hak kazanamadınız.")
        }

        builder.setPositiveButton("Çıkış"){dialog, which ->

        }

        builder.setNegativeButton("Uygulamayı Değerlendir"){dialog,which ->
            var playstoreuri2: Uri = Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)
            //var playstoreuri:Uri=Uri.parse("http://play.google.com/store/apps/details?id=manigautam.app.myplaystoreratingapp")
            var playstoreIntent2: Intent = Intent(Intent.ACTION_VIEW, playstoreuri2)
            startActivity(playstoreIntent2)
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()

    }


}
