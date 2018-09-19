package net.catito.contatinhos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastraContatinhoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnFoto.setOnClickListener() {
            Toast.makeText(this, "Vamos tirar uma foto!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.menuSalvar -> salvaContatinho()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun salvaContatinho() {
        val contatinho = Contatinho(edtNome.text.toString(),
                                    edtTelefone.text.toString(),
                                    edtEmail.text.toString(),
                                    edtEndereco.text.toString())

        Toast.makeText(this, contatinho.toString(), Toast.LENGTH_LONG).show()
    }
}
