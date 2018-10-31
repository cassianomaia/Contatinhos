package net.catito.contatinhos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contatinho (var nome: String,
                       var telefone: String,
                       var email: String? = null,
                       var endereco: String? = null,
                       @ColumnInfo(name="caminho_foto")
                       var caminhoFoto: String? = null,
                       @PrimaryKey(autoGenerate = true)
                       var id: Int = 0): Serializable