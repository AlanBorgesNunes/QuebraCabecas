package com.app.quebracabeas

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private val tamanhoPuzzle = 3 // Tamanho do quebra-cabeça (3x3)
    private var botaoVazio: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.grid_layout)

        botaoVazio = gridLayout.findViewWithTag(tamanhoPuzzle * tamanhoPuzzle - 1)

        // Carregar a imagem e dividir em peças
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.ttt
        ) // Substitua "sua_imagem" pelo ID da sua imagem no diretório drawable
        val pieces = splitImage(bitmap, tamanhoPuzzle, tamanhoPuzzle)

        // Criar os botões com as imagens das peças
        criarQuebraCabeca(pieces)
    }

private fun splitImage(image: Bitmap, rows: Int, cols: Int): MutableList<Bitmap> {
        val pieces = mutableListOf<Bitmap>()
        val pieceWidth = image.width / cols
        val pieceHeight = image.height / rows

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val x = j * pieceWidth
                val y = i * pieceHeight
                val pieceBitmap = Bitmap.createBitmap(image, x, y, pieceWidth, pieceHeight)
                pieces.add(pieceBitmap)
            }
        }
        return pieces
    }

    private fun criarQuebraCabeca(pieces: List<Bitmap>) {
        val indicesEmbaralhados = (0 until tamanhoPuzzle * tamanhoPuzzle).shuffled()

        for (i in 0 until tamanhoPuzzle) {
            for (j in 0 until tamanhoPuzzle) {
                val botao = Button(this)
                val index = i * tamanhoPuzzle + j

                if (index < pieces.size) {
                    botao.background = BitmapDrawable(resources, pieces[indicesEmbaralhados[index]])

                    // Defina uma tag para identificar a posição atual do botão
                    botao.tag = indicesEmbaralhados[index]

                    // Adicione um OnClickListener para manipular os cliques nos botões
                    botao.setOnClickListener {
                        val posicaoAtual = botao.tag as Int?


                        // Trocar as imagens dos botões
                        trocarImagemBotao(botao, botaoVazio, posicaoAtual!!)

                        // Atualizar as tags dos botões
                        botao.tag = botaoVazio?.tag
                        botaoVazio?.tag = posicaoAtual

                        // Atualizar a referência ao botão vazio
                        botaoVazio = botao

                        
                        Toast.makeText(this, "$index", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    botao.visibility = View.INVISIBLE
                }

                gridLayout.addView(botao)
            }
        }


    }

    private fun verificarSolucao(): Boolean {
        val configuracaoCorreta = Array(tamanhoPuzzle) { IntArray(tamanhoPuzzle) }

        // Preencher a matriz com a configuração correta do quebra-cabeça
        for (i in 0 until tamanhoPuzzle * tamanhoPuzzle) {
            configuracaoCorreta[i / tamanhoPuzzle][i % tamanhoPuzzle] = i
        }

        // Verificar se os botões estão na configuração correta
        for (i in 0 until tamanhoPuzzle) {
            for (j in 0 until tamanhoPuzzle) {
                val botao = gridLayout.findViewWithTag<Button>(i * tamanhoPuzzle + j)
                if (botao != null) {
                    val tagAtual = botao.tag as? Int
                    if (tagAtual != null && tagAtual != configuracaoCorreta[i][j]){
                        return false
                    }

                }else{
                    Log.d("TAG", "verificarSolucao: $i $j")
                    return false
                }
            }
        }
        return true
    }

    private fun trocarImagemBotao(botaoClicado: Button?, botaoVazio: Button?, position: Int) {
        botaoVazio?.background = botaoClicado?.background
        botaoClicado?.setBackgroundColor(position)
    }
}



