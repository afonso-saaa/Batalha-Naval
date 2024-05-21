var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()



fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {

    if (numLinhas == 4 && numColunas == 4) {
        return true
    }else if (numLinhas == 5 && numColunas == 5) {
        return true
    } else if (numLinhas == 7 && numColunas == 7) {
        return true
    } else if (numLinhas == 8 && numColunas == 8) {
        return true
    } else if (numLinhas == 10 && numColunas == 10) {
        return true
    }
    return false
}

fun processaCoordenadas(coordenadasStr: String?, numLinhas:Int, numColunas:Int): Pair<Int,Int>? {
    // Verifica se a string de coordenadas é nula
    if (coordenadasStr != null) {
        // Inicializa variáveis para verificar caracteres em branco
        var espacosEmBranco = true
        var contadorCaracteres = 0

        // Verifica se há caracteres em branco na string
        while (contadorCaracteres < coordenadasStr.length) {
            if (coordenadasStr[contadorCaracteres] != ' ') {
                espacosEmBranco = false
                contadorCaracteres = coordenadasStr.length
            }
            contadorCaracteres++
        }

        // Se a string tiver apenas caracteres em branco, retorna null
        if (espacosEmBranco) {
            return null
        }
    }

    // Inicializa variáveis para processar as coordenadas
    var contadorCaracteres = 0
    var virgula = false
    var numeroStr = ""
    var letra: Char? = null

    // Processa a string de coordenadas
    while (contadorCaracteres < coordenadasStr!!.length) {
        val char = coordenadasStr[contadorCaracteres]

        if (char == ',' && !virgula) {
            virgula = true
        } else if (virgula) {
            letra = char
        } else {
            numeroStr += char
        }

        contadorCaracteres++
    }

    // Verifica se as coordenadas processadas são válidas
    if (virgula) {
        val numero = numeroStr.toIntOrNull()

        if (letra != null &&
            numero != null && numero in 1..numLinhas &&
            letra in 'A' until ('A' + numColunas)
        ) {
            // Retorna um par representando as coordenadas válidas
            return Pair(numero, letra - 'A' + 1)
        }
    }
    // Se as coordenadas não forem válidas, retorna null
    return null
}



fun criaLegendaHorizontal(numColunas: Int): String {

    var nmrColuna = numColunas
    val caracter = 'A'
    var codigoAscii = caracter.code
    var legenda = "A" // Inicializa a legenda com 'A'

    // Enquanto houver mais colunas a serem etiquetadas
    while (nmrColuna > 1) {
        codigoAscii = (codigoAscii + 1) // Incrementa o código ASCII
        legenda += " | ${codigoAscii.toChar()}" // Adiciona a letra correspondente à legenda
        nmrColuna-- // Reduz o número de colunas restantes
    }

    return legenda // Retorna a legenda completa para as colunas do tabuleiro
}



fun criaTerreno(numLinhas:Int, numColunas:Int): String {
    // Cria a legenda horizontal das colunas
    val legendaHorizontal = criaLegendaHorizontal(numColunas)

    // Inicializa a string do terreno com a legenda horizontal
    var terreno = "\n| $legendaHorizontal |\n"

    var linha = 1
    while (linha <= numLinhas) {
        var numeroLinha = ""
        var coluna = 1

        // Constrói a representação de caracteres do terreno por linha
        while (coluna <= numColunas + 1) {
            if (coluna == 1) {
                numeroLinha += "" // Não faz nada na primeira coluna
            } else {
                numeroLinha += "| ~ " // Adiciona um caracter vazio representado por "~"
            }
            coluna++
        }

        // Adiciona a linha ao terreno, incluindo o número da linha no final
        terreno += "$numeroLinha| $linha\n"
        linha++
    }
    return terreno // Retorna a representação do terreno
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    return when {
        (numLinhas == 4 && numColunas == 4) -> arrayOf(2, 0, 0, 0) // Tabuleiro 4x4: 2 submarinos
        (numLinhas == 5 && numColunas == 5) -> arrayOf(1, 1, 1, 0) // Tabuleiro 5x5: 1 submarino, 1 contra-torpedeiro, 1 navio-tanque
        (numLinhas == 7 && numColunas == 7) -> arrayOf(2, 1, 1, 1) // Tabuleiro 7x7: 2 submarinos, 1 contra-torpedeiro, 1 navio-tanque
        (numLinhas == 8 && numColunas == 8) -> arrayOf(2, 2, 1, 1) // Tabuleiro 8x8: 2 submarinos, 2 contra-torpedeiros, 1 navio-tanque
        (numLinhas == 10 && numColunas == 10) -> arrayOf(3, 2, 1, 1) // Tabuleiro 10x10: 3 submarinos, 2 contra-torpedeiros, 1 navio-tanque
        else -> emptyArray() // Retorna um array vazio se não corresponder a nenhum caso acima
    }
}


fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    return Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }
}

