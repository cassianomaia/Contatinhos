package net.catito.contatinhos

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_contatinhos.*
import org.jetbrains.anko.activityUiThreadWithContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

class ListaContatinhosActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CADASTRO: Int = 1
        private const val LISTA = "ListaContatinhos"
    }
    var listaContatinhos: MutableList<Contatinho> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatinhos)

        carregaLista()

        btnAddContatinho.setOnClickListener() {
            val cadastrarContatinho = Intent(this, CadastraContatinhoActivity::class.java)
            startActivity(cadastrarContatinho)
        }
    }

    override fun onResume() {
        super.onResume()
        carregaLista()
    }

    private fun carregaLista() {


        val contatinhoDao = AppDatabase.getInstance(this).contatinhoDao()
        doAsync{
            listaContatinhos = contatinhoDao.getAll() as MutableList<Contatinho>

            activityUiThreadWithContext {
                val adapter = ContatinhoAdapter(this, listaContatinhos)

                adapter.setOnItemClickListener { indexContatinhoClicado ->
                    val editaContatinho = Intent(this, CadastraContatinhoActivity::class.java)
                    editaContatinho.putExtra(CadastraContatinhoActivity.CONTATINHO, listaContatinhos.get(indexContatinhoClicado))
                    startActivity(editaContatinho)
                }

                adapter.configuraClickLongo {indexContatinhoClicado ->

                    doAsync {
                        contatinhoDao.delete(listaContatinhos.get(indexContatinhoClicado))

                        uiThread {
                            carregaLista()
                        }
                    }
                    true
                }

                val layoutManager = LinearLayoutManager(this)
                rvContatinhos.adapter = adapter
                rvContatinhos.layoutManager = layoutManager
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
