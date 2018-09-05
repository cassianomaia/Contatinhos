package net.catito.contatinhos

data class Contatinho (val nome: String,
                       val telefone: String,
                       val email: String? = null,
                       val endereco: String? = null)