fun coordenadaContida(tabuleiroPassado: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    // Obtém o número de linhas do tabuleiro
    val numLinhas = tabuleiroPassado.size

    // Verifica se existem linhas no tabuleiro
    // Se existirem, define o número de colunas com base no tamanho do primeiro array interno
    val numColunas = if (numLinhas > 0){
        tabuleiroPassado[0].size
    } else {
        // Caso não existam linhas define o número de colunas como 0
        0
    }

    // Verifica se a linha está dentro do intervalo válido (de 1 a numLinhas)
    // e se a coluna está dentro do intervalo válido (de 1 a numColunas)
    return linha in 1..numLinhas && coluna in 1..numColunas
}


fun limparCoordenadasVazias(pairs: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var quantidadeNaoVazia = 0

    // Calcula a quantidade de elementos não vazios no array de pares
    for (par in pairs) {
        if (par != Pair(0, 0)) {
            quantidadeNaoVazia++
        }
    }

    // Cria um novo array com o tamanho da quantidade de elementos não vazios
    val novoArray = Array(quantidadeNaoVazia) { Pair(0, 0) }
    var indiceNovoArray = 0

    // Preenche o novo array apenas com os elementos não vazios do array original
    for (par in pairs) {
        if (par != Pair(0, 0)) {
            novoArray[indiceNovoArray] = par
            indiceNovoArray++
        }
    }

    return novoArray
}


fun juntarCoordenadas(array1: Array<Pair<Int,Int>>, array2: Array<Pair<Int,Int>>): Array<Pair<Int,Int>> {

    return array1 + array2

}


