package net.catito.contatinhos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class CadastraContatinhoActivity : AppCompatActivity() {

    companion object {
        public const val CONTATINHO: String = "Contatinho"
        private const val REQUET_PERMISSOES: Int = 3
        private const val REQUEST_CAMERA: Int = 10
    }

    var caminhoFoto:String? = null
    var caminhoFotoAceita:String? = null
    var contatinho: Contatinho? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        contatinho = intent.getSerializableExtra(CONTATINHO) as Contatinho?
        if(contatinho != null){
            carregaDados()
        }

        btnFoto.setOnClickListener() {
           tirarFoto()
        }

        imgTelefone.setOnClickListener(){ view ->
            telefonaOuEnviaMensagem(view)
        }

        imgEmail.setOnClickListener(){
            enviaEmail()
        }

        imgEndereco.setOnClickListener(){
            mostrarNoMapa()
        }
    }

    private fun carregaDados() {
        edtNome.setText(contatinho?.nome)
        edtTelefone.setText(contatinho?.telefone)
        edtEmail.setText(contatinho?.email)
        edtEndereco.setText(contatinho?.endereco)

        GlideApp.with(this)
                .load(contatinho?.caminhoFoto)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(imgFoto)

        caminhoFotoAceita = contatinho?.caminhoFoto
    }

    private fun tirarFoto() {
        val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (tirarFoto.resolveActivity(packageManager) != null) {
            val arquivoFoto = montaArquivoFoto()
            val uriFoto = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", arquivoFoto)
            tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto)
            startActivityForResult(tirarFoto, REQUEST_CAMERA)
        } else {
            Toast.makeText(this, "Impossivel tirar Foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun montaArquivoFoto(): File {
        val nomeArquivo = System.currentTimeMillis().toString()
        val diretorioArquivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val arquivoFoto = File.createTempFile(nomeArquivo, "jpg", diretorioArquivo)
        caminhoFoto = arquivoFoto.absolutePath
        return arquivoFoto
    }

    fun telefonaOuEnviaMensagem(view: View?) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_telefona_ou_mensagem)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuMensagem -> enviaMensagem()
                R.id.menuTelefone -> efetuaLigacao()
                else -> false
            }
        }

        popup.show()
    }

    fun enviaMensagem():Boolean {
        val enviarMensagem = Intent(Intent.ACTION_VIEW)
        enviarMensagem.data = Uri.parse("sms:${edtTelefone.text}")
        enviarMensagem.putExtra("sms_body", "Oi sumidx")

        if (enviarMensagem.resolveActivity(packageManager) != null) {
            startActivity(enviarMensagem)
            return true
        } else {
            Toast.makeText(this, "Impossivel enviar mensagem!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun efetuaLigacao():Boolean {
        val efetuarLigacao = Intent(Intent.ACTION_CALL)
        efetuarLigacao.data = Uri.parse("tel:${edtTelefone.text}")
        efetuarLigacao.putExtra("sms_body", "Oi sumidx")

        //verifica se pode fazer a ligaçao
        if (efetuarLigacao.resolveActivity(packageManager) != null) {

            //verifica se a versão necessita de pedir permissão em tempo real
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUET_PERMISSOES)
                    return false
                } else {
                    startActivity(efetuarLigacao)
                    return true
                }

            } else {
                startActivity(efetuarLigacao)
                return true
            }
        } else {
            Toast.makeText(this, "Impossivel efetuar ligação!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun enviaEmail() {
        val enviarEmail = Intent(Intent.ACTION_VIEW)
        enviarEmail.data = Uri.parse("sms:${edtEmail.text}")
        enviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Oi sumidx")

        if (enviarEmail.resolveActivity(packageManager) != null) {
            startActivity(enviarEmail)
        } else {
            Toast.makeText(this, "Impossivel enviar email!", Toast.LENGTH_SHORT).show()
        }
    }

    fun mostrarNoMapa() {
        val mostrarNoMapa = Intent(Intent.ACTION_VIEW)
        mostrarNoMapa.data = Uri.parse("geo:0,0?q=${edtEndereco.text}")
        if (mostrarNoMapa.resolveActivity(packageManager) != null) {
            startActivity(mostrarNoMapa)
        } else {
            Toast.makeText(this, "Impossivel mostrar no mapa", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.menuSalvar -> salvaContatinho()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUET_PERMISSOES && (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)){
            Toast.makeText(this, "Necessário permissão para fazer ligação!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            GlideApp.with(this)
                    .load(caminhoFoto)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(imgFoto)

            caminhoFotoAceita = caminhoFoto
        }
    }

    private fun salvaContatinho() {

        if(edtNome.text.isEmpty()){
            edtNome.requestFocus()
            edtNome.setError(getString(R.string.campo_obrigatorio))
            return
        }

        if(edtTelefone.text.isEmpty()){
            edtTelefone.requestFocus()
            edtTelefone.setError(getString(R.string.campo_obrigatorio))
            return
        }
        if(contatinho == null) {
            contatinho = Contatinho(edtNome.text.toString(),
                    edtTelefone.text.toString(),
                    edtEmail.text.toString(),
                    edtEndereco.text.toString(),
                    caminhoFotoAceita)
        }else {
            contatinho?.nome = edtNome.text.toString()
            contatinho?.telefone = edtTelefone.text.toString()
            contatinho?.email = edtEmail.text.toString()
            contatinho?.endereco = edtEndereco.text.toString()
            contatinho?.caminhoFoto = caminhoFotoAceita
        }
        //Toast.makeText(this, contatinho.toString(), Toast.LENGTH_LONG).show()

        val contatinhoDao: ContatinhoDao = AppDatabase.getInstance(this).contatinhoDao()
        doAsync {
            contatinhoDao.insert(contatinho!!)
            uiThread {
                finish()
            }
        }
    }
}
