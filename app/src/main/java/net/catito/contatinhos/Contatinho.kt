package net.catito.contatinhos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contatinho (val nome: String,
                       val telefone: String,
                       val email: String? = null,
                       val endereco: String? = null,
                       @ColumnInfo(name="caminho_foto")
                       val caminhoFoto: String? = null,
                       @PrimaryKey(autoGenerate = true)
                       val id: Int): Serializable