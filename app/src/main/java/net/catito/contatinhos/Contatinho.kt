package net.catito.contatinhos

import java.io.Serializable

data class Contatinho (val nome: String,
                       val telefone: String,
                       val email: String? = null,
                       val endereco: String? = null,
                       val caminhoFoto: String? = null): Serializable