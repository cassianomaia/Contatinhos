package net.catito.contatinhos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlideApp.with(this)
                .load("https://cdn.shopify.com/s/files/1/1061/1924/files/Blow_Kiss_Emoji.png")
                .placeholder(R.mipmap.logo)
                .into(imgLogoApp)


        Handler().postDelayed({
            val listaContatinhos = Intent(this, ListaContatinhosActivity::class.java)
            startActivity(listaContatinhos)
            finish()
        }, 2000)
    }
}
