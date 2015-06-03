
package net.wasnot.android.calculator;

import net.wasnot.android.calculator.realm.CalculateSolution;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HistoryActivity extends AppCompatActivity implements RealmChangeListener {

    @InjectView(R.id.recyclerView)
    public RecyclerView mRecyclerView;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.inject(this);

        mRealm = Realm.getInstance(this);
        RealmResults<CalculateSolution> query = mRealm.where(CalculateSolution.class)
                .findAllSorted("timestamp", RealmResults.SORT_ORDER_DESCENDING);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(this, query));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRealm != null)
            mRealm.addChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRealm != null)
            mRealm.removeChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null)
            mRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            new AlertDialog.Builder(this).setTitle(R.string.alert_delete_all_history_title)
                    .setMessage(R.string.alert_delete_all_history_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    Realm realm = null;
                                    try {
                                        realm = Realm.getInstance(getApplicationContext());

                                        RealmResults<CalculateSolution> results = realm.where(
                                                CalculateSolution.class).findAll();

                                        realm.beginTransaction();
                                        results.clear();
                                        realm.commitTransaction();
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }
                                    return null;
                                }
                            }.execute();
                        }
                    }).setNegativeButton(android.R.string.cancel, null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange() {
        RealmResults<CalculateSolution> query = mRealm.where(CalculateSolution.class)
                .findAllSorted("timestamp", RealmResults.SORT_ORDER_DESCENDING);

        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(this, query));
    }
}
