
package net.wasnot.android.calculator;

import net.wasnot.android.calculator.realm.CalculateSolution;

import android.content.Context;
import android.os.AsyncTask;

import io.realm.Realm;

/**
 * Created by akihiroaida on 15/06/04.
 */
public class InsertAsyncTask extends AsyncTask<CalculateSolution, Void, Void> {

    private Context mContext;

    public InsertAsyncTask(Context context) {
        mContext = context;

    }

    @Override
    protected Void doInBackground(CalculateSolution... params) {
        if (params == null || params.length == 0)
            return null;
        Realm realm = null;
        try {
            realm = Realm.getInstance(mContext);

            realm.beginTransaction();
            for (CalculateSolution solution : params) {
                realm.copyToRealm(solution);
            }
            realm.commitTransaction();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }
}
