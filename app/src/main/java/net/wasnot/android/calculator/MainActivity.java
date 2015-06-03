
package net.wasnot.android.calculator;

import java.math.BigDecimal;

import net.wasnot.android.calculator.util.LogUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
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
    @InjectView(R.id.gridLayout)
    public View gridLayout;

    private StringBuilder mCurrValue = new StringBuilder();
    private StringBuilder mNewValue = new StringBuilder();
    private int mAction = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Display dsp = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        dsp.getMetrics(metrics);

        gridLayout.setMinimumHeight((int) (metrics.widthPixels * 5 / 4f));
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
            R.id.buttonDivide, R.id.buttonEqual, R.id.buttonClear, R.id.buttonDelete
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
                // アクションがないときは前回値はリセット
                if (mAction <= 0)
                    mCurrValue.setLength(0);
                // 16桁を上限とする
                if (mNewValue.length() < 16) {
                    // 小数点を含まず0から始まるときは0を除く
                    if (mNewValue.length() > 0 && mNewValue.indexOf(".") < 0
                            && '0' == mNewValue.charAt(0))
                        mNewValue.deleteCharAt(0);
                    mNewValue.append(t.getText());
                } else {
                    Toast.makeText(this, "これ以上入力できません", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonPoint:
                // アクションがないときは前回値はリセット
                if (mAction <= 0)
                    mCurrValue.setLength(0);
                if (mNewValue.indexOf(".") < 0) {
                    if (mNewValue.length() == 0) {
                        mNewValue.append("0.");
                    } else {
                        mNewValue.append(".");
                    }
                }
                break;
            case R.id.buttonPlusMinus:
                if (mNewValue.length() > 0) {
                    int indexMinus = mNewValue.indexOf("-");
                    if (indexMinus == -1)
                        mNewValue.insert(0, "-");
                    else
                        mNewValue.deleteCharAt(indexMinus);
                }
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
                    if (calculate())
                        mAction = -1;
                }
                break;
            case R.id.buttonClear:
                mCurrValue.setLength(0);
                mNewValue.setLength(0);
                mAction = -1;
                break;
            case R.id.buttonDelete:
                if (mNewValue.length() > 0) {
                    mNewValue.deleteCharAt(mNewValue.length() - 1);
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
                actionText.setText("×");
                break;
            case R.id.buttonDivide:
                actionText.setText("÷");
                break;
            default:
                actionText.setText(null);
                break;
        }
    }

    /**
     * @return success
     */
    private boolean calculate() {
        BigDecimal currValue = new BigDecimal(mCurrValue.toString());
        BigDecimal newValue = new BigDecimal(mNewValue.toString());
        BigDecimal next;
        String nextValue = "";
        // アクションがある時のみ計算。
        if (mAction > 0) {
            switch (mAction) {
                case R.id.buttonAdd:
                    next = currValue.add(newValue);
                    nextValue = next.toPlainString();
                    break;
                case R.id.buttonSubtract:
                    next = currValue.subtract(newValue);
                    nextValue = next.toPlainString();
                    break;
                case R.id.buttonMultiply:
                    next = currValue.multiply(newValue);
                    nextValue = next.toPlainString();
                    break;
                case R.id.buttonDivide:
                    if (!newValue.equals(BigDecimal.ZERO)) {
                        try {
                            // TODO 事前に循環少数か判別できないか。
                            next = currValue.divide(newValue);
                        } catch (ArithmeticException e) {
                            // 循環小数は10桁で丸める
                            next = currValue.divide(newValue, 10, BigDecimal.ROUND_HALF_UP);
                        }
                        nextValue = next.toPlainString();
                    } else {
                        Toast.makeText(this, "0で割ることはできません", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
            }
            // 計算結果が20桁以上になる場合は桁あふれの警告を出し、計算しない
            if (nextValue.length() > 20) {
                Toast.makeText(this, "計算結果の桁数が多すぎます", Toast.LENGTH_SHORT).show();
                return false;
            }
            mCurrValue.setLength(0);
            mNewValue.setLength(0);
            mCurrValue.append(nextValue);
            return true;
            // Obtain a Realm instance
            // Realm realm = Realm.getInstance(this);
            // realm.beginTransaction();
            // // Create a new object
            // CalcurateResult result =
            // realm.createObject(CalcurateResult.class);
            // // result.setName("Wasabeef");
            // // result.setEmail("chip@wasabeef.jp");
            // realm.commitTransaction();

        }
        // アクションがない時は？
        else {
            return false;
        }
    }
}