fun gerarCoordenadasNavio(tabuleiroPassado: Array<Array<Char?>>, linha: Int,
                          coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {

    val numLinhas = tabuleiroPassado.size
    val numColunas = if (numLinhas > 0) tabuleiroPassado[0].size else 0

    // Verifica se a coordenada inicial está dentro dos limites do tabuleiro
    if (linha !in 1..numLinhas || coluna !in 1..numColunas) {
        return arrayOf()
    }

    val coordenadas = Array(dimensao) { Pair(0, 0) }
    coordenadas[0] = Pair(linha, coluna)

    // Verifica se a orientação é para E
    if (orientacao == "E") {
        var novaColuna = coluna + 1

        var coordenadaAtual = 1
        while (coordenadaAtual < dimensao) {
            // Verifica se a próxima coordenada está dentro dos limites do tabuleiro
            if (novaColuna !in 1..numColunas) {
                return arrayOf()
            }
            coordenadas[coordenadaAtual] = Pair(linha, novaColuna)
            novaColuna++
            coordenadaAtual++
        }
    } else {
        return arrayOf() // Retorna um array vazio se a orientação não for "E"
    }

    return coordenadas // Retorna as coordenadas do navio na orientação E
}



fun gerarCoordenadasFronteira(tabuleiroPassado: Array<Array<Char?>>,
                              linha: Int, coluna: Int,
                              orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {

    return arrayOf()

}

fun estaLivre(tabuleiroPassado: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    for ((linha, coluna) in coordenadas) {
        // Verifica se a linha é válida (dentro dos limites do tabuleiro)
        val linhaValida = linha - 1 in 0 until tabuleiroPassado.size
        // Verifica se a coluna é válida (dentro dos limites do tabuleiro)
        val colunaValida = coluna - 1 in 0 until tabuleiroPassado[0].size

        // Verifica se a linha e coluna são válidas e se o caracter no tabuleiro não está ocupado
        if (linhaValida && colunaValida && tabuleiroPassado[linha - 1][coluna - 1] != null ) {
            return false // Se o caracter não estiver livre, retorna falso
        }
    }
    return true // Se todos os caracteres estiverem livres, retorna verdadeiro
}



fun insereNavioSimples(tabuleiroPassado: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val numLinhas = tabuleiroPassado.size
    val numColunas = if (numLinhas > 0) tabuleiroPassado[0].size else 0

    // Verifica se a posição inicial está dentro dos limites do tabuleiro e se está vazia
    if (linha !in 1..numLinhas || coluna !in 1..numColunas || tabuleiroPassado[linha - 1][coluna - 1] != null) {
        return false // Se alguma das condições não for atendida, retorna falso
    }

    // Define deslocamentos para cima, baixo, esquerda e direita
    val deslocamentos = arrayOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    // Verifica se os caracteres adjacentes estão vazios (não ocupadas por outro navio)
    for ((linhaAdjacente, colunaAdjacente) in deslocamentos) {
        val novaLinha = linha + linhaAdjacente
        val novaColuna = coluna + colunaAdjacente

        // Verifica se o caracter adjacente está dentro dos limites do tabuleiro e se está vazio
        if (novaLinha in 1..numLinhas && novaColuna in 1..numColunas && tabuleiroPassado[novaLinha - 1][novaColuna - 1] != null) {
            return false // Se algum caracter adjacente estiver ocupado, retorna falso
        }
    }

    // Se todas os esapaços adjacentes estiverem livres, insere o navio na posição inicial
    tabuleiroPassado[linha - 1][coluna - 1] = '1'
    return true // Retorna verdadeiro indicando que o navio foi inserido com sucesso
}



fun insereNavio(tabuleiroPassado: Array<Array<Char?>>,linha:Int,
                coluna:Int, orientacao: String, dimensao:Int) : Boolean{

    return true

}

fun preencheTabuleiroComputador(tabuleiroPassado: Array<Array<Char?>>, dados: Array<Int>) {
    // Obtém o número de linhas e colunas do tabuleiro
    val numLinhas = tabuleiroPassado.size
    val numColunas = if (numLinhas > 0) tabuleiroPassado[0].size else 0

    // Caracter para representar um submarino
    val submarino = '1'

    // Inicializa variáveis para o tamanho do navio e a quantidade de navios
    var tamanhoNavio = 1
    var quantidadeNavios = dados[0]

    var indiceDados = 0

    // Itera sobre os dados fornecidos para colocar os navios no tabuleiro
    while (indiceDados < dados.size) {
        val totalNavios = quantidadeNavios

        var contadorNavios = 0

        // Coloca a quantidade especificada de navios do tamanho atual
        while (contadorNavios < totalNavios) {
            var coordenadasNavio: Array<Pair<Int, Int>>

            // Gera coordenadas para o navio e verifica se é possível colocá-lo no tabuleiro
            do {
                val linha = (1..numLinhas).random()
                val coluna = (1..numColunas).random()

                coordenadasNavio = gerarCoordenadasNavio(tabuleiroPassado, linha, coluna, "E", tamanhoNavio)
            } while (!estaLivre(tabuleiroPassado, coordenadasNavio) || !verificaDistancia(tabuleiroPassado, coordenadasNavio))

            // Coloca o navio no tabuleiro nas coordenadas geradas
            for ((linha, coluna) in coordenadasNavio) {
                tabuleiroPassado[linha - 1][coluna - 1] = submarino
            }
            contadorNavios++
        }

        // Atualiza o tamanho do navio e a quantidade de navios para a próxima iteração
        indiceDados++
        if (indiceDados < dados.size) {
            tamanhoNavio++
            quantidadeNavios = dados[indiceDados]
        }
    }
}


fun verificaDistancia(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    // Percorre todas as coordenadas dos navios
    for ((linha, coluna) in coordenadas) {
        // Verifica cada caracter adjacente às coordenadas do navio
        for (i in linha - 1..linha + 1) {
            for (j in coluna - 1..coluna + 1) {
                // Verifica se caracter vazio adjacente está dentro dos limites do tabuleiro
                if (i in 1..tabuleiro.size && j in 1..tabuleiro[0].size) {
                    // Verifica se há um navio adjacente no caracter atual
                    if (tabuleiro[i - 1][j - 1] == '1') {
                        // Se houver um navio adjacente, retorna falso
                        // Indicando que a distância entre navios não é suficiente
                        return false
                    }
                }
            }
        }
    }
    // Se não houver navios adjacentes em caracteres vizinhos, retorna verdadeiro
    // Indicando que a distância entre os navios é adequada
    return true
}




fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {

    // Verifica se a posição está dentro dos limites do tabuleiro ou se é nula
    if (linha < 1 || coluna < 1 || linha > tabuleiro.size || coluna > tabuleiro[0].size
        || tabuleiro[linha - 1][coluna - 1] == null) {
        return false
    }

    // Obtém o tamanho do navio baseado no caractere na posição atual
    val tamanhoNavioChar = tabuleiro[linha - 1][coluna - 1]
    if (tamanhoNavioChar !in '1'..'4') {
        return false
    }

    // Converte o caractere do tamanho do navio para um valor numérico
    val tamanhoNavio = tamanhoNavioChar.toString().toInt()

    // Verifica se o navio está completo horizontalmente e verticalmente
    var horizontalCompleto = true
    var verticalCompleto = true

    for (i in 1 until tamanhoNavio) {
        // Verifica se o navio está completo na direção horizontal
        if (coluna + i <= tabuleiro[0].size && tabuleiro[linha - 1][coluna + i - 1] != tamanhoNavioChar){
            horizontalCompleto = false
        }
        // Verifica se o navio está completo na direção vertical
        if (linha + i <= tabuleiro.size && tabuleiro[linha + i - 1][coluna - 1] != tamanhoNavioChar){
            verticalCompleto = false
        }
    }

    // Verifica se as coordenadas estão no meio do navio
    val coordenadasMeio = (horizontalCompleto || verticalCompleto)

    // Retorna verdadeiro se as coordenadas estiverem no meio do navio ou se o tamanho do navio estiver entre 1 e 4
    return coordenadasMeio || (tamanhoNavio in 1..4)
}


fun obtemMapa(tabuleiro: Array<Array<Char?>>, isTabuleiroReal: Boolean): Array<String> {

    // Obtém o número de linhas e colunas do tabuleiro
    val numLinhasTabuleiro = tabuleiro.size
    val numColunasTabuleiro = if (numLinhasTabuleiro > 0) tabuleiro[0].size else 0

    // Cria a legenda horizontal para o tabuleiro
    val legendaHorizontal = criaLegendaHorizontal(numColunasTabuleiro)

    // Inicializa um Array para armazenar o mapa
    val mapa = Array(numLinhasTabuleiro + 1) { "" }
    mapa[0] = "| $legendaHorizontal |" // Define a primeira linha como a legenda horizontal

    // Preenche o mapa com os conteúdos do tabuleiro
    for (linha in 0 until numLinhasTabuleiro) {
        var linhaMapa = "|"
        for (coluna in 0 until numColunasTabuleiro) {
            val conteudo = tabuleiro[linha][coluna]
            val navioCompleto = navioCompleto(tabuleiro, linha + 1, coluna + 1)

            val simbolo = when {
                isTabuleiroReal -> { // Se for o tabuleiro real
                    return arrayOf()
                }
                else -> { // Se for um tabuleiro de palpites
                    val unicodeNumero = when (conteudo) {
                        '1' -> "1"
                        '2' -> "\u2082" // Subscrito 2
                        '3' -> "\u2083" // Subscrito 3
                        '4' -> "\u2084" // Subscrito 4
                        'X' -> "X" // Tentativa de ataque falha
                        else -> "?" // Tentativa de ataque desconhecida
                    }

                    var numero = ""
                    var charAtual = '1'
                    var navioEncontrado = false

                    // Verifica se o navio foi encontrado
                    while (!navioCompleto && charAtual <= '4' && !navioEncontrado) {
                        if (conteudo == charAtual) {
                            numero = charAtual.toString()
                            navioEncontrado = true
                        }
                        charAtual++
                    }

                    if (navioEncontrado) {
                        " $numero |" // Se o navio foi encontrado, exibe o número do navio
                    } else {
                        " $unicodeNumero |" // Senão, exibe o número do navio em unicode
                    }
                }
            }
            linhaMapa += simbolo
        }
        linhaMapa += " ${linha +1}" // Adiciona o número da linha no final
        mapa[linha + 1] = linhaMapa
    }
    return mapa // Retorna o mapa criado
}


fun lancarTiro(
    tabuleiroComputador: Array<Array<Char?>>,
    tabuleiroPalpites: Array<Array<Char?>>,
    coordenadasTiro: Pair<Int, Int>
): String {

    val (linha, coluna) = coordenadasTiro // Desempacota as coordenadas do tiro

    val linhaTabuleiro = linha - 1 // Converte para índice do array (começa em 0)
    val colunaTabuleiro = coluna - 1 // Converte para índice do array (começa em 0)

    // Verifica se as coordenadas estão dentro dos limites do tabuleiro
    if (linhaTabuleiro < 0 || linhaTabuleiro >= tabuleiroComputador.size ||
        colunaTabuleiro < 0 || colunaTabuleiro >= tabuleiroComputador[0].size
    ) return "Fora do Tabuleiro" // Retorna "Fora do Tabuleiro" se estiver fora

    val posicao = tabuleiroComputador[linhaTabuleiro][colunaTabuleiro] // Obtém a posição no tabuleiro do computador

    
    // Define as ações com base na posição atingida no tabuleiro do computador
    return when (posicao) {
        null -> {
            tabuleiroPalpites[linhaTabuleiro][colunaTabuleiro] = 'X' // Marca a tentativa no tabuleiro de palpites
            "Agua." // Retorna "Agua." se atingiu água
        }
        '1' -> {
            tabuleiroPalpites[linhaTabuleiro][colunaTabuleiro] = '1' // Marca a tentativa no tabuleiro de palpites
            "Tiro num submarino." // Retorna "Tiro num submarino." se atingiu um submarino
        }
        '2' -> {
            tabuleiroPalpites[linhaTabuleiro][colunaTabuleiro] = '2' // Marca a tentativa no tabuleiro de palpites
            "Tiro num contra-torpedeiro." // Retorna "Tiro num contra-torpedeiro." se atingiu um contra-torpedeiro
        }
        '3' -> {
            tabuleiroPalpites[linhaTabuleiro][colunaTabuleiro] = '3' // Marca a tentativa no tabuleiro de palpites
            "Tiro num navio-tanque." // Retorna "Tiro num navio-tanque." se atingiu um navio-tanque
        }
        '4' -> {
            tabuleiroPalpites[linhaTabuleiro][colunaTabuleiro] = '4' // Marca a tentativa no tabuleiro de palpites
            "Tiro num porta-avioes." // Retorna "Tiro num porta-avioes." se atingiu um porta-aviões
        }
        else -> "" // Retorna uma string vazia para outras situações
    }
}



fun gerarTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {

    return Pair(0,0)

}


fun venceu(tabuleiroPalpites: Array<Array<Char?>>): Boolean {
    // Obtém o número de linhas e colunas do tabuleiro de palpites
    val numLinhasTabuleiro = tabuleiroPalpites.size
    val numColunasTabuleiro = if (numLinhasTabuleiro > 0) tabuleiroPalpites[0].size else 0

    // Calcula o número esperado de navios de cada tamanho no tabuleiro
    val numNaviosEsperados = calculaNumNavios(numLinhasTabuleiro, numColunasTabuleiro)

    // Percorre os possíveis tamanhos de navios (1 a 4)
    for (tamanhoNavio in 1..4) {
        var numNaviosDeTamanhoAtual = 0

        // Percorre os caracteres do tabuleiro de palpites
        for (linha in 0 until numLinhasTabuleiro) {
            for (coluna in 0 until numColunasTabuleiro) {
                val conteudoChar = tabuleiroPalpites[linha][coluna]

                // Verifica se o conteúdo do caracter é um navio do tamanho atual
                if (conteudoChar != null && conteudoChar == tamanhoNavio.toString()[0]) {
                    var navioCompletoHorizontal = true
                    var colunaAtual = coluna

                    // Verifica a completude do navio na horizontal
                    while (colunaAtual < coluna + tamanhoNavio &&
                        colunaAtual < numColunasTabuleiro &&
                        tabuleiroPalpites[linha][colunaAtual] == tamanhoNavio.toString()[0]) {
                        colunaAtual++
                    }

                    if (colunaAtual - coluna != tamanhoNavio) {
                        navioCompletoHorizontal = false
                    }

                    var navioCompletoVertical = true
                    var linhaAtual = linha

                    // Verifica a completude do navio na vertical
                    while (linhaAtual < linha + tamanhoNavio &&
                        linhaAtual < numLinhasTabuleiro &&
                        tabuleiroPalpites[linhaAtual][coluna] == tamanhoNavio.toString()[0]) {
                        linhaAtual++
                    }

                    if (linhaAtual - linha != tamanhoNavio) {
                        navioCompletoVertical = false
                    }

                    // Verifica se o navio está completo na horizontal ou vertical
                    if (navioCompletoHorizontal || navioCompletoVertical) {
                        numNaviosDeTamanhoAtual++
                    }
                }
            }
        }

        // Verifica se o número de navios do tamanho atual é o esperado
        if (numNaviosDeTamanhoAtual != numNaviosEsperados[tamanhoNavio - 1]) {
            return false // Retorna falso se o número não for o esperado
        }
    }

    return true // Retorna verdadeiro se todos os navios estiverem afundados
}



fun lerJogo(nomeArquivo: String, tipoTabuleiro:Int): Array<Array<Char?>> {

    when (tipoTabuleiro) {
        1 -> {

            return tabuleiroHumano
        }
        2 -> {

            return tabuleiroPalpitesDoHumano
        }
        3 -> {

            return tabuleiroComputador
        }
        4 -> {

            return tabuleiroPalpitesDoComputador
        }
        else -> {

            return arrayOf()
        }
    }
}

fun gravarJogo(nomeArquivo: String,
               tabuleiroHumano: Array<Array<Char?>>,
               tabuleiroPalpitesDoHumano: Array<Array<Char?>>,
               tabuleiroComputador: Array<Array<Char?>>,
               tabuleiroPalpitesDoComputador: Array<Array<Char?>>) {


}

fun contarNaviosDeDimensao(tabuleiroPalpites: Array<Array<Char?>>, dimensao: Int): Int {
    var naviosCompletos = 0

    // Loop pelas linhas do tabuleiro de palpites
    for (linha in 0 until tabuleiroPalpites.size) {
        // Loop pelas colunas do tabuleiro de palpites
        for (coluna in 0 until tabuleiroPalpites[linha].size) {
            val espacoVazio = tabuleiroPalpites[linha][coluna]

            // Verifica se o caracter não está vazio e é igual à dimensão fornecida
            if (espacoVazio != null && espacoVazio == dimensao.toString()[0]) {
                var navioCompletoHorizontal = true
                var colunaAtual = coluna

                // Verifica se o navio é completo na horizontal
                while (colunaAtual < coluna + dimensao && colunaAtual < tabuleiroPalpites[linha].size
                    && tabuleiroPalpites[linha][colunaAtual] == dimensao.toString()[0]
                ) {
                    colunaAtual++
                }

                if (colunaAtual - coluna != dimensao) {
                    navioCompletoHorizontal = false
                }

                var navioCompletoVertical = true
                var linhaAtual = linha

                // Verifica se o navio é completo na vertical
                while (linhaAtual < linha + dimensao && linhaAtual < tabuleiroPalpites.size
                    && tabuleiroPalpites[linhaAtual][coluna] == dimensao.toString()[0]
                ) {
                    linhaAtual++
                }

                if (linhaAtual - linha != dimensao) {
                    navioCompletoVertical = false
                }

                // Se o navio estiver completo na horizontal ou vertical, incrementa o contador
                if (navioCompletoHorizontal || navioCompletoVertical) {
                    naviosCompletos++
                }
            }
        }
    }

    return naviosCompletos
}



fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    val numLinhas = tabuleiroPalpitesComputador.size
    val numColunas = if (numLinhas > 0) tabuleiroPalpitesComputador[0].size else 0

    // Loop infinito para gerar aleatoriamente uma coordenada de tiro válida
    while (true) {
        // Gera aleatoriamente uma linha e coluna dentro dos limites do tabuleiro de palpites do computador
        val linha = (1..numLinhas).random()
        val coluna = (1..numColunas).random()

        // Verifica se o local gerado não foi atingido anteriormente
        if (tabuleiroPalpitesComputador[linha - 1][coluna - 1] == null) {
            // Retorna um par de coordenadas (linha, coluna) válidas para o tiro do computador
            return Pair(linha, coluna)
        }
    }
}



const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106

val definirTabuleiroErro = "!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente"
val colunasInvalidas = "!!! Número de colunas inválido, tente novamente"
val linhasInvalidas = "!!! Numero de linhas invalido, tente novamente"
val coordenadasInvalidas = "!!! Coordendas invalidas, tente novamente"
val opcaoInvalida = "!!! Opcao invalida, tente novamente"
val orientacaoInvalida = "!!! Orientação invalida, tente novamente"


fun tabuleiro(tabuleiro: Array<String>) {

    var posicao = 0

    // Itera sobre o array de strings do tabuleiro e imprime cada linha
    while (posicao < tabuleiro.size) {
        println(tabuleiro[posicao])
        posicao++
    }
}


fun tiposDeNavio(dimensao: Int) {
    // Determina a mensagem com base no valor da dimensão
    val mensagem = when (dimensao) {
        0 -> "Insira as coordenadas de um submarino:" // Mensagem para o submarino
        1 -> "Insira as coordenadas de um contra-torpedeiro:" // Mensagem para o contra-torpedeiro
        2 -> "Insira as coordenadas de um navio-tanque:" // Mensagem para o navio-tanque
        else -> "Insira as coordenadas de um porta-aviões:" // Mensagem para o porta-aviões
    }

    // Exibe a mensagem
    println(mensagem)
}


fun menuPrincipal(): Int {

    var menu = ""

    // Criação do menu principal
    menu += "\n"
    menu += "> > Batalha Naval < <\n"
    menu += "\n"
    menu += "1 - Definir Tabuleiro e Navios\n"
    menu += "2 - Jogar\n"
    menu += "3 - Gravar\n"
    menu += "4 - Ler\n"
    menu += "0 - Sair\n"

    println(menu)

    // Loop para receber a opção do usuário
    while (true) {
        val opcaoMenu = readLine()?.toIntOrNull()

        if (opcaoMenu != null && opcaoMenu in -1..4) {
            return when (opcaoMenu) {
                1 -> MENU_DEFINIR_TABULEIRO
                2 -> MENU_JOGAR
                3 -> MENU_GRAVAR_FICHEIRO
                4 -> MENU_LER_FICHEIRO
                0 -> SAIR
                else -> {
                    println(opcaoInvalida)
                    MENU_PRINCIPAL
                }
            }
        } else {
            println(opcaoInvalida)
        }
    }
}


fun menuDefinirNavios(): Int {
    // Calcula o número de navios disponíveis para cada dimensão no tabuleiro
    val numNavios = calculaNumNavios(numLinhas, numColunas)
    var dimensao = 0

    // Loop para definir os navios no tabuleiro para cada dimensão
    while (dimensao < numNavios.size) {
        var numNaviosLivres = numNavios[dimensao]

        // Loop para posicionar os navios restantes de uma determinada dimensão
        while (numNaviosLivres > 0) {
            // Exibe mensagem de inserção de navio para a dimensão atual
            tiposDeNavio(dimensao)

            // Obtém as coordenadas inseridas pelo jogador
            val coordenadasInseridas = obterCoordenadas()

            // Verifica e insere o navio no tabuleiro baseado na dimensão
            if (dimensao + 1 > 1) {
                val orientacao = obterOrientacao()
                val navioColocado = insereNavio(
                    tabuleiroHumano,
                    coordenadasInseridas.first,
                    coordenadasInseridas.second,
                    orientacao ?: "",
                    dimensao + 1
                )

                // Se o navio for colocado com sucesso, exibe o tabuleiro atualizado
                if (navioColocado) {
                    tabuleiro(obtemMapa(tabuleiroHumano, true))
                    numNaviosLivres--
                } else {
                    println("!!! Posicionamento inválido, tente novamente")
                }
            } else {
                val navioColocado = insereNavioSimples(
                    tabuleiroHumano,
                    coordenadasInseridas.first,
                    coordenadasInseridas.second,
                    1
                )

                // Se o navio for colocado com sucesso, exibe o tabuleiro atualizado
                if (navioColocado) {
                    tabuleiro(obtemMapa(tabuleiroHumano, true))
                    numNaviosLivres--
                } else {
                    println("!!! Posicionamento inválido, tente novamente")
                }
            }
        }
        dimensao++
    }

    // Preenche o tabuleiro do computador com os navios gerados aleatoriamente
    preencheTabuleiroComputador(tabuleiroComputador, calculaNumNavios(numLinhas, numColunas))

    // Pergunta se o jogador quer ver o mapa gerado para o Computador
    println("Pretende ver o mapa gerado para o Computador? (S/N)")
    val resposta = readLine()
    if (resposta == "S") {
        tabuleiro(obtemMapa(tabuleiroComputador, true))
    }

    return MENU_PRINCIPAL
}


fun obterCoordenadas(): Pair<Int, Int> {
    while (true) {
        println("Coordenadas? (ex: 6,G)")
        val coordenadas = readLine()

        // Verifica se o jogador deseja sair (-1) e retorna um par nulo
        if (coordenadas == "-1") {
            return Pair(0, 0)
        }

        // Verifica se as coordenadas inseridas são válidas usando a função processaCoordenadas
        val coordenadasValidas = processaCoordenadas(coordenadas, numLinhas, numColunas) != null

        if (!coordenadasValidas) {
            // Se as coordenadas não forem válidas, exibe uma mensagem de erro
            println(coordenadasInvalidas)
        } else {
            // Se as coordenadas forem válidas, retorna o par de coordenadas processadas
            val coordenadasProcessadas = processaCoordenadas(coordenadas!!, numLinhas, numColunas)!!
            return Pair(coordenadasProcessadas.first, coordenadasProcessadas.second)
        }
    }
}

fun obterOrientacao(): String? {
    while (true) {
        println("Insira a orientação do navio: (N, S, E, O)")
        val orientacao = readLine()

        // Verifica se o jogador deseja sair (-1) e retorna null
        if (orientacao == "-1") {
            return null
        }

        // Verifica se a orientação inserida é válida (um único caractere entre N, S, E ou O)
        val orientacaoValida = orientacao?.length == 1 &&
                (orientacao[0] == 'N' || orientacao[0] == 'S' ||
                        orientacao[0] == 'E' || orientacao[0] == 'O')

        if (!orientacaoValida) {
            // Se a orientação não for válida, exibe uma mensagem de erro
            println(orientacaoInvalida)
        } else {
            // Se a orientação for válida, retorna a orientação inserida
            return orientacao
        }
    }
}



fun menuDefinirTabuleiro(): Int {
    println("\n> > Batalha Naval < <\n")
    println("Defina o tamanho do tabuleiro:")

    var linhas: Int
    var colunas: Int

    // Loop para solicitar o tamanho do tabuleiro até que um tamanho válido seja fornecido
    do {
        linhas = solicitarTamanhoTabuleiro("linhas")
        if (linhas == -1) return MENU_PRINCIPAL // Se -1 for inserido, retorna para o menu principal

        colunas = solicitarTamanhoTabuleiro("colunas")
        if (colunas == -1) return MENU_PRINCIPAL // Se -1 for inserido, retorna para o menu principal

        // Verifica se o tamanho do tabuleiro fornecido é válido
        if (tamanhoTabuleiroValido(linhas, colunas)) {
            // Se for válido, define os números de linhas e colunas globais
            numColunas = colunas
            numLinhas = linhas
        }
    } while (!tamanhoTabuleiroValido(numLinhas, numColunas)) // Continua solicitando até que um tamanho válido seja fornecido

    inicializarTabuleiros() // Inicializa os tabuleiros com o tamanho especificado

    return MENU_DEFINIR_NAVIOS // Retorna o menu para a definição dos navios
}


fun solicitarTamanhoTabuleiro(tipo: String): Int {
    var valor: Int? = null

    do {
        print("Quantas $tipo?\n")
        valor = readLine()?.toIntOrNull() // Lê a entrada do usuário e tenta convertê-la para um número inteiro

        if (valor == -1) return -1 // Se o usuário inserir -1, retorna -1 para indicar saída

        // Verifica se o valor é inválido (nulo ou menor ou igual a zero)
        if (valor == null || valor <= 0) {
            // Imprime mensagem de erro dependendo do tipo de entrada (linhas ou colunas)
            println(if (tipo == "linhas") linhasInvalidas else colunasInvalidas)
        }
    } while (valor == null || valor <= 0) // Continua solicitando até receber um valor válido

    return valor // Retorna o valor válido inserido pelo usuário
}


fun inicializarTabuleiros() {
    // Cria e inicializa os tabuleiros para o jogo da Batalha Naval

    // Cria um tabuleiro vazio para o jogador humano
    tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)

    // Cria um tabuleiro vazio para o computador
    tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)

    // Cria um tabuleiro vazio para registrar os palpites do jogador humano
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)

    // Cria um tabuleiro vazio para registrar os palpites do computador
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)

    // Exibe o tabuleiro vazio do jogador humano usando a função tabuleiro()
    // Utiliza a representação do tabuleiro obtida pela função obtemMapa()
    tabuleiro(obtemMapa(tabuleiroHumano, true))
}


