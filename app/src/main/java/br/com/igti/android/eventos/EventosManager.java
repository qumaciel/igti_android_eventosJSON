package br.com.igti.android.eventos;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class EventosManager {

    private static final String TAG = "EventosManager";
    private static final String FILENAME = "eventos.json";

    private ArrayList<Evento> mEventos;
    private EventosJSONSerializer mSerializer;

    private static EventosManager sEventos;
    private Context mAppContext;

    private EventosManager(Context appContext) {
        mAppContext = appContext;

        mSerializer = new EventosJSONSerializer(mAppContext,FILENAME);
        try {
            // tentamos carregar o JSON
            mEventos = mSerializer.carregarEventos();
        } catch (Exception e) {
            // caso ele não exista, ou dê algum erro,
            // criamos nossos dados falsos
            mEventos = new ArrayList<Evento>();
            Log.e(TAG,"Não carregou!",e);
            for(int i =0; i<4; i++)  {
                Evento evento = new Evento();
                evento.setTitulo("Item " + i);
                evento.setDescricao("Descrição" + i);
                mEventos.add(evento);
            }
        }
    }

    public static EventosManager get(Context c) {
        if (sEventos == null) {
            sEventos = new EventosManager(c.getApplicationContext());
        }
        return sEventos;
    }

    public ArrayList<Evento> getEventos() {
        return mEventos;
    }

    public Evento getEvento(UUID id) {
        for(Evento evento : mEventos) {
            if(evento.getId().equals(id)) {
                return evento;
            }
        }
        return null;
    }

    public boolean salvarEventos(){
        try {
            mSerializer.salvarEventos(mEventos);
            return true;
        } catch (Exception e) {
            Log.i(TAG,"Não salvou!");
            return false;
        }
    }

    public void addEvento(Evento evento) {
        mEventos.add(evento);
    }

    public void deleteEvento(Evento evento) {
        mEventos.remove(evento);
    }
}
