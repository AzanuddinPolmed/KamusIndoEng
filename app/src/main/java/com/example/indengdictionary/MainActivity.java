package com.example.indengdictionary;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.indengdictionary.database.DatabaseHelper;
import com.example.indengdictionary.database.DictionaryDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etWordIndonesia, etWordEnglish;
    private Button btnAdd, btnReadAll;
    private ListView lvWords;
    private DictionaryDAO dictionaryDAO;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWordIndonesia = findViewById(R.id.etWordIndonesia);
        etWordEnglish = findViewById(R.id.etWordEnglish);
        btnAdd = findViewById(R.id.btnAdd);
        btnReadAll = findViewById(R.id.btnReadAll);
        lvWords = findViewById(R.id.lvWords);

        wordList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList);
        lvWords.setAdapter(adapter);

        dictionaryDAO = new DictionaryDAO(this);
        dictionaryDAO.open();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord();
            }
        });

        btnReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllWords();
            }
        });

        lvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showWordMeaning(position);
            }
        });
    }

    private void addWord() {
        String wordIndonesia = etWordIndonesia.getText().toString();
        String wordEnglish = etWordEnglish.getText().toString();

        if (wordIndonesia.isEmpty() || wordEnglish.isEmpty()) {
            Toast.makeText(this, "Please enter both words", Toast.LENGTH_SHORT).show();
            return;
        }

        dictionaryDAO.addWord(wordIndonesia, wordEnglish);
        Toast.makeText(this, "Word added", Toast.LENGTH_SHORT).show();
        etWordIndonesia.setText("");
        etWordEnglish.setText("");
    }

    private void readAllWords() {
        wordList.clear();
        Cursor cursor = dictionaryDAO.getAllWords();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String wordIndonesia = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_INDONESIA));
                @SuppressLint("Range") String wordEnglish = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH));
                wordList.add(wordIndonesia);
            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();
    }

    private void showWordMeaning(int position) {
        String wordIndonesia = wordList.get(position);
        Cursor cursor = dictionaryDAO.getWord(wordIndonesia);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String wordEnglish = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH));
            Toast.makeText(this, wordEnglish, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Translation not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dictionaryDAO.close();
        super.onDestroy();
    }
}
