package zone.com.zadapter3.helper;

import android.view.View;

import com.zone.adapter3.bean.Holder;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 * 包装类  ,并且 returen this 泛型属性
 *
 * 代理类也可以
 */
public class ExtraHelper<T extends ExtraHelper<T>> extends Holder<T> {


    /**
     * 用于  其他工具类使用,与header 与footer
     *
     * @param view
     */
    public ExtraHelper(View view) {
        super(view);
    }

    public static <U extends ExtraHelper<U>> U wrapper(Holder holder) {
        return (U)new ExtraHelper(holder.itemView);
    }

    public T heihei() {
        System.out.println("ExtraHelper->heihei!");
        return (T)this;
    }

    public T heihei2() {
        System.out.println("ExtraHelper->heihei2!");
        return (T)this;
    }

}
