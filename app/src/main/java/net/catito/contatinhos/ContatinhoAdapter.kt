package net.catito.contatinhos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.contatinho_item_lista.view.*

class ContatinhoAdapter(val contatinhos: List<String>)
    : RecyclerView.Adapter<ContatinhoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contatinho_item_lista, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contatinhos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(contatinhos[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(contatinhoNome: String) {
            itemView.tvNome.text = contatinhoNome
        }

    }
}