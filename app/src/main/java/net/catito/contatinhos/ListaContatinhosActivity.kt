package net.catito.contatinhos

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_contatinhos.*
import java.util.ArrayList

class ListaContatinhosActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CADASTRO: Int = 1
        private const val LISTA = "ListaContatinhos"
    }
    var listaContatinhos: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatinhos)

        carregaLista()

        btnAddContatinho.setOnClickListener() {
            val cadastrarContatinho = Intent(this, CadastraContatinhoActivity::class.java)
            startActivityForResult(cadastrarContatinho, REQUEST_CADASTRO)
        }
    }

    override fun onResume() {
        super.onResume()
        carregaLista()
    }

    private fun carregaLista() {
        val adapter = ContatinhoAdapter(listaContatinhos)
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        rvContatinhos.adapter = adapter
        rvContatinhos.layoutManager = layoutManager
        rvContatinhos.addItemDecoration(dividerItemDecoration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CADASTRO && resultCode == Activity.RESULT_OK) {
            val novoContatinho: String? = data?.getStringExtra(CadastraContatinhoActivity.NOME_CONTATINHO)
            if (novoContatinho != null) {
                listaContatinhos.add(novoContatinho)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putStringArrayList(LISTA, listaContatinhos as ArrayList<String>)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState != null){
            listaContatinhos = savedInstanceState.getStringArrayList(LISTA).toMutableList();
        }
    }
}
