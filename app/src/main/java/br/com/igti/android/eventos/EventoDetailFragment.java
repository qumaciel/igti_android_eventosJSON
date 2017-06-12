package br.com.igti.android.eventos;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * A fragment representing a single Evento detail screen.
 * This fragment is either contained in a {@link EventoListActivity}
 * in two-pane mode (on tablets) or a {@link EventoDetailActivity}
 * on handsets.
 */
public class EventoDetailFragment extends Fragment {

    private static final String TAG = "EventoDetailFragment";

    private EditText mTitulo;
    private EditText mDescricao;
//    private Button mData;

    // uma implementação burra do Callback que não fará nada
    // apenas para quando o fagmento não estiver anexado na atividade
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onEventoUpdated(Evento evento) {
        }
    };

    // o callback do fragmento que notifica é notificado sobre a edição
    // do evento
    private Callbacks mCallbacks = sDummyCallbacks;

    private TextWatcher mTitutloListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEvento.setTitulo(s.toString());
            mCallbacks.onEventoUpdated(mEvento);
            getActivity().setTitle(mEvento.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher mDescListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEvento.setDescricao(s.toString());
            mCallbacks.onEventoUpdated(mEvento);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // Chave do ID
    public static final String ARG_ID_EVENTO = "evento_id";

    private Evento mEvento;

    // A instancia do detalhe só será possivel com um ID
    public static EventoDetailFragment newInstance(UUID eventoId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_EVENTO,eventoId);

        EventoDetailFragment fragment = new EventoDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ID_EVENTO)) {
            UUID id = (UUID)getArguments().getSerializable(ARG_ID_EVENTO);
            mEvento = EventosManager.get(getActivity()).getEvento(id);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_evento_detail, container, false);

        if (mEvento != null) {
            mTitulo = (EditText) rootView.findViewById(R.id.evento_detail);
            mTitulo.setText(mEvento.toString());
            getActivity().setTitle(mEvento.toString());
            mTitulo.addTextChangedListener(mTitutloListener);

            mDescricao = (EditText) rootView.findViewById(R.id.evento_descricao);
            mDescricao.setText(mEvento.getDescricao());
            mDescricao.addTextChangedListener(mDescListener);

//            mData = (Button) rootView.findViewById(R.id.evento_data);
//            mData.setText(mEvento.getData().toString());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Quando o fragmento é anexado na atividade
        // garantimos que a atividade implementa o Callback
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
    public void onPause() {
        super.onPause();
        if(EventosManager.get(getActivity()).salvarEventos()) {
            Log.i(TAG, "Salvou!");
        } else {
            Log.i(TAG,"Não salvou");
        }
    }

    // Uma Callback interface para disparar o evento
    // quando algo for alterado
    public interface Callbacks {
        // Evento para quando o item for selecionado
        public void onEventoUpdated(Evento evento);
    }
}