fun numeroParaLetra(numero: Int): Char {
    // Converte o número inteiro para o caractere correspondente na tabela ASCII
    return ('A' + numero - 1) // Por exemplo, se numero for 1, retorna 'A'; se for 2, retorna 'B', e assim por diante.
}


fun menuJogar(): Int {
    // Verifica se o tabuleiro foi definido corretamente
    if (numLinhas == -1 || numColunas == -1) {
        println(definirTabuleiroErro)
        return MENU_PRINCIPAL // Retorna para o menu principal
    }

    while (true) {
        // Exibe o tabuleiro do jogador
        tabuleiro(obtemMapa(tabuleiroPalpitesDoHumano, false))
        println("Indique a posição que pretende atingir")
        println("Coordenadas? (ex: 6,G)")
        val coordenadas = readLine().toString()

        // Se for inserido -1, retorna para o menu principal
        if (coordenadas?.toIntOrNull() == -1) {
            return MENU_PRINCIPAL
        }

        // Processa as coordenadas inseridas
        val coords = processaCoordenadas(coordenadas, numLinhas, numColunas)
        if (coords != null) {
            // Realiza um tiro no tabuleiro do computador
            val resultadoTiro = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coords)
            print(">>> HUMANO >>>$resultadoTiro")
            // Verifica se o navio foi completamente destruído
            if (navioCompleto(tabuleiroPalpitesDoHumano, coords.first, coords.second)) {
                println(" Navio ao fundo!")
            } else {
                println()
            }
        }

        // Verifica se o jogador venceu
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABÉNS! Você venceu o jogo!")
            println("Pressione Enter para voltar ao menu principal")
            readLine()
            return MENU_PRINCIPAL
        }

        // O computador gera um tiro aleatório
        val tiro = geraTiroComputador(tabuleiroPalpitesDoComputador)
        val coordenadaEmLetra = numeroParaLetra(tiro.second)
        println("Computador lançou tiro para a posição (${tiro.first},$coordenadaEmLetra)")

        // Verifica o resultado do tiro do computador no tabuleiro do jogador
        val mensagemTiro = when (tabuleiroHumano[tiro.first - 1][tiro.second - 1]) {
            null -> "Água."
            '1' -> "Tiro em um submarino."
            '2' -> "Tiro em um contra-torpedeiro."
            '3' -> "Tiro em um navio-tanque."
            '4' -> "Tiro em um porta-aviões."
            else -> ""
        }

        print(">>> COMPUTADOR >>>$mensagemTiro")
        // Verifica se o computador afundou um navio do jogador
        if (navioCompleto(tabuleiroPalpitesDoComputador, tiro.first, tiro.second)) {
            println(" Navio ao fundo!")
        } else {
            println()
        }

        println("Pressione Enter para continuar")
        readLine()

        // Verifica se o computador venceu
        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("OPS! O computador venceu o jogo!")
            println("Pressione Enter para voltar ao menu principal")
            readLine()
            return MENU_PRINCIPAL
        }
    }
}



