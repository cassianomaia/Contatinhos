package net.catito.contatinhos

import android.arch.persistence.room.*

@Dao
interface ContatinhoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contatinho: Contatinho)

    @Query("SELECT * FROM contatinho")
    fun getAll(): List<Contatinho>

    @Delete
    fun delete(contatinho: Contatinho)

    @Query("SELECT * FROM contatinho WHERE id= :contatinhoId LIMIT 1")
    fun getContatinho(contatinhoId: Int): Contatinho

    @Query("SELECT * FROM contatinho WHERE nome like :contatinhoNome")
    fun busquePeloNome(contatinhoNome: String): List<Contatinho>
}