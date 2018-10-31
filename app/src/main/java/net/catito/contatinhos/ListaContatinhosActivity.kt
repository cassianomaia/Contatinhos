package net.catito.contatinhos

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_contatinhos.*
import java.util.ArrayList

class ListaContatinhosActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CADASTRO: Int = 1
        private const val LISTA = "ListaContatinhos"
    }
    var listaContatinhos: MutableList<Contatinho> = mutableListOf()
    var indexContatinhoClicado: Int = -1

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
        val adapter = ContatinhoAdapter(this, listaContatinhos)

        adapter.setOnItemClickListener { indexContatinhoClicado ->
                this.indexContatinhoClicado = indexContatinhoClicado
                val editaContatinho = Intent(this, CadastraContatinhoActivity::class.java)
                editaContatinho.putExtra(CadastraContatinhoActivity.CONTATINHO, listaContatinhos.get(indexContatinhoClicado))
                this.startActivityForResult(editaContatinho, REQUEST_CADASTRO)
        }

        adapter.configuraClickLongo {indexContatinhoClicado ->
            listaContatinhos.removeAt(indexContatinhoClicado)
            carregaLista()
            true
        }

        val layoutManager = LinearLayoutManager(this)
        rvContatinhos.adapter = adapter
        rvContatinhos.layoutManager = layoutManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CADASTRO && resultCode == Activity.RESULT_OK) {
            val contatinho: Contatinho? = data?.getSerializableExtra(CadastraContatinhoActivity.CONTATINHO) as Contatinho
            if (contatinho != null) {
                if(indexContatinhoClicado >= 0){
                    listaContatinhos.set(indexContatinhoClicado, contatinho)
                    indexContatinhoClicado = -1
                }else {
                    listaContatinhos.add(contatinho)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(LISTA, listaContatinhos as ArrayList<String>)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState != null){
            listaContatinhos = savedInstanceState.getSerializable(LISTA) as MutableList<Contatinho>
        }
    }
}
