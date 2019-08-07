
package hiaround.android.com.ui.adapter.poweradapter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@IntDef(flag = true, value = {
        AdapterLoader.STATE_LOADING,
    AdapterLoader.STATE_LASTED,
})
@Target({ PARAMETER, FIELD })
@Retention(RetentionPolicy.SOURCE)
public @interface LoadState {
}
