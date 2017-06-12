package br.com.igti.android.eventos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;


/**
 * An activity representing a list of Eventos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EventoListFragment} and the item details
 * (if present) is a {@link EventoDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EventoListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EventoListActivity extends Activity
        implements EventoListFragment.Callbacks, EventoDetailFragment.Callbacks {

    private static final String TAG = "EventoListActivity";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_list);

        if (findViewById(R.id.evento_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((EventoListFragment) getFragmentManager()
                    .findFragmentById(R.id.evento_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link EventoListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(UUID id) {
        if (mTwoPane) {
            // No modo tablet, o detalhe será exibido adicionando
            // ou substituindo o fragmento de detalhe
            EventoDetailFragment fragment = EventoDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction()
                    .replace(R.id.evento_detail_container, fragment)
                    .commit();

        } else {
            Log.i(TAG, "onClick celular");
            // no celular, chamaremos a nova atividade.
            Intent detailIntent = new Intent(this, EventoDetailActivity.class);
            detailIntent.putExtra(EventoDetailFragment.ARG_ID_EVENTO, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onEventoUpdated(Evento evento) {
        EventoListFragment listFragment = ((EventoListFragment) getFragmentManager()
                .findFragmentById(R.id.evento_list));
        listFragment.updateUI();
    }
}
