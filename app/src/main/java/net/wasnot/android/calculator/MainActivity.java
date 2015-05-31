
package net.wasnot.android.calculator;

import net.wasnot.android.calculator.util.LogUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.currentValueText)
    public TextView currentValueText;
    @InjectView(R.id.newValueText)
    public TextView newValueText;
    @InjectView(R.id.actionText)
    public TextView actionText;

    private StringBuilder mCurrValue = new StringBuilder();
    private StringBuilder mNewValue = new StringBuilder();
    private int mAction = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonPlusMinus,
            R.id.buttonPoint, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply,
            R.id.buttonDivide, R.id.buttonEqual
    })
    public void onClick(View v) {
        TextView t = (TextView) v;
        switch (v.getId()) {
            case R.id.button0:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
                mNewValue.append(t.getText());
                break;
            case R.id.buttonPoint:
                break;
            case R.id.buttonPlusMinus:
                break;
            case R.id.buttonAdd:
            case R.id.buttonSubtract:
            case R.id.buttonMultiply:
            case R.id.buttonDivide:
                // 新規値のみの時は既存値に移動させ, アクションを変更
                if (mCurrValue.length() == 0 && mNewValue.length() > 0) {
                    mCurrValue.append(mNewValue);
                    mNewValue.setLength(0);
                    mAction = v.getId();
                }
                // 既存値のみのときはアクション変更
                else if (mCurrValue.length() > 0 && mNewValue.length() == 0) {
                    mAction = v.getId();
                }
                // どちらもある時は計算結果を既存値へ、新規値はクリア、アクションを変更
                else if (mCurrValue.length() > 0 && mNewValue.length() > 0) {
                    calculate();
                    mAction = v.getId();
                }
                break;
            case R.id.buttonEqual:
                // どちらもある時は計算結果を既存値へ、新規値はクリア、アクションもクリア
                if (mCurrValue.length() > 0 && mNewValue.length() > 0) {
                    calculate();
                    mAction = -1;
                }
                break;
        }
        updateText();
    }

    private void updateText() {
        LogUtil.d(TAG, "updateText:" + mNewValue + ", " + mCurrValue);
        newValueText.setText(mNewValue.toString());
        currentValueText.setText(mCurrValue.toString());
        switch (mAction) {
            case R.id.buttonAdd:
                actionText.setText("+");
                break;
            case R.id.buttonSubtract:
                actionText.setText("-");
                break;
            case R.id.buttonMultiply:
                actionText.setText("*");
                break;
            case R.id.buttonDivide:
                actionText.setText("/");
                break;
            default:
                actionText.setText(null);
                break;
        }
    }

    private void calculate() {
        double curr = Double.parseDouble(mCurrValue.toString());
        double newv = Double.parseDouble(mNewValue.toString());
        double next;
        String nextValue = "";
        // アクションがある時のみ計算。
        if (mAction > 0) {
            switch (mAction) {
                case R.id.buttonAdd:
                    next = curr + newv;
                    nextValue = Double.toString(next);
                    break;
                case R.id.buttonSubtract:
                    next = curr - newv;
                    nextValue = Double.toString(next);
                    break;
                case R.id.buttonMultiply:
                    next = curr * newv;
                    nextValue = Double.toString(next);
                    break;
                case R.id.buttonDivide:
                    if (newv != 0) {
                        next = curr / newv;
                        nextValue = Double.toString(next);
                    }
                    break;
            }
            mCurrValue.setLength(0);
            mNewValue.setLength(0);
            mCurrValue.append(nextValue);
        }
        // アクションがない時は？
        else {
        }
    }
}
