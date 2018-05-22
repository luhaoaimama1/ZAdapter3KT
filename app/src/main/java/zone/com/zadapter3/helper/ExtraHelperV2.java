package zone.com.zadapter3.helper;

import android.view.View;

import com.zone.adapter3.bean.Holder;

/**
 * [2017] by Zone
 * 包装类  ,并且 returen this 泛型属性
 *
 * 代理类也可以
 */
public class ExtraHelperV2<T extends ExtraHelperV2<T>> extends ExtraHelper<T> {


    /**
     * 用于  其他工具类使用,与header 与footer
     *
     * @param view
     */
    public ExtraHelperV2(View view) {
        super(view);
    }

    public static <U extends ExtraHelperV2<U>> U wrapperV2(Holder holder) {
        return (U)new ExtraHelperV2(holder.itemView);
    }

    public T gaga() {
        System.out.println("ExtraHelperV2->gaga!");
        return (T)this;
    }

    public T gaga2() {
        System.out.println("ExtraHelperV2->gaga!");
        return (T)this;
    }

}