fun menuLerFicheiro(): Int {
    // Solicita ao usuário o nome do arquivo que contém o estado do jogo
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro= readLine().toString()

    // Lê os tabuleiros do arquivo fornecido para os diferentes tabuleiros do jogo (humano, computador e seus palpites)
    tabuleiroHumano = lerJogo(nomeDoFicheiro, 1)
    tabuleiroPalpitesDoHumano = lerJogo(nomeDoFicheiro, 2)
    tabuleiroComputador = lerJogo(nomeDoFicheiro, 3)
    tabuleiroPalpitesDoComputador = lerJogo(nomeDoFicheiro, 4)

    // Exibe uma mensagem confirmando que o tabuleiro foi lido com sucesso
    println("Tabuleiro ${numLinhas}x${numColunas} lido com sucesso")

    // Mostra o tabuleiro do jogador humano após a leitura
    tabuleiro(obtemMapa(tabuleiroHumano, true))

    // Retorna ao menu principal do jogo
    return MENU_PRINCIPAL
}



fun menuGravarFicheiro(): Int {
    // Verifica se o tabuleiro foi definido antes de tentar gravar
    if (numLinhas == -1 || numColunas == -1) {
        println(definirTabuleiroErro)
        return MENU_PRINCIPAL
    }

    // Solicita ao usuário o nome do arquivo para salvar o estado atual do jogo
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readLine().toString()

    // Grava o estado atual do jogo (tabuleiros do jogador humano, tabuleiros do computador)
    gravarJogo(
        nomeDoFicheiro,
        tabuleiroHumano,
        tabuleiroPalpitesDoHumano,
        tabuleiroComputador,
        tabuleiroPalpitesDoComputador
    )

    // Confirmação de que o tabuleiro foi gravado com sucesso
    println("Tabuleiro ${numLinhas}x${numColunas} gravado com sucesso")

    // Retorna ao menu principal do jogo
    return MENU_PRINCIPAL
}



fun main() {
    // Variável para controlar o menu atual do jogo
    var menuAtual = MENU_PRINCIPAL

    // Loop principal que mantém o jogo em execução
    while (true) {
        // Com base no menu atual, direciona para diferentes funcionalidades do jogo
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal() // Exibe o menu principal
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro() // Define o tamanho do tabuleiro
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios() // Define os navios no tabuleiro
            MENU_JOGAR -> menuJogar() // Inicia o jogo
            MENU_LER_FICHEIRO -> menuLerFicheiro() // Lê um jogo salvo de um arquivo
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro() // Salva um jogo em um arquivo
            SAIR -> return // Sai do loop e finaliza o programa
            else -> return // Sai do loop e finaliza o programa caso a opção não seja válida
        }
    }
}
