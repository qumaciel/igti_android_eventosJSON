package br.com.igti.android.eventos;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by maciel on 12/4/14.
 */
public class EventosJSONSerializer {

    private static final String TAG = "EventosJSONSerializer";

    private Context mContext;
    private String mFilename;

    public EventosJSONSerializer (Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void salvarEventos(ArrayList<Evento> eventos) throws JSONException, IOException {
        // Cria o array JSON com os eventos
        JSONArray array = new JSONArray();
        for(Evento evento : eventos) {
            array.put(evento.toJSON());
        }

        Writer writer = null;
        try {
            // Abre um arquivo na sandbox com permissão privada
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Evento> carregarEventos() throws IOException, JSONException {
        ArrayList<Evento> eventos = new ArrayList<Evento>();
        BufferedReader reader = null;
        try {
            // Abre o arquivo
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            // Parse JSON string
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // Popula o Array de eventos
            for(int i=0; i<array.length(); i++) {
                eventos.add(new Evento(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG,"Não achou o arquivo");
            // caso ele não encontre o arquivo, criamos alguns novos
            for(int i =0; i<4; i++)  {
                Evento evento = new Evento();
                evento.setTitulo("Item " + i);
                evento.setDescricao("Descrição" + i);
                eventos.add(evento);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return eventos;
    }
}
