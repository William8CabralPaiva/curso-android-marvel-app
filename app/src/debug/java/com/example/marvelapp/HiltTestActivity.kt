package com.example.marvelapp

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

//todo o diretorio java inteiro do debug foi criado manualmente
//pois precisamos deste arquivo apenas para teste
// para injetar as dependencias no modo debug
@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity()