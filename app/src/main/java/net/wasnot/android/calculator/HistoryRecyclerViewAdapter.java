
package net.wasnot.android.calculator;

import net.wasnot.android.calculator.realm.CalculateSolution;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.RealmResults;

/**
 * Created by akihiroaida on 15/06/04.
 */
public class HistoryRecyclerViewAdapter extends
        RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private RealmResults<CalculateSolution> mValues;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.textView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public HistoryRecyclerViewAdapter(Context context, RealmResults<CalculateSolution> items) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTextView.setText(getFromSolution(mValues.get(position)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), " " + holder.mTextView.getText(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private String getFromSolution(CalculateSolution solution) {
        return solution.getTargetTerm() + " "
                + getActionString(CalculateSolution.getActionId(solution)) + " "
                + solution.getGivenTerm() + " = " + solution.getSolution();
    }

    private String getActionString(int actionId) {
        switch (actionId) {
            case R.id.buttonAdd:
                return mContext.getString(R.string.button_act_add);
            case R.id.buttonSubtract:
                return mContext.getString(R.string.button_act_subtract);
            case R.id.buttonMultiply:
                return mContext.getString(R.string.button_act_multiply);
            case R.id.buttonDivide:
                return mContext.getString(R.string.button_act_divide);
            default:
                return "?";
        }
    }
}
