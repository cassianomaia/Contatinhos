package net.catito.contatinhos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.contatinho_item_lista.view.*

class ContatinhoAdapter(val context: Context, val contatinhos: List<Contatinho>)
    : RecyclerView.Adapter<ContatinhoAdapter.ViewHolder>() {

    var clickListener: (( index: Int) -> Unit)? = null

    var clickLongoListener: (( index: Int) -> Boolean)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contatinho_item_lista, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contatinhos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(context, contatinhos[position], clickListener, clickLongoListener)
    }


    fun setOnItemClickListener(clique: ((index: Int) -> Unit)){
        this.clickListener = clique
    }

    fun configuraClickLongo(cliqueLongo: (( index: Int) -> Boolean)){
        this.clickLongoListener = cliqueLongo
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(context: Context,
                     contatinho: Contatinho,
                     clickListener: ((index: Int) -> Unit)?,
                     clickLongoListener: (( index: Int) -> Boolean)?) {
            itemView.tvNome.text = contatinho.nome
            itemView.tvTelefone.text = contatinho.telefone

            val thumbnail = GlideApp.with(context)
                                    .load(R.drawable.ic_person)
                                    .apply(RequestOptions().circleCrop())

            GlideApp.with(context)
                    .load(contatinho.caminhoFoto)
                    .thumbnail(thumbnail)
                    .centerCrop()
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.imgFoto)

            if(clickListener != null) {
                itemView.setOnClickListener {
                    clickListener.invoke(adapterPosition)
                }
            }

            if(clickLongoListener != null){
                itemView.setOnLongClickListener {
                    clickLongoListener.invoke(adapterPosition)
                }
            }
        }
    }
}