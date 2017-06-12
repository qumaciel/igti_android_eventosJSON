package br.com.igti.android.eventos;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.UUID;

public class EventoListFragment extends ListFragment {

    private static final String TAG = "EventoListFragment";

    // Chave utilizada para o onSaveInstanceState
    private static final String KEY_POSICAO_ATUAL = "posicao_atual";

    // uma implementação burra do Callback que não fará nada
    // apenas para quando o fagmento não estiver anexado na atividade
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(UUID id) {
        }
    };

    // o callback do fragmento que notifica é notificado sobre o clique na
    private Callbacks mCallbacks = sDummyCallbacks;

    // Item atual
    private int mPosicaoAtual = ListView.INVALID_POSITION;

    // O Construtor do fragmento é obrigatório
    public EventoListFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<Evento>(
                getActivity(),  // contexto
                android.R.layout.simple_list_item_activated_1, // template do item
                android.R.id.text1,
                EventosManager.get(getActivity()).getEventos()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resolve o problema de girar o celular
        if (savedInstanceState != null
                && savedInstanceState.containsKey(KEY_POSICAO_ATUAL)) {
            setActivatedPosition(savedInstanceState.getInt(KEY_POSICAO_ATUAL));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Quando o fragmento é anexado na atividade
        // garantimos que a atividade implementa
        // os Callbacks para os cliques
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("A atividade tem que implementar os Callbacks");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Se o fragmento é desanexado
        // resetamos os callbacks
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Evento evento = (Evento)getListAdapter().getItem(position);
        // Notificamos que um item foi selecionado.
        mCallbacks.onItemSelected(evento.getId());

        Log.i(TAG,"evento " + evento.getId() + " clicado");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosicaoAtual != ListView.INVALID_POSITION) {
            // colocamos a posicao atual
            outState.putInt(KEY_POSICAO_ATUAL, mPosicaoAtual);
        }
    }

    // configura a lista quando o item é clicado e o "ativa"
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mPosicaoAtual, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mPosicaoAtual = position;
    }

    // Uma Callback interface para disparar o evento
    // quando um item for clicado
    public interface Callbacks {
        // Evento para quando o item for selecionado
        public void onItemSelected(UUID id);
    }

    public void updateUI() {
        ((ArrayAdapter<Evento>)getListAdapter()).notifyDataSetChanged();
    }
}
